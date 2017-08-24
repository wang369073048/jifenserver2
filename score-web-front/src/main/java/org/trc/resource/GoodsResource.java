package org.trc.resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.trc.mall.externalservice.dto.TairanCouponDto;

import com.trc.mall.util.CustomAck;
import com.trc.mall.util.IpUtil;
import com.txframework.core.jdbc.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trc.domain.goods.GoodsDO;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.SocketException;


/**
 * Created by george on 2016/12/21.
 */
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreConstants.Route.Goods.ROOT)
public class GoodsResource extends BaseResource {

    private Logger logger = LoggerFactory.getLogger(GoodsResource.class);

    @GET
    @Path(ScoreConstants.Route.Goods.DETAIL)
    public Response detail(@NotNull @QueryParam("goodsId") Long goodsId){
        try {
            GoodsDO goodsDO = ScoreProvider.goodsService.getGoodsDOById(goodsId,null);
            if(null != goodsDO) {
                JSONObject object = new JSONObject();
                object.put("id",goodsDO.getId());
                object.put("shopId",goodsDO.getShopId());
                object.put("category",goodsDO.getCategory());
                object.put("brandName",goodsDO.getBrandName());
                object.put("barcode", goodsDO.getBarcode());
                object.put("goodsNo", goodsDO.getGoodsNo());
                object.put("goodsName",goodsDO.getGoodsName());
                object.put("batchNumber",goodsDO.getBatchNumber());
                object.put("goodsSn",goodsDO.getGoodsSn());
                object.put("mediumImg",goodsDO.getMediumImg());
                object.put("priceMarket",goodsDO.getPriceMarket());
                object.put("priceScore",goodsDO.getPriceScore());
                object.put("stock",goodsDO.getStock());
                object.put("stockWarn",goodsDO.getStockWarn());
                object.put("exchangeQuantity",goodsDO.getExchangeQuantity()+goodsDO.getVirtualExchangeQuantity());
                object.put("upTime",goodsDO.getUpTime());
                object.put("isUp",goodsDO.getIsUp());
                object.put("content",goodsDO.getContent());
                //设置限购数量
                object.put("limitQuantity",goodsDO.getLimitQuantity());
                if(-1 != goodsDO.getLimitQuantity()){
                    try {
                        //获取userId
                        String userId = TxJerseyRestContextFactory.getInstance().getUserId();
                        int quantity = ScoreProvider.goodsService.getLimitQuantity(userId, goodsId);
                        object.put("limitQuantity", goodsDO.getLimitQuantity() - quantity);
                    }catch (Exception e){
                        logger.info("用户未登录!");
                    }
                }
                CategoryDO category = ScoreProvider.categoryService.getCategoryDOById(goodsDO.getCategory());
                if(Category.FINANCIAL_CARD.equals(category.getCode())) {
                    object.put("batchNumber", goodsDO.getBatchNumber());
                    object.put("targetUrl", goodsDO.getTargetUrl());
                    object.put("validStartTime", goodsDO.getValidStartTime());
                    object.put("validEndTime", goodsDO.getValidEndTime());
                }else if(Category.TAIRAN_CARD.equals(category.getCode())){
                    object.put("batchNumber", goodsDO.getBatchNumber());
                    object.put("targetUrl", goodsDO.getTargetUrl());
                    object.put("validStartTime", goodsDO.getValidStartTime());
                    object.put("validEndTime", goodsDO.getValidEndTime());
                    object.put("autoUpTime", goodsDO.getAutoUpTime());
                    object.put("autoDownTime", goodsDO.getAutoDownTime());
                    try {
                        TairanCouponDto tairanCouponDto = ScoreProvider.goodsService.checkTairanEid(goodsDO.getBatchNumber());
                        object.put("couponType", tairanCouponDto.getData().getCoupon_type());
                        object.put("useStartTime", tairanCouponDto.getData().getUseStartTime());
                        object.put("useEndTime", tairanCouponDto.getData().getUseEndTime());
                        object.put("expireTime", tairanCouponDto.getData().getExpireTime());
                    }catch (CouponException e){
                        logger.error("该优惠券已失效!");
                    }
                }
                return TxJerseyTools.returnSuccess(object.toJSONString());
            }else{
                return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
            }
        } catch (GoodsException e){
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    @GET
    @Path(ScoreConstants.Route.Goods.HOT)
    public Response getHotExchangeList(@NotNull @QueryParam("shopId") Long shopId, @DefaultValue("3") @QueryParam("limit") Long limit){
        try {
            logger.info(IpUtil.getRealIp());
        } catch (SocketException e) {
            logger.error("获取ip失败!");
        }
        List<GoodsDO> goodsList = ScoreProvider.goodsService.getHotExchangeList(shopId,limit);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = assembleGoodsListJSON(goodsList);
        jsonObject.put("infos",jsonArray);
        return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
    }

    @GET
    @Path(ScoreConstants.Route.Goods.VALUE)
    public Response getValueExchangeList(@NotNull @QueryParam("shopId") Long shopId){
        List<GoodsDO> goodsList = ScoreProvider.goodsService.getValueExchangeList(shopId);
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = assembleGoodsListJSON(goodsList);
        jsonObject.put("infos",jsonArray);
        return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
    }

    @GET
    @Path(ScoreConstants.Route.Goods.CATEGORY)
    public Response getGoodsList(@NotNull @QueryParam("shopId") Long shopId,
                                 @DefaultValue("1") @QueryParam("isUp") Integer isUp,
                                 @DefaultValue("0") @QueryParam("whetherPrizes") Integer whetherPrizes,
                                 @QueryParam("classificationId") Long classificationId,
                                 @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGEINDEX_STR) @QueryParam("pageIndex") String pageIndex,
                                 @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGESIZE_STR) @QueryParam("pageSize") String pageSize){

        GoodsQuery param = new GoodsQuery();
        param.setShopId(shopId);
        param.setIsUp(isUp);
        param.setWhetherPrizes(whetherPrizes);
        param.setClassificationId(classificationId);
        PageRequest<GoodsDO> pageRequest = new PageRequest<>();
        pageRequest.setCurPage(TxJerseyTools.paramsToInteger(pageIndex));
        pageRequest.setPageData(TxJerseyTools.paramsToInteger(pageSize));
        try {
            PageRequest<GoodsDO> goodsPage = ScoreProvider.goodsService.queryGoodsDOListForClassificationForUser(param, pageRequest);
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = assembleGoodsListJSON(goodsPage.getDataList());
            JSONUtil.putParam(jsonArray, goodsPage, jsonObject);
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch (GoodsException e){
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    @GET
    @Path(ScoreConstants.Route.Goods.CLASSIFICATION)
    public Response classcification(@NotNull @QueryParam("shopId") Long shopId){

        ShopClassificationDO param = new ShopClassificationDO();
        param.setShopId(shopId);
        try {
            List<ShopClassificationDO> result = ScoreProvider.shopClassificationService.listEntity(param);
            JSONArray jsonArray = new JSONArray();
            jsonArray.addAll(result);
            return TxJerseyTools.returnSuccess(jsonArray.toJSONString());
        } catch (BusinessException e) {
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    private JSONArray assembleGoodsListJSON(List<GoodsDO> bannerList) {
        JSONArray result = new JSONArray();
        bannerList.forEach(goodsDO -> {
            JSONObject json = new JSONObject();
            json.put("id", goodsDO.getId());
            json.put("shopId", goodsDO.getShopId());
            json.put("goodsName", goodsDO.getGoodsName());
            json.put("mediumImg", goodsDO.getMediumImg());
            json.put("stock", goodsDO.getStock());
            json.put("stockWarn", goodsDO.getStockWarn());
            json.put("priceMarket", goodsDO.getPriceMarket());
            json.put("priceScore", goodsDO.getPriceScore());
            json.put("exchangeQuantity", goodsDO.getExchangeQuantity()+goodsDO.getVirtualExchangeQuantity());
            json.put("isUp", goodsDO.getIsUp());
            CategoryDO category = ScoreProvider.categoryService.getCategoryDOById(goodsDO.getCategory());
            if(Category.FINANCIAL_CARD.equals(category.getCode())) {
                json.put("batchNumber", goodsDO.getBatchNumber());
                json.put("targetUrl", goodsDO.getTargetUrl());
                json.put("validStartTime", goodsDO.getValidStartTime());
                json.put("validEndTime", goodsDO.getValidEndTime());
            }
            result.add(json);
        });
        return result;
    }

}
