package org.trc.biz.impl.goods;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.trc.biz.goods.ICategoryBiz;
import org.trc.domain.goods.CategoryDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.CategoryException;
import org.trc.service.goods.ICategoryService;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * Created by hzwzhen on 2017/6/22.
 */
@Service("categoryBiz")
public class CategoryBiz implements ICategoryBiz {

    Logger logger = LoggerFactory.getLogger(CategoryBiz.class);

    @Autowired
    private ICategoryService categoryService;

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
        try {
            Assert.notNull(id, "查询Id不能为空!");
            CategoryDO categoryDO = new CategoryDO();
            categoryDO.setId(id);
            categoryDO.setIsDeleted(false);
            categoryDO = categoryService.selectOne(categoryDO);
            if (null == categoryDO) {
                throw new CategoryException(ExceptionEnum.CATEGORY_ID_NOT_EXIST, "查询结果为空!");
            }
            return categoryDO;
        } catch (IllegalArgumentException e) {
            logger.error("查询CategoryDO传入Id为空!", e);
            throw new CategoryException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "查询CategoryDO传入Id为空!");
        } catch (CategoryException e){
            throw e;
        } catch (Exception e) {
            logger.error(String.format("根据ID=>[%s]查询CategoryDO信息异常!",id));
            throw new CategoryException(ExceptionEnum.CATEGORY_QUERY_EXCEPTION, "根据ID=>[" + id + "]查询CategoryDO信息异常!");
        }
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
