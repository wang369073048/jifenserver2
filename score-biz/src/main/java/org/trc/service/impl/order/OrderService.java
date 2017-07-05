package org.trc.service.impl.order;

import com.github.pagehelper.PageHelper;
import com.txframework.core.jdbc.PageRequest;
import org.springframework.stereotype.Service;
import org.trc.domain.dto.ExportOrderDTO;
import org.trc.domain.dto.SettlementQuery;
import org.trc.domain.goods.GoodsRecommendDTO;
import org.trc.domain.order.LogisticsDO;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.order.SettlementDO;
import org.trc.mapper.order.IOrderMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.order.IOrderService;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
@Service(value = "orderService")
public class OrderService extends BaseService<OrdersDO,Long> implements IOrderService{

    private IOrderMapper orderMapper;
    @Override
    public OrdersDO selectByParams(OrdersDO ordersDO) {
        return null;
    }

    @Override
    public Pagenation<OrdersDO> selectListByParams(OrdersDO ordersDO, Pagenation<OrdersDO> pagenation) {
        List<OrdersDO> list = orderMapper.selectListByParams(ordersDO,pagenation);
        if (list != null){
            PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
            pagenation.setTotalCount(list.size());
            pagenation.setResult(list);
            return pagenation;
        }
        return pagenation;
    }

    @Override
    public List<OrdersDO> selectOrdersByParams(SettlementQuery settlementQuery, Pagenation<OrdersDO> pageRequest) {
        return null;
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
    public int updateById(OrdersDO ordersDO) {
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
        return null;
    }

    @Override
    public List<ExportOrderDTO> queryOrderAndAddressForExport(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public int confirmOrder(Map<String, Object> params) {
        return 0;
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
