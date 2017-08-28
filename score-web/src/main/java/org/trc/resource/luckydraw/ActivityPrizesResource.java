package org.trc.resource.luckydraw;


import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.trc.biz.luckydraw.IActivityPrizesBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.impower.AclUserAddPageDate;
import org.trc.domain.luckydraw.ActivityPrizesDO;
import org.trc.util.Pagenation;
import org.trc.util.TxJerseyTools;

import com.alibaba.druid.support.json.JSONUtils;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/27
 */
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.ActivityPrizes.ROOT)
public class ActivityPrizesResource {

    private Logger logger = LoggerFactory.getLogger(ActivityPrizesResource.class);

    @Autowired
    private IActivityPrizesBiz activityPrizesBiz;
    @GET
    @Path(ScoreAdminConstants.Route.ActivityPrizes.LIST)
    public Response add(@QueryParam("name") String name,
                        @QueryParam("category") Long category,
                        @BeanParam Pagenation<ActivityPrizesDO> page){
            ActivityPrizesDO param = new ActivityPrizesDO();
            param.setName(name);
            param.setCategory(category);
//            return activityPrizesBiz.queryActivityPrizes(param, page);
            Pagenation<ActivityPrizesDO> pageActivityPrizes = activityPrizesBiz.queryActivityPrizes(param, page);
            return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(pageActivityPrizes));
    }
}
