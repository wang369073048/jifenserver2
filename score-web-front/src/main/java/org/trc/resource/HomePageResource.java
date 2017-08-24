package org.trc.resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tairanchina.constants.CommonConstants;
import com.tairanchina.engine.jersey.TxJerseyTools;
import com.trc.mall.constants.ScoreConstants;
import com.trc.mall.exception.BannerContentException;
import com.trc.mall.exception.OrderException;
import com.trc.mall.model.BannerContentDTO;
import com.trc.mall.model.OrdersDO;
import com.trc.mall.util.CustomAck;
import com.txframework.core.jdbc.PageRequest;
import com.txframework.interceptor.api.annotation.TxAop;
import com.txframework.util.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.trc.mall.provider.ScoreProvider.bannerContentService;
import static com.trc.mall.provider.ScoreProvider.newOrderService;
import static com.trc.mall.util.CommonUtils.getPhoneByUserId;

@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreConstants.Route.Mall.ROOT)
@TxAop
public class HomePageResource {
    private Logger logger = LoggerFactory.getLogger(HomePageResource.class);

    /**
     * banner信息
     *
     * @param shopId
     * @return
     */
    @GET
    @Path("/banner/{shopId}")
    public Response getBannerList(@PathParam("shopId") Long shopId, @DefaultValue("PC") @QueryParam("type")String type) {
        BannerContentDTO bannerContentDTO = new BannerContentDTO();
        bannerContentDTO.setShopId(shopId);
        bannerContentDTO.setType(type);
        bannerContentDTO.setUp(true);
        try {
            List<BannerContentDTO> bannerList = bannerContentService.getValidBannerList(bannerContentDTO);
            return TxJerseyTools.returnSuccess(assembleBannerListJSON(bannerList).toJSONString());
        }catch (BannerContentException e){
            logger.error("=====>banner exception", e);
            return CustomAck.customError(e.getMessage());
        }catch (Exception e) {
            logger.error("=====>banner exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    /**
     * buildJason
     *
     * @param bannerList
     * @return
     */
    private JSONArray assembleBannerListJSON(List<BannerContentDTO> bannerList) {
        JSONArray result = new JSONArray();
        bannerList.forEach(model -> {
            JSONObject jo = new JSONObject();
            jo.put("imgUrl", ObjectUtils.convertVal(model.getImgUrl(), ""));
            jo.put("targetUrl", ObjectUtils.convertVal(model.getTargetUrl(), ""));
            result.add(jo);
        });
        return result;
    }

    public static Date getTimesMonthnight() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, 24);
        return cal.getTime();
    }

    /**
     * 弹幕列表
     *
     * @return
     */
    @GET
    @Path("/barrage")
    public Response getBarrageList() {
        try {
            PageRequest<OrdersDO> orderResult = newOrderService.queryOrdersDOListForPage(new OrdersDO(), new PageRequest<>());
            return TxJerseyTools.returnSuccess(assembleBarrageListJSON(orderResult).toJSONString());
        }catch (OrderException e) {
            logger.error("=====>barrage exception", e);
            return CustomAck.customError(e.getMessage());
        }catch (Exception e) {
            logger.error("=====>barrage exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    /**
     * buildJson
     *
     * @param orderResult
     * @return
     */
    private JSONArray assembleBarrageListJSON(PageRequest<OrdersDO> orderResult) {
        List<OrdersDO> orderResultList = orderResult.getDataList();
        JSONArray result = new JSONArray();
        orderResultList.forEach(model -> {
            JSONObject jo = new JSONObject();
            jo.put("goodsName", ObjectUtils.convertVal(model.getGoodsName(), ""));
            jo.put("createDate", ObjectUtils.convertVal(model.getCreateTime(), ""));
            jo.put("phone", ObjectUtils.convertVal(getPhoneByUserId(model.getUserId()), ""));
            result.add(jo);
        });
        return result;
    }

    /*@GET
    @Path("/ad/{shopId}")
    public Response getADList(@PathParam("shopId") String shopId) {
        try {
            ShopwindowDO shopwindowDO = new ShopwindowDO();
            shopwindowDO.setShopId(Long.valueOf(shopId));
            ResultModel<List<ShopwindowDO>> shopWindowResult = shopwindowService.getShopwindows(shopwindowDO);
            if (!shopWindowResult.isResult()) {
                if (!ErrorCode.ITEM_IS_NULL.equalsIgnoreCase(shopWindowResult.getErrorCode())) {
                    logger.error("查询广告位列表失败!");
                    return CustomAck.customError("查询广告位列表失败，请稍后再试");
                } else {
                    JSONArray result = new JSONArray();
                    return TxJerseyTools.returnSuccess(result.toJSONString());
                }
            }
            return TxJerseyTools.returnSuccess(assembleADListJSON(shopWindowResult).toJSONString());
        } catch (Exception e) {
            logger.error("=====>AD exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }*/

    /**
     * buildJson
     * @param shopWindowResult
     * @return
     */
    /*private JSONArray assembleADListJSON(ResultModel<List<ShopwindowDO>> shopWindowResult) {
        List<ShopwindowDO> adResultList = shopWindowResult.getModel();
        JSONArray result = new JSONArray();
        adResultList.forEach(model -> {
            JSONObject jo = new JSONObject();
            jo.put("imgUrl", ObjectUtils.convertVal(model.getImgUrl(), ""));
            jo.put("linkUrl", ObjectUtils.convertVal(model.getLinkUrl(), ""));
            result.add(jo);
        });
        return result;
    }*/
}
