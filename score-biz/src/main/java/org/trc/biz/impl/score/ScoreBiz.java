package org.trc.biz.impl.score;

import com.txframework.core.jdbc.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.trc.biz.score.IScoreBiz;
import org.trc.constants.ScoreCode;
import org.trc.constants.ScoreCst;
import org.trc.domain.dto.ScoreAck;
import org.trc.domain.dto.ScoreReq;
import org.trc.domain.score.Score;
import org.trc.domain.score.ScoreChange;
import org.trc.domain.score.ScoreChangeDetail;
import org.trc.domain.score.ScoreChild;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.ScoreException;
import org.trc.service.score.IScoreChangeDetailService;
import org.trc.service.score.IScoreChangeRecordService;
import org.trc.service.score.IScoreChildService;
import org.trc.service.score.IScoreService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/8
 */
@Service("scoreBiz")
public class ScoreBiz implements IScoreBiz{

    Logger logger = LoggerFactory.getLogger(ScoreBiz.class);
    @Autowired
    private IScoreService scoreService;
    @Autowired
    private IScoreChangeRecordService scoreChangeRecordService;
    @Autowired
    private IScoreChildService scoreChildService;
    @Autowired
    private IScoreChangeDetailService scoreChangeDetailService;
    @Override
    public Score getScoreByUserId(String userId) {
        return null;
    }

    @Override
    public List<Score> queryBuyerScore(PageRequest<Score> pageRequest) {
        return null;
    }

    @Override
    public ScoreAck returnScore(String orderNum, Date returnTime) {
        ScoreChange params = new ScoreChange();
        params.setOrderCode(orderNum);
        params.setBusinessCode(ScoreCst.BusinessCode.income.name());
        ScoreChange sellerScoreChange = scoreChangeRecordService.selectOne(params);
        ScoreReq scoreReq = new ScoreReq();
        scoreReq.setExpenditureAccount(sellerScoreChange.getTheOtherUserId());
        scoreReq.setExpenditureAccountName(sellerScoreChange.getTheOtherUserName());
        scoreReq.setIncomeAccount(sellerScoreChange.getUserId());
        scoreReq.setIncomeAccountName(sellerScoreChange.getUserName());
        scoreReq.setShopId(sellerScoreChange.getShopId());
        scoreReq.setFlowType(ScoreCst.FlowType.expend.name());
        scoreReq.setRemark("积分兑换商品");
        scoreReq.setScore(sellerScoreChange.getScore());
        scoreReq.setOperateTime(returnTime);
        scoreReq.setOrderCode(orderNum);
        scoreReq.setBusinessCode(ScoreCst.BusinessCode.consumeCorrect.name());
        return this.consumeCorrectScore(scoreReq);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ScoreAck consumeCorrectScore(ScoreReq scoreReq) {
        if (ScoreCst.BusinessCode.consumeCorrect.name().equals(scoreReq.getBusinessCode())) {
            scoreReq.setChannelCode(ScoreCst.ChannelCode.trc_score.name());
        }
        _validateScoreReqForConsume(scoreReq);
        ScoreChange param = new ScoreChange();
        param.setOrderCode(scoreReq.getOrderCode());
        param.setBusinessCode(ScoreCst.BusinessCode.consume.name());
        this.correctScoreChange(param);
        String expenditureAccount = scoreReq.getExpenditureAccount();
        scoreReq.setExpenditureAccount(scoreReq.getIncomeAccount());
        scoreReq.setIncomeAccount(expenditureAccount);
        scoreReq.setBusinessCode(ScoreCst.BusinessCode.incomeCorrect.name());
        scoreReq.setFlowType(ScoreCst.FlowType.expend.name());
        scoreReq.setRemark("消费冲正");
        scoreReq.setOrderCode(scoreReq.getOrderCode() + "_C");
        deductScore(scoreReq);
        scoreReq.setBusinessCode(ScoreCst.BusinessCode.consumeCorrect.name());
        scoreReq.setShopId(null);
        param.setBusinessCode(ScoreCst.BusinessCode.income.name());
        this.correctScoreChange(param);
        produceScore(scoreReq);
        return ScoreAck.renderSuccess(ScoreCode.SCORE000001, scoreReq.getOrderCode());
    }

    private void _validateScoreReqForConsume(ScoreReq scoreReq) {
        _validateScoreReqForBase(scoreReq);
        Assert.hasText(scoreReq.getExpenditureAccount(), "请求参数scoreReq对应的expenditureAccount不能为空!");
        Assert.hasText(scoreReq.getIncomeAccount(), "请求参数scoreReq对应的incomeAccount不能为空!");
        Assert.isTrue(!scoreReq.getExpenditureAccount().equals(scoreReq.getIncomeAccount()), "支出账户与收入账户不能为同一个账户!");
    }

    private void _validateScoreReqForBase(ScoreReq scoreReq) {
        Assert.notNull(scoreReq, "请求参数scoreReq不能为空!");
        Assert.notNull(scoreReq.getScore(), "请求参数scoreReq对应的score不能为空!");
        Assert.hasText(scoreReq.getOrderCode(), "请求参数scoreReq对应的orderCode不能为空!");
        Assert.notNull(scoreReq.getBusinessCode(), "请求参数scoreReq对应的businessCode不能为空!");
        Assert.notNull(scoreReq.getOperateTime(), "请求参数scoreReq对应的operateTime不能为空!");
        Assert.notNull(scoreReq.getChannelCode(), "请求参数scoreReq对应的channelCode不能为空!");
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void correctScoreChange(ScoreChange param) {
        int result = scoreChangeRecordService.correctScoreChange(param);
        if (result != 1) {
            throw new ScoreException(ExceptionEnum.CORRECT_OPERATION_FAILED, "冲正操作失败！");
        }
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ScoreAck deductScore(ScoreReq scoreReq) {
        try {
            _validateScoreReqForDeduct(scoreReq);
            Assert.isTrue(ScoreCst.BusinessCode.consume.name().equals(scoreReq.getBusinessCode()) || ScoreCst.BusinessCode.lotteryConsume.name().equals(scoreReq.getBusinessCode()) || ScoreCst.BusinessCode.incomeCorrect.name().equals(scoreReq.getBusinessCode()) || ScoreCst.BusinessCode.exchangeOut.name().equals(scoreReq.getBusinessCode()), "businessCode不合法!");
            //参数校验
            //查询积分账户是否已开启
            Score score = this.getScoreByUserId(scoreReq.getExpenditureAccount());
            if (null == score || score.getScore() < scoreReq.getScore()) {
                throw new ScoreException(ExceptionEnum.BALANCE_NOT_ENOUGH, "积分余额不足");
            }
            //不允许卖家兑出或者消费
            if (Score.ScoreUserType.SELLER.toString().equals(score.getUserType()) && !ScoreCst.BusinessCode.incomeCorrect.name().equals(scoreReq.getBusinessCode())) {
                throw new ScoreException(ExceptionEnum.SCORE_UPDATE_EXCEPTION, "不允许卖家兑出或者消费");
            }
            Date time = new Date();
            //获取所有的积分子账户
            List<ScoreChild> scoreChildList = scoreChildService.queryScoreChildByUserId(scoreReq.getExpenditureAccount());
            Long sumChildScore = 0l;
            for (ScoreChild scoreChild : scoreChildList) {
                sumChildScore += scoreChild.getScore();
            }
            if (!sumChildScore.equals(score.getScore())) {
                throw new ScoreException(ExceptionEnum.BALANCE_DOES_NOT_MATCH, "积分余额不匹配");
            }
            Long scoreTmp = scoreReq.getScore();
            int result = 0;
            for (ScoreChild scoreChild : scoreChildList) {
                if (scoreChild.getScore() >= scoreTmp) {
                    scoreChild.setScore(scoreChild.getScore() - scoreTmp);
                    scoreChild.setUpdateTime(time);
                    this.updateScoreChild(scoreChild);
                    ScoreChangeDetail scoreChangeDetail = new ScoreChangeDetail(scoreReq.getExpenditureAccount(), scoreReq.getOrderCode(), score.getId(), scoreChild.getId(),
                            scoreTmp, score.getScore() - scoreReq.getScore(), scoreChild.getFreezingScore(), ScoreCst.FlowType.expend.name(), scoreChild.getExpirationTime(), time);
                    scoreChangeDetailService.insertSelective(scoreChangeDetail);
                    break;
                } else {
                    Long consumeScore = scoreChild.getScore();
                    scoreTmp -= scoreChild.getScore();
                    scoreChild.setScore(0l);
                    scoreChild.setUpdateTime(time);
                    this.updateScoreChild(scoreChild);
                    ScoreChangeDetail scoreChangeDetail = new ScoreChangeDetail(scoreReq.getExpenditureAccount(), scoreReq.getOrderCode(), score.getId(), scoreChild.getId(),
                            consumeScore, score.getScore() + scoreTmp - scoreReq.getScore(), scoreChild.getFreezingScore(), ScoreCst.FlowType.expend.name(), scoreChild.getExpirationTime(), time);
                    scoreChangeDetailService.insertSelective(scoreChangeDetail);
                }
            }
            score.setScore(score.getScore() - scoreReq.getScore());
            score.setUpdateTime(time);
            this.updateScore(score);
            ScoreChange scoreChange = new ScoreChange(scoreReq.getExpenditureAccount(), scoreReq.getExpenditureAccountName(), scoreReq.getIncomeAccount(), scoreReq.getIncomeAccountName(), score.getId(), scoreReq.getScore(), score.getScore(), score.getFreezingScore(), scoreReq.getOrderCode(), scoreReq.getShopId(), scoreReq.getChannelCode(), scoreReq.getBusinessCode(), ScoreCst.FlowType.expend.name(), scoreReq.getRemark(), scoreReq.getExchangeCurrency(), null, time);
            scoreChangeRecordService.insertScoreChange(scoreChange);
            return ScoreAck.renderSuccess(ScoreCode.SCORE000001, scoreReq.getOrderCode());
        } catch (IllegalArgumentException e) {
            logger.error("deductScore参数校验错误!", e);
            throw new ScoreException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "deductScore参数校验错误!");
        } catch (ScoreException e) {
            throw e;
        } catch (Exception e) {
            logger.error("deductScore发生错误!", e);
            throw new ScoreException(ExceptionEnum.OPERATION_FAILED, "deductScore发生错误!" + scoreReq);
        }

        return null;

    }

    private int updateScoreChild(ScoreChild scoreChild) {
        int result = scoreChildService.updateScoreChild(scoreChild);
        if (1 != result) {
            logger.error("更新积分子账户失败!waaaaaaa");
            throw new ScoreException(ExceptionEnum.SCORE_CHILD_UPDATE_EXCEPTION, "更新积分子账户失败!");
        }
        return result;
    }

    private int updateScore(Score score) {
        int result = scoreService.updateScore(score);
        if (1 != result) {
            logger.error("更新积分账户失败!waaaaaaa");
            throw new ScoreException(ScoreException.SCORE_UPDATE_EXCEPTION, "更新积分账户失败!");
        }
        return result;
    }


    @Override
    public ScoreAck consumeScore(ScoreReq scoreReq) {
        return null;
    }

    @Override
    public ScoreAck lotteryConsumeScore(ScoreReq scoreReq) {
        return null;
    }

    private void _validateScoreReqForDeduct(ScoreReq scoreReq) {
        _validateScoreReqForBase(scoreReq);
        Assert.hasText(scoreReq.getExpenditureAccount(), "请求参数scoreReq对应的expenditureAccount不能为空!");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ScoreAck produceScore(ScoreReq scoreReq) throws ScoreException{
//        try {
//            _validateScoreReqForProduce(scoreReq);
//            //查询积分账户是否已开启
//            Score score = scoreDao.getScoreByUserId(scoreReq.getIncomeAccount());
//            Date time = new Date();
//            Date expirationTime = null;
//            if (ScoreCst.BusinessCode.income.name().equals(scoreReq.getBusinessCode()) || ScoreCst.BusinessCode.lotteryIncome.name().equals(scoreReq.getBusinessCode()) || ScoreCst.BusinessCode.consumeCorrect.name().equals(scoreReq.getBusinessCode()) || ScoreCst.BusinessCode.exchangeIn.name().equals(scoreReq.getBusinessCode())) {
//                expirationTime = scoreReq.getScoreExpirationTime();
//            }
//            //已开启
//            if (null != score) {
//                //获取积分子账户
//                Map params = new HashMap();
//                params.put("userId", scoreReq.getIncomeAccount());
//                params.put("expirationTime", scoreReq.getScoreExpirationTime());
//                ScoreChild scoreChild = scoreChildDao.getScoreChildByUserIdAndExpirationTime(params);
//                if (null != scoreChild) {
//                    scoreChild.setScore(scoreChild.getScore() + scoreReq.getScore());
//                    this.updateScoreChild(scoreChild);
//                } else {
//                    scoreChild = new ScoreChild(scoreReq.getIncomeAccount(), score.getId(), scoreReq.getScore(), expirationTime, time);
//                    this.insertScoreChild(scoreChild);
//                }
//                score.addScore(scoreReq.getScore());
//                score.setUpdateTime(time);
//                this.updateScore(score);
//                ScoreChange scoreChange = new ScoreChange(scoreReq.getIncomeAccount(), scoreReq.getIncomeAccountName(), scoreReq.getExpenditureAccount(), scoreReq.getExpenditureAccountName(), score.getId(), scoreReq.getScore(), score.getScore(), score.getFreezingScore(), scoreReq.getOrderCode(), scoreReq.getShopId(), scoreReq.getChannelCode(), scoreReq.getBusinessCode(), ScoreCst.FlowType.income.name(), scoreReq.getRemark(), scoreReq.getExchangeCurrency(), expirationTime, time);
//                //设置兑入业务外币变更数量
//                if (ScoreCst.BusinessCode.exchangeIn.name().equals(scoreReq.getBusinessCode())) {
//                    scoreChange.setForeignCurrency(scoreReq.getAmount());
//                }
//                scoreChangeDao.insertScoreChange(scoreChange);
//                ScoreChangeDetail scoreChangeDetail = new ScoreChangeDetail(scoreReq.getIncomeAccount(), scoreReq.getOrderCode(), score.getId(), scoreChild.getId(),
//                        scoreReq.getScore(), score.getScore(), scoreChild.getFreezingScore(), ScoreCst.FlowType.income.name(), expirationTime, time);
//                scoreChangeDetailDao.insertScoreChangeDetail(scoreChangeDetail);
//            } else {
//                score = _createScore(scoreReq, time);
//                scoreDao.insertScore(score);
//                ScoreChild scoreChild = new ScoreChild(scoreReq.getIncomeAccount(), score.getId(), scoreReq.getScore(), expirationTime, time);
//                scoreChildDao.insertScoreChild(scoreChild);
//                ScoreChange scoreChange = new ScoreChange(scoreReq.getIncomeAccount(), scoreReq.getIncomeAccountName(), scoreReq.getExpenditureAccount(), scoreReq.getExpenditureAccountName(), score.getId(), scoreReq.getScore(), score.getScore(), 0l, scoreReq.getOrderCode(), scoreReq.getShopId(), scoreReq.getChannelCode(), scoreReq.getBusinessCode(), ScoreCst.FlowType.income.name(), scoreReq.getRemark(), scoreReq.getExchangeCurrency(), expirationTime, time);
//                //设置兑入业务外币变更数量
//                if (ScoreCst.BusinessCode.exchangeIn.name().equals(scoreReq.getBusinessCode())) {
//                    scoreChange.setForeignCurrency(scoreReq.getAmount());
//                }
//                scoreChangeDao.insertScoreChange(scoreChange);
//                ScoreChangeDetail scoreChangeDetail = new ScoreChangeDetail(scoreReq.getIncomeAccount(), scoreReq.getOrderCode(), score.getId(), scoreChild.getId(),
//                        scoreReq.getScore(), score.getScore(), 0l, ScoreCst.FlowType.income.name(), expirationTime, time);
//                scoreChangeDetailDao.insertScoreChangeDetail(scoreChangeDetail);
//            }
//            return ScoreAck.renderSuccess(ScoreCode.SCORE000001, scoreReq.getOrderCode());
//        } catch (IllegalArgumentException e) {
//            logger.error("produceScore参数校验错误!", e);
//            throw new ScoreException(ScoreException.ABNORMAL_PARAMETER, "produceScore参数校验错误!");
//        } catch (Exception e) {
//            logger.error("produceScore发生错误!", e);
//            throw new ScoreException(ScoreException.OPERATION_FAILED, "produceScore发生错误!" + scoreReq);
//        }
        return null;
    }


}
