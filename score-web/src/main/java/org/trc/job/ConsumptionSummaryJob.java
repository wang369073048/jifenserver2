package org.trc.job;

import org.trc.mapper.settlement.IConsumptionSummaryMapper;
import org.trc.biz.settlement.IFinancialSettlementBiz;
import org.trc.domain.order.ConsumptionSummaryDO;
import org.trc.domain.order.MembershipScoreDailyDetailsDO;

import com.trc.mall.util.IpUtil;
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
 * Created by george on 2017/3/13.
 */
public class ConsumptionSummaryJob extends BaseJob {

    private Logger logger = LoggerFactory.getLogger(SettlementJob.class);

    private static Map<String,Long> currencyMap;

    static{
        currencyMap = new HashMap<>();
        currencyMap.put("TCOIN",1l);
        currencyMap.put("ecard",2l);
    }

    @Autowired
    private IConsumptionSummaryMapper consumptionSummaryMapper;

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
            logger.error("ConsumptionSummaryJob!跳过任务!");
            return ;
        }
        logger.info("--ConsumptionSummaryJob start--");
        //获取当前日结时间
        Calendar now = Calendar.getInstance();
        Date time = now.getTime();
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);
        Date dailySettlementTime = now.getTime();
        ConsumptionSummaryDO consumptionSummaryDO = consumptionSummaryMapper.getLastConsumptionSummary();
        if(null != consumptionSummaryDO){
            Date endDate = DateUtils.parseDate(consumptionSummaryDO.getAccountDay());
            endDate = DateUtils.addDay(endDate,2);
            for(;!endDate.after(dailySettlementTime);endDate = DateUtils.addDay(endDate,1)){
                _consumptionSummary(endDate, time);
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
                _consumptionSummary(endDate, time);
            }
        }
    }

    private void _consumptionSummary(Date endDate, Date time) {
        Date startTime = DateUtils.addDay(endDate,-1);
        Map<String,Date> params = new HashMap<>();
        params.put("startTime",startTime);
        params.put("endTime",endDate);
        //兑入积分汇总
        List<ConsumptionSummaryDO> exchangeInSummaryDOList = consumptionSummaryMapper.generateConsumptionSummaryForExchangeIn(params);
        logger.info("--SettlementJob 日结时间:" + endDate + ",账户个数:" + exchangeInSummaryDOList.size());
        String accountDay = DateUtils.formatDate(startTime,DateUtils.DATE_PATTERN);
        for(ConsumptionSummaryDO consumptionSummary : exchangeInSummaryDOList){
            consumptionSummary.setAccountDay(accountDay);
            consumptionSummary.setShopId(currencyMap.get(consumptionSummary.getExchangeCurrency()));
            consumptionSummary.setConsumeNum(0l);
            consumptionSummary.setConsumeCorrectNum(0l);
            consumptionSummary.setLotteryConsumeNum(0l);
            consumptionSummary.setCreateTime(time);
            consumptionSummaryMapper.insertConsumptionSummary(consumptionSummary);
        }
        //消费积分汇总
        List<ConsumptionSummaryDO> consumptionSummaryDOList = consumptionSummaryMapper.generateConsumptionSummaryForConsume(params);
        for(ConsumptionSummaryDO consumptionSummary : consumptionSummaryDOList){
            consumptionSummary.setAccountDay(accountDay);
            ConsumptionSummaryDO oldConsumptionSummary = consumptionSummaryMapper.getConsumptionSummaryByParams(consumptionSummary);
            if(null == oldConsumptionSummary){
                consumptionSummary.setExchangeInNum(0l);
                consumptionSummary.setConsumeCorrectNum(0l);
                consumptionSummary.setLotteryConsumeNum(0l);
                consumptionSummary.setCreateTime(time);
                consumptionSummaryMapper.insertConsumptionSummary(consumptionSummary);
            }else{
                oldConsumptionSummary.setConsumeNum(consumptionSummary.getConsumeNum());
                consumptionSummaryMapper.updateConsumptionSummary(oldConsumptionSummary);
            }
        }
        //add by xab 获取抽奖消费汇总 
        List<ConsumptionSummaryDO> lotteryConsumeSummaryDOList = consumptionSummaryMapper.generateConsumptionSummaryForLotteryConsume(params);
        for(ConsumptionSummaryDO lotteryConsumeSummary : lotteryConsumeSummaryDOList){
        	lotteryConsumeSummary.setAccountDay(accountDay);
            ConsumptionSummaryDO oldConsumptionSummary = consumptionSummaryMapper.getConsumptionSummaryByParams(lotteryConsumeSummary);
            if(null == oldConsumptionSummary){
            	lotteryConsumeSummary.setExchangeInNum(0l);
            	lotteryConsumeSummary.setConsumeCorrectNum(0l);
            	lotteryConsumeSummary.setConsumeNum(0l);
            	lotteryConsumeSummary.setCreateTime(time);
                consumptionSummaryMapper.insertConsumptionSummary(lotteryConsumeSummary);
            }else{
                oldConsumptionSummary.setLotteryConsumeNum(lotteryConsumeSummary.getLotteryConsumeNum());
                consumptionSummaryMapper.updateConsumptionSummary(oldConsumptionSummary);
            }
        }
        //add by xab 获取消费冲正[也就是退积分]汇总
        List<ConsumptionSummaryDO> consumeCorrectSummaryDOList = consumptionSummaryMapper.generateConsumptionSummaryForConsumeCorrect(params);
        for(ConsumptionSummaryDO consumeCorrectSummary : consumeCorrectSummaryDOList){
        	consumeCorrectSummary.setAccountDay(accountDay);
            ConsumptionSummaryDO oldConsumeCorrectSummary = consumptionSummaryMapper.getConsumptionSummaryByParams(consumeCorrectSummary);
            if(null == oldConsumeCorrectSummary){
            	consumeCorrectSummary.setExchangeInNum(0l);
            	consumeCorrectSummary.setConsumeNum(0l);
            	consumeCorrectSummary.setLotteryConsumeNum(0l);
            	consumeCorrectSummary.setCreateTime(time);
                consumptionSummaryMapper.insertConsumptionSummary(consumeCorrectSummary);
            }else{
                oldConsumeCorrectSummary.setConsumeCorrectNum(consumeCorrectSummary.getConsumeCorrectNum());
                consumptionSummaryMapper.updateConsumptionSummary(oldConsumeCorrectSummary);
            }
        }
    }

    public static void main(String[] args){
        Calendar now = Calendar.getInstance();
        Date time = now.getTime();
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);
        Date endTime = now.getTime();
        now.add(Calendar.DAY_OF_YEAR,-100);
        Date startTime = now.getTime();
        System.out.println("startTime:"+startTime);
        System.out.println("endTime:"+endTime);
        String accountDay = DateUtils.formatDate(startTime,DateUtils.DATE_PATTERN);
        System.out.println("accountDay:"+accountDay);
    }

}
