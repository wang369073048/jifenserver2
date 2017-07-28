package org.trc.biz.impl.goods;

import com.txframework.core.jdbc.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.biz.goods.IShopClassificationBiz;
import org.trc.domain.goods.ShopClassificationDO;
import org.trc.service.goods.IShopClassificationService;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/28
 */
@Service(value = "shopClassificationBiz")
public class ShopClassificationBiz implements IShopClassificationBiz{
    @Autowired
    private IShopClassificationService shopClassificationService;
    @Override
    public int insert(ShopClassificationDO shopClassification) {
        return shopClassificationService.insertSelective(shopClassification);
    }

    @Override
    public int delete(ShopClassificationDO shopClassification) {
        return 0;
    }

    @Override
    public int update(ShopClassificationDO shopClassification) {
        return 0;
    }

    @Override
    public ShopClassificationDO getEntityByParam(ShopClassificationDO param) {
        return null;
    }

    @Override
    public Pagenation<ShopClassificationDO> queryEntity(ShopClassificationDO param, Pagenation<ShopClassificationDO> pageRequest) {
        return shopClassificationService.queryEntityByPage(param,pageRequest);
    }

    @Override
    public List<ShopClassificationDO> listEntity(ShopClassificationDO param) {
        return shopClassificationService.listEntity(param);
    }
}
