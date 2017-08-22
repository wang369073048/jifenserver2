package org.trc.job;


import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.order.INewOrderBiz;
import org.trc.constants.OrderStatus;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.query.SettlementQuery;
import org.trc.util.Pagenation;

import com.trc.mall.util.IpUtil;
import com.txframework.core.jdbc.PageRequest;

/**
 *
 */
public class SystemConfirmJob extends BaseJob{

    private Logger logger = LoggerFactory.getLogger(SystemConfirmJob.class);

    @Resource
    private INewOrderBiz newOrderBiz;

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
            logger.error("SystemConfirmJob!跳过任务!");
            return ;
        }
        logger.info("--SystemConfirmJob start--");
        //1：查询待处理的订单总数
        Pagenation<OrdersDO> pageRequest = new Pagenation<OrdersDO>();
        pageRequest.setPageSize(1000);
        SettlementQuery settlementQuery = new SettlementQuery();
        List<Integer> ordersStates = new ArrayList<>();
        ordersStates.add(OrderStatus.WAITING_FOR_RECEIVING);
        settlementQuery.setOrderStates(ordersStates);
        Pagenation<OrdersDO> result = newOrderBiz.queryOrdersByParams(settlementQuery, pageRequest);
        //2：循环处理待处理的订单
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);
        Date time = now .getTime();
        for(int i = 0 ; i < result.getTotalPage(); i++){
        	newOrderBiz.confirmOrder(OrderStatus.SYSTEM_CONFIRM_SUCCESS, OrderStatus.WAITING_FOR_RECEIVING, time);
        }
        logger.info("--SystemConfirmJob end--");
    }

}
