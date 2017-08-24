package org.trc.resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tairanchina.engine.jersey.TxJerseyTools;
import com.trc.mall.base.BaseResource;
import com.trc.mall.constants.ScoreConstants;
import com.trc.mall.model.BarrageDO;
import com.trc.mall.provider.ScoreProvider;
import com.txframework.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author wilsonlee
 *
 */
@Path(ScoreConstants.Route.Barrage.ROOT)
public class BarrageResource extends BaseResource {

    private Logger logger = LoggerFactory.getLogger(BarrageResource.class);

    @GET
    @Path(ScoreConstants.Route.Barrage.LIST)
    public Response getBarrageList(	@NotNull @QueryParam("startTime") Long startTime,
							    		@NotNull @QueryParam("endTime") Long endTime, 
							    		@DefaultValue("-1") @QueryParam("shopId") Long shopId,
							    		@DefaultValue("10") @QueryParam("limit") Long limit){
    	
        List<BarrageDO> barrageList = ScoreProvider.barrageService.getByTime(shopId, new Date(startTime), new Date(endTime),limit);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        if (ListUtils.isNotEmpty(barrageList)) {
            for (BarrageDO barrageDO : barrageList) {
                JSONObject json = new JSONObject();
                json.put("id", barrageDO.getId());
                json.put("shopId", barrageDO.getShopId());
                json.put("orderId", barrageDO.getOrderId());
                json.put("userId", barrageDO.getUserId());
                json.put("isDeleted", barrageDO.getIsDeleted());
                json.put("createTime", barrageDO.getCreateTime());
                json.put("updateTime", barrageDO.getUpdateTime());
                json.put("goodsName", barrageDO.getGoodsName());
                json.put("phone", barrageDO.getPhone());
                json.put("avatar", barrageDO.getAvatar());
                jsonArray.add(json);
            }
        }
        jsonObject.put("infos",jsonArray);
        return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
    }
}
