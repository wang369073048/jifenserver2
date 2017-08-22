package org.trc.operation;

import com.alibaba.fastjson.JSON;
import com.trc.mall.constants.BusinessSide;
import com.trc.mall.constants.BusinessType;
import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.TairanCouponOperation;
import com.trc.mall.externalservice.dto.TairanCouponDto;
import com.trc.mall.model.RequestFlow;
import com.trc.mall.operation.dto.GainTairanCardDto;
import com.trc.mall.service.RequestFlowService;
import com.txframework.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by george on 2017/7/18.
 */
@Component("exchangeTairanCard")
public class ExchangeTairanCard {

    private Logger logger = LoggerFactory.getLogger(ExchangeTairanCard.class);

    @Resource
    private TairanCouponOperation tairanCouponOperation;

    @Resource
    private RequestFlowService requestFlowService;

    private boolean _validate(RequestFlow requestFlow){
        try {
            Assert.isTrue(null != requestFlow, "请求流水不能为空!");
            Assert.isTrue(BusinessSide.SCORE.equals(requestFlow.getRequester()), "请求发起方不正确!");
            Assert.isTrue(BusinessSide.TAIRAN_FINANCIAL.equals(requestFlow.getResponder()), "请求响应方不正确!");
            Assert.isTrue(BusinessType.EXCHANGE_TAIRAN_COUPONS.equals(requestFlow.getType()), "请求业务类型不正确!");
            Assert.isTrue(StringUtils.isNotBlank(requestFlow.getRequestNum()), "请求号不能为空!");
            Assert.isTrue(null != requestFlow.getRequestTime(), "请求时间不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(requestFlow.getRequestParam()), "请求参数不能为空!");
            return true;
        } catch (Exception e){
            logger.error("请求流水参数校验未通过!requestFlow:" + requestFlow.toString());
            return false;
        }
    }

    @Transactional(propagation= Propagation.REQUIRES_NEW)
    public void execute(RequestFlow requestFlow) {
        if(!_validate(requestFlow)){
            return ;
        }
        if(null == requestFlow.getId()){
            requestFlow = requestFlowService.insert(requestFlow);
        }
        GainTairanCardDto gtcParam = JSON.parseObject(requestFlow.getRequestParam(), GainTairanCardDto.class);
        if(RequestFlow.Status.INITIAL.equals(requestFlow.getStatus()) || RequestFlow.Status.FAILURE.equals(requestFlow.getStatus())){
            try {
                HttpBaseAck<TairanCouponDto> resultAck = tairanCouponOperation.exchangeCoupon(gtcParam.getEid(), gtcParam.getUserId(), gtcParam.getPhone());
                if(resultAck.isSuccess() && null != resultAck.getData()){
                    if(resultAck.getData().isStatus() && TairanCouponDto.SUCCESS_CODE.equals(resultAck.getData().getCode())) {
                        logger.info("用户：" + gtcParam.getUserId() + "兑换泰然优惠券:" + gtcParam.getEid() + ",请求号为:" + gtcParam.getRequestNo() + "兑换成功!");
                        //发放成功，设置状态
                        requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.SUCCESS, null);
                        return;
                    }else{
                        //发放失败，设置状态
                        requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.ERROR, resultAck.getData().getMsg());
                        return;
                    }
                }
                //发放失败，设置状态
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.UNKNOWN, null);
                return;
            } catch (ConnectTimeoutException e) {
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.FAILURE, null);
                logger.error("泰然优惠券兑换服务请求超时不可用!等待重试!"+requestFlow.getRequestNum());
                return ;
            } catch (SocketTimeoutException e) {
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.SOCKET_TIME_OUT, null);
                logger.error("泰然优惠券兑换服务响应超时不可用!等待人工确认!"+requestFlow.getRequestNum());
                return ;
            } catch (ClientProtocolException e) {
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.FAILURE, null);
                logger.error("泰然优惠券兑换服务协议错误不可用!等待重试!"+requestFlow.getRequestNum());
                return ;
            } catch (IOException e) {
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.FAILURE, null);
                logger.error("泰然优惠券兑换服务io未知异常!等待重试!"+requestFlow.getRequestNum());
                return ;
            }
        }
        logger.info("该流水不需要进行补偿!"+requestFlow.getRequestNum());
        return ;
    }

}
