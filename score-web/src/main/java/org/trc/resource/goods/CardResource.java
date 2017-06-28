package org.trc.resource.goods;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.goods.ICouponsBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;
import org.trc.domain.goods.CardCouponsDO;

import org.trc.util.Pagenation;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Date;

/**
 * Created by hzwzhen on 2017/6/27.
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.CardCoupuns.ROOT)
public class CardResource {

    private Logger logger = LoggerFactory.getLogger(CardResource.class);
    @Autowired
    private ICouponsBiz couponsBiz;
    @Autowired
    private IAuthBiz authBiz;
    @GET
    public Pagenation<CardCouponsDO> list(@QueryParam("couponName") String couponName,
                                         @QueryParam("batchNumber") String batchNumber,
                                         @QueryParam("startTime") Long startTime,
                                         @QueryParam("endTime") Long endTime,
                                         @BeanParam Pagenation<CardCouponsDO> page,
                                         @Context ContainerRequestContext requestContext){
        //获取userId
        String userId= (String) requestContext.getProperty("userId");
        Auth auth = authBiz.getAuthByUserId(userId);
        CardCouponsDO cardCouponsQuery = new CardCouponsDO();
//        BaseQueryDTO baseQueryDTO = new BaseQueryDTO();
//        if(null != startTime) {
//            baseQueryDTO.setStartTime(new Date(startTime));
//        }
//        if(null != endTime) {
//            baseQueryDTO.setEndTime(new Date(endTime));
//        }
//        cardCouponsQuery.setBaseQueryDTO(baseQueryDTO);
        cardCouponsQuery.setCouponName(couponName);
        cardCouponsQuery.setBatchNumber(batchNumber);
        cardCouponsQuery.setShopId(cardCouponsQuery.getShopId());
        page = couponsBiz.queryCouponsForPage(cardCouponsQuery, page);
        return page;
    }
}
