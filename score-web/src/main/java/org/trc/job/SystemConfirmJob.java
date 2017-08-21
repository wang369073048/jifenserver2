package org.trc.job;

import com.trc.mall.constants.OrderStatus;
import com.trc.mall.model.OrdersDO;
import com.trc.mall.query.SettlementQuery;
import com.trc.mall.service.NewOrderService;
import com.trc.mall.util.IpUtil;
import com.txframework.core.jdbc.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 */
public class SystemConfirmJob extends BaseJob{

    private Logger logger = LoggerFactory.getLogger(SystemConfirmJob.class);

    @Resource
    private NewOrderService orderService;

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
        PageRequest<OrdersDO> pageRequest = new PageRequest<OrdersDO>();
        pageRequest.setPageData(1000);
        SettlementQuery settlementQuery = new SettlementQuery();
        List<Integer> ordersStates = new ArrayList<>();
        ordersStates.add(OrderStatus.WAITING_FOR_RECEIVING);
        settlementQuery.setOrderStates(ordersStates);
        PageRequest<OrdersDO> result = orderService.queryOrdersByParams(settlementQuery, pageRequest);
        //2：循环处理待处理的订单
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);
        Date time = now .getTime();
        for(int i = 0 ; i < result.getTotalPage(); i++){
            orderService.confirmOrder(OrderStatus.SYSTEM_CONFIRM_SUCCESS, OrderStatus.WAITING_FOR_RECEIVING, time);
        }
        logger.info("--SystemConfirmJob end--");
    }

}
