package org.trc.service.impl.shop;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.txframework.core.jdbc.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.dto.GoodsRecommendDTO;
import org.trc.domain.shop.ShopDO;
import org.trc.mapper.shop.IShopMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.shop.IShopService;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
@Service(value = "shopService")
public class ShopService extends BaseService<ShopDO,Long> implements IShopService{
    @Autowired
    private IShopMapper shopMapper;
    @Override
    public ShopDO selectById(Long id) {
        return shopMapper.selectByPrimaryKey(id);
    }

    @Override
    public Pagenation<ShopDO> selectListByPage(ShopDO query, Pagenation<ShopDO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageIndex(), pagenation.getPageSize());
        List<ShopDO> list = shopMapper.select(query);
        pagenation.setTotalData(page.getTotal());
        pagenation.setInfos(list);
        return pagenation;
    }

    @Override
    public int selectCountByParams(ShopDO shopDO) {
        return 0;
    }

    @Override
    public int insert(ShopDO shopDO) {
        return 0;
    }

    @Override
    public int updateById(ShopDO shopDO) {
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }
}
