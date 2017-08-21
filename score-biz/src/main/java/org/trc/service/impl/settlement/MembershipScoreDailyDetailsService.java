package org.trc.service.impl.settlement;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.dto.SettlementIntervalDTO;
import org.trc.domain.query.SettlementQuery;
import org.trc.mapper.settlement.IMembershipScoreDailyDetailsMapper;
import org.trc.domain.order.MembershipScoreDailyDetailsDO;
import org.trc.service.impl.BaseService;
import org.trc.service.settlement.IMembershipScoreDailyDetailsService;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/25
 */
@Service("membershipScoreDailyDetailsService")
public class MembershipScoreDailyDetailsService extends BaseService<MembershipScoreDailyDetailsDO,Long> implements IMembershipScoreDailyDetailsService{
    @Autowired
    private IMembershipScoreDailyDetailsMapper membershipScoreDailyDetailsMapper;
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
        List<MembershipScoreDailyDetailsDO> list = membershipScoreDailyDetailsMapper.selectListByParams(settlementQuery);
        pagenation.setTotalCount(page.getTotal());
        pagenation.setResult(list);
        return pagenation;
    }

    @Override
    public List<MembershipScoreDailyDetailsDO> queryMembershipScoreDailyDetailForExport(SettlementQuery settlementQuery) {
        return membershipScoreDailyDetailsMapper.queryMembershipScoreDailyDetailForExport(settlementQuery);
    }

    @Override
    public SettlementIntervalDTO getSettlementIntervalForMembershipScoreDailyDetail(SettlementQuery settlementQuery) {
        return null;
    }
}
