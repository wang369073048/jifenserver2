package org.trc.mapper.goods;

import org.trc.domain.goods.CategoryDO;
import org.trc.util.BaseMapper;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
public interface ICatetoryMapper extends BaseMapper<CategoryDO>{
    /**
     * 多条件查询表信息
     * @param categoryDO CategoryDO
     * @return List<CategoryDO>
     */
    List<CategoryDO> selectListByParams(CategoryDO categoryDO);
}
