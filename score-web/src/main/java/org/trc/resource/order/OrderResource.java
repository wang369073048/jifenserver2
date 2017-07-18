package org.trc.resource.order;

import com.alibaba.fastjson.JSONObject;
import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.service.UserService;
import com.tairanchina.md.api.QueryType;
import com.trc.mall.externalservice.LogisticAck;
import com.trc.mall.externalservice.TrcExpressAck;
import com.txframework.util.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.order.INewOrderBiz;
import org.trc.biz.order.ISettlementBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;
import org.trc.domain.dto.OrderDTO;
import org.trc.domain.dto.OrderQuery;
import org.trc.domain.dto.SettlementQuery;
import org.trc.domain.order.LogisticsDO;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.order.SettlementDO;
import org.trc.domain.shop.ManagerDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.OrderException;
import org.trc.exception.ShopException;
import org.trc.util.AppResult;
import org.trc.util.FatherToChildUtils;
import org.trc.util.Pagenation;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.trc.util.ResultUtil.createFailAppResult;
import static org.trc.util.ResultUtil.createSucssAppResult;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/1
 */
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.Order.ROOT)
public class OrderResource {

    private Logger logger = LoggerFactory.getLogger(OrderResource.class);
    @Autowired
    private INewOrderBiz newOrderBiz;
    @Autowired
    private IShopBiz shopBiz;
    @Resource
    private UserService userService;
    @Autowired
    private IAuthBiz authBiz;
    @Autowired
    private ISettlementBiz settlementBiz;

    @POST
    @Path("/updateState")
    //@Manager TODO 缺少注解
    public AppResult updateState(@FormParam("orderId") Long orderId,
                                 @FormParam("orderState") Integer orderState,
                                 @Context ContainerRequestContext requestContext) {

        String userId = (String) requestContext.getProperty("userId");
        //取该用户的权限
        ManagerDO manager = shopBiz.getManagerByUserId(userId);
        OrdersDO param = new OrdersDO();
        param.setId(orderId);
        OrdersDO order = newOrderBiz.selectByParams(param);
        if (!order.getShopId().equals(manager.getShopId()) || (!"ORDER_MANAGER".equals(manager.getRoleType()) && !"OWNER".equals(manager.getRoleType()))) {
            throw new ShopException(ExceptionEnum.ERROR_ILLEGAL_OPERATION, "操作不合法");
        }
        OrdersDO ordersDO = new OrdersDO();
        ordersDO.setId(orderId);
        ordersDO.setOrderState(orderState);
        newOrderBiz.modifyOrderState(ordersDO);
        return createSucssAppResult("更新订单状态成功", "");
    }

    /**
     * 修改订单物流信息
     *
     * @param orderId        订单号
     * @param companyName    公司名称
     * @param shipperCode
     * @param logisticsNum
     * @param freight
     * @param requestContext
     * @return
     */
    @POST
    @Path("/modify")
    //@Manager TODO 缺少注解
    public AppResult modifyOrderLogistics(@FormParam("orderId") Long orderId,
                                          @FormParam("companyName") String companyName,
                                          @FormParam("shipperCode") String shipperCode,
                                          @FormParam("logisticsNum") String logisticsNum,
                                          @FormParam("freight") Integer freight,
                                          @Context ContainerRequestContext requestContext) {
        String userId = (String) requestContext.getProperty("userId");
        //取该用户的权限
        ManagerDO manager = shopBiz.getManagerByUserId(userId);
        OrdersDO param = new OrdersDO();
        param.setId(orderId);
        OrdersDO order = newOrderBiz.selectByParams(param);
        if (!order.getShopId().equals(manager.getShopId()) || (!"ORDER_MANAGER".equals(manager.getRoleType()) && !"OWNER".equals(manager.getRoleType()))) {
            throw new ShopException(ExceptionEnum.ERROR_ILLEGAL_OPERATION, "操作不合法");
        }
        LogisticsDO logistics = new LogisticsDO();
        logistics.setOrderId(orderId);
        logistics.setCompanyName(companyName);
        logistics.setShipperCode(shipperCode);
        logistics.setLogisticsNum(logisticsNum);
        logistics.setFreight(freight);
        logistics.setOperatorUserId(userId);
        logistics.setUpdateTime(Calendar.getInstance().getTime());
        newOrderBiz.modifyOrderLogistic(logistics);
        return createSucssAppResult("修改订单物流信息成功", "");
    }

    /**
     * 订单发货
     *
     * @param orderId
     * @param companyName
     * @param shipperCode
     * @param logisticsNum
     * @param freight
     * @return
     */
    @POST
    @Path("/ship")
    //@Manager TODO 缺少注解
    public AppResult shipOrder(@FormParam("orderId") Long orderId,
                               @FormParam("companyName") String companyName,
                               @FormParam("shipperCode") String shipperCode,
                               @FormParam("logisticsNum") String logisticsNum,
                               @FormParam("freight") Integer freight,
                               @Context ContainerRequestContext requestContext) {

        String userId = (String) requestContext.getProperty("userId");
        //取该用户的权限
        ManagerDO manager = shopBiz.getManagerByUserId(userId);
        OrdersDO param = new OrdersDO();
        param.setId(orderId);
        OrdersDO order = newOrderBiz.selectByParams(param);
        if (!order.getShopId().equals(manager.getShopId()) || (!"ORDER_MANAGER".equals(manager.getRoleType()) && !"OWNER".equals(manager.getRoleType()))) {
            throw new ShopException(ExceptionEnum.ERROR_ILLEGAL_OPERATION, "操作不合法");
        }
        LogisticsDO logistics = new LogisticsDO(orderId, companyName, shipperCode, logisticsNum, freight, userId, null);
        LogisticsDO logisticsDO = newOrderBiz.shipOrder(logistics);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logisticsId", logisticsDO.getId());
        return createSucssAppResult("订单发货成功", "");
    }

    /**
     * 查询订单列表
     *
     * @param orderNum
     * @param phone
     * @param orderType
     * @param orderState
     * @param startTime
     * @param endTime
     * @param page
     * @param requestContext
     * @return
     */
    @GET
    @Path("/list")
    //@Manager TODO 缺少注解
    public Pagenation<OrdersDO> queryOrderList(@QueryParam("orderNum") String orderNum,
                                               @QueryParam("phone") String phone,
                                               @QueryParam("orderType") Integer orderType,
                                               @QueryParam("orderState") Integer orderState,
                                               @QueryParam("startTime") Long startTime,
                                               @QueryParam("endTime") Long endTime,
                                               @BeanParam Pagenation<OrdersDO> page,
                                               @Context ContainerRequestContext requestContext) {
        String userId = (String) requestContext.getProperty("userId");
        //取该用户的权限
        ManagerDO manager = shopBiz.getManagerByUserId(userId);
        OrderDTO order = new OrderDTO();
        order.setOrderNum(orderNum);
        order.setUsername(phone);
        if (StringUtils.isNotBlank(phone)) {
            UserDO userDO = userService.getUserDO(QueryType.Phone, phone);
            if (null == userDO) {
                throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION, "手机号不存在");
            }
            order.setUserId(userDO.getUserId());
        }
        order.setShopId(manager.getShopId());
        order.setOrderType(orderType);
        order.setOrderState(orderState);
        if (null != startTime) {
            order.setOperateTimeMin(new Date(startTime));
        }
        if (null != endTime) {
            order.setOperateTimeMax(new Date(endTime));
        }
        return newOrderBiz.queryOrdersDOListForPage(order, page);
    }

    //TODO 发货订单导出


    @GET
    @Path("/get")
    //@Manager
    public AppResult selectOrder(@QueryParam("id") Long id,
                                 @Context ContainerRequestContext requestContext) {
        OrdersDO order = new OrdersDO();
        order.setId(id);
        //用户权限
        String userId = (String) requestContext.getProperty("userId");
        //取该用户的权限
        ManagerDO manager = shopBiz.getManagerByUserId(userId);
        order.setShopId(manager.getShopId());

        OrdersDO result = newOrderBiz.selectByParams(order);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", result.getId());
        jsonObject.put("orderNum", result.getOrderNum());
        jsonObject.put("goodsId", result.getGoodsId());
        jsonObject.put("goodsName", result.getGoodsName());
        jsonObject.put("shopId", result.getShopId());
        jsonObject.put("shopName", result.getShopName());
        jsonObject.put("minImg", result.getMinImg());
        jsonObject.put("price", result.getPrice());
        jsonObject.put("payment", result.getPayment());
        jsonObject.put("goodsCount", result.getGoodsCount());
        jsonObject.put("orderState", result.getOrderState());
        jsonObject.put("orderType", result.getOrderType());
        jsonObject.put("createTime", result.getCreateTime());
        jsonObject.put("deliveryTime", result.getDeliveryTime());
        jsonObject.put("confirmTime", result.getConfirmTime());
        jsonObject.put("updateTime", result.getUpdateTime());
        if (result.getOrderAddressDO() != null) {
            jsonObject.put("provinceCode", result.getOrderAddressDO().getProvinceCode());
            jsonObject.put("cityCode", result.getOrderAddressDO().getCityCode());
            jsonObject.put("areaCode", result.getOrderAddressDO().getAreaCode());
            jsonObject.put("address", result.getOrderAddressDO().getAddress());
            jsonObject.put("province", result.getOrderAddressDO().getProvince());
            jsonObject.put("city", result.getOrderAddressDO().getCity());
            jsonObject.put("area", result.getOrderAddressDO().getArea());
            jsonObject.put("receiverName", result.getOrderAddressDO().getReceiverName());
            jsonObject.put("receiverPhone", result.getOrderAddressDO().getPhone());
            jsonObject.put("postcode", result.getOrderAddressDO().getPostcode());
        }
        if (result.getLogisticsDO() != null) {
            jsonObject.put("companyName", result.getLogisticsDO().getCompanyName());
            jsonObject.put("shipperCode", result.getLogisticsDO().getShipperCode());
            jsonObject.put("logisticsNum", result.getLogisticsDO().getLogisticsNum());
            jsonObject.put("freight", result.getLogisticsDO().getFreight());
        }
        return createSucssAppResult("订单发货成功", jsonObject);
    }

    @GET
    @Path("/settlement")
    //@Logined
    public Pagenation<SettlementDO> querySettlementList(@QueryParam("billNum") String billNum,
                                                        @BeanParam Pagenation<SettlementDO> page,
                                                        @Context ContainerRequestContext requestContext) {

        String userId = (String) requestContext.getProperty("userId");
        //取该用户的权限
        Auth auth = authBiz.getAuthByUserId(userId);
        SettlementDO settlementDO = new SettlementDO();
        settlementDO.setShopId(auth.getShopId());
        return settlementBiz.queryListByParams(settlementDO, page);
    }

    @GET
    @Path("/settlementOrder")
    //@Logined
    public Pagenation<OrdersDO> querySettlementOrderList(@QueryParam("orderNum") String orderNum,
                                                         @QueryParam("goodsName") String goodsName,
                                                         @QueryParam("startTime") Long startTime,
                                                         @QueryParam("endTime") Long endTime,
                                                         @BeanParam Pagenation<OrdersDO> page,
                                                         @Context ContainerRequestContext requestContext) {
        String userId = (String) requestContext.getProperty("userId");
        //取该用户的权限
        Auth auth = authBiz.getAuthByUserId(userId);
        SettlementQuery settlementQuery = new SettlementQuery();
        settlementQuery.setShopId(auth.getShopId());
        settlementQuery.setOrderNum(orderNum);
        settlementQuery.setGoodsName(goodsName);
        settlementQuery.setStartTime(new Date(startTime));
        settlementQuery.setEndTime(new Date(endTime));
        Pagenation<OrdersDO> orderPage = newOrderBiz.queryOrdersByParams(settlementQuery, page);
        if (orderPage != null && ListUtils.isNotEmpty(orderPage.getResult()) && orderPage.getResult().size() > 0) {
            List<OrdersDO> ordersDOList = orderPage.getResult();
            for (int i= 0; i < ordersDOList.size();i++) {
                OrdersDO ordersDO = ordersDOList.get(i);
                if(ordersDO.getOrderAddressDO()!=null) {
                    OrderQuery orderQuery = new OrderQuery();
                    FatherToChildUtils.fatherToChild(ordersDO,orderQuery);
                    orderQuery.setAddress(ordersDO.getOrderAddressDO().getAddress());
                    orderQuery.setReceiverName(ordersDO.getOrderAddressDO().getReceiverName());
                    orderQuery.setReceiverPhone(ordersDO.getOrderAddressDO().getPhone());
                    ordersDOList.set(i,orderQuery);
                }

            }
        }
        return orderPage;
    }

    /**
     * 物流查询接口
     *
     * @param id
     * @param requestContext
     * @return
     */
    @GET
    @Path("/logisticsTracking")
    //@Manager
    public AppResult logisticsTracking(@NotNull @QueryParam("id") Long id,
                                       @Context ContainerRequestContext requestContext) {
        String userId = (String) requestContext.getProperty("userId");
        //取该用户的权限
        ManagerDO manager = shopBiz.getManagerByUserId(userId);
        OrdersDO param = new OrdersDO();
        param.setId(id);
        OrdersDO order = newOrderBiz.selectByParams(param);
        if (!order.getShopId().equals(manager.getShopId()) || (!"ORDER_MANAGER".equals(manager.getRoleType()) && !"OWNER".equals(manager.getRoleType()))) {
            throw new OrderException(ExceptionEnum.ERROR_ILLEGAL_OPERATION, "操作不合法");
        }

        LogisticsDO logistic = new LogisticsDO();
        logistic.setOrderId(id);
        LogisticsDO logisticsDO = newOrderBiz.getOrderLogistic(logistic);
        if (null == logisticsDO || StringUtils.isEmpty(logisticsDO.getShipperCode()) || StringUtils.isEmpty(logisticsDO.getLogisticsNum())) {
            throw new OrderException(ExceptionEnum.LOGISTICS_QUERY_EXCEPTION, "物流信息查询异常");
        }
        LogisticAck logisticAck = newOrderBiz.logisticsTracking(logisticsDO.getShipperCode(), logisticsDO.getLogisticsNum());
        if (logisticAck.isSuccess()) {
            JSONObject json = new JSONObject();
            json.put("success", logisticAck.isSuccess());
            json.put("state", logisticAck.getState());
            json.put("traces", logisticAck.getTraces());
            return createSucssAppResult("查询物流信息成功!", json);
        } else {
            logger.error(logisticAck.getReason());
            return createFailAppResult(logisticAck.getReason());
        }
    }

    /**
     * 快递100查询物流信息
     *
     * @param id
     * @param requestContext
     * @return
     */
    @GET
    @Path("/pull")
    //@Manager
    public AppResult<TrcExpressAck> pull(@NotNull @QueryParam("id") Long id,
                                         @Context ContainerRequestContext requestContext) {

        String userId = (String) requestContext.getProperty("userId");
        //取该用户的权限
        ManagerDO manager = shopBiz.getManagerByUserId(userId);
        OrdersDO param = new OrdersDO();
        param.setId(id);
        OrdersDO order = newOrderBiz.selectByParams(param);
        if (!order.getShopId().equals(manager.getShopId()) || (!"ORDER_MANAGER".equals(manager.getRoleType()) && !"OWNER".equals(manager.getRoleType()))) {
            throw new OrderException(ExceptionEnum.ERROR_ILLEGAL_OPERATION, "操作不合法");
        }

        LogisticsDO logistic = new LogisticsDO();
        logistic.setOrderId(id);
        LogisticsDO logisticsDO = newOrderBiz.getOrderLogistic(logistic);
        if (null == logisticsDO || StringUtils.isEmpty(logisticsDO.getShipperCode()) || StringUtils.isEmpty(logisticsDO.getLogisticsNum())) {
            throw new OrderException(ExceptionEnum.LOGISTICS_QUERY_EXCEPTION, "物流信息查询异常");
        }
        TrcExpressAck result = newOrderBiz.pull(logisticsDO.getShipperCode(), logisticsDO.getLogisticsNum());
        if ("200".equals(result.getCode())) {
            return createSucssAppResult("快递100查询物流信息成功!", result);
        } else {
            logger.error(result.getMessage());
            return createFailAppResult(result.getMessage());
        }
    }

    /**
     * 查询待退款订单
     *
     * @param orderNum
     * @param phone
     * @return
     */
    @GET
    @Path("/checkOrder")
    //@CustomerService TODO 缺少注解
    public AppResult<JSONObject> checkOrder(@NotNull @QueryParam("orderNum") String orderNum,
                                            @NotNull @QueryParam("phone") String phone) {
        OrdersDO param = new OrdersDO();
        param.setOrderNum(orderNum);
        UserDO userDO = userService.getUserDO(QueryType.Phone, phone);
        if (null == userDO) {
            throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION, "手机号不存在");
        }
        param.setUserId(userDO.getUserId());
        OrdersDO order = newOrderBiz.selectByParams(param);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", order.getId());
        jsonObject.put("orderNum", order.getOrderNum());
        jsonObject.put("goodsId", order.getGoodsId());
        jsonObject.put("goodsName", order.getGoodsName());
        jsonObject.put("shopId", order.getShopId());
        jsonObject.put("shopName", order.getShopName());
        jsonObject.put("minImg", order.getMinImg());
        jsonObject.put("price", order.getPrice());
        jsonObject.put("payment", order.getPayment());
        jsonObject.put("goodsCount", order.getGoodsCount());
        jsonObject.put("orderState", order.getOrderState());
        jsonObject.put("orderType", order.getOrderType());
        jsonObject.put("goodsNo", order.getGoodsNo());
        jsonObject.put("username", order.getUsername());
        jsonObject.put("createTime", order.getCreateTime());
        jsonObject.put("deliveryTime", order.getDeliveryTime());
        jsonObject.put("confirmTime", order.getConfirmTime());
        jsonObject.put("updateTime", order.getUpdateTime());
        if (order.getOrderAddressDO() != null) {
            jsonObject.put("provinceCode", order.getOrderAddressDO().getProvinceCode());
            jsonObject.put("cityCode", order.getOrderAddressDO().getCityCode());
            jsonObject.put("areaCode", order.getOrderAddressDO().getAreaCode());
            jsonObject.put("address", order.getOrderAddressDO().getAddress());
            jsonObject.put("province", order.getOrderAddressDO().getProvince());
            jsonObject.put("city", order.getOrderAddressDO().getCity());
            jsonObject.put("area", order.getOrderAddressDO().getArea());
            jsonObject.put("receiverName", order.getOrderAddressDO().getReceiverName());
            jsonObject.put("receiverPhone", order.getOrderAddressDO().getPhone());
            jsonObject.put("postcode", order.getOrderAddressDO().getPostcode());
        }
        if (order.getLogisticsDO() != null) {
            jsonObject.put("companyName", order.getLogisticsDO().getCompanyName());
            jsonObject.put("shipperCode", order.getLogisticsDO().getShipperCode());
            jsonObject.put("logisticsNum", order.getLogisticsDO().getLogisticsNum());
            jsonObject.put("freight", order.getLogisticsDO().getFreight());
        }
        return createSucssAppResult("查询待退款订单成功!", jsonObject);
    }

    /**
     * 退款
     * @param orderNum
     * @param phone
     * @param remark
     * @return
     */
    @POST
    @Path("/returnGoods")
    //@CustomerService TODO 缺少注解
    public AppResult returnGoods(@NotNull @FormParam("orderNum") String orderNum,
                                @NotNull @FormParam("phone") String phone,
                                @NotNull @FormParam("remark") String remark) {
        UserDO userDO = userService.getUserDO(QueryType.Phone, phone);
        if (null == userDO) {
            throw new OrderException(ExceptionEnum.ORDER_UPDATE_EXCEPTION,String.format("手机号[%s]未注册",phone));
        }
        //TODO 退款
        newOrderBiz.returnGoods(orderNum, userDO.getUserId(), remark);
        return createSucssAppResult("退款成功!", "");
    }
}
