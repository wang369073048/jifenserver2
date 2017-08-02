package org.trc.service.goods;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.goods.CategoryDO;
import org.trc.util.Pagenation;

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

    /**
     * 多条件查询表信息(分页)
     * @param categoryDO CategoryDO
     * @param pageRequest PageRequest<CategoryDO>
     * @return List<CategoryDO>
     */
    Pagenation<CategoryDO> selectListByParams(CategoryDO categoryDO, Pagenation<CategoryDO> pageRequest);

    /**
     * 根据ID更新删除状态
     * @param categoryDO CategoryDO
     * @return int
     */
    int updateIsDeletedById(CategoryDO categoryDO);

}
