package org.trc.service.settlement;

import org.trc.IBaseService;
import org.trc.domain.settlement.SettlementDO;
import org.trc.util.Pagenation;


/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/17
 */
public interface ISettlementService extends IBaseService<SettlementDO,Long>{

    /**
     * 多条件查询表信息(分页)
     * @param settlementDO
     * @param pageRequest
     * @return
     */
    Pagenation<SettlementDO> selectListByParams(SettlementDO settlementDO, Pagenation<SettlementDO> pageRequest);
}
