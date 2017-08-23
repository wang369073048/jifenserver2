package org.trc.service.impl.goods;

import org.springframework.stereotype.Service;
import org.trc.domain.goods.GoodsSnapshotDO;
import org.trc.service.goods.IGoodsSnapshotService;
import org.trc.service.impl.BaseService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/22
 */
@Service("goodsSnapshotService")
public class GoodsSnapshotService extends BaseService<GoodsSnapshotDO,Long> implements IGoodsSnapshotService{
}
