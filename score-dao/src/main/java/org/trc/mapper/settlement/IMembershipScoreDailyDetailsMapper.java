package org.trc.mapper.settlement;

import org.trc.domain.dto.SettlementIntervalDTO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.order.MembershipScoreDailyDetailsDO;
import org.trc.util.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/25
 */
public interface IMembershipScoreDailyDetailsMapper extends BaseMapper<MembershipScoreDailyDetailsDO> {

    List<MembershipScoreDailyDetailsDO> selectListByParams(SettlementQuery SettlementQuery);

    MembershipScoreDailyDetailsDO getLastMembershipScoreDailyDetails(String userId);

    int insertMembershipScoreDailyDetails(MembershipScoreDailyDetailsDO membershipDcoreDailyDetailsDO);

    MembershipScoreDailyDetailsDO generateMembershipScoreDailyDetailsForExchangeIn(Map timeInterval);

    MembershipScoreDailyDetailsDO generateMembershipScoreDailyDetailsForConsume(Map timeInterval);

    List<MembershipScoreDailyDetailsDO> queryMembershipScoreDailyDetailForExport(SettlementQuery settlementQuery);

    SettlementIntervalDTO getSettlementIntervalForMembershipScoreDailyDetail(SettlementQuery settlementQuery);
}
