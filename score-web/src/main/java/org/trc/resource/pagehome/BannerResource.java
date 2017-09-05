package org.trc.resource.pagehome;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.pagehome.IBannerBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.pagehome.Banner;
import org.trc.form.pagehome.BannerForm;
import org.trc.interceptor.annotation.Authority;
import org.trc.util.Pagenation;
import org.trc.util.TxJerseyTools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by hzwzhen on 2017/6/9.
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("{shopId}/" + ScoreAdminConstants.Route.Banner.ROOT)
public class BannerResource{

    @Autowired
    private IBannerBiz bannerBiz;

    /**
     * 创建Banner
     * @param form BannerForm
     * @return AppResult
     */
    @POST
    @Authority
    public Response createBanner(@PathParam("shopId") Long shopId,@BeanParam BannerForm form,
                                 @NotBlank @FormParam("name") String name,@FormParam("type") String type,
                                  @Context ContainerRequestContext requestContext){

        Banner banner = new Banner();
        banner.setShopId(form.getShopId());
        banner.setName(name);
        banner.setType(type);
        String userId= (String) requestContext.getProperty("userId");
        banner.setOperatorUserId(userId);
        bannerBiz.saveBanner(banner);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bannerId", banner.getId());
//        return createSucssAppResult("保存banner成功", jsonObject);
        return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
    }

    /**
     * 修改Banner
     *
     * @param name 名称
     * @return Response
     */
    @PUT
    @Path("{id}")
    @Authority
    public Response modifyBanner(@PathParam("shopId") Long shopId,@BeanParam BannerForm form,
                                 @NotBlank @FormParam("name") String name,
                                 @Context ContainerRequestContext requestContext){

            Banner banner = bannerBiz.selectByIdAndShopId(form);
            banner.setName(name);
            String userId= (String) requestContext.getProperty("userId");
            banner.setOperatorUserId(userId);
            bannerBiz.updateBanner(banner);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bannerId", banner.getId());
//            return createSucssAppResult("更新banner成功", jsonObject);
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
    }

    /**
     * 查询banner列表
     * @param form
     * @param page
     * @return Pagenation<Banner>
     * @throws Exception
     */
    @GET
    @Authority
    public Response bannerPage(@PathParam("shopId") Long shopId,@BeanParam BannerForm form,@BeanParam Pagenation<Banner> page,
                                         @Context ContainerRequestContext requestContext){
//        return bannerBiz.bannerPage(form,page);
        Pagenation<Banner> pageBanners = bannerBiz.bannerPage(form,page);
        return TxJerseyTools.returnSuccess(JSON.toJSONString(pageBanners));
    }

    /**
     * 根据Id查询banner
     * @param form
     * @return
     * @throws Exception
     */
    @GET
    @Path("{id}")
    @Authority
    public Response getBannerById(@PathParam("shopId") Long shopId,@BeanParam BannerForm form,@Context ContainerRequestContext requestContext){
//        return createSucssAppResult("查询banner成功", bannerBiz.selectByIdAndShopId(form));
        Banner banner = bannerBiz.selectByIdAndShopId(form);
        if(banner!=null){
        	return TxJerseyTools.returnSuccess(JSON.toJSONString(banner));
        }
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 设置banner内容
     * @param form
     * @return
     * @throws Exception
     */
    @PUT
    @Path(ScoreAdminConstants.Route.Banner.SETCONTENT + "/{id}")
    @Authority
    public Response setBannerContent(@PathParam("shopId") Long shopId,@BeanParam BannerForm form, @NotNull@FormParam("contentId")Long contentId,@Context ContainerRequestContext requestContext){
        Banner banner = bannerBiz.selectByIdAndShopId(form);
        banner.setContentId(contentId);
        String userId= (String) requestContext.getProperty("userId");
        banner.setOperatorUserId(userId);
        bannerBiz.updateBanner(banner);
//        return createSucssAppResult("设置成功", "");
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 设置Banner启用停用
     * @param form
     * @param isUp
     * @param requestContext
     * @return
     * @throws Exception
     */
    @PUT
    @Path(ScoreAdminConstants.Route.Banner.SETUP + "/{id}")
    @Authority
    public Response setBannerIsUp(@PathParam("shopId") Long shopId,@BeanParam BannerForm form, @NotNull@FormParam("isUp")Boolean isUp,@Context ContainerRequestContext requestContext){
        Banner banner = bannerBiz.selectByIdAndShopId(form);
        banner.setIsUp(isUp);
        String userId= (String) requestContext.getProperty("userId");
        banner.setOperatorUserId(userId);
        bannerBiz.updateBanner(banner);
//        return createSucssAppResult("设置成功", "");
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 交换排序
     * @param shopId  商铺Id
     * @param idA ID
     * @param idB ID
     * @return
     * @throws Exception
     */
    @PUT
    @Path(ScoreAdminConstants.Route.Banner.SORT)
    @Authority
    public Response exchangeSort(@PathParam("shopId") Long shopId,
                                  @NotNull@FormParam("idA") Long idA,
                                  @NotNull@FormParam("idB") Long idB,
                                  @Context ContainerRequestContext requestContext){
        BannerForm bannerFormA = new BannerForm();
        bannerFormA.setShopId(shopId);
        bannerFormA.setId(idA);
        Banner bannerA = bannerBiz.selectByIdAndShopId(bannerFormA);
        BannerForm bannerFormB = new BannerForm();
        bannerFormB.setShopId(shopId);
        bannerFormB.setId(idB);
        Banner bannerB = bannerBiz.selectByIdAndShopId(bannerFormB);
        bannerBiz.exchangeSort(bannerA,bannerB);
//        return createSucssAppResult("设置成功","");
        return TxJerseyTools.returnSuccess();
    }


}
