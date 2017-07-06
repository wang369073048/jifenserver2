package org.trc.service.impl.goods;

import com.github.pagehelper.PageHelper;
import com.txframework.core.jdbc.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.goods.GoodsRecommendDO;
import org.trc.domain.goods.GoodsRecommendDTO;
import org.trc.mapper.goods.IGoodsRecommendMapper;
import org.trc.service.goods.IGoodsRecommendService;
import org.trc.service.impl.BaseService;
import org.trc.util.Pagenation;

import java.util.Calendar;
import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/6/30
 */
@Service("goodsRecommendService")
public class GoodsRecommendService extends BaseService<GoodsRecommendDO,Long> implements IGoodsRecommendService{

    @Autowired
    private IGoodsRecommendMapper goodsRecommendMapper;

    @Override
    public GoodsRecommendDO selectById(Long id) {
        GoodsRecommendDO goodsRecommendDO = new GoodsRecommendDO();
        goodsRecommendDO.setId(id);
        goodsRecommendDO.setIsDeleted(false);
        return goodsRecommendMapper.selectOne(goodsRecommendDO);
    }

    @Override
    public List<GoodsRecommendDO> selectListByParams(GoodsRecommendDO goodsRecommendDO, PageRequest<GoodsRecommendDO> pageRequest) {
        return null;
    }

    @Override
    public Pagenation<GoodsRecommendDTO> selectGoodsRecommendsByPage(GoodsRecommendDTO query, Pagenation<GoodsRecommendDTO> pagenation) {
        List<GoodsRecommendDTO> list = goodsRecommendMapper.selectGoodsRecommendsByPage(query);
        if (list != null){
            PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
            pagenation.setTotalCount(list.size());
            pagenation.setResult(list);
            return pagenation;
        }
        return pagenation;
    }

    @Override
    public int selectCountByParams(GoodsRecommendDO goodsRecommendDO) {
        return 0;
    }

    @Override
    public int updateById(GoodsRecommendDO goodsRecommendDO) {
        goodsRecommendDO.setUpdateTime(Calendar.getInstance().getTime());
        return goodsRecommendMapper.updateByPrimaryKey(goodsRecommendDO);
    }

    @Override
    public int deleteById(Long id) {
        GoodsRecommendDO goodsRecommendDO = new GoodsRecommendDO();
        goodsRecommendDO.setId(id);
        goodsRecommendDO.setIsDeleted(true);
        goodsRecommendDO.setUpdateTime(Calendar.getInstance().getTime());
        return goodsRecommendMapper.updateByPrimaryKey(goodsRecommendDO);
    }

    @Override
    public int getNextSort() {
        return goodsRecommendMapper.getNextSort();
    }

    @Override
    public int selectCountByGoodsId(Long goodsId) {
        return 0;
    }
}
