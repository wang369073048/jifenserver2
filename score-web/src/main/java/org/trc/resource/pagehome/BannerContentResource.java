package org.trc.resource.pagehome;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
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
import org.trc.biz.pagehome.IBannerContentBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.pagehome.BannerContent;
import org.trc.form.pagehome.BannerContentForm;
import org.trc.interceptor.annotation.Authority;
import org.trc.util.Pagenation;
import org.trc.util.TxJerseyTools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by hzwzhen on 2017/6/12.
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path("{shopId}/" + ScoreAdminConstants.Route.BannerContent.ROOT)
public class BannerContentResource {

    @Autowired
    private IBannerContentBiz bannerContentBiz;

    /**
     * 新增banner内容
     * @param shopId 业务方Id
     * @param title 标题
     * @param imgUrl 图片地址
     * @param targetUrl  目标地址
     * @param description
     * @param requestContext
     * @return
     */
    @POST
    @Authority
    public Response createBannerContent(@PathParam("shopId") Long shopId,
                                        @NotBlank @FormParam("type") String type,
                                        @NotBlank @FormParam("title") String title,
                                        @NotBlank @FormParam("imgUrl") String imgUrl,
                                        @NotBlank @FormParam("targetUrl") String targetUrl,
                                        @NotBlank @FormParam("description") String description,
                                         @Context ContainerRequestContext requestContext) {
        BannerContent bannerContent = new BannerContent();
        bannerContent.setShopId(shopId);
        bannerContent.setType(type);
        bannerContent.setTitle(title);
        bannerContent.setImgUrl(imgUrl);
        bannerContent.setTargetUrl(targetUrl);
        bannerContent.setDescription(description);
        String userId= (String) requestContext.getProperty("userId");
        bannerContent.setOperatorUserId(userId);
        bannerContentBiz.saveBannerContent(bannerContent);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("bannerContentId", bannerContent.getId());
//        return createSucssAppResult("保存bannerContent成功", jsonObject);
        return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
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
    @Authority
    public Response modifyBannerContent(@PathParam("shopId") Long shopId,
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
//        return createSucssAppResult("更新bannerContent成功", "");
        return TxJerseyTools.returnSuccess();
    }
    /**
     * 删除banner内容
     */
    @DELETE
    @Path("{id}")
    @Authority
    public Response deleteBannerContent(@PathParam("shopId") Long shopId,
                                         @PathParam("id") Long id,
                                         @Context ContainerRequestContext requestContext) {
        BannerContent bannerContent = new BannerContent();
        bannerContent.setId(id);
        bannerContent.setShopId(shopId);
        bannerContent = bannerContentBiz.selectByIdAndShopId(bannerContent);
        String userId= (String) requestContext.getProperty("userId");
        bannerContent.setOperatorUserId(userId);
        bannerContentBiz.deleteBannerContent(bannerContent);
//        return createSucssAppResult("删除bannerContent成功", "");
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 查询Banner内容列表
     * @param bannerContentForm
     * @param page
     * @return
     */
    @GET
    @Authority
    public Response getBannerContentList(@PathParam("shopId") Long shopId,@BeanParam BannerContentForm bannerContentForm,
                                                          @BeanParam Pagenation<BannerContent> page,
                                                          @Context ContainerRequestContext requestContext) {
//        return bannerContentBiz.bannerContentPage(bannerContentForm ,page);
        Pagenation<BannerContent> pageBannerContents = bannerContentBiz.bannerContentPage(bannerContentForm ,page);
        return TxJerseyTools.returnSuccess(JSON.toJSONString(pageBannerContents));
    }

    /**
     * 查询banner内容
     * @param shopId
     * @param id
     * @return
     */
    @GET
    @Path("/{id}")
    @Authority
    public Response getBannerContentById(@PathParam("shopId") Long shopId,
                                          @PathParam("id") Long id,@Context ContainerRequestContext requestContext) {
        BannerContent bannerContent = new BannerContent();
        bannerContent.setShopId(shopId);
        bannerContent.setId(id);
        bannerContent = bannerContentBiz.selectByIdAndShopId(bannerContent);
//        return createSucssAppResult("查询banner成功",bannerContent);
        return TxJerseyTools.returnSuccess(JSON.toJSONString(bannerContent));

    }
}
