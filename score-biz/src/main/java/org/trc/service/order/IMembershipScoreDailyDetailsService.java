package org.trc.service.order;

import org.trc.IBaseService;
import org.trc.domain.dto.SettlementIntervalDTO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.order.MembershipScoreDailyDetailsDO;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/25
 */
public interface IMembershipScoreDailyDetailsService extends IBaseService<MembershipScoreDailyDetailsDO,Long> {

    MembershipScoreDailyDetailsDO getLastMembershipDcoreDailyDetails(String userId);

    int insertMembershipDcoreDailyDetails(MembershipScoreDailyDetailsDO membershipDcoreDailyDetailsDO);

    MembershipScoreDailyDetailsDO generateMembershipDcoreDailyDetailsForExchangeIn(Map timeInterval);

    MembershipScoreDailyDetailsDO generateMembershipDcoreDailyDetailsForConsume(Map timeInterval);

    Pagenation<MembershipScoreDailyDetailsDO> selectListByParams(SettlementQuery SettlementQuery, Pagenation<MembershipScoreDailyDetailsDO> pageRequest );

    List<MembershipScoreDailyDetailsDO> queryMembershipScoreDailyDetailForExport(SettlementQuery settlementQuery);

    SettlementIntervalDTO getSettlementIntervalForMembershipScoreDailyDetail(SettlementQuery settlementQuery);}
