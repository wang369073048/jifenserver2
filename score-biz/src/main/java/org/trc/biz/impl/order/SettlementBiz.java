package org.trc.biz.impl.order;

import org.trc.biz.order.ISettlementBiz;
import org.trc.domain.order.SettlementDO;
import org.trc.util.Pagenation;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/5
 */
public class SettlementBiz implements ISettlementBiz{
    @Override
    public SettlementDO getLastSettlement(Long shopId) {
        return null;
    }

    @Override
    public int insert(SettlementDO settlementDO) {
        return 0;
    }

    @Override
    public Pagenation<SettlementDO> queryListByParams(SettlementDO settlementDO, Pagenation<SettlementDO> pageRequest) {
        return null;
    }

    @Override
    public int updateById(SettlementDO settlementDO) {
        return 0;
    }
}
