package org.trc.resource.impower;

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
import org.trc.biz.impower.IAclRoleBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.impower.AclRole;
import org.trc.domain.impower.AclRoleAddPageData;
import org.trc.domain.order.OrdersDO;
import org.trc.form.impower.RoleForm;
import org.trc.util.AppResult;
import org.trc.util.Pagenation;
import org.trc.util.ResultUtil;
import org.trc.util.TxJerseyTools;

import com.alibaba.druid.support.json.JSONUtils;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
@Component
@Path(ScoreAdminConstants.Route.Role.ROOT)
public class AclRoleResource {

    @Resource
    private IAclRoleBiz roleBiz;

    //角色分页查询
    @GET
    @Path(ScoreAdminConstants.Route.Role.ROLE_PAGE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response rolePage(@BeanParam RoleForm form, @BeanParam Pagenation<AclRole> page){
//        return roleBiz.rolePage(form,page);
        
        Pagenation<AclRole> pageAclRole = roleBiz.rolePage(form,page);
        return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(pageAclRole));
    }
    //修改角色信息以及与之对应的角色权限关联表信息的修改
    @PUT
    @Path(ScoreAdminConstants.Route.Role.ROLE+"/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRole(@BeanParam AclRoleAddPageData roleAddPageData){

        AclRole aclRole = roleAddPageData;
        roleBiz.updateRole(aclRole, roleAddPageData.getRoleJurisdiction());
//        return  ResultUtil.createSucssAppResult("修改角色信息成功","");
        return TxJerseyTools.returnSuccess();

    }
    //保存角色信息以及与之对应的角色权限关联表信息的保存
    @POST
    @Path(ScoreAdminConstants.Route.Role.ROLE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveRole(@BeanParam AclRoleAddPageData roleAddPageData, @Context ContainerRequestContext requestContext){

        AclRole aclRole = roleAddPageData;
        roleBiz.saveRole(aclRole, roleAddPageData.getRoleJurisdiction(),requestContext);
//        return  ResultUtil.createSucssAppResult("保存成功","");
        return TxJerseyTools.returnSuccess();

    }
    //根据角色名查询角色
    @GET
    @Path(ScoreAdminConstants.Route.Role.ROLE)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRoleByName(@QueryParam("name") String name ){
        //  前台接受为null则数据没问题 ，有数据则名称不能使用，"1" 为标志存在数据
//        return  ResultUtil.createSucssAppResult("查询角色成功", roleBiz.findRoleByName(name)==null ? null :"1");
        AclRole aclRole = roleBiz.findRoleByName(name);
        if(aclRole!=null){
        	return TxJerseyTools.returnSuccess(JSONUtils.toJSONString("1"));
        }
    	return TxJerseyTools.returnSuccess();
    }
    //根据角色的id 查询使用该角色的用户数量，以及启用状态
    @GET
    @Path(ScoreAdminConstants.Route.Role.ROLE_ACCREDITINFO)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findNumFromRoleAndAccreditInfoByRoleId(@QueryParam("roleId") Long roleId){
//        return  ResultUtil.createSucssAppResult("查询角色数量成功",roleBiz.findNumFromRoleAndAccreditInfoByRoleId(roleId));
        Integer num = roleBiz.findNumFromRoleAndAccreditInfoByRoleId(roleId);
        return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(num));
    }
    //修改角色的状态
    @POST
    @Path(ScoreAdminConstants.Route.Role.UPDATE_STATE+"/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoleState(@BeanParam AclRole aclRole){
        roleBiz.updateRoleState(aclRole);
//        return ResultUtil.createSucssAppResult("修改角色状态成功","");
        return TxJerseyTools.returnSuccess();
    }

    @GET
    @Path(ScoreAdminConstants.Route.Role.ROLE+"/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRoleById(@PathParam("id") Long id){
//        return ResultUtil.createSucssAppResult("查询角色成功",roleBiz.findRoleById(id));
        AclRole aclRole = roleBiz.findRoleById(id);
        if(aclRole!=null){
        	return TxJerseyTools.returnSuccess(JSONUtils.toJSONString(aclRole));
        }
        return TxJerseyTools.returnSuccess();
    }

}
