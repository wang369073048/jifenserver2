package org.trc.service.goods;

import org.trc.IBaseService;
import org.trc.domain.goods.CategoryDO;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
public interface ICategoryService extends IBaseService<CategoryDO,Long>{

    /**
     * 多条件查询表信息
     * @param categoryDO CategoryDO
     * @return List<CategoryDO>
     */
    List<CategoryDO> selectListByParams(CategoryDO categoryDO);

}
