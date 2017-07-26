package org.trc.service.impl.order;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.txframework.core.jdbc.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.IBaseService;
import org.trc.domain.dto.SettlementIntervalDTO;
import org.trc.domain.dto.SettlementQuery;
import org.trc.domain.order.MembershipScoreDailyDetailsDO;
import org.trc.domain.order.OrdersDO;
import org.trc.mapper.order.IMembershipDcoreDailyDetailsMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.order.IMembershipDcoreDailyDetailsService;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/25
 */
@Service("membershipDcoreDailyDetailsService")
public class MembershipDcoreDailyDetailsService extends BaseService<MembershipScoreDailyDetailsDO,Long> implements IMembershipDcoreDailyDetailsService{
    @Autowired
    private IMembershipDcoreDailyDetailsMapper membershipDcoreDailyDetailsMapper;
    @Override
    public MembershipScoreDailyDetailsDO getLastMembershipDcoreDailyDetails(String userId) {
        return null;
    }

    @Override
    public int insertMembershipDcoreDailyDetails(MembershipScoreDailyDetailsDO membershipDcoreDailyDetailsDO) {
        return 0;
    }

    @Override
    public MembershipScoreDailyDetailsDO generateMembershipDcoreDailyDetailsForExchangeIn(Map timeInterval) {
        return null;
    }

    @Override
    public MembershipScoreDailyDetailsDO generateMembershipDcoreDailyDetailsForConsume(Map timeInterval) {
        return null;
    }

    @Override
    public Pagenation<MembershipScoreDailyDetailsDO> selectListByParams(SettlementQuery settlementQuery, Pagenation<MembershipScoreDailyDetailsDO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
        List<MembershipScoreDailyDetailsDO> list = membershipDcoreDailyDetailsMapper.selectListByParams(settlementQuery);
        pagenation.setTotalCount(page.getTotal());
        pagenation.setResult(list);
        return pagenation;
    }

    @Override
    public List<MembershipScoreDailyDetailsDO> queryMembershipScoreDailyDetailForExport(SettlementQuery settlementQuery) {
        return null;
    }

    @Override
    public SettlementIntervalDTO getSettlementIntervalForMembershipScoreDailyDetail(SettlementQuery settlementQuery) {
        return null;
    }
}
