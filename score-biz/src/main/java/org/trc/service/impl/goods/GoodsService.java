package org.trc.service.impl.goods;

import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.goods.GoodsDO;
import org.trc.domain.goods.GoodsRecommendDTO;
import org.trc.mapper.goods.IGoodsMapper;
import org.trc.service.goods.IGoodsService;
import org.trc.service.impl.BaseService;
import org.trc.util.Pagenation;

import java.util.List;

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
        return goodsMapper.updateByPrimaryKey(goodsDO);
    }

    @Override//TODO 测试分页
    public Pagenation<GoodsDO> queryGoodsDOListExceptRecommendForPage(GoodsDO query, Pagenation<GoodsDO> pagenation) {
        List<GoodsDO> list = goodsMapper.selectListExceptRecommendByPage(query);
        if (list != null){
            PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
            pagenation.setTotalCount(list.size());
            pagenation.setResult(list);
            return pagenation;
        }
        return null;
    }
}
