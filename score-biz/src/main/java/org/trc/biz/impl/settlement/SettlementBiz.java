package org.trc.biz.impl.settlement;

import org.springframework.stereotype.Service;
import org.trc.biz.settlement.ISettlementBiz;
import org.trc.domain.settlement.SettlementDO;
import org.trc.service.settlement.ISettlementService;
import org.trc.util.Pagenation;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/5
 */
@Service("settlementBiz")
public class SettlementBiz implements ISettlementBiz{

    private ISettlementService settlementService;

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
        return settlementService.selectListByParams(settlementDO,pageRequest);
    }

    @Override
    public int updateById(SettlementDO settlementDO) {
        return 0;
    }
}
