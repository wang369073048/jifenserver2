package org.trc.operation;

import com.alibaba.fastjson.JSON;
import com.trc.mall.constants.BusinessSide;
import com.trc.mall.constants.BusinessType;
import com.trc.mall.constants.LuckyDraw;
import com.trc.mall.constants.ScoreCode;
import com.trc.mall.dto.ScoreAck;
import com.trc.mall.dto.ScoreReq;
import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.TcoinOperation;
import com.trc.mall.externalservice.dto.TcoinAccountDto;
import com.trc.mall.mapper.luckydraw.WinningRecordMapper;
import com.trc.mall.model.RequestFlow;
import com.trc.mall.model.WinningRecordDO;
import com.trc.mall.service.RequestFlowService;
import com.trc.mall.service.ScoreService;
import com.txframework.core.spring.SpringContextHolder;
import com.txframework.util.Assert;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.SocketTimeoutException;

/**
 * Created by george on 2017/7/14.
 */
public class GainScore implements Runnable{

    private Logger logger = LoggerFactory.getLogger(GainScore.class);

    private static WinningRecordMapper winningRecordMapper;

    private static TcoinOperation tcoinOperation;

    private static RequestFlowService requestFlowService;

    private static ScoreService scoreService;

    static {
        winningRecordMapper = (WinningRecordMapper) SpringContextHolder.getBean("winningRecordMapper");
        tcoinOperation = (TcoinOperation) SpringContextHolder.getBean("tcoinOperation");
        requestFlowService = (RequestFlowService)SpringContextHolder.getBean("requestFlowService");
        scoreService = (ScoreService)SpringContextHolder.getBean("scoreService");
    }

    private RequestFlow requestFlow;

    public GainScore(RequestFlow requestFlow){
        this.requestFlow = requestFlow;
    }

    private boolean _validate(RequestFlow requestFlow){
        try {
            Assert.isTrue(null != requestFlow, "请求流水不能为空!");
            Assert.isTrue(BusinessSide.SCORE.equals(requestFlow.getRequester()), "请求发起方不正确!");
            Assert.isTrue(BusinessSide.TAIRAN_FINANCIAL.equals(requestFlow.getResponder()), "请求响应方不正确!");
            Assert.isTrue(BusinessType.LUCKY_DRAW_GAIN_SCORE_COINS.equals(requestFlow.getType()), "请求业务类型不正确!");
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
        ScoreReq scoreReq = JSON.parseObject(requestFlow.getRequestParam(), ScoreReq.class);
        WinningRecordDO param = new WinningRecordDO();
        param.setRequestNo(requestFlow.getRequestNum());
        WinningRecordDO winningRecord = winningRecordMapper.selectOne(param);
        if(LuckyDraw.WinningState.ALREADY_ISSUED == winningRecord.getState()){
            logger.info("奖品发放已完成,不能再次发放!");
            return;
        }
        if(RequestFlow.Status.INITIAL.equals(requestFlow.getStatus()) || RequestFlow.Status.GAIN_FAILURE.equals(requestFlow.getStatus()) || RequestFlow.Status.GAIN_SOCKET_TIME_OUT.equals(requestFlow.getStatus())){
            try {
                HttpBaseAck<TcoinAccountDto> resultAck = tcoinOperation.winningPoints(scoreReq.getIncomeAccount(), scoreReq.getAmount(), scoreReq.getOrderCode(), "SCORE");
                if(resultAck.isSuccess() && null != resultAck.getData()){
                    if(TcoinAccountDto.SUCCESS_CODE.equals(resultAck.getData().getResultCode()) || TcoinAccountDto.RETRY_SUCCESS_CODE.equals(resultAck.getData().getResultCode())) {
                        logger.info("用户：" + scoreReq.getIncomeAccount() + "抽奖获得积分:" + scoreReq.getScore() + ",请求号为:" + scoreReq.getOrderCode() + "T币发放成功!");
                        _exchange(scoreReq, requestFlow, winningRecord);
                        return;
                    }else{
                        //发放失败，设置状态
                        requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.GAIN_FAILURE, null);
                        winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                        winningRecordMapper.updateState(winningRecord);
                        return;
                    }
                }
                //发放失败，设置状态
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.GAIN_UNKNOWN, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordMapper.updateState(winningRecord);
                return;
            } catch (ConnectTimeoutException e) {
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.GAIN_FAILURE, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordMapper.updateState(winningRecord);
                logger.error("发放T币服务请求超时不可用!等待重试!"+requestFlow.getRequestNum());
                return ;
            } catch (SocketTimeoutException e) {
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.GAIN_SOCKET_TIME_OUT, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordMapper.updateState(winningRecord);
                logger.error("发放T币服务响应超时不可用!等待重试!"+requestFlow.getRequestNum());
                return ;
            } catch (ClientProtocolException e) {
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.GAIN_FAILURE, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordMapper.updateState(winningRecord);
                logger.error("发放T币服务协议错误不可用!等待重试!"+requestFlow.getRequestNum());
                return ;
            } catch (IOException e) {
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.GAIN_FAILURE, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordMapper.updateState(winningRecord);
                logger.error("发放T币服务io未知异常!等待重试!"+requestFlow.getRequestNum());
                return ;
            }
        } else if(RequestFlow.Status.GAIN_SUCCESS_EXCHANGE_FAILURE.equals(requestFlow.getStatus()) || RequestFlow.Status.EXCHANGE_SOCKET_TIME_OUT.equals(requestFlow.getStatus())){
            _exchange(scoreReq, requestFlow, winningRecord);
            return;
        } else if(RequestFlow.Status.EXCHANGE_SUCCESS_PRODUCE_FAILURE.equals(requestFlow.getStatus())){
            _produceScore(scoreReq, requestFlow, winningRecord);
            return;
        }
    }

    private void _exchange(ScoreReq scoreReq, RequestFlow requestFlow, WinningRecordDO winningRecord){
        try {
            HttpBaseAck<TcoinAccountDto> exchangeAck = tcoinOperation.operateTcoin(scoreReq.getIncomeAccount(), 0 - scoreReq.getAmount(), scoreReq.getOrderCode()+"_d");
            if (exchangeAck.isSuccess() && null != exchangeAck.getData()) {
                if (TcoinAccountDto.SUCCESS_CODE.equals(exchangeAck.getData().getResultCode()) || TcoinAccountDto.RETRY_SUCCESS_CODE.equals(exchangeAck.getData().getResultCode())) {
                    logger.info("用户：" + scoreReq.getIncomeAccount() + "T币:" + scoreReq.getAmount() + "兑换积分,请求号为:" + scoreReq.getOrderCode() + "T币扣减成功!");
                    _produceScore(scoreReq, requestFlow, winningRecord);
                    return;
                } else {
                    //发放失败，设置状态
                    requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.GAIN_SUCCESS_EXCHANGE_FAILURE, exchangeAck.getMessage());
                    winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                    winningRecordMapper.updateState(winningRecord);
                    return;
                }
            }
            //发放失败，设置状态
            requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.GAIN_SUCCESS_EXCHANGE_UNKNOWN, exchangeAck.toString());
            winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
            winningRecordMapper.updateState(winningRecord);
            return;
        } catch (ConnectTimeoutException e) {
            requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.GAIN_SUCCESS_EXCHANGE_FAILURE, null);
            winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
            winningRecordMapper.updateState(winningRecord);
            logger.error("T币兑换服务请求超时不可用!等待重试!" + requestFlow.getRequestNum());
            return;
        } catch (SocketTimeoutException e) {
            requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.EXCHANGE_SOCKET_TIME_OUT, null);
            winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
            winningRecordMapper.updateState(winningRecord);
            logger.error("T币兑换服务响应超时不可用!等待重试!" + requestFlow.getRequestNum());
            return;
        } catch (ClientProtocolException e) {
            requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.GAIN_SUCCESS_EXCHANGE_FAILURE, null);
            winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
            winningRecordMapper.updateState(winningRecord);
            logger.error("T币兑换服务协议错误不可用!等待重试!" + requestFlow.getRequestNum());
            return;
        } catch (IOException e) {
            requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.GAIN_SUCCESS_EXCHANGE_FAILURE, null);
            winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
            winningRecordMapper.updateState(winningRecord);
            logger.error("T币兑换服务io未知异常!等待重试!" + requestFlow.getRequestNum());
            return;
        }
    }

    @Transactional
    private void _produceScore(ScoreReq scoreReq, RequestFlow requestFlow, WinningRecordDO winningRecord){
        //增加积分
        try {
            //scoreReq.setOrderCode(scoreReq.getOrderCode()+"_d");
            ScoreAck scoreAck = scoreService.produceScore(scoreReq);
            if(ScoreCode.SCORE000001.equals(scoreAck.getRespCode())) {
                logger.info("抽中积分时，T币发放成功，兑换积分成功!:"+ winningRecord.getRequestNo()+"_d");
                //发放成功，设置状态
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.SUCCESS, null);
                winningRecord.setState(LuckyDraw.WinningState.ALREADY_ISSUED);
                winningRecordMapper.updateState(winningRecord);
                return ;
            }else{
                logger.error("抽中积分时，T币发放成功，发放积分失败！需进行补偿:"+ winningRecord.getRequestNo()+"_d");
                //发放成功，设置状态
                requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.EXCHANGE_SUCCESS_PRODUCE_FAILURE, null);
                winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
                winningRecordMapper.updateState(winningRecord);
                return ;
            }
        } catch (Exception e) {
            //更新流水为EXCHANGE_SUCCESS_PRODUCE_FAILURE状态
            requestFlowService.modify(requestFlow.getId(), RequestFlow.Status.EXCHANGE_SUCCESS_PRODUCE_FAILURE, null);
            winningRecord.setState(LuckyDraw.WinningState.PAYMENT_FAILURE);
            winningRecordMapper.updateState(winningRecord);
            logger.error("T币扣除成功，积分添加失败，等待补偿!");
            return;
        }
    }

}
