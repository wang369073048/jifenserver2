package org.trc.resource.admin;

import com.alibaba.fastjson.JSONObject;
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
import org.trc.util.AppResult;
import org.trc.util.Pagenation;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;


import static org.trc.util.ResultUtil.createSucssAppResult;

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
    public AppResult<JSONObject> createCategory(@NotBlank @FormParam("categoryName") String categoryName,
                                                @FormParam("isVirtual") Integer isVirtual,
                                                @NotBlank @FormParam("logoUrl") String logoUrl,
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
        return createSucssAppResult("保存类目成功", jsonObject);
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
    public AppResult modifyCategory(@PathParam("id") Long id,
                                   @NotBlank @FormParam("categoryName") String categoryName,
                                   @NotBlank @FormParam("logoUrl") String logoUrl,
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
        return createSucssAppResult("更新类目成功", "");

    }

    /**
     * 删除Category
     *
     * @param id ID
     * @return Response
     */
    @DELETE
    @Path("/{id}")
    public AppResult deleteCategory(@PathParam("id") Long id,
                                   @Context ContainerRequestContext requestContext) {
        //获取登录者userId
        String userId = (String) requestContext.getProperty("userId");
        CategoryDO categoryDO = new CategoryDO();
        categoryDO.setId(id);
        categoryDO.setOperatorUserId(userId);
        categoryBiz.deleteCategoryDO(categoryDO);
        return createSucssAppResult("删除类目成功", "");
    }

    /**
     * 根据id查询类目
     *
     * @return Response
     */
    @GET
    @Path("/{id}")
    public AppResult<JSONObject> getCategoryById(@PathParam("id") Long id) {
        CategoryDO categoryDO = categoryBiz.getCategoryDOById(id);
        JSONObject json = new JSONObject();
        json.put("id", categoryDO.getId());
        json.put("categoryName", categoryDO.getCategoryName());
        json.put("allowUpdates", categoryDO.isAllowUpdates());
        json.put("logoUrl", categoryDO.getLogoUrl());
        json.put("sort", categoryDO.getSort());
        json.put("description", categoryDO.getDescription());
        return createSucssAppResult("删除类目成功", json);
    }

    /**
     * 查询类目列表
     *
     * @param name 类目名称
     * @return Response
     */
    @GET
    public Pagenation<CategoryDO> getCategoryList(@QueryParam("name") String name,
                                    @BeanParam Pagenation<CategoryDO> page) {
        CategoryDO query = new CategoryDO();
        query.setCategoryName(name);
        page = categoryBiz.queryCategoryDOListForPage(query, page);
        return page;

    }
}
