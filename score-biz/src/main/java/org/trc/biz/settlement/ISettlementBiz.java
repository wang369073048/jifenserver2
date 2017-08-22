package org.trc.biz.settlement;

import org.trc.domain.settlement.SettlementDO;
import org.trc.util.Pagenation;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/5
 */
public interface ISettlementBiz {
    /**
     * 获取最新的结算信息
     * @param shopId
     * @return
     */
    SettlementDO getLastSettlement(Long shopId);

    /**
     * 插入信息
     * @param settlementDO SettlementDO
     * @return int
     */
    int insert(SettlementDO settlementDO);

    /**
     * 多条件查询表信息(分页)
     * @param settlementDO
     * @param pageRequest
     * @return
     */
    Pagenation<SettlementDO> queryListByParams(SettlementDO settlementDO, Pagenation<SettlementDO> pageRequest);

    /**
     * 根据ID更新信息
     * @param settlementDO SettlementDO
     * @return int
     */
    int updateById(SettlementDO settlementDO);
}
