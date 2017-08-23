package org.trc.resource.settlement;

import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.service.UserService;
import com.tairanchina.md.api.QueryType;
import com.txframework.util.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.validator.constraints.NotBlank;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.order.INewOrderBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.dto.ExportOrderDTO;
import org.trc.domain.dto.OrderDTO;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.query.SettlementQuery;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.OrderException;
import org.trc.util.CellDefinition;
import org.trc.util.ExportExcel;
import org.trc.util.Pagenation;

import javax.annotation.Resource;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/21
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.Refund.ROOT)
public class RefundRescorce {

    Logger logger = LoggerFactory.getLogger(RefundRescorce.class);

    @Autowired
    private INewOrderBiz newOrderBiz;

    @Resource
    private UserService userService;

    /**
     * 退款结算分页查询
     * @param shopId
     * @param phone
     * @param startTime
     * @param endTime
     * @param page
     * @return
     */
    @GET
    @Path(ScoreAdminConstants.Route.Refund.SETTLEMENT)
    public Pagenation<OrderDTO> settlement(@NotBlank@QueryParam("shopId") Long shopId,
                                           @QueryParam("phone") String phone,
                                           @QueryParam("startTime") Long startTime,
                                           @QueryParam("endTime") Long endTime,
                                           @BeanParam Pagenation<OrderDTO> page) {
        SettlementQuery param = new SettlementQuery();
        List<Integer> orderStates = new ArrayList<>();
        orderStates.add(5);//5为退款
        param.setOrderStates(orderStates);
        UserDO userDO = userService.getUserDO(QueryType.Phone, phone);
        if (null == userDO) {
            throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION, "手机号不存在");
        }
        param.setUserId(userDO.getUserId());
        if (null != startTime) {
            param.setStartTime(new Date(startTime));
        }
        if (null != endTime) {
            param.setEndTime(new Date(endTime));
        }
        param.setShopId(shopId);
        return newOrderBiz.queryRefundOrdersByParams(param,page);
    }

    /**
     * 退款结算导出
     * @param shopId
     * @param phone
     * @param startTime
     * @param endTime
     * @return
     */
    @GET
    @Path(ScoreAdminConstants.Route.Refund.EXPORT)
    public Response settlementExport(@QueryParam("shopId") Long shopId,
                                     @QueryParam("phone") String phone,
                                     @QueryParam("startTime") Long startTime,
                                     @QueryParam("endTime") Long endTime) throws Exception{
        SettlementQuery settlementQuery = new SettlementQuery();
        settlementQuery.setOrderState(5);//5为退款
        settlementQuery.setShopId(shopId);
        UserDO userDO = userService.getUserDO(QueryType.Phone, phone);
        if (null == userDO) {
            throw new OrderException(ExceptionEnum.ORDER_QUERY_EXCEPTION, "手机号不存在");
        }
        settlementQuery.setUserId(userDO.getUserId());
        if (null != startTime) {
            settlementQuery.setStartTime(new Date(startTime));
        }
        if (null != endTime) {
            settlementQuery.setEndTime(new Date(endTime));
        }
        settlementQuery.setShopId(shopId);
        List<OrderDTO> result = newOrderBiz.queryRefundOrdersByParamsForExport(settlementQuery);
        //导出文件
        CellDefinition userId = new CellDefinition("userId", "userId", CellDefinition.TEXT, 4000);
        CellDefinition returnTime = new CellDefinition("returnTime", "日期", CellDefinition.DATE, 4000);
        CellDefinition orderNum = new CellDefinition("orderNum", "订单编号", CellDefinition.TEXT, 8000);
        CellDefinition shopName = new CellDefinition("shopName", "所属商家", CellDefinition.TEXT, 4000);
        CellDefinition barCode = new CellDefinition("barcode", "商品条码", CellDefinition.TEXT, 4000);
        CellDefinition goodsName = new CellDefinition("goodsName", "商品名称", CellDefinition.TEXT, 4000);
        CellDefinition goodsCount = new CellDefinition("goodsCount", "退货数量", CellDefinition.NUM_0, 4000);
        CellDefinition payment = new CellDefinition("payment", "退积分数量", CellDefinition.NUM_0, 4000);
        List<CellDefinition> cellDefinitionList = new ArrayList<>();
        cellDefinitionList.add(userId);
        cellDefinitionList.add(returnTime);
        cellDefinitionList.add(orderNum);
        cellDefinitionList.add(shopName);
        cellDefinitionList.add(barCode);
        cellDefinitionList.add(goodsName);
        cellDefinitionList.add(goodsCount);
        cellDefinitionList.add(payment);
        String sheetName = "退款结算";
        String fileName = "退款结算-" + DateUtils.formatDate(settlementQuery.getStartTime(), DateUtils.DATE_PATTERN) + "-" + DateUtils.formatDate(settlementQuery.getEndTime(), DateUtils.DATE_PATTERN) + ".xls";
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
