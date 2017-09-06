package org.trc.resource.goods;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trc.mall.externalservice.dto.CouponDto;
import com.trc.mall.externalservice.dto.TairanCouponDto;
import com.trc.mall.util.CustomAck;
import com.txframework.util.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.trc.biz.goods.ICategoryBiz;
import org.trc.biz.goods.IGoodsBiz;
import org.trc.constants.Category;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.goods.CategoryDO;
import org.trc.domain.goods.GoodsClassificationRelationshipDO;
import org.trc.domain.goods.GoodsDO;
import org.trc.domain.goods.ShopClassificationDO;
import org.trc.domain.query.GoodsQuery;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.CouponException;
import org.trc.exception.GoodsException;
import org.trc.interceptor.annotation.Authority;
import org.trc.util.GuidUtil;
import org.trc.util.Pagenation;
import org.trc.util.TxJerseyTools;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by hzwzhen on 2017/6/22.
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("{shopId}/" + ScoreAdminConstants.Route.Goods.ROOT)
public class GoodsResource {

    Logger logger = LoggerFactory.getLogger(GoodsResource.class);

    @Autowired
    private IGoodsBiz goodsBiz;
    @Autowired
    private ICategoryBiz categoryBiz;

    /**
     * 增加商品
     */
    @POST
    @Authority
    @Path(ScoreAdminConstants.Route.Goods.ENTITY)
    public Response publish(@NotNull(message = "shopId不能为空") @PathParam("shopId") Long shopId, @NotNull(message = "category不能为空")@FormParam("category") Long category,
                             @FormParam("brandName") String brandName, @NotNull(message = "goodsName不能为空") @FormParam("goodsName") String goodsName, @FormParam("barcode") String barcode,
                             @FormParam("goodsNo") String goodsNo, @FormParam("batchNumber") String batchNumber, @FormParam("mainImg") String mainImg,
                             @FormParam("mediumImg") String mediumImg, @FormParam("priceMarket") Integer priceMarket,
                             @FormParam("priceScore") Integer priceScore, @NotNull(message = "stock不能为空") @FormParam("stock") Integer stock,
                             @NotNull(message = "stockWarn不能为空") @FormParam("stockWarn") Integer stockWarn, @FormParam("targetUrl") String targetUrl,
                             @FormParam("validStartTime") Long validStartTime, @FormParam("validEndTime") Long validEndTime,
                             @FormParam("autoUpTime") Long autoUpTime, @FormParam("autoDownTime") Long autoDownTime, @NotNull(message = "content不能为空") @FormParam("content") String content,
                             @FormParam("virtualExchangeQuantity") Integer virtualExchangeQuantity, @NotNull(message = "whetherPrizes不能为空")@FormParam("whetherPrizes") Integer whetherPrizes,
                             @FormParam("sort") Integer sort, @FormParam("shopClassificationIds") String shopClassificationIds,
                             @NotNull(message = "limitQuantity不能为空")@FormParam("limitQuantity") Integer limitQuantity,@Context ContainerRequestContext requestContext) {
        //当非奖品时.mediumImg和priceScore不能为空
        if(whetherPrizes == 0){
            Assert.isTrue(StringUtils.isNotBlank(mediumImg),"mediumImg不能为空");
            Assert.notNull(priceScore,"priceScore不能为空");
        }
        Date time = Calendar.getInstance().getTime();
        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setShopId(shopId);
        goodsDO.setCategory(category);
        goodsDO.setBarcode(barcode);
        goodsDO.setGoodsNo(goodsNo);
        goodsDO.setBrandName(brandName);
        goodsDO.setGoodsName(goodsName);
        goodsDO.setMainImg(mainImg);
        goodsDO.setMediumImg(mediumImg);
        goodsDO.setPriceMarket(priceMarket);
        goodsDO.setPriceScore(priceScore);
        goodsDO.setStock(stock);
        goodsDO.setStockWarn(stockWarn);
        goodsDO.setContent(content);
        goodsDO.setWhetherPrizes(whetherPrizes);
        goodsDO.calMainImg();
        goodsDO.setIsDeleted(0);
        goodsDO.setIsUp(0);
        goodsDO.setVirtualExchangeQuantity(null == virtualExchangeQuantity ? 0 : virtualExchangeQuantity);
        goodsDO.setCreateTime(time);
        handleGoodsDO(goodsDO, category, batchNumber, targetUrl, validStartTime, validEndTime, autoUpTime, autoDownTime);
        goodsDO.setLimitQuantity(limitQuantity);
        goodsDO.setSort(sort);
        goodsDO.setCreateTime(time);
        _checkString(shopClassificationIds);
        if(StringUtils.isNotBlank(shopClassificationIds)) {
            String[] shopClassificationIdStrs = shopClassificationIds.split(",");
            List<GoodsClassificationRelationshipDO> list = new ArrayList<>();
            if (null != shopClassificationIdStrs && shopClassificationIdStrs.length > 0) {
                for (String shopClassificationIdStr : shopClassificationIdStrs) {
                    GoodsClassificationRelationshipDO item = new GoodsClassificationRelationshipDO();
                    item.setShopClassificationId(Long.valueOf(shopClassificationIdStr));
                    list.add(item);
                }
            }
            goodsDO.setGoodsClassificationRelationshipList(list);
        }
        goodsBiz.saveGoodsDO(goodsDO);
        return TxJerseyTools.returnSuccess();
    }
    private boolean _checkString(String shopClassificationIds){
        if(StringUtils.isNotBlank(shopClassificationIds)){
            String reg = "^[1-9]\\d*([,]\\d+)+";
            return Pattern.compile(reg).matcher(shopClassificationIds).matches();
        }
        return true;
    }

    /**
     * 修改商品
     */
    @POST
    @Path(ScoreAdminConstants.Route.Goods.ENTITY + "/{id}")
    @Authority
    public Response modify(@NotNull@PathParam("shopId") Long shopId, @NotNull(message = "id不能为空") @FormParam("id") Long id,
                            @NotNull(message = "category不能为空") @FormParam("category") Long category,
                            @FormParam("brandName") String brandName, @NotEmpty(message = "goodsName不能为空") @FormParam("goodsName") String goodsName,
                            @NotEmpty(message = "barcode不能为空") @FormParam("barcode") String barcode,
                            @FormParam("goodsNo") String goodsNo, @FormParam("batchNumber") String batchNumber, @NotEmpty @FormParam("mediumImg") String mediumImg,
                            @FormParam("priceMarket") Integer priceMarket, @NotNull(message = "priceScore不能为空") @FormParam("priceScore") Integer priceScore,
                            @NotNull(message = "stock不能为空") @FormParam("stock") Integer stock,
                            @NotNull(message = "stockWarn不能为空") @FormParam("stockWarn") Integer stockWarn, @FormParam("targetUrl") String targetUrl, @FormParam("validStartTime") Long validStartTime,
                            @FormParam("validEndTime") Long validEndTime, @FormParam("autoUpTime") Long autoUpTime, @FormParam("autoDownTime") Long autoDownTime,
                            @NotEmpty(message = "content不能为空") @FormParam("content") String content, @FormParam("virtualExchangeQuantity") Integer virtualExchangeQuantity,
                            @FormParam("whetherPrizes") Integer whetherPrizes, @FormParam("sort") Integer sort, @FormParam("shopClassificationIds") String shopClassificationIds,
                            @NotNull(message = "limitQuantity不能为空") @FormParam("limitQuantity") Integer limitQuantity,@Context ContainerRequestContext requestContext) {

        GoodsDO goods = goodsBiz.getGoodsDOById(id, whetherPrizes,null);
        if (null == goods || goods.getShopId().longValue() != shopId.longValue()) {
            throw new GoodsException(ExceptionEnum.ERROR_ILLEGAL_OPERATION, "操作不合法");
        }
        Date time = Calendar.getInstance().getTime();
        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setId(id);
        goodsDO.setShopId(shopId);
        goodsDO.setCategory(category);
        goodsDO.setGoodsNo(goodsNo);
        CategoryDO category1 = categoryBiz.getCategoryDOById(category);
        if (1 == category1.getIsVirtual().intValue()) {
            goodsDO.setBatchNumber(batchNumber);
            goodsDO.setTargetUrl(targetUrl);
            if (null != validStartTime) {
                goodsDO.setValidStartTime(new Date(validStartTime));
            }
            if (null != validEndTime) {
                goodsDO.setValidEndTime(new Date(validEndTime));
            }
            if (null != autoUpTime) {
                goodsDO.setAutoUpTime(new Date(autoUpTime));
            }
            if (null != autoDownTime) {
                goodsDO.setAutoDownTime(new Date(autoDownTime));
            }
        }
        goodsDO.setBrandName(brandName);
        goodsDO.setGoodsName(goodsName);
        goodsDO.setBarcode(barcode);
        goodsDO.setMediumImg(mediumImg);
        goodsDO.setPriceMarket(priceMarket);
        goodsDO.setPriceScore(priceScore);
        goodsDO.setStock(stock);
        goodsDO.setStockWarn(stockWarn);
        goodsDO.setContent(content);
        goodsDO.calMainImg();
        goodsDO.setVirtualExchangeQuantity(null == virtualExchangeQuantity ? 0 : virtualExchangeQuantity);
        goodsDO.setLimitQuantity(limitQuantity);
        goodsDO.setSort(sort);
        goodsDO.setUpdateTime(time);
        _checkString(shopClassificationIds);
        if(StringUtils.isNotBlank(shopClassificationIds)) {
            String[] shopClassificationIdStrs = shopClassificationIds.split(",");
            List<GoodsClassificationRelationshipDO> list = new ArrayList<>();
            if (null != shopClassificationIdStrs && shopClassificationIdStrs.length > 0) {
                for (String shopClassificationIdStr : shopClassificationIdStrs) {
                    GoodsClassificationRelationshipDO item = new GoodsClassificationRelationshipDO();
                    item.setGoodsId(id);
                    item.setShopClassificationId(Long.valueOf(shopClassificationIdStr));
                    list.add(item);
                }
            }
            goodsDO.setGoodsClassificationRelationshipList(list);
        }
        goodsBiz.updateGoodsDO(goodsDO);
        return TxJerseyTools.returnSuccess();

    }
    /**
     * 查询商品
     *
     * @return
     */
    @GET
    @Authority
    @Path(ScoreAdminConstants.Route.Goods.ENTITY + "/{id}")
    public Response getGoodsById(@PathParam("shopId") Long shopId,
                                 @PathParam("id") Long id,@Context ContainerRequestContext requestContext) {
        GoodsDO goods = goodsBiz.getGoodsDOById(id, null);
        //校验商品是否属于该店铺
        if (null == goods || shopId != goods.getShopId()) {
            throw new GoodsException(ExceptionEnum.ERROR_ILLEGAL_OPERATION, "操作不合法");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", goods.getId());
        jsonObject.put("shopId", goods.getShopId());
        jsonObject.put("category", goods.getCategory());
        jsonObject.put("barcode", goods.getBarcode());
        jsonObject.put("goodsNo", goods.getGoodsNo());
        jsonObject.put("goodsName", goods.getGoodsName());
        jsonObject.put("mediumImg", goods.getMediumImg());
        jsonObject.put("stock", goods.getStock());
        jsonObject.put("stockWarn", goods.getStockWarn());
        jsonObject.put("priceMarket", goods.getPriceMarket());
        jsonObject.put("priceScore", goods.getPriceScore());
        jsonObject.put("exchangeQuantity", goods.getExchangeQuantity());
        jsonObject.put("virtualExchangeQuantity", goods.getVirtualExchangeQuantity());
        jsonObject.put("content", goods.getContent());
        jsonObject.put("whetherPrizes",goods.getWhetherPrizes());
        jsonObject.put("limitQuantity",goods.getLimitQuantity());
        jsonObject.put("isUp", goods.getIsUp());
        jsonObject.put("sort", goods.getSort());
        if(CollectionUtils.isNotEmpty(goods.getShopClassificationList())){
            String shopClassificationIds = "";
            for(ShopClassificationDO item : goods.getShopClassificationList()){
                shopClassificationIds += item.getId()+",";
            }
            shopClassificationIds = shopClassificationIds.substring(0,shopClassificationIds.length()-1);
            jsonObject.put("shopClassificationIds", shopClassificationIds);
        }
        CategoryDO category = categoryBiz.getCategoryDOById(goods.getCategory());
        if (Category.FINANCIAL_CARD.equals(category.getCode())) {
            jsonObject.put("batchNumber", goods.getBatchNumber());
            jsonObject.put("targetUrl", goods.getTargetUrl());
            jsonObject.put("validStartTime", goods.getValidStartTime());
            jsonObject.put("validEndTime", goods.getValidEndTime());
            jsonObject.put("autoUpTime", goods.getAutoUpTime());
            jsonObject.put("autoDownTime", goods.getAutoDownTime());
        } else if (Category.EXTERNAL_CARD.equals(category.getCode())) {
            jsonObject.put("batchNumber", goods.getBatchNumber());
            jsonObject.put("validStartTime", goods.getValidStartTime());
            jsonObject.put("validEndTime", goods.getValidEndTime());
            jsonObject.put("autoUpTime", goods.getAutoUpTime());
            jsonObject.put("autoDownTime", goods.getAutoDownTime());
        }
        return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
    }

    /**
     * 删除商品
     */
    @DELETE
    @Authority
    @Path(ScoreAdminConstants.Route.Goods.ENTITY + "/{id}")
    public Response deleteGoodsDO(@PathParam("shopId") Long shopId, @PathParam("id") Long id,@Context ContainerRequestContext requestContext){

        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setId(id);
        goodsDO.setShopId(shopId);
        goodsBiz.deleteGoodsDO(goodsDO);
        return TxJerseyTools.returnSuccess();
    }


    /**
     * 查询商品列表
     *
     * @return
     */
    @GET
    @Authority
    @Path(ScoreAdminConstants.Route.Goods.ENTITY)
    public Response getGoodsList(@PathParam("shopId") Long shopId,
                                 @QueryParam("goodsName") String goodsName,
                                 @QueryParam("isUp") Integer isUp,
                                 @QueryParam("whetherPrizes") Integer whetherPrizes,
                                 @QueryParam("classificationId") Long classificationId,
                                 @BeanParam Pagenation<GoodsDO> page,@Context ContainerRequestContext requestContext) {
        GoodsQuery param = new GoodsQuery();
        param.setShopId(shopId);
        param.setGoodsName(goodsName);
        param.setIsUp(isUp);
        param.setWhetherPrizes(whetherPrizes);
        param.setClassificationId(classificationId);
        page =  goodsBiz.queryGoodsDOListForClassification(param,page);
        List<GoodsDO> goodsDOs = page.getInfos();
        if (ListUtils.isNotEmpty(goodsDOs)){
            for (GoodsDO goodsDO : goodsDOs) {
                if(CollectionUtils.isNotEmpty(goodsDO.getShopClassificationList())){
                    String shopClassificationNames = "";
                    for(ShopClassificationDO item : goodsDO.getShopClassificationList()){
                        shopClassificationNames += item.getClassificationName()+",";
                    }
                    shopClassificationNames = shopClassificationNames.substring(0, shopClassificationNames.length()-1);
                    goodsDO.setShopClassificationNames(shopClassificationNames);
                }
            }
        }
        return TxJerseyTools.returnSuccess(JSON.toJSONString(page));
    }

    //构建GoodsDO
    private void handleGoodsDO(GoodsDO goodsDO, Long category, String batchNumber, String targetUrl, Long validStartTime, Long validEndTime, Long autoUpTime, Long autoDownTime) {
        CategoryDO category1 = categoryBiz.getCategoryDOById(category);
        if (1 == category1.getIsVirtual().intValue()) {
            goodsDO.setBatchNumber(batchNumber);
            goodsDO.setTargetUrl(targetUrl);
            if (null != validStartTime) {
                goodsDO.setValidStartTime(new Date(validStartTime));
            }
            if (null != validEndTime) {
                goodsDO.setValidEndTime(new Date(validEndTime));
            }
            if (null != autoUpTime) {
                goodsDO.setAutoUpTime(new Date(autoUpTime));
            }
            if (null != autoDownTime) {
                goodsDO.setAutoDownTime(new Date(autoDownTime));
            }
            goodsDO.setBarcode(GuidUtil.getNextUid("bar"));
        }
    }

    @POST
    @Path(ScoreAdminConstants.Route.Goods.SET_CLASSIFICATION)
    @Authority
    public Response setClassification(@PathParam("shopId") Long shopId, @NotEmpty(message = "goodsIds不能为空") @FormParam("goodsIds") String goodsIds,
                                      @NotEmpty(message = "classificationIds不能为空") @FormParam("classificationIds") String classificationIds,@Context ContainerRequestContext requestContext) {
        _checkString(goodsIds);
        _checkString(classificationIds);
        String[] goodsIdStrs = goodsIds.split(",");
        String[] classificationIdStrs = classificationIds.split(",");
        List<Long> goodsList = new ArrayList<>();
        for(String goodsId : goodsIdStrs){
            goodsList.add(Long.valueOf(goodsId));
        }
        List<GoodsClassificationRelationshipDO> gcrList =  new ArrayList<>();
        for(String classificationId : classificationIdStrs){
            for(Long goodsId : goodsList){
                GoodsClassificationRelationshipDO item = new GoodsClassificationRelationshipDO();
                item.setGoodsId(goodsId);
                item.setShopClassificationId(Long.valueOf(classificationId));
                gcrList.add(item);
            }
        }
        goodsBiz.setClassification(goodsList, gcrList);
        return TxJerseyTools.returnSuccess();

    }

    /**
     * 上架商品
     * @param shopId
     * @param id
     * @return
     */
    @PUT
    @Authority
    @Path(ScoreAdminConstants.Route.Goods.ENTITY_UP + "/{id}")
    public Response upById(@PathParam("shopId") Long shopId, @PathParam("id") Long id,@Context ContainerRequestContext requestContext) {
        GoodsDO goods = goodsBiz.getGoodsDOById(id, null);
        if (null == goods || goods.getShopId().longValue() != shopId.longValue()) {
            throw new GoodsException(ExceptionEnum.ERROR_ILLEGAL_OPERATION,"操作不合法");
        }
        return up(id);
    }

    /**
     * 下架商品
     * @param shopId
     * @param id
     * @return
     */
    @PUT
    @Authority
    @Path(ScoreAdminConstants.Route.Goods.ENTITY_DOWN + "/{id}")
    public Response downById(@PathParam("shopId") Long shopId, @PathParam("id") Long id,@Context ContainerRequestContext requestContext) {
        GoodsDO goods = goodsBiz.getGoodsDOById(id, null);
        if (null == goods || goods.getShopId().longValue() != shopId.longValue()) {
            throw new GoodsException(ExceptionEnum.ERROR_ILLEGAL_OPERATION,"操作不合法");
        }
        return down(id);
    }

    private Response up(Long goodsId) {
        goodsBiz.upById(goodsId);
        return TxJerseyTools.returnSuccess();
    }

    private Response down(Long goodsId) {
        goodsBiz.downById(goodsId);
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 查询类目列表
     * @param name 类目名称
     * @return Response
     */
    @GET
    @Authority
    @Path(ScoreAdminConstants.Route.Goods.ENTITY + ScoreAdminConstants.Route.Goods.CATEGORY)
    public Response getCategoryList(@PathParam("shopId") Long shopId, @QueryParam("name") String name,@Context ContainerRequestContext requestContext) {

        CategoryDO query = new CategoryDO();
        query.setCategoryName(name);
        List<CategoryDO> categoryDOs = categoryBiz.selectCategoryDOList(query);
        JSONArray jsonArray = new JSONArray();
        if (ListUtils.isNotEmpty(categoryDOs)) {
            for (CategoryDO categoryDO : categoryDOs) {
                JSONObject json = new JSONObject();
                json.put("id", categoryDO.getId());
                json.put("pid", categoryDO.getPid());
                json.put("categoryName", categoryDO.getCategoryName());
                json.put("logoUrl", categoryDO.getLogoUrl());
                json.put("sort", categoryDO.getSort());
                json.put("description", categoryDO.getDescription());
                json.put("operatorUserId", categoryDO.getOperatorUserId());
                json.put("createTime", categoryDO.getCreateTime().getTime());
                json.put("updateTime", categoryDO.getUpdateTime().getTime());
                json.put("code", categoryDO.getCode());
                jsonArray.add(json);
            }
        }
        return TxJerseyTools.returnSuccess(JSON.toJSONString(jsonArray));
    }

    /**
     * 检查批次号
     * @param shopId
     * @param eid
     * @return
     */
    @GET
    @Authority
    @Path(ScoreAdminConstants.Route.Goods.CHECKEID)
    public Response checkEid(@PathParam("shopId") Long shopId, @NotNull @QueryParam("eid")String eid,@Context ContainerRequestContext requestContext){
        try {
            CouponDto couponDto = goodsBiz.checkEid(eid);
            if(CouponDto.SUCCESS_CODE.equals(couponDto.getCode())) {
                JSONObject result = new JSONObject();
                result.put("packageFrom", couponDto.getData().getPackageFrom());
                result.put("packageTo", couponDto.getData().getPackageTo());
                return TxJerseyTools.returnSuccess(JSON.toJSONString(result));
            } else{
            	return CustomAck.customError("金融卡券批次号不存在!");
            }
        }catch (Exception e){
            logger.error("金融卡券查询服务不可用!");
            return CustomAck.customError("金融卡券查询服务不可用!");
        }

    }

    /**
     * 检查泰然优惠券ID
     * @param shopId
     * @param eid
     * @return
     */

    @GET
    @Authority
    @Path(ScoreAdminConstants.Route.Goods.CHECKEID + ScoreAdminConstants.Route.Goods.TAIRAN)
    public Response checkTairanEid(@PathParam("shopId") Long shopId, @NotNull @QueryParam("eid")String eid,@Context ContainerRequestContext requestContext){
        try {
            TairanCouponDto tairanCouponDto = goodsBiz.checkTairanEid(eid);
            logger.info("" + TairanCouponDto.SUCCESS_CODE.intValue());
            logger.info("" + tairanCouponDto.getCode().intValue());
            if(TairanCouponDto.SUCCESS_CODE.intValue() == tairanCouponDto.getCode().intValue()) {
                JSONObject result = new JSONObject();
                result.put("packageFrom", tairanCouponDto.getData().getPackageFrom());
                result.put("packageTo", tairanCouponDto.getData().getPackageTo());
                result.put("useStartTime", tairanCouponDto.getData().getUseStartTime());
                result.put("useEndTime", tairanCouponDto.getData().getUseEndTime());
                result.put("expireTime", tairanCouponDto.getData().getExpireTime());
                result.put("limitQuantity", tairanCouponDto.getData().getUser_obtain_limit());
                result.put("couponType", tairanCouponDto.getData().getCoupon_type());
                return TxJerseyTools.returnSuccess(result.toJSONString());
            } else{
                return CustomAck.customError(tairanCouponDto.getMsg());
            }
        } catch (CouponException e){
            logger.error(e.getMessage());
            return CustomAck.customError(e.getMessage());
        } catch (Exception e){
            logger.error("泰然优惠券查询服务不可用!");
            return CustomAck.customError("泰然优惠券查询服务不可用!");
        }
    }
}
