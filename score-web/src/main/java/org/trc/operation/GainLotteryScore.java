package org.trc.operation;

import com.alibaba.fastjson.JSON;

import com.txframework.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.admin.IRequestFlowBiz;
import org.trc.biz.score.IScoreBiz;
import org.trc.constants.BusinessSide;
import org.trc.constants.BusinessType;
import org.trc.constants.ScoreCode;
import org.trc.domain.admin.RequestFlow;
import org.trc.domain.dto.ScoreAck;
import org.trc.domain.dto.ScoreReq;
import org.trc.enums.ExceptionEnum;

import javax.annotation.Resource;

/**
 * Created by george on 2017/8/1.
 */
@Component("gainLotteryScore")
public class GainLotteryScore {

    private Logger logger = LoggerFactory.getLogger(GainLotteryScore.class);

    @Resource
    private IRequestFlowBiz requestFlowBiz;

    @Autowired
    private IScoreBiz scoreBiz;

    private boolean _validate(RequestFlow requestFlow) {
        try {
            Assert.isTrue(null != requestFlow, "请求流水不能为空!");
            Assert.isTrue(BusinessSide.SCORE.equals(requestFlow.getRequester()), "请求发起方不正确!");
            Assert.isTrue(BusinessSide.SCORE.equals(requestFlow.getResponder()), "请求响应方不正确!");
            Assert.isTrue(BusinessType.GAIN_LOTTERY_SCORE.equals(requestFlow.getType()), "请求业务类型不正确!");
            Assert.isTrue(StringUtils.isNotBlank(requestFlow.getRequestNum()), "请求号不能为空!");
            Assert.isTrue(null != requestFlow.getRequestTime(), "请求时间不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(requestFlow.getRequestParam()), "请求参数不能为空!");
            return true;
        } catch (Exception e){
            logger.error("请求流水参数校验未通过!requestFlow:" + requestFlow.toString());
            return false;
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ScoreAck execute(RequestFlow requestFlow) {
        if(!_validate(requestFlow)){
            return ScoreAck.renderFailure(ScoreCode.SCORE999999, "兑换参数为空！");
        }
        if(null == requestFlow.getId()){
            requestFlow = requestFlowBiz.insert(requestFlow);
        }
        ScoreReq scoreReq = JSON.parseObject(requestFlow.getRequestParam(), ScoreReq.class);
        if(RequestFlow.Status.INITIAL.equals(requestFlow.getStatus()) || RequestFlow.Status.FAILURE.equals(requestFlow.getStatus())) {
            //增加积分
            try {
                ScoreAck scoreAck = scoreBiz.produceScore(scoreReq);
                logger.info("原积分产生单据号为:" + scoreReq.getOrderCode() + "卖家积分增加操作成功!本次兑入成功！请求参数为:" + scoreReq);
                //更新流水为成功状态
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.SUCCESS, null);
                return scoreAck;
            } catch (Exception e) {
                //更新流水为SCORE_OPERATION_FAILED状态
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.FAILURE, null);
                logger.error("卖家积分增加失败，等待补偿!");
                return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "卖家积分增加失败，等待补偿!", requestFlow.getRequestNum());
            }
        }
        return ScoreAck.renderSuccess(ScoreCode.SCORE000001, scoreReq.getOrderCode());
    }

}
