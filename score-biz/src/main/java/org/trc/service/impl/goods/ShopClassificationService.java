package org.trc.service.impl.goods;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.goods.GoodsClassificationRelationshipDO;
import org.trc.domain.goods.ShopClassificationDO;
import org.trc.mapper.goods.IShopClassificationMapper;
import org.trc.service.goods.IShopClassificationService;
import org.trc.service.impl.BaseService;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/28
 */
@Service("shopClassificationService")
public class ShopClassificationService extends BaseService<ShopClassificationDO,Long> implements IShopClassificationService{

    @Autowired
    private IShopClassificationMapper shopClassificationMapper;
    @Override
    public Pagenation<ShopClassificationDO> queryEntityByPage(ShopClassificationDO param, Pagenation<ShopClassificationDO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
        List<ShopClassificationDO> list = shopClassificationMapper.queryEntity(param);
        pagenation.setTotalCount(page.getTotal());
        pagenation.setResult(list);
        return pagenation;
    }

    @Override
    public List<ShopClassificationDO> listEntity(ShopClassificationDO param) {
        return shopClassificationMapper.queryEntity(param);
    }

    @Override
    public List<ShopClassificationDO> listEntityByParam(GoodsClassificationRelationshipDO param) {
        return shopClassificationMapper.listEntityByParam(param);
    }
}
