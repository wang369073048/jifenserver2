package org.trc.operation;

import com.alibaba.fastjson.JSON;
import com.trc.mall.constants.BusinessSide;
import com.trc.mall.constants.BusinessType;
import com.trc.mall.constants.LuckyDraw;
import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.TcoinOperation;
import com.trc.mall.externalservice.dto.TcoinAccountDto;
import com.trc.mall.mapper.luckydraw.WinningRecordMapper;
import com.trc.mall.model.RequestFlow;
import com.trc.mall.model.WinningRecordDO;
import com.trc.mall.operation.dto.GainTcoinDto;
import com.trc.mall.service.RequestFlowService;
import com.txframework.core.spring.SpringContextHolder;
import com.txframework.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by george on 2017/7/14.
 */
public class GainTcoin implements Runnable{

    private Logger logger = LoggerFactory.getLogger(GainTcoin.class);

    private static WinningRecordMapper winningRecordMapper;

    private static TcoinOperation tcoinOperation;

    private static RequestFlowService requestFlowService;

    static {
        winningRecordMapper = (WinningRecordMapper) SpringContextHolder.getBean("winningRecordMapper");
        tcoinOperation = (TcoinOperation) SpringContextHolder.getBean("tcoinOperation");
        requestFlowService = (RequestFlowService)SpringContextHolder.getBean("requestFlowService");
    }

    private RequestFlow requestFlow;

    public GainTcoin(RequestFlow requestFlow){
        this.requestFlow = requestFlow;
    }

    private boolean _validate(RequestFlow requestFlow){
        try {
            Assert.isTrue(null != requestFlow, "请求流水不能为空!");
            Assert.isTrue(BusinessSide.SCORE.equals(requestFlow.getRequester()), "请求发起方不正确!");
            Assert.isTrue(BusinessSide.TAIRAN_FINANCIAL.equals(requestFlow.getResponder()), "请求响应方不正确!");
            Assert.isTrue(BusinessType.LUCKY_DRAW_GAIN_T_COINS.equals(requestFlow.getType()), "请求业务类型不正确!");
            Assert.isTrue(StringUtils.isNotBlank(requestFlow.getRequestNum()), "请求号不能为空!");
            Assert.isTrue(null != requestFlow.getRequestTime(), "请求时间不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(requestFlow.getRequestParam()), "请求参数不能为空!");
            return true;
        } catch (Exception e){
            logger.error("请求流水参数校验未通过!requestFlow:" + requestFlow.toString());
            return false;
        }
    }

    public void run() {
        if(!_validate(requestFlow)){
            return ;
        }
        if(null == requestFlow.getId()){
            requestFlow = requestFlowService.insert(requestFlow);
        }
        GainTcoinDto gtParam = JSON.parseObject(requestFlow.getRequestParam(), GainTcoinDto.class);
        WinningRecordDO param = new WinningRecordDO();
        param.setRequestNo(requestFlow.getRequestNum());
        WinningRecordDO winningRecord = winningRecordMapper.selectOne(param);
        if(LuckyDraw.WinningState.ALREADY_ISSUED == winningRecord.getState()){
            logger.info("奖品发放已完成,不能再次发放!");
            return;
        }
        if(RequestFlow.Status.INITIAL.equals(requestFlow.getStatus()) || RequestFlow.Status.FAILURE.equals(requestFlow.getStatus()) || RequestFlow.Status.SOCKET_TIME_OUT.equals(requestFlow.getStatus())){
            try {
                HttpBaseAck<TcoinAccountDto> resultAck = tcoinOperation.winningPoints(gtParam.getUserId(), gtParam.getAmount(), gtParam.getRequestNo(), gtParam.getWinningType());
                if(resultAck.isSuccess() && null != resultAck.getData()){
                    if(TcoinAccountDto.SUCCESS_CODE.equals(resultAck.getData().getResultCode()) || TcoinAccountDto.RETRY_SUCCESS_CODE.equals(resultAck.getData().getResultCode())) {
                        logger.info("用户：" + gtParam.getUserId() + "抽奖获得T币:" + gtParam.getAmount() + ",请求号为:" + gtParam.getRequestNo() + "兑奖成功!");
                        //发放成功，设置状态
                        requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.SUCCESS, null);
                        winningRecord.setState(LuckyDraw.WinningState.ALREADY_ISSUED);
                        winningRecordMapper.updateState(winningRecord);
                        return;
                    }else{
                        //发放失败，设置状态
                        requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.FAILURE, null);
                        winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                        winningRecordMapper.updateState(winningRecord);
                        return;
                    }
                }
                //发放失败，设置状态
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.UNKNOWN, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordMapper.updateState(winningRecord);
                return;
            } catch (ConnectTimeoutException e) {
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.FAILURE, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordMapper.updateState(winningRecord);
                logger.error("发放T币服务请求超时不可用!等待重试!"+requestFlow.getRequestNum());
                return ;
            } catch (SocketTimeoutException e) {
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.SOCKET_TIME_OUT, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordMapper.updateState(winningRecord);
                logger.error("发放T币服务响应超时不可用!等待重试!"+requestFlow.getRequestNum());
                return ;
            } catch (ClientProtocolException e) {
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.FAILURE, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordMapper.updateState(winningRecord);
                logger.error("发放T币服务协议错误不可用!等待重试!"+requestFlow.getRequestNum());
                return ;
            } catch (IOException e) {
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.FAILURE, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordMapper.updateState(winningRecord);
                logger.error("发放T币服务io未知异常!等待重试!"+requestFlow.getRequestNum());
                return ;
            }
        }
        logger.info("该流水不需要进行补偿!"+requestFlow.getRequestNum());
        return;
    }

}
