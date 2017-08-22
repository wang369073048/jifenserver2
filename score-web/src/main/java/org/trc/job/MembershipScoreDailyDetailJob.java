package org.trc.job;

import org.trc.mapper.settlement.IMembershipScoreDailyDetailsMapper;
import org.trc.domain.order.MembershipScoreDailyDetailsDO;
import org.trc.domain.score.Score;
import org.trc.domain.score.ScoreSettlement;
import org.trc.service.score.IScoreService;
import org.trc.util.Pagenation;
import org.trc.biz.settlement.IScoreSettlementBiz;
import com.trc.mall.util.IpUtil;
import com.txframework.core.jdbc.PageRequest;
import com.txframework.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.SocketException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by george on 2017/3/14.
 */
public class MembershipScoreDailyDetailJob extends BaseJob {

    private Logger logger = LoggerFactory.getLogger(SettlementJob.class);

    @Autowired
    private IMembershipScoreDailyDetailsMapper membershipScoreDailyDetailsMapper;

    @Autowired
    private IScoreService scoreService;

    @Autowired
    private IScoreSettlementBiz scoreSettlementBiz;

    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void execute() throws ParseException {
        //ip检查，补偿任务暂时单机进行
        try {
            String realIp = IpUtil.getRealIp();
            if(!realIp.equals(this.getTaskIp())){
                logger.info("非任务补偿机，跳过任务!"+realIp+"!="+this.getTaskIp());
                return ;
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.error("MembershipScoreDailyDetailJob!跳过任务!");
            return ;
        }
        logger.info("--MembershipScoreDailyDetailJob start--");
        //统计待处理的任务总量

        //1、获取所有账户
        int count = scoreService.getScoreCountByType("BUYER");
        //获取当前日结时间
        Calendar now = Calendar.getInstance();
        Date time = now.getTime();
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);
        Date dailySettlementTime = now.getTime();
        int totalPage = count/1000 + 1;
        for(int i = 1 ; i < totalPage ; i++){
            Pagenation<Score> pageRequest = new Pagenation<Score>();
            pageRequest.setPageSize(1000);
            pageRequest.setPageIndex(i);
            List<Score> scoreList = scoreService.queryBuyerScore(pageRequest);
            //循环处理所有积分账户
            _settlementBatchMembershipDcoreDailyDetail(scoreList, time, dailySettlementTime);
        }

        logger.info("--MembershipScoreDailyDetailJob end--");
        logger.info("--共处理账户个数:" +count+ "--");
    }

    private void _settlementBatchMembershipDcoreDailyDetail(List<Score> scoreList, Date time, Date dailySettlementTime) throws ParseException {
        for(Score score : scoreList){
            MembershipScoreDailyDetailsDO lastMembershipDcoreDailyDetails = membershipScoreDailyDetailsMapper.getLastMembershipScoreDailyDetails(score.getUserId());
            if(null != lastMembershipDcoreDailyDetails){
                Date startDate = DateUtils.parseDate(lastMembershipDcoreDailyDetails.getAccountDay());
                Date endDate = DateUtils.addDay(startDate,2);
                for(;!endDate.after(dailySettlementTime);endDate = DateUtils.addDay(endDate,1)){
                    _settlementMembershipDcoreDailyDetail(score, endDate, time);
                }
            }else{
                Date endDate = DateUtils.parse("2017-01-20 00:00:00",DateUtils.TIME_PATTERN);
                Calendar cal = Calendar.getInstance();
                cal.setTime(endDate);
                cal.add(Calendar.DAY_OF_YEAR,1);
                cal.set(Calendar.HOUR_OF_DAY,0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);
                endDate = cal.getTime();
                for(;!endDate.after(dailySettlementTime);endDate = DateUtils.addDay(endDate,1)){
                    _settlementMembershipDcoreDailyDetail(score, endDate, time);
                }
            }
        }
    }

    private void _settlementMembershipDcoreDailyDetail(Score score, Date dailySettlementTime, Date time) {
        Date startTime = DateUtils.addDay(dailySettlementTime,-1);
        String accountDay = DateUtils.formatDate(startTime,DateUtils.DATE_PATTERN);
        Map params = new HashMap<>();
        params.put("userId",score.getUserId());
        params.put("startTime",startTime);
        params.put("endTime",dailySettlementTime);
        ScoreSettlement scoreSettlement = scoreSettlementBiz.getScoreSettlementByUserIdAndAccountDay(score.getUserId(),accountDay);
        if(null != scoreSettlement){
            //获取日结余额明细
            MembershipScoreDailyDetailsDO membershipDcoreDailyDetailsDO = new MembershipScoreDailyDetailsDO();
            membershipDcoreDailyDetailsDO.setUserId(score.getUserId());
            membershipDcoreDailyDetailsDO.setAccountDay(accountDay);
            membershipDcoreDailyDetailsDO.setBalance(scoreSettlement.getDailyBalance());
            //获取积分兑入汇总
            MembershipScoreDailyDetailsDO exchangeInNumResult = membershipScoreDailyDetailsMapper.generateMembershipScoreDailyDetailsForExchangeIn(params);
            membershipDcoreDailyDetailsDO.setExchangeInNum(null!=exchangeInNumResult?exchangeInNumResult.getExchangeInNum():0l);
            //获取积分消费汇总
            MembershipScoreDailyDetailsDO consumeRsesult = membershipScoreDailyDetailsMapper.generateMembershipScoreDailyDetailsForConsume(params);
            membershipDcoreDailyDetailsDO.setConsumeNum(null!=consumeRsesult?consumeRsesult.getConsumeNum():0l);
            //add by xab 获取抽奖消费汇总 
            MembershipScoreDailyDetailsDO lotteryConsumeRsesult = membershipScoreDailyDetailsMapper.generateMembershipScoreDailyDetailsForLotteryConsume(params);
            membershipDcoreDailyDetailsDO.setLotteryConsumeNum(null!=lotteryConsumeRsesult?lotteryConsumeRsesult.getLotteryConsumeNum():0l);
            //add by xab 获取消费冲正[也就是退积分]汇总
            MembershipScoreDailyDetailsDO consumeCorrectRsesult = membershipScoreDailyDetailsMapper.generateMembershipScoreDailyDetailsForConsumeCorrect(params);
            membershipDcoreDailyDetailsDO.setConsumeCorrectNum(null!=consumeCorrectRsesult?consumeCorrectRsesult.getConsumeCorrectNum():0l);
            membershipScoreDailyDetailsMapper.insertMembershipScoreDailyDetails(membershipDcoreDailyDetailsDO);
        }
    }

}
