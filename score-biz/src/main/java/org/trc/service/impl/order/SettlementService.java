package org.trc.service.impl.order;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.order.SettlementDO;
import org.trc.mapper.order.ISettlementMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.order.ISettlementService;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/17
 */
@Service(value = "settlementService")
public class SettlementService extends BaseService<SettlementDO,Long> implements ISettlementService{

    @Autowired
    private ISettlementMapper settlementMapper;

    @Override
    public Pagenation<SettlementDO> selectListByParams(SettlementDO settlementDO, Pagenation<SettlementDO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageIndex(), pagenation.getPageSize());
        List<SettlementDO> list = settlementMapper.selectListByParams(settlementDO);
        pagenation.setTotalData(page.getTotal());
        pagenation.setInfos(list);
        return pagenation;
    }
}
