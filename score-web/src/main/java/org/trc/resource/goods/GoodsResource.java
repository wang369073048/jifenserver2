package org.trc.resource.goods;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.goods.ICategoryBiz;
import org.trc.biz.goods.IGoodsBiz;
import org.trc.constants.Category;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.goods.CategoryDO;
import org.trc.domain.goods.GoodsDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.GoodsException;
import org.trc.util.AppResult;
import org.trc.util.GuidUtil;
import org.trc.util.Pagenation;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Calendar;
import java.util.Date;

import static org.trc.util.ResultUtil.createSucssAppResult;

/**
 * Created by hzwzhen on 2017/6/22.
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("{shopId}/" + ScoreAdminConstants.Route.Goods.ROOT)
public class GoodsResource {

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
                             @FormParam("batchNumber") String batchNumber, @NotEmpty @FormParam("mediumImg") String mediumImg, @FormParam("priceMarket") Integer priceMarket,
                             @NotNull @FormParam("priceScore") Integer priceScore, @NotNull @FormParam("stock") Integer stock, @NotNull @FormParam("stockWarn") Integer stockWarn,
                             @FormParam("targetUrl") String targetUrl, @FormParam("validStartTime") Long validStartTime, @FormParam("validEndTime") Long validEndTime,
                             @FormParam("autoUpTime") Long autoUpTime, @FormParam("autoDownTime") Long autoDownTime, @NotEmpty @FormParam("content") String content,
                             @FormParam("virtualExchangeQuantity") Integer virtualExchangeQuantity) {
        Date time = Calendar.getInstance().getTime();
        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setShopId(shopId);
        goodsDO.setCategory(category);
        goodsDO.setBarcode(barcode);
        goodsDO.setGoodsNo(goodsNo);
        goodsDO.setBrandName(brandName);
        goodsDO.setGoodsName(goodsName);
        goodsDO.setMediumImg(mediumImg);
        goodsDO.setPriceMarket(priceMarket);
        goodsDO.setPriceScore(priceScore);
        goodsDO.setStock(stock);
        goodsDO.setStockWarn(stockWarn);
        goodsDO.setContent(content);
        goodsDO.calMainImg();
        goodsDO.setIsDeleted(0);
        goodsDO.setIsUp(0);
        goodsDO.setVirtualExchangeQuantity(null == virtualExchangeQuantity ? 0 : virtualExchangeQuantity);
        goodsDO.setCreateTime(time);
        handleGoodsDO(goodsDO, category, batchNumber, targetUrl, validStartTime, validEndTime, autoUpTime, autoDownTime);
        goodsBiz.saveGoodsDO(goodsDO);
        return createSucssAppResult("保存商品成功", "");

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
                            @NotEmpty @FormParam("content") String content, @FormParam("virtualExchangeQuantity") Integer virtualExchangeQuantity) {

        GoodsDO goods = goodsBiz.getGoodsDOById(id, null);
        if (null == goods || goods.getShopId().longValue() != shopId.longValue()) {
            throw new GoodsException(ExceptionEnum.ERROR_ILLEGAL_OPERATION, "操作不合法");
        }
        Date time = Calendar.getInstance().getTime();
        GoodsDO goodsDO = new GoodsDO();
        goodsDO.setId(id);
        goodsDO.setShopId(shopId);
        goodsDO.setCategory(category);
        goodsDO.setGoodsNo(goodsNo);
        if (1l == category) {
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
        goodsDO.setUpdateTime(time);
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
        jsonObject.put("isUp", goods.getIsUp());
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
                                 @BeanParam Pagenation<GoodsDO> page) {
        GoodsDO query = new GoodsDO();
        query.setShopId(shopId);
        query.setGoodsName(goodsName);
        query.setIsDeleted(0);
        query.setIsUp(isUp);
        page =  goodsBiz.queryGoodsDOListForPage(query,page);
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
}
