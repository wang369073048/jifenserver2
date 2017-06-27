package org.trc.biz.impl.goods;

import org.springframework.stereotype.Service;
import org.trc.biz.goods.ICategoryBiz;
import org.trc.domain.goods.CategoryDO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * Created by hzwzhen on 2017/6/22.
 */
@Service("categoryBiz")
public class CategoryBiz implements ICategoryBiz {
    @Override
    public Pagenation<CategoryDO> queryCategoryDOListForPage(CategoryDO categoryDO, Pagenation<CategoryDO> pageRequest) {
        return null;
    }

    @Override
    public List<CategoryDO> selectCategoryDOList(CategoryDO categoryDO) {
        return null;
    }

    @Override
    public CategoryDO getCategoryDOById(Long id) {
        return null;
    }

    @Override
    public CategoryDO getCategoryDOByCode(String code) {
        return null;
    }

    @Override
    public void addCategoryDO(CategoryDO categoryDO) {

    }

    @Override
    public int modifyCategoryDO(CategoryDO categoryDO) {
        return 0;
    }

    @Override
    public int deleteCategoryDO(CategoryDO categoryDO) {
        return 0;
    }
}
