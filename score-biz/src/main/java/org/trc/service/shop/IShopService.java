package org.trc.service.shop;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.shop.ShopDO;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
public interface IShopService extends IBaseService<ShopDO,Long>{
    /**
     * 根据ID查询表数据
     * @param id Long
     * @return ShopDO
     */
    ShopDO selectById(Long id);

    /**
     * 多条件查询表信息(分页)
     * @param shopDO ShopDO
     * @param pageRequest PageRequest<ShopDO>
     * @return List<ShopDO>
     */
    List<ShopDO> selectListByParams(ShopDO shopDO, PageRequest<ShopDO> pageRequest);
    int selectCountByParams(ShopDO shopDO);

    /**
     * 插入信息
     * @param shopDO ShopDO
     * @return int
     */
    int insert(ShopDO shopDO);

    /**
     * 根据ID更新信息
     * @param shopDO ShopDO
     * @return int
     */
    int updateById(ShopDO shopDO);

    /**
     * 根据ID删除信息
     * @param id Long
     * @return int
     */
    int deleteById(Long id);
}
