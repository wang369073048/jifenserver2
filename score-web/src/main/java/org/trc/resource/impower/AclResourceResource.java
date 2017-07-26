package org.trc.resource.impower;

import com.alibaba.fastjson.JSONArray;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.stereotype.Component;
import org.trc.biz.impower.IAclResourceBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.impower.AclResource;
import org.trc.form.impower.JurisdictionTreeNode;
import org.trc.util.AppResult;
import org.trc.util.ResultUtil;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
@Component
@Path(ScoreAdminConstants.Route.Jurisdiction.ROOT)
public class AclResourceResource {
    @Resource
    private IAclResourceBiz jurisdictionBiz;

    /**
     * 提供两种角色下对应的角色权限
     * 1.提供全局角色对应的权限资源
     * 2.提供渠道角色对应的权限资源
     */
    @GET
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_WHOLE)
    @Produces(MediaType.APPLICATION_JSON)
    public AppResult<AclResource> findWholeJurisdiction(){

        return ResultUtil.createSucssAppResult("查询全局角色成功", jurisdictionBiz.findWholeJurisdiction());

    }

    @GET
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_CHANNEL)
    @Produces(MediaType.APPLICATION_JSON)
    public AppResult<AclResource> findChannelJurisdiction(){

        return ResultUtil.createSucssAppResult("查询渠道角色成功", jurisdictionBiz.findChannelJurisdiction());

    }

    /**
     * 提供两种角色下对应的角色权限，用于回写被选中的权限
     * 1.提供带有角色id的，角色与权限的关联信息查询<全局角色>
     * 2.提供带有角色id的，角色与权限的关联信息查询<渠道角色>
     */
    @GET
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_WHOLE + "/{roleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResult<AclResource> findWholeJurisdictionAndCheckedByRoleId(@PathParam("roleId") Long roleId){

        return ResultUtil.createSucssAppResult("查询全局角色成功", jurisdictionBiz.findWholeJurisdictionAndCheckedByRoleId(roleId));

    }

    @GET
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_CHANNEL + "/{roleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResult<AclResource> findChannelJurisdictionAndCheckedByRoleId(@PathParam("roleId") Long roleId){

        return ResultUtil.createSucssAppResult("查询全局角色成功", jurisdictionBiz.findChannelJurisdictionAndCheckedByRoleId(roleId));

    }

    @GET
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_TREE)
    @Produces(MediaType.APPLICATION_JSON)
    public AppResult<JSONArray> jurisdictionTree(@NotBlank@QueryParam("parentId") Long parentId, @QueryParam("isRecursive") boolean isRecursive){
        return ResultUtil.createSucssAppResult("查询权限资源成功", jurisdictionBiz.getNodes(parentId, isRecursive));
    }

    @POST
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_SAVE)
    @Produces(MediaType.APPLICATION_JSON)
    public AppResult<JSONArray> saveJurisdiction(@BeanParam JurisdictionTreeNode jurisdictionTreeNode, @Context ContainerRequestContext requestContext) {
        jurisdictionBiz.saveJurisdiction(jurisdictionTreeNode,requestContext);
        return ResultUtil.createSucssAppResult("新增权限资源成功", "");
    }

    @PUT
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_EDIT+"/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AppResult<JSONArray> updateJurisdiction(@BeanParam JurisdictionTreeNode jurisdictionTreeNode){
        jurisdictionBiz.updateJurisdiction(jurisdictionTreeNode);
        return ResultUtil.createSucssAppResult("更新权限资源成功", "");
    }

    /**
     * 查询用户html页面权限
     * @param requestContext
     * @return
     */
    @GET
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_HTML)
    @Produces(MediaType.APPLICATION_JSON)
    public AppResult<JSONArray> updateJurisdiction(@Context ContainerRequestContext requestContext){
        String userId= (String) requestContext.getProperty(ScoreAdminConstants.Route.Authorization.USER_ID);
        return ResultUtil.createSucssAppResult("查询用户html页面权限成功", jurisdictionBiz.getHtmlJurisdiction(userId));
    }
}
