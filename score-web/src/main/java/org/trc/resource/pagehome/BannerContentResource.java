package org.trc.resource.pagehome;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.pagehome.IBannerContentBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.pagehome.BannerContent;
import org.trc.form.pagehome.BannerContentForm;
import org.trc.util.AppResult;
import org.trc.util.Pagenation;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import static org.trc.util.ResultUtil.createSucssAppResult;

/**
 * Created by hzwzhen on 2017/6/12.
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("{shopId}/" + ScoreAdminConstants.Route.BannerContent.ROOT)
public class BannerContentResource {

    @Autowired
    private IBannerContentBiz bannerContentBiz;

    @POST
    public AppResult<JSONObject> createBannerContent(@PathParam("shopId") Long shopId,
                                        @NotBlank @FormParam("title") String title,
                                        @NotBlank @FormParam("imgUrl") String imgUrl,
                                        @NotBlank @FormParam("targetUrl") String targetUrl,
                                        @NotBlank @FormParam("description") String description,
                                         @Context ContainerRequestContext requestContext) {
        BannerContent bannerContent = new BannerContent();
        bannerContent.setShopId(shopId);
        bannerContent.setTitle(title);
        bannerContent.setImgUrl(imgUrl);
        bannerContent.setTargetUrl(targetUrl);
        bannerContent.setDescription(description);
        String userId= (String) requestContext.getProperty("userId");
        bannerContent.setOperatorUserId(userId);
        bannerContentBiz.saveBannerContent(bannerContent);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bannerContentId", bannerContent.getId());
        return createSucssAppResult("保存bannerContent成功", jsonObject);
    }

    /**
     * 修改Banner内容
     * @param shopId 业务方Id
     * @param id 主键
     * @param title 标题
     * @param imgUrl 图片地址
     * @param targetUrl 目标地址
     * @param description 备注
     * @param requestContext requestContext
     * @return
     */
    @PUT
    @Path("{id}")
    public AppResult modifyBannerContent(@PathParam("shopId") Long shopId,
                                        @PathParam("id") Long id,
                                        @NotBlank @FormParam("title") String title,
                                        @NotBlank @FormParam("imgUrl") String imgUrl,
                                        @NotBlank @FormParam("targetUrl") String targetUrl,
                                        @NotBlank @FormParam("description") String description,
                                        @Context ContainerRequestContext requestContext) {
        BannerContent bannerContent = new BannerContent();
        bannerContent.setId(id);
        bannerContent.setShopId(shopId);
        bannerContent = bannerContentBiz.selectByIdAndShopId(bannerContent);
        bannerContent.setTitle(title);
        bannerContent.setImgUrl(imgUrl);
        bannerContent.setTargetUrl(targetUrl);
        bannerContent.setDescription(description);
        String userId= (String) requestContext.getProperty("userId");
        bannerContent.setOperatorUserId(userId);
        bannerContentBiz.updateBannerContent(bannerContent);
        return createSucssAppResult("更新bannerContent成功", "");
    }
    /**
     * 删除banner内容
     */
    @DELETE
    @Path("{id}")
    public AppResult deleteBannerContent(@PathParam("shopId") Long shopId,
                                         @PathParam("id") Long id,
                                         @Context ContainerRequestContext requestContext) {
        BannerContent bannerContent = new BannerContent();
        bannerContent.setId(id);
        bannerContent.setShopId(shopId);
        bannerContent = bannerContentBiz.selectByIdAndShopId(bannerContent);
        String userId= (String) requestContext.getProperty("userId");
        bannerContent.setOperatorUserId(userId);
        bannerContentBiz.deleteBannerContent(bannerContent);
        return createSucssAppResult("删除bannerContent成功", "");
    }

    /**
     * 查询Banner内容列表
     * @param bannerContentForm
     * @param page
     * @return
     */
    @GET
    public Pagenation<BannerContent> getBannerContentList(@BeanParam BannerContentForm bannerContentForm,@BeanParam Pagenation<BannerContent> page) {
        return bannerContentBiz.bannerContentPage(bannerContentForm ,page);
    }

    /**
     * 查询banner内容
     * @param shopId
     * @param id
     * @return
     */
    @GET
    @Path("/{id}")
    public AppResult<BannerContent> getBannerContentById(@PathParam("shopId") Long shopId,
                                          @PathParam("id") Long id) {
        BannerContent bannerContent = new BannerContent();
        bannerContent.setShopId(shopId);
        bannerContent.setId(id);
        bannerContent = bannerContentBiz.selectByIdAndShopId(bannerContent);
        return createSucssAppResult("查询banner成功",bannerContent);

    }
}
