package org.trc.resource.goods;

import com.alibaba.dubbo.common.utils.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trc.mall.externalservice.dto.CouponDto;
import com.txframework.util.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
import org.trc.exception.GoodsException;
import org.trc.util.AppResult;
import org.trc.util.GuidUtil;
import org.trc.util.Pagenation;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.trc.util.ResultUtil.createFailAppResult;
import static org.trc.util.ResultUtil.createSucssAppResult;

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
    @Path(ScoreAdminConstants.Route.Goods.ENTITY)
    public AppResult publish(@NotNull @PathParam("shopId") Long shopId, @NotNull @FormParam("category") Long category, @FormParam("brandName") String brandName,
                             @NotEmpty @FormParam("goodsName") String goodsName, @FormParam("barcode") String barcode, @FormParam("goodsNo") String goodsNo,
                             @FormParam("batchNumber") String batchNumber, @NotEmpty @FormParam("mainImg") String mainImg,@NotEmpty @FormParam("mediumImg") String mediumImg, @FormParam("priceMarket") Integer priceMarket,
                             @NotNull @FormParam("priceScore") Integer priceScore, @NotNull @FormParam("stock") Integer stock, @NotNull @FormParam("stockWarn") Integer stockWarn,
                             @FormParam("targetUrl") String targetUrl, @FormParam("validStartTime") Long validStartTime, @FormParam("validEndTime") Long validEndTime,
                             @FormParam("autoUpTime") Long autoUpTime, @FormParam("autoDownTime") Long autoDownTime, @NotEmpty @FormParam("content") String content,
                             @FormParam("virtualExchangeQuantity") Integer virtualExchangeQuantity, @FormParam("whetherPrizes") Integer whetherPrizes,
                             @FormParam("sort") Integer sort, @FormParam("shopClassificationIds") String shopClassificationIds,
                             @NotNull @FormParam("limitQuantity") Integer limitQuantity) {
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
        return createSucssAppResult("保存商品成功", "");
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
    public AppResult modify(@NotNull @PathParam("shopId") Long shopId, @NotNull @FormParam("id") Long id, @NotNull @FormParam("category") Long category,
                            @FormParam("brandName") String brandName, @NotEmpty @FormParam("goodsName") String goodsName, @NotEmpty @FormParam("barcode") String barcode,
                            @FormParam("goodsNo") String goodsNo, @FormParam("batchNumber") String batchNumber, @NotEmpty @FormParam("mediumImg") String mediumImg,
                            @FormParam("priceMarket") Integer priceMarket, @NotNull @FormParam("priceScore") Integer priceScore, @NotNull @FormParam("stock") Integer stock,
                            @NotNull @FormParam("stockWarn") Integer stockWarn, @FormParam("targetUrl") String targetUrl, @FormParam("validStartTime") Long validStartTime,
                            @FormParam("validEndTime") Long validEndTime, @FormParam("autoUpTime") Long autoUpTime, @FormParam("autoDownTime") Long autoDownTime,
                            @NotEmpty @FormParam("content") String content, @FormParam("virtualExchangeQuantity") Integer virtualExchangeQuantity,
                            @FormParam("whetherPrizes") Integer whetherPrizes, @FormParam("sort") Integer sort, @FormParam("shopClassificationIds") String shopClassificationIds,
                            @NotNull @FormParam("limitQuantity") Integer limitQuantity) {

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
        return createSucssAppResult("修改商品成功", "");

    }
    /**
     * 查询商品
     *
     * @return
     */
    @GET
    @Path(ScoreAdminConstants.Route.Goods.ENTITY + "/{id}")
    public AppResult<JSONObject> getGoodsById(@PathParam("shopId") Long shopId,
                                 @PathParam("id") Long id) {
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
        return createSucssAppResult("查询商品成功", jsonObject);

    }

    /**
     * 删除商品
     */
    @DELETE
    @Path(ScoreAdminConstants.Route.Goods.ENTITY + "/{id}")
    public AppResult deleteGoodsDO(@PathParam("shopId") Long shopId, @PathParam("id") Long id){

        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setId(id);
        goodsDO.setShopId(shopId);
        goodsBiz.deleteGoodsDO(goodsDO);
        return createSucssAppResult("删除商品成功", "");

    }


    /**
     * 查询商品列表
     *
     * @return
     */
    @GET
    @Path(ScoreAdminConstants.Route.Goods.ENTITY)
    public Pagenation<GoodsDO>  getGoodsList(@PathParam("shopId") Long shopId,
                                 @QueryParam("goodsName") String goodsName,
                                 @QueryParam("isUp") Integer isUp,
                                 @QueryParam("whetherPrizes") Integer whetherPrizes,
                                 @QueryParam("classificationId") Long classificationId,
                                 @BeanParam Pagenation<GoodsDO> page) {
        GoodsQuery param = new GoodsQuery();
        param.setShopId(shopId);
        param.setGoodsName(goodsName);
        param.setIsUp(isUp);
        param.setWhetherPrizes(whetherPrizes);
        param.setClassificationId(classificationId);
        page =  goodsBiz.queryGoodsDOListForClassification(param,page);
        List<GoodsDO> goodsDOs = page.getResult();
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
        return page;
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
    public AppResult setClassification(@PathParam("shopId") Long shopId, @NotEmpty @FormParam("goodsIds") String goodsIds,
                                      @NotEmpty @FormParam("classificationIds") String classificationIds) {
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
        return createSucssAppResult("操作成功", "");

    }

    /**
     * 上架商品
     * @param shopId
     * @param id
     * @return
     */
    @PUT
    @Path(ScoreAdminConstants.Route.Goods.ENTITY_UP + "/{id}")
    public AppResult upById(@PathParam("shopId") Long shopId, @PathParam("id") Long id) {
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
    @Path(ScoreAdminConstants.Route.Goods.ENTITY_DOWN + "/{id}")
    public AppResult downById(@PathParam("shopId") Long shopId, @PathParam("id") Long id) {
        GoodsDO goods = goodsBiz.getGoodsDOById(id, null);
        if (null == goods || goods.getShopId().longValue() != shopId.longValue()) {
            throw new GoodsException(ExceptionEnum.ERROR_ILLEGAL_OPERATION,"操作不合法");
        }
        return down(id);
    }

    private AppResult up(Long goodsId) {
        goodsBiz.upById(goodsId);
        return createSucssAppResult("商品上架成功", "");
    }

    private AppResult down(Long goodsId) {
        goodsBiz.downById(goodsId);
        return createSucssAppResult("商品下架成功", "");
    }

    /**
     * 查询类目列表
     * @param name 类目名称
     * @return Response
     */
    @GET
    @Path(ScoreAdminConstants.Route.Goods.ENTITY + ScoreAdminConstants.Route.Goods.CATEGORY)
    public AppResult<JSONArray> getCategoryList(@PathParam("shopId") Long shopId, @QueryParam("name") String name) {

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
        return createSucssAppResult("商品下架成功", jsonArray);
    }

    /**
     * 检查批次号
     * @param shopId
     * @param eid
     * @return
     */
    @GET
    @Path(ScoreAdminConstants.Route.Goods.CHECKEID)
    public AppResult checkEid(@PathParam("shopId") Long shopId, @NotNull @QueryParam("eid")String eid){
        try {
            CouponDto couponDto = goodsBiz.checkEid(eid);
            if(CouponDto.SUCCESS_CODE.equals(couponDto.getCode())) {
                JSONObject result = new JSONObject();
                result.put("packageFrom", couponDto.getData().getPackageFrom());
                result.put("packageTo", couponDto.getData().getPackageTo());
                return createSucssAppResult("查询成功", result);
            } else{
                return createFailAppResult("金融卡券批次号不存在!");
            }
        }catch (Exception e){
            logger.error("金融卡券查询服务不可用!");
            return createFailAppResult("金融卡券查询服务不可用!");
        }

    }
}
