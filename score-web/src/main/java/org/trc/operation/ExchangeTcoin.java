package org.trc.operation;

import com.alibaba.fastjson.JSON;

import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.TcoinOperation;
import com.trc.mall.externalservice.dto.TcoinAccountDto;

import com.txframework.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.admin.ILimitBiz;
import org.trc.biz.admin.IRequestFlowBiz;
import org.trc.biz.score.IScoreBiz;
import org.trc.constants.BusinessSide;
import org.trc.constants.BusinessType;
import org.trc.constants.ScoreCode;
import org.trc.constants.ScoreCst;
import org.trc.domain.admin.RequestFlow;
import org.trc.domain.dto.ScoreAck;
import org.trc.domain.dto.ScoreReq;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BusinessException;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by george on 2017/7/14.
 */
@Component("exchangeTcoin")
public class ExchangeTcoin {

    public static final String PLAT_PRE = "plat_";

    public static final String PERSONAL_PRE = "personal_";

    public static final String IN = "in_";

    public static final String OUT ="out_";

    private Logger logger = LoggerFactory.getLogger(ExchangeTcoin.class);

    @Resource
    private TcoinOperation tcoinOperation;

    @Resource
    private IRequestFlowBiz requestFlowBiz;

    @Autowired
    private ILimitBiz limitBiz;

    @Autowired
    private IScoreBiz scoreService;

    private boolean _validate(RequestFlow requestFlow){
        try {
            Assert.isTrue(null != requestFlow, "请求流水不能为空!");
            Assert.isTrue(BusinessSide.SCORE.equals(requestFlow.getRequester()), "请求发起方不正确!");
            Assert.isTrue(BusinessSide.TAIRAN_FINANCIAL.equals(requestFlow.getResponder()), "请求响应方不正确!");
            Assert.isTrue(BusinessType.TCOIN_EXCHANGE_IN.equals(requestFlow.getType()), "请求业务类型不正确!");
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
        ScoreCst.ExchangeCurrency currency = Enum.valueOf(ScoreCst.ExchangeCurrency.class, scoreReq.getExchangeCurrency());
        if(RequestFlow.Status.INITIAL.equals(requestFlow.getStatus())) {
            //平台限额检查
            if (!limitBiz.enoughLimitInAmount(scoreReq.getExchangeCurrency(), scoreReq.getOrderCode(), scoreReq.getScore().intValue(), scoreReq.getOperateTime())) {
                this.rollbackPlatLimitInAmount(scoreReq);
                //更新流水为LACK_OF_EXCHANGE_AMOUNT状态
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.LACK_OF_EXCHANGE_AMOUNT, null);
                throw new BusinessException(ExceptionEnum.LACK_OF_PLATFORM_EXCHANGE_AMOUNT, currency.getValue() + "平台兑换额度不足");
            }
            if (!limitBiz.enoughPersonalLimitInAmount(scoreReq.getExchangeCurrency(), scoreReq.getIncomeAccount(), scoreReq.getOrderCode(), scoreReq.getScore().intValue(), scoreReq.getOperateTime())) {
                this.rollbackLimitInAmount(scoreReq);
                //更新流水为LACK_OF_EXCHANGE_AMOUNT状态
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.LACK_OF_EXCHANGE_AMOUNT, null);
                throw new BusinessException(ExceptionEnum.LACK_OF_PLATFORM_EXCHANGE_AMOUNT, currency.getValue() + "个人兑换额度不足");
            }
            try {
                HttpBaseAck<TcoinAccountDto> resultAck = tcoinOperation.operateTcoin(scoreReq.getIncomeAccount(), 0 - scoreReq.getAmount(), scoreReq.getOrderCode());
                if (resultAck.isSuccess() && null != resultAck.getData()) {
                    if (TcoinAccountDto.SUCCESS_CODE.equals(resultAck.getData().getResultCode())) {
                        logger.info("用户：" + scoreReq.getIncomeAccount() + "T币:" + scoreReq.getAmount() + "兑换积分,请求号为:" + scoreReq.getOrderCode() + "T币扣减成功!");
                        //增加积分
                        try {
                            ScoreAck scoreAck = scoreService.produceScore(scoreReq);
                            logger.info("单据号为:" + scoreReq.getOrderCode() + "积分增加操作成功!本次兑入成功！请求参数为:" + scoreReq);
                            //更新流水为成功状态
                            requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.SUCCESS, null);
                            return scoreAck;
                        } catch (Exception e) {
                            //更新流水为SCORE_OPERATION_FAILED状态
                            requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.EXCHANGE_SUCCESS_PRODUCE_FAILURE, null);
                            logger.error("T币扣除成功，积分添加失败，等待补偿!");
                            return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "T币兑换成功,添加积分失败，稍后系统会自行补偿!", requestFlow.getRequestNum());
                        }
                    } else {
                        //发放失败，设置状态
                        requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.ERROR, resultAck.toString());
                        return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), resultAck.getData().getResultMsg(), requestFlow.getRequestNum());
                    }
                }
                //发放失败，设置状态
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.UNKNOWN, resultAck.toString());
                this.rollbackLimitInAmount(scoreReq);
                return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "T币兑换服务不可用!", requestFlow.getRequestNum());
            } catch (ConnectTimeoutException e) {
                this.rollbackLimitInAmount(scoreReq);
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.ERROR, null);
                logger.error("T币兑换服务请求超时不可用!等待重试!" + requestFlow.getRequestNum());
                return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "T币兑换服务不可用!", requestFlow.getRequestNum());
            } catch (SocketTimeoutException e) {
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.SOCKET_TIME_OUT, null);
                logger.error("T币兑换服务响应超时不可用!等待重试!" + requestFlow.getRequestNum());
                return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "稍后系统将自动补偿!", requestFlow.getRequestNum());
            } catch (ClientProtocolException e) {
                this.rollbackLimitInAmount(scoreReq);
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.ERROR, null);
                logger.error("T币兑换服务协议错误不可用!等待重试!" + requestFlow.getRequestNum());
                return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "T币兑换服务不可用!", requestFlow.getRequestNum());
            } catch (IOException e) {
                this.rollbackLimitInAmount(scoreReq);
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.ERROR, null);
                logger.error("T币兑换服务io未知异常!等待重试!" + requestFlow.getRequestNum());
                return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "T币兑换服务不可用!", requestFlow.getRequestNum());
            }
        } else if(RequestFlow.Status.SOCKET_TIME_OUT.equals(requestFlow.getStatus())){
            //查询T币是否扣除
            try {
                HttpBaseAck<TcoinAccountDto> resultAck = tcoinOperation.queryOperation(requestFlow.getRequestNum());
                //T币已扣除，补偿用户积分
                if(resultAck.isSuccess() && null != resultAck.getData()){
                    if(TcoinAccountDto.SUCCESS_CODE.equals(resultAck.getData().getResultCode())) {
                        logger.info("用户：" + scoreReq.getIncomeAccount() + "T币:" + scoreReq.getAmount() + "兑换积分,请求号为:" + scoreReq.getOrderCode() + "T币扣减完成，等待积分补偿!");
                        //增加积分
                        try {
                            ScoreAck scoreAck = scoreService.produceScore(scoreReq);
                            logger.info("原超时单据号为:" + scoreReq.getOrderCode() + "积分增加操作成功!本次兑入成功！请求参数为:" + scoreReq);
                            //更新流水为成功状态
                            requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.SUCCESS, null);
                            return scoreAck;
                        } catch (Exception e) {
                            //更新流水为SCORE_OPERATION_FAILED状态
                            requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.EXCHANGE_SUCCESS_PRODUCE_FAILURE, null);
                            logger.error("T币扣除成功，积分添加失败，等待补偿!");
                            return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "T币兑换成功,添加积分失败，稍后系统会自行补偿!", requestFlow.getRequestNum());
                        }
                    }else{
                        //发放失败，设置状态
                        requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.UNKNOWN, resultAck.toString());
                        return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "T币兑换服务不可用!", requestFlow.getRequestNum());
                    }
                }
                //T币未扣除，该请求废弃
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.ERROR, resultAck.toString());
                this.rollbackLimitInAmount(scoreReq);
                return ScoreAck.renderSuccess(ScoreCode.SCORE000001, scoreReq.getOrderCode());
            } catch (ConnectTimeoutException e) {
                logger.error("T币兑换请求确认超时不可用!等待重试!"+requestFlow.getRequestNum());
                return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "T币兑换请求确认服务不可用，稍后系统会自行补偿!", requestFlow.getRequestNum());
            } catch (SocketTimeoutException e) {
                logger.error("T币兑换请求确认响应超时不可用!等待重试!"+requestFlow.getRequestNum());
                return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "T币兑换请求确认服务不可用，稍后系统会自行补偿!", requestFlow.getRequestNum());
            } catch (ClientProtocolException e) {
                logger.error("T币兑换请求确认服务协议错误不可用!等待重试!"+requestFlow.getRequestNum());
                return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "T币兑换请求确认服务不可用，稍后系统会自行补偿!", requestFlow.getRequestNum());
            } catch (IOException e) {
                logger.error("T币兑换请求确认服务io未知异常!等待重试!"+requestFlow.getRequestNum());
                return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "T币兑换请求确认服务不可用，稍后系统会自行补偿!", requestFlow.getRequestNum());
            }
        } else if(RequestFlow.Status.EXCHANGE_SUCCESS_PRODUCE_FAILURE.equals(requestFlow.getStatus())){
            //增加积分
            try {
                ScoreAck scoreAck = scoreService.produceScore(scoreReq);
                logger.info("单据号为:" + scoreReq.getOrderCode() + "积分增加操作成功!本次兑入成功！请求参数为:" + scoreReq);
                //更新流水为成功状态
                requestFlowBiz.modify(requestFlow.getId(), RequestFlow.Status.SUCCESS, null);
                return scoreAck;
            } catch (Exception e) {
                //更新流水为SCORE_OPERATION_FAILED状态
                logger.error("积分产生失败，等待补偿!");
                return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "积分产生失败，稍后系统会自行补偿!", requestFlow.getRequestNum());
            }
        }
        return ScoreAck.renderFailure(ExceptionEnum.EXTERNAL_SERVICE_FAILED.getCode(), "该流水不需要进行补偿!", requestFlow.getRequestNum());
    }

    //Redis平台限额回滚
    private void rollbackPlatLimitInAmount(ScoreReq scoreReq) {
        String limitInKey = PLAT_PRE + IN + scoreReq.getExchangeCurrency() + limitBiz.getKeySuffix(scoreReq.getOperateTime());
        limitBiz.rollbackLimitInAmount(limitInKey, scoreReq.getOrderCode(), scoreReq.getScore().intValue());
    }

    //Redis平台、个人限额回滚
    private void rollbackLimitInAmount(ScoreReq scoreReq) {
        String limitInKey = PLAT_PRE + IN + scoreReq.getExchangeCurrency() + limitBiz.getKeySuffix(scoreReq.getOperateTime());
        limitBiz.rollbackLimitInAmount(limitInKey, scoreReq.getOrderCode(), scoreReq.getScore().intValue());
        String personalLimitInKey = PERSONAL_PRE + scoreReq.getIncomeAccount() + IN + scoreReq.getExchangeCurrency() + limitBiz.getKeySuffix(scoreReq.getOperateTime());
        limitBiz.rollbackPersonalLimitOutAmount(personalLimitInKey, scoreReq.getIncomeAccount(), scoreReq.getOrderCode(), scoreReq.getScore().intValue());
    }

    //Redis平台限额回滚
    private void rollbackPlatLimitOutAmount(ScoreReq scoreReq) {
        String limitOutKey = PLAT_PRE + OUT + scoreReq.getExchangeCurrency() + limitBiz.getKeySuffix(scoreReq.getOperateTime());
        limitBiz.rollbackLimitOutAmount(limitOutKey, scoreReq.getOrderCode(), scoreReq.getScore().intValue());
    }

    //Redis平台、个人限额回滚
    private void rollbackLimitOutAmount(ScoreReq scoreReq) {
        String limitOutKey = PLAT_PRE + OUT + scoreReq.getExchangeCurrency() + limitBiz.getKeySuffix(scoreReq.getOperateTime());
        limitBiz.rollbackLimitOutAmount(limitOutKey, scoreReq.getOrderCode(), scoreReq.getScore().intValue());
        String personalLimitOutKey = PERSONAL_PRE + scoreReq.getExpenditureAccount() + OUT + scoreReq.getExchangeCurrency() + limitBiz.getKeySuffix(scoreReq.getOperateTime());
        limitBiz.rollbackPersonalLimitOutAmount(personalLimitOutKey, scoreReq.getExpenditureAccount(), scoreReq.getOrderCode(), scoreReq.getScore().intValue());
    }

}
