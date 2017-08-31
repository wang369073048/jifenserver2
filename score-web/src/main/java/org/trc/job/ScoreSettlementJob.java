package org.trc.job;


import com.trc.mall.util.IpUtil;
import com.txframework.core.jdbc.PageRequest;
import com.txframework.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trc.domain.score.Score;
import org.trc.domain.score.ScoreChange;
import org.trc.domain.score.ScoreSettlement;
import org.trc.service.score.IScoreChangeRecordService;
import org.trc.service.score.IScoreService;
import org.trc.service.settlement.IScoreSettlementService;
import org.trc.util.Pagenation;

import javax.annotation.Resource;
import java.net.SocketException;
import java.text.ParseException;
import java.util.*;

/**
 * 所有店铺所有者账户余额日结
 * Created by george on 2016/12/29.
 */
public class ScoreSettlementJob extends BaseJob{

    private Logger logger = LoggerFactory.getLogger(ScoreSettlementJob.class);

    @Resource
    private IScoreService scoreService;

    @Resource
    private IScoreChangeRecordService scoreChangeRecordService;

    @Resource
    private IScoreSettlementService scoreSettlementService;

    /**
     * @description 执行任务
     */
    @Transactional(propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
    public void execute() {
        //ip检查，补偿任务暂时单机进行
        try {
            String realIp = IpUtil.getRealIp();
            if(!realIp.equals(this.getTaskIp())){
                logger.info("非任务补偿机，跳过任务!"+realIp);
                return ;
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.error("ScoreSettlementJob!跳过任务!");
            return ;
        }
        logger.info("--ScoreSettlementJob start--");
        //统计待处理的任务总量
        //1、获取账户总数
        int count = scoreService.getScoreCountByType(null);
        //获取当前日结时间
        Calendar now = Calendar.getInstance();
        Date time = now.getTime();
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);
        Date dailySettlementTime = now.getTime();
        int totalPage = count/1000 + 1;
        int end = totalPage+1;
        for(int i = 1 ; i < end ; i++){
            Pagenation<Score> pageRequest = new Pagenation<Score>();
            pageRequest.setPageSize(1000);
            pageRequest.setPageIndex(i);
            List<Score> scoreList = scoreService.queryScore(pageRequest);
            //循环处理所有积分账户
            _settlementBatchScore(scoreList, time, dailySettlementTime);
        }
        logger.info("--ScoreSettlementJob end--");
        logger.info("--共处理账户个数:" +count+ "--");
    }

    private void _settlementBatchScore(List<Score> scoreList, Date time, Date dailySettlementTime){
        //处理本批次积分账户
        for(Score score : scoreList){
            //2、获取上个结算节点
            ScoreSettlement lastScoreSettlement = scoreSettlementService.getLastScoreSettlement(score.getId());
            if(null!=lastScoreSettlement){
                try {
                    Date startDate = DateUtils.parseDate(lastScoreSettlement.getAccountDay());
                    startDate = DateUtils.addDay(startDate,2);
                    for(;!startDate.after(dailySettlementTime);startDate = DateUtils.addDay(startDate,1)){
                        score = scoreService.selectByPrimaryKey(score.getId());
                        _settlementScore(score, startDate, time);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    logger.error("日结操作异常，异常账户为:"+score.getId());
                    break;
                }
            }else {
                Date startDate = score.getCreateTime();
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.DAY_OF_YEAR,1);
                cal.set(Calendar.HOUR_OF_DAY,0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);
                startDate = cal.getTime();
                for(;!startDate.after(dailySettlementTime);startDate = DateUtils.addDay(startDate,1)) {
                    score = scoreService.selectByPrimaryKey(score.getId());
                    _settlementScore(score, startDate, time);
                }
            }
        }
    }

    //日结操作
    private void _settlementScore(Score score, Date dailySettlementTime, Date time){
        String accountDay = DateUtils.formatDate(DateUtils.addDay(dailySettlementTime,-1),DateUtils.DATE_PATTERN);
        //结算时间点后没有更新，则当前余额等于上一日日结余额
        if (score.getUpdateTime().before(dailySettlementTime)) {
            score.setPreviousScore(score.getScore());
            score.setPreviousFreezingScore(score.getFreezingScore());
            scoreService.updateScore(score);
            ScoreSettlement scoreSettlement = new ScoreSettlement();
            scoreSettlement.setAccountDay(accountDay);
            scoreSettlement.setScoreId(score.getId());
            scoreSettlement.setDailyBalance(score.getScore());
            scoreSettlement.setCreateTime(time);
            scoreSettlementService.insertSelective(scoreSettlement);
        }
        //结算时间点后有更新，则查找此前最后一笔交易流水，以变更后的余额作为日结余额
        else {
            Map<String, Object> params = new HashMap();
            params.put("scoreId", score.getId());
            params.put("dailySettlementTime", dailySettlementTime);
            ScoreChange scoreChange = scoreChangeRecordService.getLastScoreChange(params);
            if (null != scoreChange) {
                score.setPreviousScore(scoreChange.getScoreBalance());
                score.setPreviousFreezingScore(scoreChange.getFreezingScoreBalance());
                scoreService.updateScore(score);
                ScoreSettlement scoreSettlement = new ScoreSettlement();
                scoreSettlement.setAccountDay(accountDay);
                scoreSettlement.setScoreId(score.getId());
                scoreSettlement.setDailyBalance(scoreChange.getScoreBalance());
                scoreSettlement.setCreateTime(time);
                scoreSettlementService.insertSelective(scoreSettlement);
            }else{
                score.setPreviousScore(0l);
                score.setPreviousFreezingScore(0l);
                scoreService.updateScore(score);
                ScoreSettlement scoreSettlement = new ScoreSettlement();
                scoreSettlement.setAccountDay(accountDay);
                scoreSettlement.setScoreId(score.getId());
                scoreSettlement.setDailyBalance(score.getScore());
                scoreSettlement.setCreateTime(time);
                scoreSettlementService.insertSelective(scoreSettlement);
            }
        }
    }

}
