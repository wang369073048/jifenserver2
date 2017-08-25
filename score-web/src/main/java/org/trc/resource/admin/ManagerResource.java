package org.trc.resource.admin;

import com.alibaba.fastjson.JSONObject;
import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.service.UserService;
import com.tairanchina.md.api.QueryType;
import org.apache.commons.lang.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;
import org.trc.domain.dto.AuthQueryDTO;
import org.trc.domain.shop.ShopDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.ManagerException;
import org.trc.interceptor.Admin;
import org.trc.util.AppResult;
import org.trc.util.Pagenation;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.trc.util.ResultUtil.createSucssAppResult;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:  店铺管理员管理
 * since Date： 2017/7/5
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.Manager.ROOT)
//@TxAop
public class ManagerResource {
    @Resource
    private UserService userService;
    @Autowired
    private IShopBiz shopBiz;
    @Autowired
    private IAuthBiz authBiz;

    /**
     * 添加店铺管理员
     *
     * @param shopId
     * @param phone
     * @param contactsUser
     * @return
     */
    @POST
    @Admin
    public AppResult<JSONObject> createManager(@NotNull @FormParam("shopId") Long shopId,
                                               @NotEmpty @FormParam("phone") String phone,
                                               @NotEmpty @FormParam("contactsUser") String contactsUser,
                                               @Context ContainerRequestContext requestContext) {
        UserDO userDO = userService.getUserDO(QueryType.Phone, phone);
        if (null == userDO) {
            throw new ManagerException(ExceptionEnum.MANAGER_SAVE_EXCEPTION, "手机号不存在");
        }
        Auth auth = new Auth();
        auth.setUserId(userDO.getUserId());
        auth.setPhone(phone);
        auth.setContactsUser(contactsUser);
        String channelCode = getChannelByShopId(shopId);
        if (StringUtils.isEmpty(channelCode)) {
            throw new ManagerException(ExceptionEnum.PARAM_ERROR_ILLEGAL, "参数shopId不合法");
        }
        auth.setChannelCode(channelCode);
        auth.setShopId(shopId);
        authBiz.saveAuth(auth);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("managerId", auth.getId());
        return createSucssAppResult("添加店铺管理员成功!" ,jsonObject);
    }
    private String getChannelByShopId(Long shopId) {
        ShopDO shopDO = shopBiz.getShopDOById(shopId);
        if (shopDO != null) {
            return shopDO.getChannelCode();
        }
        return null;
    }

    /**
     * 更新店铺管理员
     *
     * @param id
     * @param shopId
     * @param phone
     * @param contactsUser
     * @return
     */
    @PUT
    @Path("/{id}")
    @Admin
    public AppResult modifyManager(@PathParam("id") Long id,
                                  @NotNull @FormParam("shopId") Long shopId,
                                  @NotEmpty @FormParam("phone") String phone,
                                   @Context ContainerRequestContext requestContext,
                                  @NotEmpty @FormParam("contactsUser") String contactsUser) {
            UserDO userDO = userService.getUserDO(QueryType.Phone, phone);
            if (null == userDO) {
                throw new ManagerException(ExceptionEnum.MANAGER_SAVE_EXCEPTION, "手机号不存在");
            }
            Auth auth = new Auth();
            auth.setId(id);
            auth.setUserId(userDO.getUserId());
            auth.setPhone(phone);
            auth.setContactsUser(contactsUser);
            String channelCode = getChannelByShopId(shopId);
            if (StringUtils.isEmpty(channelCode)) {
                throw new ManagerException(ExceptionEnum.PARAM_ERROR_ILLEGAL, "参数shopId不合法");
            }
            auth.setChannelCode(channelCode);
            auth.setShopId(shopId);
            authBiz.updateAuth(auth);
            return createSucssAppResult("更新店铺管理员成功!" ,"");
    }

    /**
     * 查询manager列表
     *
     * @return Response
     */
    @GET
    @Admin
    public Pagenation<Auth> getManagerList(@QueryParam("shopId") Long shopId,
                                   @QueryParam("userKeyWord") String userKeyWord,
                                   @Context ContainerRequestContext requestContext,
                                   @BeanParam Pagenation<Auth> page) {
            AuthQueryDTO query = new AuthQueryDTO();
            query.setUserKeyword(userKeyWord);
            query.setShopId(shopId);
            return authBiz.queryAuthListForPage(query, page);
    }

    /**
     * 根据id查询manager
     *
     * @return Response
     */
    @GET
    @Path("/{id}")
    @Admin
    public AppResult getManagerById(@PathParam("id") Long id,
                                    @Context ContainerRequestContext requestContext) {
        Auth auth = authBiz.getAuthById(id);
        return createSucssAppResult("查询店铺管理员成功!" ,auth);
    }
}
