package org.trc.service.impl.order;

import com.txframework.core.jdbc.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.order.LogisticsCodeDO;
import org.trc.domain.order.LogisticsDO;
import org.trc.mapper.order.ILogisticsMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.order.ILogisticsService;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/5
 */
@Service(value = "logisticsService")
public class LogisticsService extends BaseService<LogisticsDO,Long> implements ILogisticsService{
    @Autowired
    private ILogisticsMapper logisticsMapper;
    @Override
    public LogisticsDO selectByParams(LogisticsDO logisticsDO) {
        return null;
    }

    @Override
    public List<LogisticsDO> selectListByParams(LogisticsDO logisticsDO, PageRequest<LogisticsDO> pageRequest) {
        return null;
    }

    @Override
    public int selectCountByParams(LogisticsDO logisticsDO) {
        return 0;
    }

    @Override
    public int insert(LogisticsDO logisticsDO) {
        return 0;
    }

    @Override
    public int updateById(LogisticsDO logisticsDO) {
        return logisticsMapper.updateById(logisticsDO);
    }

    @Override
    public int deleteById(Long id) {
        return 0;
    }

    @Override
    public LogisticsCodeDO getLogisticsCodeDOByCode(String code) {
        return logisticsMapper.getLogisticsCodeDOByCode(code);
    }
}
