package org.trc.service.impl.goods;

import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.trc.domain.goods.PurchaseRestrictionsDO;
import org.trc.domain.goods.PurchaseRestrictionsHistoryDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BusinessException;
import org.trc.mapper.goods.IPurchaseRestrictionsMapper;
import org.trc.service.goods.IPurchaseRestrictionsService;
import org.trc.service.impl.BaseService;

import java.util.Calendar;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/31
 */
@Service(value = "purchaseRestrictionsService")
public class PurchaseRestrictionsService extends BaseService<PurchaseRestrictionsDO,Long> implements IPurchaseRestrictionsService{

    Logger logger = LoggerFactory.getLogger(PurchaseRestrictionsService.class);

    @Autowired
    private IPurchaseRestrictionsMapper purchaseRestrictionsMapper;

    /**
     * 商品新增或商品限购信息更新时，调用此方法
     * @param item
     * @return
     */
    @Override
    @Transactional
    public int deal(PurchaseRestrictionsDO item) {
        //1参数校验
        try {
            _validate(item);
            //2限购表没有数据，则初始化数据
            PurchaseRestrictionsDO param = new PurchaseRestrictionsDO();
            param.setGoodsId(item.getGoodsId());
            PurchaseRestrictionsDO entity = purchaseRestrictionsMapper.selectOne(param);
            if(null == entity){
                purchaseRestrictionsMapper.insertSelective(item);
            }
            //3限购表已有数据，则更新数据
            else{
                Date now = Calendar.getInstance().getTime();
                //3.1限购表原商品限购类型为限制，后改为不限制;限购表原商品限购类型为不限制，后改为限制
                if((-1==item.getLimitQuantity().intValue() && -1!=entity.getLimitQuantity().intValue()) || (-1!=item.getLimitQuantity().intValue() && -1==entity.getLimitQuantity().intValue())){
                    //3.1.1原限购信息更新至历史表
                    PurchaseRestrictionsHistoryDO history = new PurchaseRestrictionsHistoryDO();
                    history.setId(entity.getId());
                    history.setGoodsId(entity.getGoodsId());
                    history.setLimitQuantity(entity.getLimitQuantity());
                    history.setVersion(entity.getVersion());
                    history.setCreateTime(now);
                    purchaseRestrictionsMapper.insertHistory(history);
                    //3.1.2原限购信息删除
                    int result = purchaseRestrictionsMapper.delete(param);
                    if(result == 0){
                        logger.error("商品限购信息处理失败!");
                        throw new BusinessException(ExceptionEnum.FAILED_INFORMATION_PROCESSING, "商品限购信息处理失败！");
                    }
                    //3.1.3插入新的限购信息
                    purchaseRestrictionsMapper.insertSelective(item);
                }else if(item.getLimitQuantity().intValue() != entity.getLimitQuantity().intValue()){
                    entity.setLimitQuantity(item.getLimitQuantity());
                    int result = purchaseRestrictionsMapper.updateByGoodsId(entity);
                    if(result == 0){
                        logger.error("商品限购信息处理失败!");
                        throw new BusinessException(ExceptionEnum.FAILED_INFORMATION_PROCESSING, "商品限购信息处理失败！");
                    }
                }
            }
        } catch (IllegalArgumentException e){
            throw new BusinessException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "参数校验未通过!");
        }
        return 1;
    }

    private void _validate(PurchaseRestrictionsDO param) {
        Asserts.notNull(param, "待处理的限购信息不能为空！");
        Asserts.notNull(param.getGoodsId(), "待处理的限购信息商品id不能为空！");
        Asserts.notNull(param.getLimitQuantity(), "待处理的限购信息限购数量不能为空！");
    }
}
