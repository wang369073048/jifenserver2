package org.trc.service.impl.luckydraw;

import org.springframework.stereotype.Service;
import org.trc.domain.luckydraw.ActivityPrizesDO;
import org.trc.service.impl.BaseService;
import org.trc.service.luckydraw.IActivityPrizesService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
@Service("activityPrizesService")
public class ActivityPrizesService extends BaseService<ActivityPrizesDO,Long> implements IActivityPrizesService{
}
