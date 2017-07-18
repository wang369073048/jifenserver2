package org.trc.resource.admin;

import com.alibaba.fastjson.JSONObject;
import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.service.UserService;
import com.tairanchina.md.api.QueryType;
import com.trc.mall.externalservice.LogisticAck;
import com.trc.mall.externalservice.TrcExpressAck;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.trc.biz.order.INewOrderBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.dto.OrderDTO;
import org.trc.domain.order.LogisticsDO;
import org.trc.domain.order.OrdersDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.OrderException;
import org.trc.util.AppResult;
import org.trc.util.Pagenation;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import static org.trc.util.ResultUtil.*;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/3
 */
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.Order.ROOT)
public class OrderResource {

    private Logger logger = LoggerFactory.getLogger(OrderResource.class);
    @Resource
    private UserService userService;
    @Autowired
    private INewOrderBiz newOrderBiz;

    /**
     * 分页查询订单列表
     * @param orderNum 订单号
     * @param phone  手机号
     * @param orderState 订单状态
     * @param page page
     * @return
     */
    @GET
    //@Admin TODO 缺少注解
    public Pagenation<OrdersDO> queryOrderList(@QueryParam("orderNum") String orderNum,
                                    @QueryParam("phone") String phone,
                                    @QueryParam("orderState") Integer orderState,
                                    @BeanParam Pagenation<OrdersDO> page) {
        OrderDTO order = new OrderDTO();
        order.setOrderNum(orderNum);
        order.setUsername(phone);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(phone)) {
            UserDO userDO = userService.getUserDO(QueryType.Phone, phone);
            if (null == userDO) {
                throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION,"手机号不存在");
            }
            order.setUserId(userDO.getUserId());
        }
        order.setOrderState(orderState);
        return  newOrderBiz.queryOrdersDOListForPage(order, page);
    }

    /**
     * 物流查询接口
     * @param id
     * @return
     */
    @GET
    @Path(ScoreAdminConstants.Route.Order.LOGISTICSTRACKING+"/{id}")
    public AppResult<JSONObject> logisticsTracking(@NotNull @PathParam("id") Long id ){
        LogisticsDO logistic = new LogisticsDO();
        logistic.setOrderId(id);
        LogisticsDO logisticsDO = newOrderBiz.getOrderLogistic(logistic);
        if(null==logisticsDO || StringUtils.isEmpty(logisticsDO.getShipperCode()) || StringUtils.isEmpty(logisticsDO.getLogisticsNum())){
            //TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_CUSTOM);
            throw new OrderException(ExceptionEnum.LOGISTICS_QUERY_EXCEPTION,"物流信息查询异常");
        }
        LogisticAck logisticAck = newOrderBiz.logisticsTracking(logisticsDO.getShipperCode(), logisticsDO.getLogisticsNum());
        if(logisticAck.isSuccess()){
            JSONObject json = new JSONObject();
            json.put("success",logisticAck.isSuccess());
            json.put("state",logisticAck.getState());
            json.put("traces",logisticAck.getTraces());
            return createSucssAppResult("查询物流信息成功!" ,json);
        }else{
            logger.error(logisticAck.getReason());
            return createFailAppResult(logisticAck.getReason());
        }
    }

    /**
     * 快递100查询物流
     * @param id
     * @return
     */
    @GET
    @Path(ScoreAdminConstants.Route.Order.PULL+"/{id}")
    public AppResult<TrcExpressAck> pull(@NotNull @PathParam("id") Long id ){
        LogisticsDO logistic = new LogisticsDO();
        logistic.setOrderId(id);
        LogisticsDO logisticsDO = newOrderBiz.getOrderLogistic(logistic);
        if(null==logisticsDO || StringUtils.isEmpty(logisticsDO.getShipperCode()) || StringUtils.isEmpty(logisticsDO.getLogisticsNum())){
            throw new OrderException(ExceptionEnum.LOGISTICS_QUERY_EXCEPTION,"物流信息查询异常");
        }
        TrcExpressAck result = newOrderBiz.pull(logisticsDO.getShipperCode(), logisticsDO.getLogisticsNum());
        if ("200".equals(result.getCode())) {
            return createSucssAppResult("快递100查询物流信息成功!" ,result);
        } else {
            logger.error(result.getMessage());
            return createFailAppResult(result.getMessage());
        }
    }
}
