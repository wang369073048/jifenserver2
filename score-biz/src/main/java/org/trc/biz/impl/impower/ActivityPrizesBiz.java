package org.trc.biz.impl.impower;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.biz.goods.IGoodsBiz;
import org.trc.biz.luckydraw.IActivityPrizesBiz;
import org.trc.domain.luckydraw.ActivityPrizesDO;
import org.trc.service.goods.IGoodsService;
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

    @Autowired
    private IGoodsService goodsService;
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
        if(pageRequest.getPageSize() == 1){
            List<ActivityPrizesDO> activityPrizes = ActivityPrizesDO.getDefaultActivityPrizes();
            pageRequest = goodsService.queryActivityPrizes(param, pageRequest);
            List<ActivityPrizesDO> dataList = pageRequest.getInfos();
            activityPrizes.addAll(dataList);
            pageRequest.setInfos(activityPrizes);
            return pageRequest;
        }else {
            return goodsService.queryActivityPrizes(param, pageRequest);
        }
    }
}
