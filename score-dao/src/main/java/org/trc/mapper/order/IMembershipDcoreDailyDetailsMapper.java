package org.trc.mapper.order;

import org.trc.domain.dto.SettlementQuery;
import org.trc.domain.order.MembershipScoreDailyDetailsDO;
import org.trc.util.BaseMapper;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/25
 */
public interface IMembershipDcoreDailyDetailsMapper extends BaseMapper<MembershipScoreDailyDetailsDO> {

    List<MembershipScoreDailyDetailsDO> selectListByParams(SettlementQuery SettlementQuery);
}
