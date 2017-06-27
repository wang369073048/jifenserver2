package org.trc.biz.goods;

import org.trc.domain.goods.CategoryDO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * Created by hzwzhen on 2017/6/22.
 */
public interface ICategoryBiz {

    /**
     * 多条件查询(分页)
     * @param categoryDO CategoryDO
     * @param pageRequest Pagenation<CategoryDO>
     * @return Pagenation<CategoryDO>
     */
    Pagenation<CategoryDO> queryCategoryDOListForPage(CategoryDO categoryDO, Pagenation<CategoryDO> pageRequest);

    /**
     * 查询分类
     * @param categoryDO
     * @return
     */
    List<CategoryDO> selectCategoryDOList(CategoryDO categoryDO);

    /**
     * 根据用户ID查询
     * @param id Long
     * @return CategoryDO
     */
    CategoryDO getCategoryDOById(Long id);

    /**
     * 根据用户ID查询
     * @param code String
     * @return CategoryDO
     */
    CategoryDO getCategoryDOByCode(String code);

    /**
     * 添加
     * @param categoryDO CategoryDO
     */
    void addCategoryDO(CategoryDO categoryDO);

    /**
     * 修改
     * @param categoryDO CategoryDO
     * @return int
     */
    int modifyCategoryDO(CategoryDO categoryDO);

    /**
     * 删除类目
     * @param categoryDO CategoryDO
     * @return int
     */
    int deleteCategoryDO(CategoryDO categoryDO);
}
