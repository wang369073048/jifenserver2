package org.trc.biz.luckydraw;


import org.trc.domain.luckydraw.ActivityPrizesDO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/7
 */
public interface IActivityPrizesBiz {

    int insertActivityPrizes(ActivityPrizesDO activityPrizes);

    List<ActivityPrizesDO> listActivityPrizes(ActivityPrizesDO activityPrizes);

    int updateActivityPrizes(ActivityPrizesDO activityPrizes);

    Pagenation<ActivityPrizesDO> queryActivityPrizes(ActivityPrizesDO param, Pagenation<ActivityPrizesDO> pageRequest);
}
