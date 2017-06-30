package org.trc.service.impl.goods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.goods.CategoryDO;
import org.trc.mapper.goods.ICatetoryMapper;
import org.trc.service.goods.ICategoryService;
import org.trc.service.impl.BaseService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/29
 */
@Service("categoryService")
public class CategoryService extends BaseService<CategoryDO,Long> implements ICategoryService{


}
