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

	/**
	 * 根据ID查询表数据
	 * @param logisticsDO LogisticsDO
	 * @return LogisticsDO
	 */
	LogisticsDO selectByParams(LogisticsDO logisticsDO);
	
    LogisticsCodeDO getLogisticsCodeDOByCode(String code);

    /**
     * 根据ID更新信息
     * @param logisticsDO LogisticsDO
     * @return int
     */
    int updateById(LogisticsDO logisticsDO);
}
