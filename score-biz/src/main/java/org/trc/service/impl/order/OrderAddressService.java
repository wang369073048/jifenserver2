package org.trc.service.impl.order;

import com.txframework.core.jdbc.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.order.OrderAddressDO;
import org.trc.mapper.order.IOrderAddressMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.order.IOrderAddressService;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/18
 */
@Service("orderAddressService")
public class OrderAddressService extends BaseService<OrderAddressDO,Long> implements IOrderAddressService{
    @Autowired
    private IOrderAddressMapper orderAddressMapper;
    @Override
    public OrderAddressDO getOrderAddressDOByParams(OrderAddressDO orderAddress) {
        return null;
    }

    @Override
    public List<OrderAddressDO> selectListByParams(OrderAddressDO orderAddressDO, PageRequest<OrderAddressDO> pageRequest) {
        return null;
    }

    @Override
    public int selectCountByParams(OrderAddressDO orderAddressDO) {
        return 0;
    }

    @Override
    public int updateById(OrderAddressDO orderAddressDO) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public OrderAddressDO getOrderAddressDOByOrderNum(String orderNum) {
        return orderAddressMapper.getOrderAddressDOByOrderNum(orderNum);
    }
}
