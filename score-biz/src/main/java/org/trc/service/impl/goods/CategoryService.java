package org.trc.service.impl.goods;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.trc.domain.goods.CategoryDO;
import org.trc.domain.goods.GoodsDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.CategoryException;
import org.trc.mapper.goods.ICatetoryMapper;
import org.trc.service.goods.ICategoryService;
import org.trc.service.impl.BaseService;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/6/29
 */
@Service("categoryService")
public class CategoryService extends BaseService<CategoryDO,Long> implements ICategoryService{

    Logger logger = LoggerFactory.getLogger(CategoryService.class);
    @Autowired
    private ICatetoryMapper catetoryMapper;
    @Override
    public List<CategoryDO> selectListByParams(CategoryDO categoryDO) {
        return catetoryMapper.selectListByParams(categoryDO);
    }

    @Override
    public Pagenation<CategoryDO> selectListByParams(CategoryDO categoryDO, Pagenation<CategoryDO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageIndex(), pagenation.getPageSize());
        List<CategoryDO> list = catetoryMapper.selectListByParams(categoryDO);
        pagenation.setTotalData(page.getTotal());
        pagenation.setInfos(list);
        return pagenation;
    }

    @Override
    public int updateIsDeletedById(CategoryDO categoryDO) {
        return catetoryMapper.updateIsDeletedById(categoryDO);
    }

    @Override
    public CategoryDO getCategoryDOById(Long id) {
        try {
            Assert.notNull(id, "查询Id不能为空!");
            CategoryDO param = new CategoryDO();
            param.setId(id);
            param.setIsDeleted(false);
            CategoryDO categoryDO = catetoryMapper.selectOne(param);
            if (null == categoryDO) {
                throw new CategoryException(ExceptionEnum.ENTITY_NOT_EXIST, "查询结果为空!");
            }
            return categoryDO;
        } catch (IllegalArgumentException e) {
            logger.error("查询CategoryDO传入Id为空!", e);
            throw new CategoryException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "查询CategoryDO传入Id为空!");
        } catch (CategoryException e){
            throw e;
        } catch (Exception e) {
            logger.error("根据ID=>[" + id + "]查询CategoryDO信息异常!", e);
            throw new CategoryException(ExceptionEnum.GET_EXCEPTION, "根据ID=>[" + id + "]查询CategoryDO信息异常!");
        }
    }
}
