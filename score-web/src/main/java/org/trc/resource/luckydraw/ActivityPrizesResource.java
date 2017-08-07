package org.trc.resource.luckydraw;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.trc.biz.luckydraw.IActivityPrizesBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.luckydraw.ActivityPrizesDO;

import org.trc.util.Pagenation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */

@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.ActivityPrizes.ROOT)
public class ActivityPrizesResource {

    private Logger logger = LoggerFactory.getLogger(ActivityPrizesResource.class);

    @Autowired
    private IActivityPrizesBiz activityPrizesBiz;
    @POST
    @Path(ScoreAdminConstants.Route.ActivityPrizes.ADD)
    public Pagenation<ActivityPrizesDO> add(@FormParam("name") String name,
                        @FormParam("category") Long category,
                        @BeanParam Pagenation<ActivityPrizesDO> page){
            ActivityPrizesDO param = new ActivityPrizesDO();
            param.setName(name);
            param.setCategory(category);
            return activityPrizesBiz.queryActivityPrizes(param, page);
    }
}
