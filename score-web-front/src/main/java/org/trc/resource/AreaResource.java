package org.trc.resource;

import com.trc.mall.constants.ScoreConstants;
import com.trc.mall.interceptor.Logined;
import com.trc.mall.provider.ScoreProvider;
import com.txframework.interceptor.api.annotation.TxAop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by george on 2016/12/24.
 */
@Path(ScoreConstants.Route.Area.ROOT)
@Produces(MediaType.APPLICATION_JSON)
@TxAop
public class AreaResource {

    private Logger logger = LoggerFactory.getLogger(AreaResource.class);

    /**
     * 根据用户查询积分流水
     *
     * @param
     * @return
     */
    @GET
    @Logined
    public Response getAreaDesc(){
        String areaDesc = ScoreProvider.areaService.getAreaDesc();
        return  areaDesc != null?Response.status(Response.Status.OK).entity(areaDesc).type("application/json").encoding("UTF-8").header("Cache-Control","no-cache, must-revalidate").build():Response.status(Response.Status.OK).build();
    }

}
