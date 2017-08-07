package org.trc.service.luckydraw;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.luckydraw.ActivityPrizesDO;
import org.trc.domain.query.UsableActivityQuery;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */

public interface IActivityPrizesService extends IBaseService<ActivityPrizesDO,Long>{

    int checkActivityPrizes(Map params);

    int statisticalTotalWinningRate(ActivityPrizesDO activityPrizes);

    int insertActivityPrizes(ActivityPrizesDO activityPrizes);

    List<ActivityPrizesDO> listActivityPrizes(ActivityPrizesDO activityPrizes);

    List<ActivityPrizesDO> listUsableActivityPrizes(UsableActivityQuery param);

    ActivityPrizesDO selectOneByParams(ActivityPrizesDO activityPrizes);

    int updateActivityPrizes(ActivityPrizesDO activityPrizes);

    List<ActivityPrizesDO> selectByParams(ActivityPrizesDO param, PageRequest<ActivityPrizesDO> pageRequest);

    void deleteActivityPrizes(Map<String, Object> params);
}
