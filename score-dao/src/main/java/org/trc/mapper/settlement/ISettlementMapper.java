package org.trc.mapper.settlement;

import java.util.List;

import org.trc.domain.settlement.SettlementDO;
import org.trc.util.BaseMapper;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/17
 */
public interface ISettlementMapper extends BaseMapper<SettlementDO>{

    /**
     * 多条件查询表信息
     * @param settlementDO
     * @return
     */
    List<SettlementDO> selectListByParams(SettlementDO settlementDO);
}
