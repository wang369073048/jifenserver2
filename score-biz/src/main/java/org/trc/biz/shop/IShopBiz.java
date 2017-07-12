package org.trc.biz.shop;

import com.txframework.core.jdbc.PageRequest;
import org.trc.domain.shop.ManagerDO;
import org.trc.domain.shop.ShopDO;
import org.trc.util.Pagenation;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/4
 */
public interface IShopBiz {

    /**
     * 多条件查询(分页)
     * @param shopDO ShopDO
     * @param pageRequest PageRequest<ShopDO>
     * @return PageRequest<ShopDO>
     */
    Pagenation<ShopDO> queryShopDOListForPage(ShopDO shopDO, Pagenation<ShopDO> pageRequest);

    /**
     * 根据用户ID查询
     * @param id Long
     * @return ResultModel<ShopDO>
     */
    ShopDO getShopDOById(Long id);

    /**
     * 添加
     * @param shopDO ShopDO
     * @return
     */
    int addShopDO(ShopDO shopDO);

    /**
     * 修改
     * @param shopDO ShopDO
     * @return
     */
    int modifyShopDO(ShopDO shopDO);

    ManagerDO getManagerByUserId(String userId);
}
