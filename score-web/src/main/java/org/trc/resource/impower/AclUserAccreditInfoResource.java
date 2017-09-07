package org.trc.resource.impower;

import java.util.List;

import javax.annotation.Resource;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;
import org.trc.biz.impower.IAclUserAccreditInfoBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.goods.CardCouponsDO;
import org.trc.domain.impower.AclRole;
import org.trc.domain.impower.AclUserAccreditInfo;
import org.trc.domain.impower.AclUserAddPageDate;
import org.trc.form.impower.UserAccreditInfoForm;
import org.trc.util.AppResult;
import org.trc.util.AssertUtil;
import org.trc.util.Pagenation;
import org.trc.util.ResultUtil;
import org.trc.util.TxJerseyTools;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.tairanchina.md.account.user.service.UserService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
@Component
@Path(ScoreAdminConstants.Route.UserAccreditInfo.ROOT)
public class AclUserAccreditInfoResource {

    @Resource
    private IAclUserAccreditInfoBiz userAccreditInfoBiz;
    @Resource
    private UserService userService;

    //授权信息分页查询
    @GET
    @Path(ScoreAdminConstants.Route.UserAccreditInfo.ACCREDIT_PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response UserAccreditInfoPage(@BeanParam UserAccreditInfoForm form, @BeanParam Pagenation<AclUserAddPageDate> page){
//        return userAccreditInfoBiz.userAccreditInfoPage(form, page);
        Pagenation<AclUserAddPageDate> pageAclUserAddPageDates =userAccreditInfoBiz.userAccreditInfoPage(form, page);
        return TxJerseyTools.returnSuccess(JSON.toJSONString(pageAclUserAddPageDates));
    }

    //授权里面的采购员列表
    @GET
    @Path(ScoreAdminConstants.Route.UserAccreditInfo.PURCHASE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findPurchase(){
//        return ResultUtil.createSucssAppResult("查询采购员成功", userAccreditInfoBiz.findPurchase());
        List<AclUserAccreditInfo> aclUserAccreditInfos = userAccreditInfoBiz.findPurchase();
        if(aclUserAccreditInfos!=null){
        	return TxJerseyTools.returnSuccess(JSON.toJSONString(aclUserAccreditInfos));
        }
        return TxJerseyTools.returnSuccess();
    }

    @POST
    @Path(ScoreAdminConstants.Route.UserAccreditInfo.UPDATE_STATE + "/{id}")
    public Response updateUserAccreditInfoStatus(@BeanParam AclUserAccreditInfo aclUserAccreditInfo){
        userAccreditInfoBiz.updateUserAccreditInfoStatus(aclUserAccreditInfo);
//        return ResultUtil.createSucssAppResult("修改状态成功", "");
        return TxJerseyTools.returnSuccess();
    }

   /* *//**
     * 编辑时采购组校验
     *
     * @param id
     * @param name
     * @return
     *//*
    @GET
    @Path(SupplyConstants.AclUserAccreditInfo.CHECK + "/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResult findUserAccreditInfoByName(@QueryParam("id") Long id, @QueryParam("name") String name) throws Exception {
        userAccreditInfoBiz.checkUserByName(id, name);
        return null;
    }*/
  /*  *//**
     * 新增时用户名是否存在
     * @param name
     * @return
     *//*
    @GET
    @Path(SupplyConstants.AclUserAccreditInfo.CHECK)
    @Produces(MediaType.APPLICATION_JSON)
    public AppResult checkName(@QueryParam("name") String name) throws Exception {
        if (userAccreditInfoBiz.checkName( name) >0) {
            return ResultUtil.createSucssAppResult("查询成功", "查询授权用户已存在");
        } else {
            return ResultUtil.createSucssAppResult("查询成功", "查询授权用户可用");
        }
    }*/

//    /**
//     * 查询已启用的渠道
//     */
//    @GET
//    @Path(ScoreAdminConstants.Route.UserAccreditInfo.CHANNEL)
//    @Produces(MediaType.APPLICATION_JSON)
//    public AppResult findChannel(){
//        return ResultUtil.createSucssAppResult("查询已启用的渠道成功", userAccreditInfoBiz.findChannel());
//
//    }

    /**
     * 查询全局&渠道&混用角色
     */
    @GET
    @Path(ScoreAdminConstants.Route.UserAccreditInfo.ROLE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findChaAndWhole(@QueryParam("roleType") String roleType){
//        return ResultUtil.createSucssAppResult("查询对应角色成功", userAccreditInfoBiz.findChannelOrWholeJur(roleType));
        List<AclRole> aclRoles =userAccreditInfoBiz.findChannelOrWholeJur(roleType);
        if(aclRoles!=null){
        	return TxJerseyTools.returnSuccess(JSON.toJSONString(aclRoles));
        }
        return TxJerseyTools.returnSuccess();

    }

    /**
     * 新增授权
     */
    @POST
    @Path(ScoreAdminConstants.Route.UserAccreditInfo.SAVE_ACCREDIT)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveUserAccredit(@BeanParam AclUserAddPageDate userAddPageDate, @Context ContainerRequestContext requestContext){

        userAccreditInfoBiz.saveUserAccreditInfo(userAddPageDate, requestContext);
//        return ResultUtil.createSucssAppResult("新增授权成功", "");
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 根据ID查询用户
     */
    @GET
    @Path(ScoreAdminConstants.Route.UserAccreditInfo.ACCREDIT + "/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUserAccreditInfoById(@QueryParam("id") Long id){
//        return ResultUtil.createSucssAppResult("查询用户成功", userAccreditInfoBiz.findUserAccreditInfoById(id));
        AclUserAccreditInfo aclUserAccreditInfo =userAccreditInfoBiz.findUserAccreditInfoById(id);
        if(aclUserAccreditInfo!=null){
        	return TxJerseyTools.returnSuccess(JSON.toJSONString(aclUserAccreditInfo));
        }
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 修改用户
     *
     * @param userAddPageDate
     * @return
     * @throws Exception
     */
    @PUT
    @Path(ScoreAdminConstants.Route.UserAccreditInfo.UPDATE_ACCREDIT + "/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUserAccreditInfo(@BeanParam AclUserAddPageDate userAddPageDate, @Context ContainerRequestContext requestContext){
        userAccreditInfoBiz.updateUserAccredit(userAddPageDate,requestContext);
//        return ResultUtil.createSucssAppResult("修改用户成功", "");
        return TxJerseyTools.returnSuccess();
    }

    @GET
    @Path(ScoreAdminConstants.Route.UserAccreditInfo.CHECK_PHONE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkPhone(@QueryParam("phone") String phone){
        AssertUtil.notBlank(phone, "校验手机号时输入参数phone为空");
//        return ResultUtil.createSucssAppResult("查询成功", userAccreditInfoBiz.checkPhone(phone));
        String result =userAccreditInfoBiz.checkPhone(phone);
        if(result!=null){
        	return TxJerseyTools.returnSuccess(JSON.toJSONString(result));
        }
        return TxJerseyTools.returnSuccess();
    }

//    @GET
//    @Path(ScoreAdminConstants.Route.UserAccreditInfo.CHECK_PURCHASE + "/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public AppResult checkPurchase(@PathParam("id") Long id){
//        return ResultUtil.createSucssAppResult("查询成功", userAccreditInfoBiz.purchaseRole(id));
//    }

    /**
     * 查询用户关联的角色起停用状态
     */
    @GET
    @Path(ScoreAdminConstants.Route.UserAccreditInfo.ROLE_VALID + "/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkRoleValid(@PathParam("id") Long id){
//        return ResultUtil.createSucssAppResult("查询成功", userAccreditInfoBiz.checkRoleValid(id));
        String[] result =userAccreditInfoBiz.checkRoleValid(id);
        if(result!=null){
            return TxJerseyTools.returnSuccess(JSON.toJSONString(result));
        }
        return TxJerseyTools.returnSuccess();
    }
}
