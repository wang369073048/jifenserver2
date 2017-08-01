package org.trc.resource.goods;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.goods.IShopClassificationBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;
import org.trc.domain.goods.ShopClassificationDO;
import org.trc.util.AppResult;
import org.trc.util.Pagenation;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.trc.util.ResultUtil.createSucssAppResult;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/28
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.ShopClassification.ROOT)
public class ShopClassificationResource {

    private Logger logger = LoggerFactory.getLogger(ShopClassificationResource.class);

    @Autowired
    private IAuthBiz authBiz;

    @Autowired
    private IShopClassificationBiz shopClassificationBiz;

    @GET
    @Path(ScoreAdminConstants.Route.ShopClassification.PAGE)
    public Pagenation<ShopClassificationDO> page(@BeanParam Pagenation<ShopClassificationDO> page,
                                                 @Context ContainerRequestContext requestContext) {
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        ShopClassificationDO param = new ShopClassificationDO();
        param.setShopId(auth.getShopId());
        return shopClassificationBiz.queryEntity(param, page);
    }

    @GET
    public AppResult list(@Context ContainerRequestContext requestContext) {
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        ShopClassificationDO param = new ShopClassificationDO();
        param.setShopId(auth.getShopId());
        return createSucssAppResult("查询列表成功", shopClassificationBiz.listEntity(param));
    }

    @POST
    public AppResult insert(@NotEmpty @FormParam("classificationName") String classificationName, @NotEmpty @FormParam("pictureUrl") String pictureUrl,
                           @NotNull @FormParam("sort") Integer sort, @FormParam("description") String description,
                           @Context ContainerRequestContext requestContext) {
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        ShopClassificationDO param = new ShopClassificationDO();
        param.setClassificationName(classificationName);
        param.setPictureUrl(pictureUrl);
        param.setSort(sort);
        param.setDescription(description);
        param.setShopId(auth.getShopId());
        param.setUuid(UUID.randomUUID().toString().toLowerCase().replaceAll("-", ""));
        return createSucssAppResult("新增成功", shopClassificationBiz.insert(param));
    }

    @PUT
    public AppResult update(@NotNull @FormParam("id") Long id, @NotEmpty @FormParam("classificationName") String classificationName,
                           @NotEmpty @FormParam("pictureUrl")String pictureUrl, @NotNull @FormParam("sort")Integer sort,
                           @FormParam("description")String description){
            ShopClassificationDO param = new ShopClassificationDO();
            param.setId(id);
            param.setClassificationName(classificationName);
            param.setPictureUrl(pictureUrl);
            param.setSort(sort);
            param.setDescription(description);
            return createSucssAppResult("更新成功", shopClassificationBiz.update(param));
    }
    @GET
    @Path("/{id}")
    public AppResult getEntity(@NotNull @PathParam("id") Long id,@Context ContainerRequestContext requestContext){
            //获取userId
            String userId = (String) requestContext.getProperty("userId");
            Auth auth = authBiz.getAuthByUserId(userId);
            ShopClassificationDO param = new ShopClassificationDO();
            param.setId(id);
            param.setShopId(auth.getShopId());
            ShopClassificationDO result = shopClassificationBiz.getEntityByParam(param);
            return createSucssAppResult("查询成功", result);
    }

    @DELETE
    @Path("/{id}")
    public AppResult delete(@NotNull @PathParam("id") Long id,@Context ContainerRequestContext requestContext){
            //获取userId
            String userId = (String) requestContext.getProperty("userId");
            Auth auth = authBiz.getAuthByUserId(userId);
            ShopClassificationDO param = new ShopClassificationDO();
            param.setId(id);
            param.setShopId(auth.getShopId());
            return createSucssAppResult("删除成功", shopClassificationBiz.delete(param));
    }
}
