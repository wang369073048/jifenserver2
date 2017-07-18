package org.trc.service.order;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.order.OrderAddressDO;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/18
 */
public interface IOrderAddressService extends IBaseService<OrderAddressDO,Long>{

    /**
     * 根据ID查询表数据
     * @param orderAddress
     * @return OrderAddressDO
     */
    OrderAddressDO getOrderAddressDOByParams(OrderAddressDO orderAddress);

    /**
     * 多条件查询表信息(分页)
     * @param orderAddressDO OrderAddressDO
     * @param pageRequest PageRequest<OrderAddressDO>
     * @return List<OrderAddressDO>
     */
    List<OrderAddressDO> selectListByParams(OrderAddressDO orderAddressDO, PageRequest<OrderAddressDO> pageRequest);
    int selectCountByParams(OrderAddressDO orderAddressDO);

    /**
     * 插入信息
     * @param orderAddressDO OrderAddressDO
     * @return int
     */
    int insert(OrderAddressDO orderAddressDO);

    /**
     * 根据ID更新信息
     * @param orderAddressDO OrderAddressDO
     * @return int
     */
    int updateById(OrderAddressDO orderAddressDO);

    /**
     * 根据ID删除信息
     * @param id Long
     * @return int
     */
    int deleteById(Long id);

    OrderAddressDO getOrderAddressDOByOrderNum(String orderNum);
}
