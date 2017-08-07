package org.trc.service.impl.luckydraw;

import com.txframework.core.jdbc.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.luckydraw.ActivityPrizesDO;
import org.trc.domain.query.UsableActivityQuery;
import org.trc.mapper.luckydraw.IActivityPrizesMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.luckydraw.IActivityPrizesService;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
@Service("activityPrizesService")
public class ActivityPrizesService extends BaseService<ActivityPrizesDO,Long> implements IActivityPrizesService{

    @Autowired
    private IActivityPrizesMapper activityPrizesMapper;
    @Override
    public int checkActivityPrizes(Map params) {
        return activityPrizesMapper.checkActivityPrizes(params);
    }

    @Override
    public int statisticalTotalWinningRate(ActivityPrizesDO activityPrizes) {
        return 0;
    }

    @Override
    public int insertActivityPrizes(ActivityPrizesDO activityPrizes) {
        return 0;
    }

    @Override
    public List<ActivityPrizesDO> listActivityPrizes(ActivityPrizesDO activityPrizes) {
        return null;
    }

    @Override
    public List<ActivityPrizesDO> listUsableActivityPrizes(UsableActivityQuery param) {
        return null;
    }

    @Override
    public ActivityPrizesDO selectOneByParams(ActivityPrizesDO activityPrizes) {
        return null;
    }

    @Override
    public int updateActivityPrizes(ActivityPrizesDO activityPrizes) {
        return 0;
    }

    @Override
    public List<ActivityPrizesDO> selectByParams(ActivityPrizesDO param, PageRequest<ActivityPrizesDO> pageRequest) {
        return null;
    }

    @Override
    public void deleteActivityPrizes(Map<String, Object> params) {
        activityPrizesMapper.deleteActivityPrizes(params);
    }
}
