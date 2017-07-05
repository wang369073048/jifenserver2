package org.trc.mapper.order;

import org.trc.domain.order.LogisticsCodeDO;
import org.trc.domain.order.LogisticsDO;
import org.trc.util.BaseMapper;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/5
 */
public interface ILogisticsMapper extends BaseMapper<LogisticsDO>{

    LogisticsCodeDO getLogisticsCodeDOByCode(String code);

    /**
     * 根据ID更新信息
     * @param logisticsDO LogisticsDO
     * @return int
     */
    int updateById(LogisticsDO logisticsDO);
}
