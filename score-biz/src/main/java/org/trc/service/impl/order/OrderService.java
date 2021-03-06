package org.trc.service.impl.order;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.dto.ExportOrderDTO;
import org.trc.domain.dto.OrderDTO;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.settlement.SettlementDO;
import org.trc.mapper.order.IOrderMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.order.IOrderService;
import org.trc.util.Pagenation;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
@Service(value = "orderService")
public class OrderService extends BaseService<OrdersDO,Long> implements IOrderService{

    @Autowired
    private IOrderMapper orderMapper;


    @Override
    public Pagenation<OrdersDO> selectListByParams(OrderDTO ordersDO, Pagenation<OrdersDO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageIndex(), pagenation.getPageSize());
        List<OrdersDO> list = orderMapper.selectListByParams(ordersDO);
        pagenation.setTotalData(page.getTotal());
        pagenation.setInfos(list);
        return pagenation;
    }

    @Override
    public Pagenation<OrdersDO> selectOrdersByParams(SettlementQuery settlementQuery, Pagenation<OrdersDO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageIndex(), pagenation.getPageSize());
        List<OrdersDO> list = orderMapper.selectOrdersByParams(settlementQuery);
        pagenation.setTotalData(page.getTotal());
        pagenation.setInfos(list);
        return pagenation;
    }

    @Override
    public Pagenation<OrderDTO> selectRefundOrdersByParams(SettlementQuery settlementQuery, Pagenation<OrderDTO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageIndex(), pagenation.getPageSize());
        List<OrderDTO> list = orderMapper.selectRefundOrdersByParams(settlementQuery);
        pagenation.setTotalData(page.getTotal());
        pagenation.setInfos(list);
        return pagenation;
    }

    @Override
    public List<OrderDTO> queryRefundOrdersByParamsForExport(SettlementQuery settlementQuery) {
        return orderMapper.selectRefundOrdersByParams(settlementQuery);
    }

    @Override
    public int selectCountByParams(OrdersDO ordersDO) {
        return 0;
    }

    @Override
    public int insert(OrdersDO ordersDO) {
        return 0;
    }



    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public SettlementDO getSettlementByParams(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public List<ExportOrderDTO> queryOrdersForExport(SettlementQuery settlementQuery) {
        return orderMapper.queryOrdersForExport(settlementQuery);
    }

    @Override
    public List<ExportOrderDTO> queryOrderAndAddressForExport(SettlementQuery settlementQuery) {
        return orderMapper.queryOrderAndAddressForExport(settlementQuery);
    }

    @Override
    public int confirmOrder(Map<String, Object> params) {
        return orderMapper.confirmOrder(params);
    }

    @Override
    public int quantityToBeDelivered(OrdersDO order) {
        return 0;
    }

    @Override
    public int quantityToBeReceived(OrdersDO order) {
        return 0;
    }
}
