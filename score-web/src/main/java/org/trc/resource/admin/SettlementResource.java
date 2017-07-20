package org.trc.resource.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.service.UserService;
import com.tairanchina.md.api.QueryType;
import com.txframework.util.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.order.INewOrderBiz;
import org.trc.biz.order.ISettlementBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.constants.TemporaryContext;
import org.trc.domain.dto.SettlementQuery;
import org.trc.domain.order.OrdersDO;
import org.trc.domain.order.SettlementDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.OrderException;
import org.trc.util.AppResult;
import org.trc.util.JSONUtil;
import org.trc.util.Pagenation;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.util.Date;

import static org.trc.util.ResultUtil.createSucssAppResult;


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
    public Pagenation<SettlementDO> querySettlementList(@NotNull @QueryParam("shopId") Long shopId, @QueryParam("billNum") String billNum,
                                         @BeanParam Pagenation<SettlementDO> page) {
        SettlementDO settlementDO = new SettlementDO();
        settlementDO.setShopId(shopId);
        return settlementBiz.queryListByParams(settlementDO, page);
    }

    @GET
    @Path(ScoreAdminConstants.Route.Settlement.ORDER)
    public AppResult querySettlementOrderList(@QueryParam("shopId") Long shopId,
                                              @QueryParam("phone") String phone,
                                              @QueryParam("orderNum") String orderNum,
                                              @QueryParam("goodsName") String goodsName,
                                              @QueryParam("startTime") Long startTime,
                                              @QueryParam("endTime") Long endTime,
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
            Pagenation<OrdersDO> result = newOrderBiz.queryOrdersByParams(settlementQuery, page);
            JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            if (ListUtils.isNotEmpty(result.getResult())) {
                for (OrdersDO ordersDO : result.getResult()) {
                    JSONObject json = new JSONObject();
                    json.put("id", ordersDO.getId());
                    json.put("orderNum", ordersDO.getOrderNum());
                    json.put("shopId", ordersDO.getShopId());
                    json.put("shopName", TemporaryContext.getShopNameById(ordersDO.getShopId()));
                    json.put("userId", ordersDO.getUserId());
                    json.put("username", ordersDO.getUsername());
                    json.put("goodsId", ordersDO.getGoodsId());
                    json.put("barcode", ordersDO.getBarcode());
                    json.put("goodsName", ordersDO.getGoodsName());
                    json.put("goodsCount", ordersDO.getGoodsCount());
                    json.put("price", ordersDO.getPrice());
                    json.put("payment", ordersDO.getPayment());
                    json.put("minImg", ordersDO.getMinImg());
                    json.put("orderState", ordersDO.getOrderState());
                    json.put("createTime", ordersDO.getCreateTime());
                    json.put("updateTime", ordersDO.getUpdateTime());
                    if(ordersDO.getOrderAddressDO()!=null) {
                        json.put("address", ordersDO.getOrderAddressDO().getAddress());
                        json.put("receiverName", ordersDO.getOrderAddressDO().getReceiverName());
                        json.put("receiverPhone", ordersDO.getOrderAddressDO().getPhone());
                    }
                    jsonArray.add(json);
                }
            }
            JSONUtil.putParam(jsonArray, result, jsonObject);
            return createSucssAppResult("查询成功!", jsonObject);
    }
    //TODO 导出
}
