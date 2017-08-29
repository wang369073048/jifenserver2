package org.trc.resource.order;

import static org.trc.util.ResultUtil.createFailAppResult;
import static org.trc.util.ResultUtil.createSucssAppResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.sun.xml.bind.v2.TODO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.order.INewOrderBiz;
import org.trc.biz.settlement.ISettlementBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.auth.Auth;
import org.trc.domain.dto.ExportOrderDTO;
import org.trc.domain.dto.OrderDTO;
import org.trc.domain.dto.OrderQuery;
import org.trc.domain.dto.WinningRecordDTO;
import org.trc.domain.order.LogisticsDO;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.settlement.SettlementDO;
import org.trc.domain.shop.ManagerDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.OrderException;
import org.trc.exception.ShopException;
import org.trc.interceptor.CustomerService;
import org.trc.interceptor.Manager;
import org.trc.util.AppResult;
import org.trc.util.CellDefinition;
import org.trc.util.ExportExcel;
import org.trc.util.FatherToChildUtils;
import org.trc.util.Pagenation;
import org.trc.util.TxJerseyTools;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.service.UserService;
import com.tairanchina.md.api.QueryType;
import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.LogisticAck;
import com.trc.mall.externalservice.dto.TrcExpressDto;
import com.trc.mall.util.CustomAck;
import com.txframework.util.DateUtils;
import com.txframework.util.ListUtils;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/1
 */
@Component
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
    @Manager
    public Response updateState(@FormParam("orderId") Long orderId,
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
//        return createSucssAppResult("更新订单状态成功", "");
        return TxJerseyTools.returnSuccess();
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
    @Manager
    public Response modifyOrderLogistics(@FormParam("orderId") Long orderId,
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
//        return createSucssAppResult("修改订单物流信息成功", "");
        return TxJerseyTools.returnSuccess();
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
    @Manager
    public Response shipOrder(@FormParam("orderId") Long orderId,
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
//        return createSucssAppResult("订单发货成功", "");
        return TxJerseyTools.returnSuccess();
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
    @Path(ScoreAdminConstants.Route.Order.LIST)
    @Manager
    public Response queryOrderList(@QueryParam("orderNum") String orderNum,
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
//      return newOrderBiz.queryOrdersDOListForPage(order, page);
        Pagenation<OrdersDO> pageOrders = newOrderBiz.queryOrdersDOListForPage(order, page);
        return TxJerseyTools.returnSuccess(JSON.toJSONString(pageOrders));
    }

    @GET
    @Path("/list/export")
    @Manager
    public Response exportOrder(@QueryParam("orderNum") String orderNum,
                                @QueryParam("phone") String phone,
                                @QueryParam("orderState") Integer orderState,
                                @QueryParam("orderType") Integer orderType,
                                @QueryParam("startTime") Long startTime,
                                @QueryParam("endTime") Long endTime,
                                @Context ContainerRequestContext requestContext) throws IOException {
            String userId = (String) requestContext.getProperty("userId");
            //取该用户的权限
            ManagerDO manager = shopBiz.getManagerByUserId(userId);
            SettlementQuery settlementQuery = new SettlementQuery();
            settlementQuery.setOrderNum(orderNum);
            if(StringUtils.isNotBlank(phone)) {
                UserDO userDO = userService.getUserDO(QueryType.Phone, phone);
                if(null==userDO){
                    throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION, "手机号不存在");
                }
                settlementQuery.setUserId(userDO.getUserId());
            }
            settlementQuery.setShopId(manager.getShopId());
            settlementQuery.setOrderState(orderState);
            settlementQuery.setOrderType(orderType);
            if(null!=startTime) {
                settlementQuery.setStartTime(new Date(startTime));
            }
            if(null!=endTime) {
                settlementQuery.setEndTime(new Date(endTime));
            }
            List<ExportOrderDTO> result = newOrderBiz.queryOrderAndAddressForExport(settlementQuery);
            //导出文件
            CellDefinition orderDate = new CellDefinition("orderDate", "日期", CellDefinition.DATE, 4000);
            CellDefinition orderNumCell = new CellDefinition("orderNum", "订单编号", CellDefinition.TEXT, 9000);
            CellDefinition goodsNo = new CellDefinition("goodsNo", "商品货号", CellDefinition.TEXT, 4000);
            CellDefinition goodsName = new CellDefinition("goodsName", "商品名称", CellDefinition.TEXT, 16000);
            CellDefinition goodsCount = new CellDefinition("goodsCount", "兑换数量", CellDefinition.NUM_0, 4000);
            CellDefinition receiverName = new CellDefinition("receiverName", "收货人姓名", CellDefinition.TEXT, 4000);
            CellDefinition phoneCell = new CellDefinition("phone", "手机号", CellDefinition.TEXT, 4000);
            CellDefinition province = new CellDefinition("province", "省", CellDefinition.TEXT, 4000);
            CellDefinition city = new CellDefinition("city", "市", CellDefinition.TEXT, 4000);
            CellDefinition area = new CellDefinition("area", "区", CellDefinition.TEXT, 4000);
            CellDefinition address = new CellDefinition("address", "详细地址", CellDefinition.TEXT, 16000);
            CellDefinition companyName = new CellDefinition("companyName", "物流公司", CellDefinition.TEXT, 4000);
            CellDefinition logisticsNum = new CellDefinition("logisticsNum", "运单号", CellDefinition.TEXT, 4000);
            CellDefinition price = new CellDefinition("price", "商品单价", CellDefinition.NUM_0, 4000);
            CellDefinition payment = new CellDefinition("payment", "消费积分数量", CellDefinition.NUM_0, 4000);
            List<CellDefinition> cellDefinitionList = new ArrayList<>();
            cellDefinitionList.add(orderDate);
            cellDefinitionList.add(orderNumCell);
            cellDefinitionList.add(goodsNo);
            cellDefinitionList.add(goodsName);
            cellDefinitionList.add(goodsCount);
            cellDefinitionList.add(receiverName);
            cellDefinitionList.add(phoneCell);
            cellDefinitionList.add(province);
            cellDefinitionList.add(city);
            cellDefinitionList.add(area);
            cellDefinitionList.add(address);
            cellDefinitionList.add(companyName);
            cellDefinitionList.add(logisticsNum);
            cellDefinitionList.add(price);
            cellDefinitionList.add(payment);
            String sheetName = "订单列表";
            String fileName = "订单列表"+ ((null != settlementQuery.getStartTime() )?"-"+ DateUtils.formatDate(settlementQuery.getStartTime(),DateUtils.DATE_PATTERN): "")+( null != settlementQuery.getEndTime()?"-"+DateUtils.formatDate(settlementQuery.getEndTime(),DateUtils.DATE_PATTERN):"")+".xls";
            try{
                fileName = URLEncoder.encode(fileName, "UTF-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
            HSSFWorkbook hssfWorkbook = ExportExcel.generateExcel(result, cellDefinitionList, sheetName);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            hssfWorkbook.write(stream);
            /**模拟数据结束*/
            return Response.ok(stream.toByteArray()).header(HttpHeaders.CONTENT_DISPOSITION,"attachment;filename*=utf-8'zh_cn'" + fileName).type(MediaType.APPLICATION_OCTET_STREAM)
                    .header("Cache-Control", "no-cache").build();

    }

    @GET
    @Path("/get")
    @Manager
    public Response selectOrder(@QueryParam("id") Long id,
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
        if(result.getOrderAddressDO()!=null) {
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
        if(result.getLogisticsDO()!=null){
            jsonObject.put("companyName", result.getLogisticsDO().getCompanyName());
            jsonObject.put("shipperCode", result.getLogisticsDO().getShipperCode());
            jsonObject.put("logisticsNum", result.getLogisticsDO().getLogisticsNum());
            jsonObject.put("freight", result.getLogisticsDO().getFreight());
        }
        handleJson(order,jsonObject);
//        return createSucssAppResult("订单发货成功", jsonObject);
        return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
    }

    @GET
    @Path("/settlement")
    public Response querySettlementList(@QueryParam("billNum") String billNum,
                                                        @BeanParam Pagenation<SettlementDO> page,
                                                        @Context ContainerRequestContext requestContext) {

        String userId = (String) requestContext.getProperty("userId");
        //取该用户的权限
        Auth auth = authBiz.getAuthByUserId(userId);
        SettlementDO settlementDO = new SettlementDO();
        settlementDO.setShopId(auth.getShopId());
//        return settlementBiz.queryListByParams(settlementDO, page);
        Pagenation<SettlementDO> pageSettlementDOs = settlementBiz.queryListByParams(settlementDO, page);
        return TxJerseyTools.returnSuccess(JSON.toJSONString(pageSettlementDOs));
    }

    @GET
    @Path("/settlementOrder")
    public Response querySettlementOrderList(@QueryParam("orderNum") String orderNum,
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
        if (orderPage != null && ListUtils.isNotEmpty(orderPage.getInfos()) && orderPage.getInfos().size() > 0) {
            List<OrdersDO> ordersDOList = orderPage.getInfos();
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
//        return orderPage;
        return TxJerseyTools.returnSuccess(JSON.toJSONString(orderPage));
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
    public Response logisticsTracking(@NotNull @QueryParam("id") Long id,
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
//            return createSucssAppResult("查询物流信息成功!", json);
            return TxJerseyTools.returnSuccess(json.toJSONString());
        } else {
            logger.error(logisticAck.getReason());
//            return createFailAppResult(logisticAck.getReason());
            return CustomAck.customError(logisticAck.getReason());
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
    @Manager
    public Response pull(@NotNull @QueryParam("id") Long id,
                                         @Context ContainerRequestContext requestContext) throws IOException {

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
        HttpBaseAck<TrcExpressDto> resultAck =  newOrderBiz.pull(logisticsDO.getShipperCode(), logisticsDO.getLogisticsNum());
        if(resultAck.isSuccess() && null != resultAck.getData() && TrcExpressDto.SUCCESS_CODE.equals(resultAck.getData().getCode())){
//          return createSucssAppResult("查询物流信息成功!", resultAck.getData());
            return TxJerseyTools.returnSuccess(JSON.toJSONString(resultAck.getData()));
        }
        logger.error("物流查询服务不可用!");
//        return createFailAppResult("物流查询服务不可用!");
        return CustomAck.customError("物流查询服务不可用!");
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
    @CustomerService
    public Response checkOrder(@NotNull @QueryParam("orderNum") String orderNum,
                                            @NotNull @QueryParam("phone") String phone,
                                            @Context ContainerRequestContext requestContext) {
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
        handleJson(order,jsonObject);
//        return createSucssAppResult("查询待退款订单成功!", jsonObject);
        return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
    }


    private void handleJson(OrdersDO order,JSONObject jsonObject){
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
    @CustomerService
    public Response returnGoods(@NotNull @FormParam("orderNum") String orderNum,
                                @NotNull @FormParam("phone") String phone,
                                @NotNull @FormParam("remark") String remark,
                                 @Context ContainerRequestContext requestContext) {
    	try{
    		UserDO userDO = userService.getUserDO(QueryType.Phone, phone);
            if (null == userDO) {
                throw new OrderException(ExceptionEnum.ORDER_UPDATE_EXCEPTION,String.format("手机号[%s]未注册",phone));
            }
            //TODO 退款
            newOrderBiz.returnGoods(orderNum, userDO.getUserId(), remark);
    	}catch(OrderException e){
    		logger.error(e.getMessage(), e);
//            return createFailAppResult(e.getMessage());
            return CustomAck.customError(e.getMessage());
    	}
//        return createSucssAppResult("退款成功!", "");
    	return TxJerseyTools.returnSuccess();
    }
}
