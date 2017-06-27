package org.trc.resource.pagehome;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.pagehome.IBannerBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.pagehome.Banner;
import org.trc.form.pagehome.BannerForm;
import org.trc.util.AppResult;
import org.trc.util.Pagenation;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static org.trc.util.ResultUtil.*;

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
    public AppResult<JSONObject> createBanner(@BeanParam BannerForm form,
                                  @Context ContainerRequestContext requestContext){

        Banner banner = new Banner();
        banner.setShopId(form.getShopId());
        banner.setName(form.getName());
        String userId= (String) requestContext.getProperty("userId");
        banner.setOperatorUserId(userId);
        bannerBiz.saveBanner(banner);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bannerId", banner.getId());
        return createSucssAppResult("保存banner成功", jsonObject);
    }

    /**
     * 修改Banner
     *
     * @param name 名称
     * @return Response
     */
    @PUT
    @Path("{id}")
    public AppResult<JSONObject> modifyBanner(@BeanParam BannerForm form,
                                 @NotBlank @FormParam("name") String name,
                                 @Context ContainerRequestContext requestContext){

            Banner banner = bannerBiz.selectByIdAndShopId(form);
            banner.setName(name);
            String userId= (String) requestContext.getProperty("userId");
            banner.setOperatorUserId(userId);
            bannerBiz.updateBanner(banner);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("bannerId", banner.getId());
            return createSucssAppResult("更新banner成功", jsonObject);
    }

    /**
     * 查询banner列表
     * @param form
     * @param page
     * @return Pagenation<Banner>
     * @throws Exception
     */
    @GET
    public Pagenation<Banner> bannerPage(@BeanParam BannerForm form,@BeanParam Pagenation<Banner> page){
        return bannerBiz.bannerPage(form,page);
    }

    /**
     * 根据Id查询banner
     * @param form
     * @return
     * @throws Exception
     */
    @GET
    @Path("{id}")
    public AppResult<Banner> getBannerById(@BeanParam BannerForm form){
        return createSucssAppResult("查询banner成功", bannerBiz.selectByIdAndShopId(form));
    }

    /**
     * 设置banner内容
     * @param form
     * @return
     * @throws Exception
     */
    @PUT
    @Path(ScoreAdminConstants.Route.Banner.SETCONTENT + "/{id}")
    public AppResult setBannerContent(@BeanParam BannerForm form){
        Banner banner = bannerBiz.selectByIdAndShopId(form);
        return null;
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
    public AppResult setBannerIsUp(@BeanParam BannerForm form, @NotNull@FormParam("isUp")Boolean isUp,@Context ContainerRequestContext requestContext){
        Banner banner = bannerBiz.selectByIdAndShopId(form);
        banner.setIsUp(isUp);
        String userId= (String) requestContext.getProperty("userId");
        banner.setOperatorUserId(userId);
        bannerBiz.updateBanner(banner);
        return createSucssAppResult("设置成功", "");
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
    public AppResult exchangeSort(@PathParam("shopId") Long shopId,
                                  @NotNull@FormParam("idA") Long idA,
                                  @NotNull@FormParam("idB") Long idB){
        BannerForm bannerFormA = new BannerForm();
        bannerFormA.setShopId(shopId);
        bannerFormA.setId(idA);
        Banner bannerA = bannerBiz.selectByIdAndShopId(bannerFormA);
        BannerForm bannerFormB = new BannerForm();
        bannerFormB.setShopId(shopId);
        bannerFormB.setId(idB);
        Banner bannerB = bannerBiz.selectByIdAndShopId(bannerFormB);
        bannerBiz.exchangeSort(bannerA,bannerB);
        return createSucssAppResult("设置成功","");
    }


}
