package org.trc.resource.impower;

import java.util.List;
import java.util.Map;

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
import org.trc.biz.impower.IAclResourceBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.impower.AclResource;
import org.trc.form.impower.JurisdictionTreeNode;
import org.trc.util.AppResult;
import org.trc.util.ResultUtil;
import org.trc.util.TxJerseyTools;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;

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
     *查询所有权限资源
     */
    @GET
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_WHOLE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findWholeJurisdiction(){
//        return ResultUtil.createSucssAppResult("查询全局角色成功", jurisdictionBiz.findWholeJurisdiction());
    	List<AclResource> aclResources = jurisdictionBiz.findWholeJurisdiction();
    	if(aclResources!=null){
        	return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(aclResources));
        }
        return TxJerseyTools.returnSuccess();
    }

//    @GET
//    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_CHANNEL)
//    @Produces(MediaType.APPLICATION_JSON)
//    public AppResult<AclResource> findChannelJurisdiction(){
//
//        return ResultUtil.createSucssAppResult("查询渠道角色成功", jurisdictionBiz.findChannelJurisdiction());
//
//    }

    /**
     * 提供对应的角色权限，用于回写被选中的权限
     * 提供带有角色id的，角色与权限的关联信息查询
     */
    @GET
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_WHOLE + "/{roleId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findWholeJurisdictionAndCheckedByRoleId(@PathParam("roleId") Long roleId){
//        return ResultUtil.createSucssAppResult("查询全局角色成功", jurisdictionBiz.findWholeJurisdictionAndCheckedByRoleId(roleId));
        List<AclResource> aclResources = jurisdictionBiz.findWholeJurisdictionAndCheckedByRoleId(roleId);
    	if(aclResources!=null){
        	return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(aclResources));
        }
        return TxJerseyTools.returnSuccess();

    }

//    @GET
//    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_CHANNEL + "/{roleId}")
//    @Produces(MediaType.APPLICATION_JSON)
//    public AppResult<AclResource> findChannelJurisdictionAndCheckedByRoleId(@PathParam("roleId") Long roleId){
//
//        return ResultUtil.createSucssAppResult("查询全局角色成功", jurisdictionBiz.findChannelJurisdictionAndCheckedByRoleId(roleId));
//
//    }

    @GET
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_TREE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response jurisdictionTree(@QueryParam("parentId") Long parentId, @QueryParam("isRecursive") boolean isRecursive){
//        return ResultUtil.createSucssAppResult("查询权限资源成功", jurisdictionBiz.getNodes(parentId, isRecursive));
    	List<JurisdictionTreeNode> jurisdictionTreeNodes = jurisdictionBiz.getNodes(parentId, isRecursive);
    	if(jurisdictionTreeNodes!=null){
        	return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(jurisdictionTreeNodes));
        }
        return TxJerseyTools.returnSuccess();
    }

    @POST
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_SAVE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveJurisdiction(@BeanParam JurisdictionTreeNode jurisdictionTreeNode, @Context ContainerRequestContext requestContext) {
        jurisdictionBiz.saveJurisdiction(jurisdictionTreeNode,requestContext);
//        return ResultUtil.createSucssAppResult("新增权限资源成功", "");
        return TxJerseyTools.returnSuccess();
    }

    @PUT
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_EDIT+"/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateJurisdiction(@BeanParam JurisdictionTreeNode jurisdictionTreeNode){
        jurisdictionBiz.updateJurisdiction(jurisdictionTreeNode);
//        return ResultUtil.createSucssAppResult("更新权限资源成功", "");
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 查询用户html页面权限
     * @param requestContext
     * @return
     */
    @GET
    @Path(ScoreAdminConstants.Route.Jurisdiction.JURISDICTION_HTML)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateJurisdiction(@Context ContainerRequestContext requestContext){
        String userId= (String) requestContext.getProperty(ScoreAdminConstants.Route.Authorization.USER_ID);
//        return ResultUtil.createSucssAppResult("查询用户html页面权限成功", jurisdictionBiz.getHtmlJurisdiction(userId));
        List<Map<String,Object>>  htmlJurisdictions= jurisdictionBiz.getHtmlJurisdiction(userId);
    	if(htmlJurisdictions!=null){
        	return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(htmlJurisdictions));
        }
        return TxJerseyTools.returnSuccess();
    }
}
