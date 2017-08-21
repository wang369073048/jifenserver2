package org.trc.mapper.settlement;

import org.trc.domain.order.SettlementDO;
import org.trc.util.BaseMapper;

import java.util.List;

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
