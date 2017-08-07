package org.trc.biz.impl.impower;

import org.springframework.stereotype.Service;
import org.trc.biz.luckydraw.IActivityPrizesBiz;
import org.trc.domain.luckydraw.ActivityPrizesDO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/7
 */
@Service(value = "activityPrizesBiz")
public class ActivityPrizesBiz implements IActivityPrizesBiz{
    @Override
    public int insertActivityPrizes(ActivityPrizesDO activityPrizes) {
        return 0;
    }

    @Override
    public List<ActivityPrizesDO> listActivityPrizes(ActivityPrizesDO activityPrizes) {
        return null;
    }

    @Override
    public int updateActivityPrizes(ActivityPrizesDO activityPrizes) {
        return 0;
    }

    @Override
    public Pagenation<ActivityPrizesDO> queryActivityPrizes(ActivityPrizesDO param, Pagenation<ActivityPrizesDO> pageRequest) {
        return null;
    }
}
