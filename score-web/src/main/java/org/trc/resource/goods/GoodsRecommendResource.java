package org.trc.resource.goods;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.goods.IGoodsBiz;
import org.trc.biz.goods.IGoodsRecommendBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;
import org.trc.domain.dto.GoodsRecommendDTO;
import org.trc.domain.goods.GoodsDO;
import org.trc.domain.goods.GoodsRecommendDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.GoodsRecommendException;
import org.trc.interceptor.Authority;
import org.trc.util.Pagenation;
import org.trc.util.TxJerseyTools;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/1
 */
@Component
@Path("{shopId}/" + ScoreAdminConstants.Route.Recommand.ROOT)
public class GoodsRecommendResource {

    private Logger logger = LoggerFactory.getLogger(GoodsRecommendResource.class);

    @Autowired
    private IGoodsRecommendBiz goodsRecommendBiz;
    @Autowired
    private IAuthBiz authBiz;
    @Autowired
    private IGoodsBiz goodsBiz;
    /**
     * 获取当前商铺的推荐商品列表
     *
     * @param shopId 商铺id
     * @return 查询到的推荐商品列表
     */
    @GET
    @Authority
    public Response getGoodsRecommands(@PathParam("shopId") Long shopId,
                                                            @BeanParam Pagenation<GoodsRecommendDTO> page,
                                                            @Context ContainerRequestContext requestContext) {

        GoodsRecommendDTO query = new GoodsRecommendDTO();
        query.setShopId(shopId);
        //执行查询
        Pagenation<GoodsRecommendDTO> pageGoodRecommends = goodsRecommendBiz.queryGoodsRecommondsForPage(query, page);
        return TxJerseyTools.returnSuccess(JSON.toJSONString(pageGoodRecommends));
    }

    /**
     * 添加推荐商品
     * @param shopId
     * @param goodsIds
     * @return
     */
    @POST
    @Authority
    public Response addGoodsRecommends(@PathParam("shopId") Long shopId,
                                        @NotEmpty @FormParam(value = "goodsIds") String goodsIds,
                                        @Context ContainerRequestContext requestContext) {
        String userId= (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        Date time = Calendar.getInstance().getTime();
        List<GoodsRecommendDO> goodsRecommendDOList = new ArrayList<GoodsRecommendDO>();
        String[] goodsIdArray = goodsIds.split(",");

        //判断商品所属权限
        boolean owner = goodsBiz.isOwnerOf(goodsIdArray, shopId);
        if (!owner) {
            throw new GoodsRecommendException(ExceptionEnum.ERROR_ILLEGAL_OPERATION,"操作不合法");
        }

        for (String goodsId : goodsIdArray) {
            GoodsRecommendDO goodsRecommendDO = new GoodsRecommendDO();
            goodsRecommendDO.setGoodsId(Long.valueOf(goodsId));
            goodsRecommendDO.setShopId(auth.getShopId());
            goodsRecommendDO.setOperatorUserId(userId);
            goodsRecommendDO.setIsDeleted(false);
            goodsRecommendDO.setCreateTime(time);
            goodsRecommendDO.setUpdateTime(time);
            goodsRecommendDOList.add(goodsRecommendDO);
        }
        goodsRecommendBiz.batchAddRecommends(goodsRecommendDOList);
        return TxJerseyTools.returnSuccess();
    }

    /**
     * 获取商品列表，以供选择（已被推荐的商品不需展示）
     *
     * @param shopId
     * @param goodsName
     * @return
     */
    @GET
    @Authority
    @Path(ScoreAdminConstants.Route.Recommand.GOODS)
    public Response getGoodsListExceptRecommend(@PathParam("shopId") Long shopId,
                                                @QueryParam("goodsName") String goodsName,
                                                @BeanParam Pagenation<GoodsDO> page, @Context ContainerRequestContext requestContext) {
            long time1 = System.currentTimeMillis();
            GoodsDO query = new GoodsDO();
            query.setShopId(shopId);
            query.setGoodsName(goodsName);
            long time2 = System.currentTimeMillis();
            System.out.println(time2 - time1);
            Pagenation<GoodsDO> pageGoods = goodsBiz.queryGoodsDOListExceptRecommendForPage(query, page);
            return TxJerseyTools.returnSuccess(JSON.toJSONString(pageGoods));

    }

    /**
     * 上移，下移推荐商品（采用交换方式）
     *
     * @param recommendAId 要交换的商品A的id
     * @param recommendBId 要交换的商品B的id
     * @return 交换结果
     */
    @PUT
    @Authority
    public Response moveRecommend(@PathParam("shopId") Long shopId,
                                  @FormParam("recommendAId") Long recommendAId,
                                  @FormParam("recommendBId") Long recommendBId,
                                  @Context ContainerRequestContext requestContext) {
            String userId= (String) requestContext.getProperty("userId");
            //执行移动操作
            GoodsRecommendDO recommendA = goodsRecommendBiz.getGoodsRecommendDOById(recommendAId);
            if (null == recommendA || recommendA.getShopId().longValue() != shopId.longValue()) {
                throw new GoodsRecommendException(ExceptionEnum.ERROR_ILLEGAL_OPERATION,"操作不合法");
            }
            GoodsRecommendDO recommendB = goodsRecommendBiz.getGoodsRecommendDOById(recommendBId);
            if (null == recommendB || recommendB.getShopId().longValue() != shopId.longValue()) {
                throw new GoodsRecommendException(ExceptionEnum.ERROR_ILLEGAL_OPERATION,"操作不合法");
            }

            goodsRecommendBiz.upOrDown(recommendAId, recommendBId, shopId, userId);
            return TxJerseyTools.returnSuccess();
    }

    /**
     * 删除推荐商品
     *
     * @param shopId 店铺id
     * @param id     要删除的推荐id
     * @return 删除结果
     */
    @DELETE
    @Path("/{id}")
    @Authority
    public Response deleteRecommend(@PathParam("shopId") Long shopId,
                                    @PathParam("id") Long id,
                                     @Context ContainerRequestContext requestContext) {
        GoodsRecommendDO goodsRecommendDD = goodsRecommendBiz.getGoodsRecommendDOById(id);
        if (null == goodsRecommendDD || goodsRecommendDD.getShopId().longValue() != shopId.longValue()) {
            throw new GoodsRecommendException(ExceptionEnum.ERROR_ILLEGAL_OPERATION, "操作不合法");
        }
        //执行删除操作操作
        goodsRecommendBiz.deleteById(id);
        return TxJerseyTools.returnSuccess();
    }

}
