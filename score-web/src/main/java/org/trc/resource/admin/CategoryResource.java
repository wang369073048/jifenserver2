package org.trc.resource.admin;

import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
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

import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.goods.ICategoryBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.goods.CategoryDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BannerException;
import org.trc.interceptor.annotation.Admin;
import org.trc.util.CommonConstants;
import org.trc.util.Pagenation;
import org.trc.util.TxJerseyTools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import static org.trc.util.CommonConstants.ErrorCode.ERROR_NO_DATA;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments: 类目管理
 * since Date： 2017/6/27
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.Category.ROOT)
public class CategoryResource {

    Logger logger = LoggerFactory.getLogger(CategoryResource.class);

    @Autowired
    private ICategoryBiz categoryBiz;

    /**
     * 创建Category
     *
     * @param categoryName 类目名称
     * @param description  描述
     * @param logoUrl      LOGO地址
     * @param sort         排序
     * @param pid          父id
     * @return Response
     */
    @POST
    @Admin
    public Response createCategory(@NotBlank @FormParam("categoryName") String categoryName,
                                                @FormParam("isVirtual") Integer isVirtual,
                                                @FormParam("logoUrl") String logoUrl,
                                                @NotNull @FormParam("sort") Integer sort,
                                                @FormParam("description") String description,
                                                @FormParam("pid") Long pid,
                                                @Context ContainerRequestContext requestContext) {
        //校验排序数
        if (sort <= 0) {
            logger.error("新增类目异常!");
            throw new BannerException(ExceptionEnum.PARAM_ERROR_ILLEGAL, "排序数必须大于0");
        }
        //获取登录者userId
        String userId = (String) requestContext.getProperty("userId");
        //构建categoryDO
        CategoryDO categoryDO = new CategoryDO();
        categoryDO.setCategoryName(categoryName);
        categoryDO.setIsVirtual(null == isVirtual ? 0 : isVirtual);
        categoryDO.setAllowUpdates(true);
        categoryDO.setDescription(description);
        categoryDO.setLogoUrl(logoUrl);
        categoryDO.setSort(sort);
        categoryDO.setPid(pid);
        categoryDO.setOperatorUserId(userId);
        categoryDO.setIsDeleted(false);
        //新增
        categoryBiz.addCategoryDO(categoryDO);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("categoryId", categoryDO.getId());
        return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
    }

    /**
     * 修改Category
     *
     * @param id           ID
     * @param categoryName 类目名称
     * @param description  描述
     * @param logoUrl      LOGO地址
     * @param sort         排序
     * @return Response
     */
    @PUT
    @Path("/{id}")
    @Admin
    public Response modifyCategory(@PathParam("id") Long id,
                                   @NotBlank @FormParam("categoryName") String categoryName,
                                   @FormParam("logoUrl") String logoUrl,
                                   @NotNull @FormParam("sort") Integer sort,
                                   @FormParam("description") String description,
                                   @FormParam("pid") Long pid,
                                   @Context ContainerRequestContext requestContext) {
        //校验排序数
        if (sort <= 0) {
            throw new BannerException(ExceptionEnum.PARAM_ERROR_ILLEGAL, "排序数必须大于0");
        }
        //获取登录者userId
        String userId = (String) requestContext.getProperty("userId");
        CategoryDO categoryDO = categoryBiz.getCategoryDOById(id);
        if(categoryDO == null) {
            throw new BannerException(ExceptionEnum.BANNER_QUERY_EXCEPTION, "该类目不存在! Id ==> " + id);
        }
        categoryDO.setCategoryName(categoryName);
        categoryDO.setLogoUrl(logoUrl);
        categoryDO.setSort(sort);
        categoryDO.setDescription(description);
        categoryDO.setPid(pid);
        categoryDO.setOperatorUserId(userId);
        categoryBiz.modifyCategoryDO(categoryDO);
        return TxJerseyTools.returnSuccess("更新类目成功!");
    }

    /**
     * 删除Category
     *
     * @param id ID
     * @return Response
     */
    @DELETE
    @Path("/{id}")
    @Admin
    public Response deleteCategory(@PathParam("id") Long id,
                                   @Context ContainerRequestContext requestContext) {
        //获取登录者userId
        String userId = (String) requestContext.getProperty("userId");
        CategoryDO categoryDO = new CategoryDO();
        categoryDO.setId(id);
        categoryDO.setOperatorUserId(userId);
        categoryBiz.deleteCategoryDO(categoryDO);
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 根据id查询类目
     *
     * @return Response
     */
    @GET
    @Path("/{id}")
    @Admin
    public Response getCategoryById(@PathParam("id") Long id,
                                                 @Context ContainerRequestContext requestContext) {
        CategoryDO categoryDO = categoryBiz.getCategoryDOById(id);
        JSONObject json = new JSONObject();
        json.put("id", categoryDO.getId());
        json.put("categoryName", categoryDO.getCategoryName());
        json.put("allowUpdates", categoryDO.isAllowUpdates());
        json.put("logoUrl", categoryDO.getLogoUrl());
        json.put("sort", categoryDO.getSort());
        json.put("description", categoryDO.getDescription());
        return TxJerseyTools.returnSuccess(json.toJSONString());
    }

    /**
     * 查询类目列表
     *
     * @param name 类目名称
     * @return Response
     */
    @GET
    @Admin
    public Response getCategoryList(@QueryParam("categoryName") String categoryName,
                                    @BeanParam Pagenation<CategoryDO> page,@Context ContainerRequestContext requestContext) {
        CategoryDO query = new CategoryDO();
        query.setCategoryName(categoryName);
        page = categoryBiz.queryCategoryDOListForPage(query, page);
        if(page!=null){
        	return TxJerseyTools.returnSuccess(JSON.toJSONString(page));
        }else{
        	return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_NO_DATA);
        }
    }
}
