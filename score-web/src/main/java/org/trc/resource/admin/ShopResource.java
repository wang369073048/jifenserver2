package org.trc.resource.admin;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.shop.ShopDO;
import org.trc.util.AppResult;
import org.trc.util.Pagenation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static org.trc.util.ResultUtil.createSucssAppResult;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:店铺管理
 * since Date： 2017/7/1
 */
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
    public AppResult<JSONObject> createShop(@NotBlank @FormParam("shopName") String shopName,
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
        return createSucssAppResult("创建店铺成功!", jsonObject);

    }

    /**
     * 修改Shop
     *
     * @return Response
     */
    @PUT
    @Path("/{id}")
    //@Admin
    public AppResult modifyShop(@PathParam("id") Long id,
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
        return createSucssAppResult("修改店铺成功!", "");

    }

    /**
     * 查询shop列表
     *
     * @return Response
     */
    @GET
    //@Admin
    public Pagenation<ShopDO> getShopList(@QueryParam("shopName") String shopName,
                                          @BeanParam Pagenation<ShopDO> page) {
        ShopDO query = new ShopDO();
        query.setShopName(shopName);
        return shopBiz.queryShopDOListForPage(query, page);
    }

    /**
     * 根据id查询shop
     * @return Response
     */
    @GET
    @Path("/{id}")
    //@Admin
    public AppResult<ShopDO> getShopById(@PathParam("id") Long id) {
        ShopDO shopDO = shopBiz.getShopDOById(id);
        return createSucssAppResult("查询店铺成功!", shopDO);
    }
}
