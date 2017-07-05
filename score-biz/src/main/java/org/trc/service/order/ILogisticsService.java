package org.trc.service.order;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.order.LogisticsCodeDO;
import org.trc.domain.order.LogisticsDO;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/5
 */
public interface ILogisticsService extends IBaseService<LogisticsDO,Long> {

    /**
     * 根据ID查询表数据
     * @param logisticsDO LogisticsDO
     * @return LogisticsDO
     */
    LogisticsDO selectByParams(LogisticsDO logisticsDO);

    /**
     * 多条件查询表信息(分页)
     * @param logisticsDO LogisticsDO
     * @param pageRequest PageRequest<LogisticsDO>
     * @return List<LogisticsDO>
     */
    List<LogisticsDO> selectListByParams(LogisticsDO logisticsDO, PageRequest<LogisticsDO> pageRequest);
    int selectCountByParams(LogisticsDO logisticsDO);

    /**
     * 插入信息
     * @param logisticsDO LogisticsDO
     * @return int
     */
    int insert(LogisticsDO logisticsDO);

    /**
     * 根据ID更新信息
     * @param logisticsDO LogisticsDO
     * @return int
     */
    int updateById(LogisticsDO logisticsDO);

    /**
     * 根据ID删除信息
     * @param id Long
     * @return int
     */
    int deleteById(Long id);

    LogisticsCodeDO getLogisticsCodeDOByCode(String code);
}
