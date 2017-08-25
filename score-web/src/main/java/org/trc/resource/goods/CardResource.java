package org.trc.resource.goods;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.goods.ICouponsBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;
import org.trc.domain.goods.CardCouponsDO;
import org.trc.domain.goods.CardItemDO;
import org.trc.form.goods.CardCouponsForm;
import org.trc.util.AppResult;
import org.trc.util.Pagenation;
import org.trc.util.XlsProcessing;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.trc.util.ResultUtil.createFailAppResult;
import static org.trc.util.ResultUtil.createSucssAppResult;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments: 卡券管理
 * since Date： 2017/6/27
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.CardCoupuns.ROOT)
public class CardResource {

    private Logger logger = LoggerFactory.getLogger(CardResource.class);
    @Autowired
    private ICouponsBiz couponsBiz;
    @Autowired
    private IAuthBiz authBiz;

    /**
     * 分页查询
     *
     * @param cardCouponsForm cardCouponsForm
     * @param page            page
     * @return Pagenation
     */
    @GET
    public Pagenation<CardCouponsDO> list(@BeanParam CardCouponsForm cardCouponsForm,
                                          @BeanParam Pagenation<CardCouponsDO> page,
                                          @Context ContainerRequestContext requestContext) {
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        cardCouponsForm.setShopId(auth.getShopId());
        return couponsBiz.queryCouponsForPage(cardCouponsForm, page);
    }

    /**
     * 新增卡券
     *
     * @param couponName     卡券名
     * @param validStartTime 有效期开始时间
     * @param validEndTime   有效期结束时间
     * @param remark         备注
     * @param requestContext requestContext
     * @return AppResult
     */
    @POST
    public AppResult create(@FormParam("couponName") String couponName,
                            @FormParam("validStartTime") Long validStartTime,
                            @FormParam("validEndTime") Long validEndTime,
                            @FormParam("remark") String remark,
                            @Context ContainerRequestContext requestContext){
        //获取userId
        String userId= (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        CardCouponsDO cardCoupon = new CardCouponsDO();
        cardCoupon.setCouponName(couponName);
        cardCoupon.setRemark(remark);
        cardCoupon.setShopId(auth.getShopId());
        if (null != validStartTime) {
            cardCoupon.setValidStartTime(new Date(validStartTime));
        }
        if (null != validEndTime) {
            cardCoupon.setValidEndTime(new Date(validEndTime));
        }
        Date createTime = Calendar.getInstance().getTime();
        cardCoupon.setCreateTime(createTime);
        cardCoupon.setUpdateTime(createTime);
        cardCoupon.setBatchNumber(RandomStringUtils.randomAlphanumeric(16));
        couponsBiz.insert(cardCoupon);
        return createSucssAppResult("保存卡券成功", "");
    }

    /**
     * 删除卡券
     *
     * @param batchNumber    批次号
     * @param requestContext requestContext
     * @return AppResult
     */
    @DELETE
    public AppResult delete(@QueryParam("batchNumber") String batchNumber,
                            @Context ContainerRequestContext requestContext) {
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        CardCouponsDO cardCoupon = new CardCouponsDO();
        cardCoupon.setBatchNumber(batchNumber);
        cardCoupon.setShopId(auth.getShopId());
        Date updateTime = Calendar.getInstance().getTime();
        cardCoupon.setUpdateTime(updateTime);
        int result = couponsBiz.deleteByBatchNumber(cardCoupon);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("batchNumber", cardCoupon.getBatchNumber());
        jsonObject.put("count", result >= 1 ? result : 0);
        return createSucssAppResult("删除卡券成功", jsonObject);
    }

    /**
     * 检查批次号
     *
     * @param batchNumber 批次号
     * @param requestContext requestContext
     * @return AppResult
     */
    @Path("/checkEid")
    @POST
    public AppResult checkEid(@FormParam("batchNumber") String batchNumber,
                              @Context ContainerRequestContext requestContext) {

        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        CardCouponsDO cardCouponsDO = couponsBiz.selectByBatchNumer(auth.getShopId(), batchNumber);
        JSONObject result = new JSONObject();
        if (null == cardCouponsDO) {
            return createFailAppResult(String.format("批次号:%s对应的卡券不存在!", batchNumber));
        } else {
            result.put("validStartTime", cardCouponsDO.getValidStartTime());
            result.put("validEndTime", cardCouponsDO.getValidEndTime());
            return createSucssAppResult("查询卡券成功", result);
        }
    }

    @POST
    @Path("/uploadExcel")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public AppResult uploadExcel(@Context ContainerRequestContext requestContext,
                                @QueryParam("batchNumber") String batchNumber,
                                @FormDataParam("Filedata") InputStream fileInputStream,
                                @FormDataParam("Filedata") FormDataContentDisposition disposition) throws Exception{
        if (null == disposition) {
            return createFailAppResult("未找到导入文件!");
        }
        String dirName = DateFormatUtils.format(Calendar.getInstance(), DateFormatUtils.ISO_DATE_FORMAT.getPattern());
        String fileName = Calendar.getInstance().getTimeInMillis() + "-" + new String(disposition.getFileName().getBytes("ISO8859-1"), "UTF-8").toLowerCase();
        if (!fileName.endsWith(".xls") && !fileName.endsWith(".xlsx")) {
            return createFailAppResult("只支持文件类型为.xls的文件上传!");
        }
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        List<CardItemDO> cardItemList = XlsProcessing.getInstance().dealFile(batchNumber, auth.getShopId(), dirName, fileName, fileInputStream);
        couponsBiz.importCardItem(batchNumber, auth.getShopId(), cardItemList);
        return createSucssAppResult("导入成功!", "");
    }

    /**
     * 查询卡券明细
     *
     * @param batchNumber    批次号
     * @param code           卡券码
     * @param requestContext requestContext
     * @return AppResult
     */
    @GET
    @Path(ScoreAdminConstants.Route.CardCoupuns.ITEM)
    public AppResult<CardItemDO> itemList(@NotNull @QueryParam("batchNumber") String batchNumber,
                                          @NotNull @QueryParam("code") String code,
                                          @Context ContainerRequestContext requestContext) {
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        CardItemDO cardItem = couponsBiz.selectItemByCode(auth.getShopId(), batchNumber, code);
        return createSucssAppResult("查询卡券明细成功", cardItem);
    }

    /**
     * 删除卡券明细
     *
     * @param id             id
     * @param requestContext requestContext
     * @return AppResult
     */
    @DELETE
    @Path(ScoreAdminConstants.Route.CardCoupuns.ITEM)
    public AppResult itemDelete(@QueryParam("id") Long id,
                                @Context ContainerRequestContext requestContext) {
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        CardItemDO cardItem = new CardItemDO();
        cardItem.setShopId(auth.getShopId());
        cardItem.setId(id);
        int result = couponsBiz.deleteItemById(cardItem);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", cardItem.getId());
        jsonObject.put("count", 1 == result?1:0);
        return createSucssAppResult("删除卡券明细成功", jsonObject);
    }
}
