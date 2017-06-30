package org.trc.service.goods;

import org.trc.IBaseService;
import org.trc.domain.goods.GoodsDO;

/**
 * Created by hzwzhen on 2017/6/22.
 */
public interface IGoodsService extends IBaseService<GoodsDO,Long>{

    /**
     * 根据ID更新信息
     * @param goodsDO GoodsDO
     * @return int
     */
    int updateById(GoodsDO goodsDO);
}
