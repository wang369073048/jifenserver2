package org.trc.resource.admin;

import static org.trc.util.ResultUtil.createSucssAppResult;

import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;
import org.trc.domain.shop.ShopDO;
import org.trc.util.AppResult;
import org.trc.util.Pagenation;
import org.trc.util.TxJerseyTools;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:店铺管理
 * since Date： 2017/7/1
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.Shop.ROOT)
//@TxAop
public class ShopResource {
    @Autowired
    private IShopBiz shopBiz;

    /**
     * 创建店铺
     *
     * @param shopName
     * @param logo
     * @param channelCode
     * @param servicePhone
     * @param description
     * @return
     */
    @POST
    public Response createShop(@NotBlank @FormParam("shopName") String shopName,
                                            @NotBlank @FormParam("channelCode") String channelCode,
                                            @FormParam("logo") String logo,
                                            @FormParam("servicePhone") String servicePhone,
                                            @FormParam("description") String description) {
        ShopDO shopDO = new ShopDO();
        shopDO.setShopName(shopName);
        shopDO.setChannelCode(channelCode);
        shopDO.setLogo(logo);
        shopDO.setServicePhone(servicePhone);
        shopDO.setDescription(description);
        shopBiz.addShopDO(shopDO);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("shopId", shopDO.getId());
        return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
    }

    /**
     * 修改Shop
     *
     * @return Response
     */
    @PUT
    @Path("/{id}")
    //@Admin
    public Response modifyShop(@PathParam("id") Long id,
                                @NotEmpty @FormParam("shopName") String shopName,
                                @NotEmpty @FormParam("channelCode") String channelCode,
                                @FormParam("logo") String logo,
                                @FormParam("servicePhone") String servicePhone,
                                @FormParam("description") String description) {
        ShopDO shopDO = new ShopDO();
        shopDO.setId(id);
        shopDO.setShopName(shopName);
        shopDO.setChannelCode(channelCode);
        shopDO.setLogo(logo);
        shopDO.setServicePhone(servicePhone);
        shopDO.setDescription(description);
        shopBiz.modifyShopDO(shopDO);
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 查询shop列表
     *
     * @return Response
     */
    @GET
    //@Admin
    public Response getShopList(@QueryParam("shopName") String shopName,
                                          @BeanParam Pagenation<ShopDO> page) {
        ShopDO query = new ShopDO();
        query.setShopName(shopName);
        Pagenation<ShopDO> pageShops = shopBiz.queryShopDOListForPage(query, page);
        return TxJerseyTools.returnSuccess(JSON.toJSONString(pageShops));
    }

    /**
     * 根据id查询shop
     * @return Response
     */
    @GET
    @Path("/{id}")
    //@Admin
    public Response getShopById(@PathParam("id") Long id) {
        ShopDO shopDO = shopBiz.getShopDOById(id);
        if(shopDO!=null){
        	return TxJerseyTools.returnSuccess(JSON.toJSONString(shopDO));
        }
        return TxJerseyTools.returnSuccess();
    }
}
