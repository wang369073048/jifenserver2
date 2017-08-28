package org.trc.resource.goods;

import com.alibaba.druid.support.json.JSONUtils;
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
import org.trc.domain.goods.CardCouponsDO;
import org.trc.domain.goods.ShopClassificationDO;
import org.trc.util.AppResult;
import org.trc.util.Pagenation;
import org.trc.util.TxJerseyTools;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
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
    public Response page(@BeanParam Pagenation<ShopClassificationDO> page,
                                                 @Context ContainerRequestContext requestContext) {
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        ShopClassificationDO param = new ShopClassificationDO();
        param.setShopId(auth.getShopId());
//        return shopClassificationBiz.queryEntity(param, page);
        Pagenation<ShopClassificationDO> pageShopClassifications = shopClassificationBiz.queryEntity(param, page);
        return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(pageShopClassifications));
    }

    @GET
    public Response list(@Context ContainerRequestContext requestContext) {
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        ShopClassificationDO param = new ShopClassificationDO();
        param.setShopId(auth.getShopId());
        
//        return createSucssAppResult("查询列表成功", shopClassificationBiz.listEntity(param));
        List<ShopClassificationDO> shopClassifications = shopClassificationBiz.listEntity(param);
        if(shopClassifications!=null){
        	return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(shopClassifications));
        }
        return TxJerseyTools.returnSuccess();
    }

    @POST
    public Response insert(@NotEmpty @FormParam("classificationName") String classificationName, @NotEmpty @FormParam("pictureUrl") String pictureUrl,
                            @FormParam("selectPicUrl") String selectPicUrl,
                           @NotNull @FormParam("sort") Integer sort, @FormParam("description") String description,
                           @Context ContainerRequestContext requestContext) {
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        ShopClassificationDO param = new ShopClassificationDO();
        param.setClassificationName(classificationName);
        param.setPictureUrl(pictureUrl);
        param.setSelectPicUrl(selectPicUrl);
        param.setSort(sort);
        param.setDescription(description);
        param.setShopId(auth.getShopId());
        param.setUuid(UUID.randomUUID().toString().toLowerCase().replaceAll("-", ""));
//        return createSucssAppResult("新增成功", shopClassificationBiz.insert(param));
        Integer id = shopClassificationBiz.insert(param);
        return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(id==null?0:id));
    }

    @PUT
    public Response update(@NotNull @FormParam("id") Long id, @NotEmpty @FormParam("classificationName") String classificationName,
                           @NotEmpty @FormParam("pictureUrl")String pictureUrl, @NotNull @FormParam("sort")Integer sort,
                           @FormParam("description")String description){
            ShopClassificationDO param = new ShopClassificationDO();
            param.setId(id);
            param.setClassificationName(classificationName);
            param.setPictureUrl(pictureUrl);
            param.setSort(sort);
            param.setDescription(description);
//            return createSucssAppResult("更新成功", shopClassificationBiz.update(param));
            Integer nid = shopClassificationBiz.update(param);
            return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(nid));
    }
    @GET
    @Path("/{id}")
    public Response getEntity(@NotNull @PathParam("id") Long id,@Context ContainerRequestContext requestContext){
            //获取userId
            String userId = (String) requestContext.getProperty("userId");
            Auth auth = authBiz.getAuthByUserId(userId);
            ShopClassificationDO param = new ShopClassificationDO();
            param.setId(id);
            param.setShopId(auth.getShopId());
            ShopClassificationDO result = shopClassificationBiz.getEntityByParam(param);
//            return createSucssAppResult("查询成功", result);
            if(result!=null){
            	return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(result));
            }
            return TxJerseyTools.returnSuccess();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@NotNull @PathParam("id") Long id,@Context ContainerRequestContext requestContext){
            //获取userId
            String userId = (String) requestContext.getProperty("userId");
            Auth auth = authBiz.getAuthByUserId(userId);
            ShopClassificationDO param = new ShopClassificationDO();
            param.setId(id);
            param.setShopId(auth.getShopId());
            Integer nid = shopClassificationBiz.delete(param);
            return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(nid));
    }
}
