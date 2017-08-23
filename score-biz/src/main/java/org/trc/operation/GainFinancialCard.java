package org.trc.operation;

import com.alibaba.fastjson.JSON;

import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.TrCouponOperation;
import com.trc.mall.externalservice.dto.CouponDto;

import com.txframework.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.admin.IRequestFlowBiz;
import org.trc.constants.BusinessSide;
import org.trc.constants.BusinessType;
import org.trc.constants.LuckyDraw;
import org.trc.domain.admin.RequestFlow;
import org.trc.domain.luckydraw.WinningRecordDO;
import org.trc.operation.dto.GainFinancialCardDto;
import org.trc.service.luckydraw.IWinningRecordService;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by george on 2017/7/18.
 */
@Component("gainFinancialCard")
public class GainFinancialCard {

    private Logger logger = LoggerFactory.getLogger(GainFinancialCard.class);

    @Resource
    private IWinningRecordService winningRecordService;

    @Resource
    private TrCouponOperation trCouponOperation;

    @Resource
    private IRequestFlowBiz requestFlowBiz;

    private boolean _validate(RequestFlow requestFlow){
        try {
            Assert.isTrue(null != requestFlow, "请求流水不能为空!");
            Assert.isTrue(BusinessSide.SCORE.equals(requestFlow.getRequester()), "请求发起方不正确!");
            Assert.isTrue(BusinessSide.TAIRAN_FINANCIAL.equals(requestFlow.getResponder()), "请求响应方不正确!");
            Assert.isTrue(BusinessType.LUCKY_DRAW_GAIN_FINANCIAL_COUPONS.equals(requestFlow.getType()), "请求业务类型不正确!");
            Assert.isTrue(StringUtils.isNotBlank(requestFlow.getRequestNum()), "请求号不能为空!");
            Assert.isTrue(null != requestFlow.getRequestTime(), "请求时间不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(requestFlow.getRequestParam()), "请求参数不能为空!");
            return true;
        } catch (Exception e){
            logger.error("请求流水参数校验未通过!requestFlow:" + requestFlow.toString());
            return false;
        }
    }

    @Transactional
    public boolean execute(RequestFlow requestFlow) {
        if(!_validate(requestFlow)){
            return false;
        }
        if(null == requestFlow.getId()){
            requestFlow = requestFlowBiz.insert(requestFlow);
        }
        GainFinancialCardDto gfcParam = JSON.parseObject(requestFlow.getRequestParam(), GainFinancialCardDto.class);
        WinningRecordDO param = new WinningRecordDO();
        param.setOrderNum(requestFlow.getRequestNum());
        WinningRecordDO winningRecord = winningRecordService.selectOneForUpdate(param);
        if(LuckyDraw.WinningState.ALREADY_ISSUED == winningRecord.getState()){
            logger.info("奖品发放已完成,不能再次发放!");
            return true;
        }
        if(RequestFlow.Status.INITIAL.equals(requestFlow.getStatus()) || RequestFlow.Status.FAILURE.equals(requestFlow.getStatus())){
            try {
                HttpBaseAck<CouponDto> resultAck  = trCouponOperation.exchangeCoupon(gfcParam.getUserId(), gfcParam.getEid(), gfcParam.getRequestNo());
                if(resultAck.isSuccess() && null != resultAck.getData()){
                    if(CouponDto.SUCCESS_CODE.equals(resultAck.getData().getCode())) {
                        logger.info("用户：" + gfcParam.getUserId() + "兑换金融卡券:" + gfcParam.getEid() + ",请求号为:" + gfcParam.getRequestNo() + "兑奖成功!");
                        //发放成功，设置状态
                        requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.SUCCESS, null);
                        winningRecord.setState(LuckyDraw.WinningState.ALREADY_ISSUED);
                        winningRecordService.updateState(winningRecord);
                        return true;
                    }else{
                        //发放失败，设置状态
                        requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.FAILURE, resultAck.getData().getMessage());
                        winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                        winningRecordService.updateState(winningRecord);
                        return false;
                    }
                }
                //发放失败，设置状态
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.UNKNOWN, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordService.updateState(winningRecord);
                return false;
            } catch (ConnectTimeoutException e) {
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.FAILURE, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordService.updateState(winningRecord);
                logger.error("卡券兑换服务请求超时不可用!等待重试!"+requestFlow.getRequestNum());
                return false;
            } catch (SocketTimeoutException e) {
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordService.updateState(winningRecord);
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.SOCKET_TIME_OUT, null);
                logger.error("卡券兑换服务响应超时不可用!等待人工确认!"+requestFlow.getRequestNum());
                return false;
            } catch (ClientProtocolException e) {
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordService.updateState(winningRecord);
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.FAILURE, null);
                logger.error("卡券兑换服务协议错误不可用!等待重试!"+requestFlow.getRequestNum());
                return false;
            } catch (IOException e) {
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordService.updateState(winningRecord);
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.FAILURE, null);
                logger.error("卡券兑换服务io未知异常!等待重试!"+requestFlow.getRequestNum());
                return false;
            }
        }
        logger.info("该流水不需要进行补偿!"+requestFlow.getRequestNum());
        return false;
    }

}
