package org.trc.mapper.luckydraw;

import org.trc.domain.luckydraw.ActivityPrizesDO;
import org.trc.domain.query.UsableActivityQuery;
import org.trc.util.BaseMapper;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/28
 */
public interface IActivityPrizesMapper extends BaseMapper<ActivityPrizesDO>{

    int checkActivityPrizes(Map params);

    int statisticalTotalWinningRate(ActivityPrizesDO activityPrizes);

    int insertActivityPrizes(ActivityPrizesDO activityPrizes);

    List<ActivityPrizesDO> listActivityPrizes(ActivityPrizesDO activityPrizes);

    List<ActivityPrizesDO> listUsableActivityPrizes(UsableActivityQuery param);

    ActivityPrizesDO selectOneByParams(ActivityPrizesDO activityPrizes);

    int updateActivityPrizes(ActivityPrizesDO activityPrizes);

    List<ActivityPrizesDO> selectByParams(ActivityPrizesDO param, Pagenation<ActivityPrizesDO> pageRequest);

    void deleteActivityPrizes(Map<String, Object> params);
}
