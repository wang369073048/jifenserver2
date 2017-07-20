package org.trc.service.impl.goods;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.goods.GoodsDO;
import org.trc.mapper.goods.IGoodsMapper;
import org.trc.service.goods.IGoodsService;
import org.trc.service.impl.BaseService;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * Created by hzwzhen on 2017/6/22.
 */
@Service("goodsService")
public class GoodsService extends BaseService<GoodsDO,Long> implements IGoodsService{

    @Autowired
    private IGoodsMapper goodsMapper;

    @Override
    public int updateById(GoodsDO goodsDO) {
        return goodsMapper.updateById(goodsDO);
    }

    @Override
    public int deleteByParam(GoodsDO goodsDO) {
        return goodsMapper.deleteByParam(goodsDO);
    }

    @Override
    public int upOrDownById(GoodsDO goodsDO) {
        return goodsMapper.updateByPrimaryKeySelective(goodsDO);
    }

    @Override
    public Pagenation<GoodsDO> selectListByParams(GoodsDO goodsDO, Pagenation<GoodsDO> pagenation) {
        //int totalCount = goodsMapper.selectCount(goodsDO);
        Page page = PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
        List<GoodsDO> list = goodsMapper.selectListByParams(goodsDO);
        pagenation.setTotalCount(page.getTotal());
        pagenation.setResult(list);
        return pagenation;
    }

    @Override
    public Pagenation<GoodsDO> queryGoodsDOListExceptRecommendForPage(GoodsDO query, Pagenation<GoodsDO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
        List<GoodsDO> list = goodsMapper.selectListExceptRecommendByPage(query);
        pagenation.setTotalCount(page.getTotal());
        pagenation.setResult(list);
        return pagenation;
    }

    @Override
    public int isOwnerOf(Map<String, Object> params) {
        return goodsMapper.isOwnerOf(params);
    }
}
