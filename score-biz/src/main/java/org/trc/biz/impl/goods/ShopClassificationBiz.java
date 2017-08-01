package org.trc.biz.impl.goods;

import com.txframework.core.jdbc.PageRequest;
import com.txframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.biz.goods.IShopClassificationBiz;
import org.trc.domain.goods.GoodsClassificationRelationshipDO;
import org.trc.domain.goods.ShopClassificationDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BusinessException;
import org.trc.service.goods.IGoodsClassificationRelationshipService;
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

    Logger logger = LoggerFactory.getLogger(ShopClassificationBiz.class);
    @Autowired
    private IShopClassificationService shopClassificationService;
    @Autowired
    private IGoodsClassificationRelationshipService goodsClassificationRelationshipService;
    @Override
    public int insert(ShopClassificationDO shopClassification) {
        return shopClassificationService.insertSelective(shopClassification);
    }

    @Override
    public int delete(ShopClassificationDO shopClassification) {
        _validateForDelete(shopClassification);
        GoodsClassificationRelationshipDO param = new GoodsClassificationRelationshipDO();
        param.setShopClassificationId(shopClassification.getId());
        goodsClassificationRelationshipService.delete(param);
        return shopClassificationService.delete(shopClassification);
    }

    private void _validateForDelete(ShopClassificationDO shopClassification) {
        try {
            Assert.notNull(shopClassification,"参数不能为空!");
            Assert.notNull(shopClassification.getShopId(),"被删除的店铺id不能为空!");
        } catch (IllegalArgumentException e) {
            logger.error("删除ShopClassificationDO参数校验异常!", e);
            throw new BusinessException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "删除ShopClassificationDO参数校验异常!");
        }
    }

    @Override
    public int update(ShopClassificationDO shopClassification) {
        return shopClassificationService.updateByPrimaryKeySelective(shopClassification);
    }

    @Override
    public ShopClassificationDO getEntityByParam(ShopClassificationDO param) {
        return shopClassificationService.selectByPrimaryKey(param.getId());
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
