package org.trc.job;

import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.settlement.IScoreSettlementBiz;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.score.ScoreSettlement;
import org.trc.domain.settlement.SettlementDO;
import org.trc.domain.shop.ShopDO;
import org.trc.mapper.order.IOrderMapper;
import org.trc.mapper.settlement.ISettlementMapper;
import org.trc.mapper.shop.IShopMapper;
import org.trc.util.IpUtil;
import org.trc.util.Pagenation;
import org.trc.util.SNUtil;

import com.txframework.util.DateUtils;

/**
 * Created by george on 2016/12/5.
 */
public class SettlementJob extends BaseJob{

    private Logger logger = LoggerFactory.getLogger(SettlementJob.class);

    @Autowired
    private IOrderMapper ordersMapper;

    @Autowired
    private IShopMapper shopMapper;

    @Autowired
    private ISettlementMapper settlementMapper;

    @Autowired
    private IScoreSettlementBiz scoreSettlementBiz;

    /**
     * @description 执行任务
     */
    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    public void execute() {
        //ip检查，补偿任务暂时单机进行
        try {
            String realIp = IpUtil.getRealIp();
            if(!realIp.equals(this.getTaskIp())){
                logger.info("非任务补偿机，跳过任务!"+realIp+"!="+this.getTaskIp());
                return ;
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.error("SettlementJob!跳过任务!");
            return ;
        }
        logger.info("--SettlementJob start--");
        Pagenation<ShopDO> pageRequest = new Pagenation<ShopDO>();
        pageRequest.setPageSize(5000);
        List<ShopDO> shopDOList= shopMapper.selectListByParams(new ShopDO(), pageRequest);
        //获取当前日结时间
        Calendar now = Calendar.getInstance();
        Date time = now.getTime();
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);
        Date dailySettlementTime = now.getTime();
        logger.info("--SettlementJob 日结时间:" + dailySettlementTime + ",账户个数:" + pageRequest.getTotalCount());
        for(ShopDO shop : shopDOList){
            SettlementDO lastSettlement = settlementMapper.getLastSettlement(shop.getId());
            if(null!=lastSettlement){
                Date startDate = lastSettlement.getEndTime();
                startDate = DateUtils.addDay(startDate,1);
                for(;!startDate.after(dailySettlementTime);startDate = DateUtils.addDay(startDate,1)){
                    settlement(shop, startDate, time);
                }
            }else {
                Date startDate = shop.getCreateTime();
                Calendar cal = Calendar.getInstance();
                cal.setTime(startDate);
                cal.add(Calendar.DAY_OF_YEAR,1);
                cal.set(Calendar.HOUR_OF_DAY,0);
                cal.set(Calendar.MINUTE,0);
                cal.set(Calendar.SECOND,0);
                cal.set(Calendar.MILLISECOND,0);
                startDate = cal.getTime();
                for(;!startDate.after(dailySettlementTime);startDate = DateUtils.addDay(startDate,1)){
                    settlement(shop, startDate, time);
                }
            }
        }
        logger.info("--SettlementJob end--");
    }

    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    private void settlement(ShopDO shop, Date dailySettlementTime, Date time){
        String userId = shop.getUserId();
        if( null==userId ){
            logger.error("店铺shopId:"+shop.getId()+"所有者不存在");
            return ;
        }
        Date startTime = DateUtils.addDay(dailySettlementTime,-1);
        String accountDay = DateUtils.formatDate(startTime,DateUtils.DATE_PATTERN);
        Date preAccountTime = DateUtils.addDay(startTime,-1);
        String preAccountDay = DateUtils.formatDate(preAccountTime,DateUtils.DATE_PATTERN);
        ScoreSettlement scoreSettlement = scoreSettlementBiz.getScoreSettlementByUserIdAndAccountDay(userId,accountDay);
        ScoreSettlement preScoreSettlement = scoreSettlementBiz.getScoreSettlementByUserIdAndAccountDay(userId,preAccountDay);
        SettlementQuery settlementQuery = new SettlementQuery();
        settlementQuery.setShopId(shop.getId());
        settlementQuery.setStartTime(startTime);
        settlementQuery.setEndTime(dailySettlementTime);
        SettlementDO settlementDO = ordersMapper.getSettlementByParams(settlementQuery);
        if(null!=settlementDO && settlementDO.getTotalAmount() > 0) {
            settlementDO.setSettlementState(0);
            if(null!=preScoreSettlement) {
                settlementDO.setPreviousBalance(preScoreSettlement.getDailyBalance());
            }else{
                settlementDO.setPreviousBalance(0l);
            }
            if(null!=scoreSettlement) {
                settlementDO.setBalance(scoreSettlement.getDailyBalance());
            }else{
                settlementDO.setBalance(0l);
            }
            settlementDO.setShopName(shop.getShopName());
            settlementDO.setBillNum(SNUtil.createSeriesNumber());
            settlementDO.setAccountDay(accountDay);
            settlementDO.setCreateTime(time);
            settlementMapper.insert(settlementDO);
        }else{
            SettlementDO settlement = new SettlementDO();
            settlement.setSettlementState(0);
            if(null!=preScoreSettlement) {
                settlement.setPreviousBalance(preScoreSettlement.getDailyBalance());
            }else{
                settlement.setPreviousBalance(0l);
            }
            if(null!=scoreSettlement) {
                settlement.setBalance(scoreSettlement.getDailyBalance());
            }else{
                settlement.setBalance(0l);
            }
            settlement.setQuantity(0);
            settlement.setTotalAmount(0l);
            settlement.setTotalFreight(0l);
            settlement.setShopId(shop.getId());
            settlement.setShopName(shop.getShopName());
            settlement.setStartTime(startTime);
            settlement.setEndTime(dailySettlementTime);
            settlement.setBillNum(SNUtil.createSeriesNumber());
            settlement.setAccountDay(accountDay);
            settlement.setCreateTime(time);
            settlementMapper.insert(settlement);
        }
    }


}
