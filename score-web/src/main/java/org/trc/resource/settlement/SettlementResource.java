package org.trc.resource.settlement;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BeanParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.order.INewOrderBiz;
import org.trc.biz.settlement.ISettlementBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.constants.TemporaryContext;
import org.trc.domain.dto.ExportOrderDTO;
import org.trc.domain.dto.OrderQuery;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.query.SettlementQuery;
import org.trc.domain.settlement.SettlementDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.OrderException;
import org.trc.interceptor.Admin;
import org.trc.util.CellDefinition;
import org.trc.util.ExportExcel;
import org.trc.util.FatherToChildUtils;
import org.trc.util.Pagenation;

import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.service.UserService;
import com.tairanchina.md.api.QueryType;
import com.txframework.util.DateUtils;
import com.txframework.util.ListUtils;


/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/10
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.Settlement.ROOT)
public class SettlementResource {

    public Logger logger = LoggerFactory.getLogger(SettlementResource.class);
    @Autowired
    private ISettlementBiz settlementBiz;
    @Resource
    private UserService userService;
    @Autowired
    private INewOrderBiz newOrderBiz;


    @GET
    @Admin
    public Pagenation<SettlementDO> querySettlementList(@NotNull @QueryParam("shopId") Long shopId, @QueryParam("billNum") String billNum,
                                                        @Context ContainerRequestContext requestContext,
                                                        @BeanParam Pagenation<SettlementDO> page) {
        SettlementDO settlementDO = new SettlementDO();
        settlementDO.setShopId(shopId);
        return settlementBiz.queryListByParams(settlementDO, page);
    }

    @GET
    @Admin
    @Path(ScoreAdminConstants.Route.Settlement.ORDER)
    public Pagenation querySettlementOrderList(@QueryParam("shopId") Long shopId,
                                              @QueryParam("phone") String phone,
                                              @QueryParam("orderNum") String orderNum,
                                              @QueryParam("goodsName") String goodsName,
                                              @QueryParam("startTime") Long startTime,
                                              @QueryParam("endTime") Long endTime,
                                               @Context ContainerRequestContext requestContext,
                                              @BeanParam Pagenation<OrdersDO> page) {
            SettlementQuery settlementQuery = new SettlementQuery();
            settlementQuery.setShopId(shopId);
            if(org.apache.commons.lang3.StringUtils.isNotBlank(phone)) {
                UserDO userDO = userService.getUserDO(QueryType.Phone, phone);
                if(null==userDO){
                    throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION,"手机号不存在");
                }
                settlementQuery.setUserId(userDO.getUserId());
            }
            settlementQuery.setOrderNum(orderNum);
            settlementQuery.setGoodsName(goodsName);
            if (null != startTime) {
                settlementQuery.setStartTime(new Date(startTime));
            }
            if (null != endTime) {
                settlementQuery.setEndTime(new Date(endTime));
            }
            Pagenation<OrdersDO> orderPage = newOrderBiz.queryOrdersByParams(settlementQuery, page);
            if (orderPage != null && ListUtils.isNotEmpty(orderPage.getInfos()) && orderPage.getInfos().size() > 0) {
                List<OrdersDO> ordersDOList = orderPage.getInfos();
                for (int i= 0; i < ordersDOList.size();i++) {
                    OrdersDO ordersDO = ordersDOList.get(i);
                    UserDO userDO = userService.getUserDO(QueryType.UserId, ordersDO.getUserId());
                    ordersDO.setShopName(TemporaryContext.getShopNameById(ordersDO.getShopId()));
                    ordersDO.setUserPhone(userDO.getPhone());
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

    @GET
    @Admin
    @Path(ScoreAdminConstants.Route.Settlement.EXPORT)
    public Response exportSettlementOrderList(@QueryParam("shopId") Long shopId,
                                              @QueryParam("phone") String phone,
                                              @QueryParam("startTime") Long startTime,
                                              @QueryParam("endTime") Long endTime,
                                              @Context ContainerRequestContext requestContext) throws Exception{
        SettlementQuery settlementQuery = new SettlementQuery();
        settlementQuery.setShopId(shopId);
        if (org.apache.commons.lang3.StringUtils.isNotBlank(phone)) {
            UserDO userDO = userService.getUserDO(QueryType.Phone, phone);
            if (null == userDO) {
                throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION, "手机号不存在");
            }
            settlementQuery.setUserId(userDO.getUserId());
        }
        if (null != startTime) {
            settlementQuery.setStartTime(new Date(startTime));
        }
        if (null != endTime) {
            settlementQuery.setEndTime(new Date(endTime));
        }
        List<ExportOrderDTO> result = newOrderBiz.queryOrdersForExport(settlementQuery);
        //导出文件
        CellDefinition orderDate = new CellDefinition("orderDate", "日期", CellDefinition.DATE, 4000);
        CellDefinition orderNum = new CellDefinition("orderNum", "订单编号", CellDefinition.TEXT, 9000);
        CellDefinition shopName = new CellDefinition("shopName", "店铺名称", CellDefinition.TEXT, 4000);
        CellDefinition username = new CellDefinition("username", "会员手机号", CellDefinition.TEXT, 4000);
        CellDefinition barCode = new CellDefinition("barCode", "商品条码", CellDefinition.TEXT, 4000);
        CellDefinition goodsName = new CellDefinition("goodsName", "商品名称", CellDefinition.TEXT, 16000);
        CellDefinition goodsCount = new CellDefinition("goodsCount", "商品数量", CellDefinition.NUM_0, 4000);
        CellDefinition payment = new CellDefinition("payment", "积分数量", CellDefinition.NUM_0, 4000);
        List<CellDefinition> cellDefinitionList = new ArrayList<>();
        cellDefinitionList.add(orderDate);
        cellDefinitionList.add(orderNum);
        cellDefinitionList.add(shopName);
        cellDefinitionList.add(username);
        cellDefinitionList.add(barCode);
        cellDefinitionList.add(goodsName);
        cellDefinitionList.add(goodsCount);
        cellDefinitionList.add(payment);
        String sheetName = "消费结算";
        String fileName = "消费结算-" + DateUtils.formatDate(settlementQuery.getStartTime(), DateUtils.DATE_PATTERN) + "-" + DateUtils.formatDate(settlementQuery.getEndTime(), DateUtils.DATE_PATTERN) + ".xls";
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        HSSFWorkbook hssfWorkbook = ExportExcel.generateExcel(result, cellDefinitionList, sheetName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        hssfWorkbook.write(stream);
        /**模拟数据结束*/
        return Response.ok(stream.toByteArray()).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8'zh_cn'" + fileName).type(MediaType.APPLICATION_OCTET_STREAM)
                .header("Cache-Control", "no-cache").build();
    }
}
