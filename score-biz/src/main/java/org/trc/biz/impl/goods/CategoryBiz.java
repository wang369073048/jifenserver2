package org.trc.biz.impl.goods;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.trc.biz.goods.ICategoryBiz;
import org.trc.domain.goods.CategoryDO;
import org.trc.domain.goods.GoodsDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.CategoryException;
import org.trc.service.goods.ICategoryService;
import org.trc.service.goods.IGoodsService;
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
    @Autowired
    private IGoodsService goodsService;

    @Override
    public Pagenation<CategoryDO> queryCategoryDOListForPage(CategoryDO categoryDO, Pagenation<CategoryDO> pageRequest) {
        try {
            Assert.notNull(pageRequest, "分页参数不能为空");
            Assert.notNull(categoryDO, "传入参数不能为空");
            return categoryService.selectListByParams(categoryDO, pageRequest);
        } catch (IllegalArgumentException e) {
            logger.error("多条件查询CategoryDO校验参数异常!", e);
            throw new CategoryException(ExceptionEnum.CATEGORY_QUERY_EXCEPTION, "多条件查询CategoryDO校验参数异常!");
        } catch (Exception e) {
            logger.error("多条件查询CategoryDO信息异常!", e);
            throw new CategoryException(ExceptionEnum.CATEGORY_QUERY_EXCEPTION, "多条件查询CategoryDO信息异常!");
        }
    }

    @Override
    public List<CategoryDO> selectCategoryDOList(CategoryDO categoryDO) {
        try {
            Assert.notNull(categoryDO, "传入参数不能为空");
            List<CategoryDO> list = categoryService.selectListByParams(categoryDO);
            return list;
        } catch (IllegalArgumentException e) {
            logger.error("多条件查询CategoryDO校验参数异常!", e);
            throw new CategoryException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "多条件查询CategoryDO校验参数异常!");
        } catch (Exception e) {
            logger.error("多条件查询CategoryDO信息异常!", e);
            throw new CategoryException(ExceptionEnum.CATEGORY_QUERY_EXCEPTION, "多条件查询CategoryDO信息异常!");
        }
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
        try {
            validateForAdd(categoryDO);
            categoryService.insertSelective(categoryDO);
            logger.info("新增ID=>[" + categoryDO.getId() + "]的CategoryDO成功");
        } catch (IllegalArgumentException e) {
            logger.error("新增CategoryDO校验参数异常!", e);
            throw new CategoryException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "新增CategoryDO校验参数异常!");
        } catch (Exception e) {
            logger.error("新增CategoryDO校验参数异常!", e);
            throw new CategoryException(ExceptionEnum.INSERT_EXCEPTION, "新增CategoryDO校验参数异常!");
        }
    }

    /**
     * Validate Add
     *
     * @param categoryDO CategoryDO
     */
    private void validateForAdd(CategoryDO categoryDO) {
        Assert.notNull(categoryDO, "categoryDO不能为空!");
        Assert.isTrue(StringUtils.isNotBlank(categoryDO.getCategoryName()), "类目名称不能为空!");
        Assert.isTrue(categoryDO.getIsVirtual() == 0||categoryDO.getIsVirtual() == 1, "是否虚拟类目可选值只有0或者1!");
        Assert.isTrue(categoryDO.getSort() > 0, "排序不能为空!");
        Assert.isTrue(StringUtils.isNotBlank(categoryDO.getOperatorUserId()), "操作人ID不能为空!");
    }

    @Override
    public int modifyCategoryDO(CategoryDO categoryDO) {
        try {
            validateForUpdate(categoryDO);
            int result = categoryService.updateByPrimaryKey(categoryDO);
            if (result != 1) {
                throw new CategoryException(ExceptionEnum.UPDATE_EXCEPTION, "修改ID=>[" + categoryDO.getId() + "]的CategoryDO信息异常!");
            }
            logger.info("修改ID=>[" + categoryDO.getId() + "]的CategoryDO成功!");
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("修改CategoryDO校验参数异常!", e);
            throw new CategoryException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "修改CategoryDO校验参数异常!");
        } catch (CategoryException e){
            throw e;
        } catch (Exception e) {
            logger.error("修改ID=>[" + categoryDO.getId() + "]的CategoryDO信息异常", e);
            throw new CategoryException(ExceptionEnum.UPDATE_EXCEPTION, "修改ID=>[" + categoryDO.getId() + "]的CategoryDO信息异常!");
        }
    }

    /**
     * Validate Update
     *
     * @param categoryDO CategoryDO
     * @return CategoryDO
     */
    private CategoryDO validateForUpdate(CategoryDO categoryDO) {
        Assert.notNull(categoryDO, "CategoryDO不能为空!");
        Assert.notNull(categoryDO.getId(), "查询Id不能为空!");
        Assert.isTrue(StringUtils.isNotBlank(categoryDO.getCategoryName()), "类目名称不能为空!");
        Assert.isTrue(categoryDO.getSort() > 0, "排序不能为空!");
        Assert.isTrue(StringUtils.isNotBlank(categoryDO.getOperatorUserId()), "操作人ID不能为空!");
        CategoryDO param = new CategoryDO();
        param.setId(categoryDO.getId());
        param.setIsDeleted(false);
        CategoryDO oldCategoryDO = categoryService.selectOne(param);
        Assert.notNull(oldCategoryDO, "查询不到ID=>[" + categoryDO.getId() + "]的信息!");
        return oldCategoryDO;
    }

    @Override
    public int deleteCategoryDO(CategoryDO categoryDO) {
        try {
            Assert.notNull(categoryDO, "CategoryDO不能为空!");
            Assert.notNull(categoryDO.getId(), "ID不能为空!");
            Assert.isTrue(StringUtils.isNotBlank(categoryDO.getOperatorUserId()), "操作人ID不能为空!");
            GoodsDO query = new GoodsDO();
            query.setCategory(categoryDO.getId());
            int goodsCount = goodsService.selectCountByParams(query);
            if (goodsCount > 0) {
                throw new CategoryException(ExceptionEnum.UPDATE_EXCEPTION, "该类目下还有商品，不允许删除!");
            }
            int result = categoryService.updateIsDeletedById(categoryDO);
            if (result < 1) {
                throw new CategoryException(ExceptionEnum.UPDATE_EXCEPTION, "删除ID=>[" + categoryDO.getId() + "]的CategoryDO信息异常!");
            }
            logger.info("逻辑删除ID=>[" + categoryDO.getId() + "]的CategoryDO成功!");
            return result;
        } catch (IllegalArgumentException e) {
            logger.error("删除CategoryDO校验参数异常!", e);
            throw new CategoryException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "删除CategoryDO校验参数异常!");
        } catch (CategoryException e){
            throw e;
        } catch (Exception e) {
            logger.error("删除ID=>[" + categoryDO.getId() + "]的CategoryDO信息异常!", e);
            throw new CategoryException(ExceptionEnum.UPDATE_EXCEPTION, "删除ID=>[" + categoryDO.getId() + "]的CategoryDO信息异常!");
        }
    }
}
