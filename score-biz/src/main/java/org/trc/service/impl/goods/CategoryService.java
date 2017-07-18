package org.trc.service.impl.goods;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.goods.CategoryDO;
import org.trc.domain.goods.GoodsDO;
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

    @Autowired
    private ICatetoryMapper catetoryMapper;
    @Override
    public List<CategoryDO> selectListByParams(CategoryDO categoryDO) {
        return catetoryMapper.selectListByParams(categoryDO);
    }

    @Override
    public Pagenation<CategoryDO> selectListByParams(CategoryDO categoryDO, Pagenation<CategoryDO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
        List<CategoryDO> list = catetoryMapper.selectListByParams(categoryDO);
        pagenation.setTotalCount(page.getTotal());
        pagenation.setResult(list);
        return pagenation;
    }
}
