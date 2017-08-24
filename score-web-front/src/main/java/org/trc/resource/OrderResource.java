package org.trc.resource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tairanchina.constants.CommonConstants;
import com.tairanchina.context.TxJerseyRestContextFactory;
import com.tairanchina.engine.jersey.TxJerseyTools;
import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.model.UserProfile;
import com.tairanchina.md.api.QueryType;
import com.trc.mall.cache.UserAvatar;
import com.trc.mall.constants.OrderStatus;
import com.trc.mall.constants.ScoreConstants;
import com.trc.mall.dto.OrderDTO;
import com.trc.mall.externalservice.HttpBaseAck;
import com.trc.mall.externalservice.dto.TrcExpressDto;
import com.trc.mall.interceptor.Logined;
import com.trc.mall.model.*;
import com.trc.mall.provider.ScoreProvider;
import com.trc.mall.provider.UserApiProvider;
import com.trc.mall.util.CustomAck;
import com.txframework.core.jdbc.PageRequest;
import com.txframework.interceptor.api.annotation.TxAop;
import com.txframework.util.ListUtils;
import com.txframework.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Date;

/**
 * 订单资源 Created by dy on 2016/12/05
 */

@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreConstants.Route.Order.ROOT)
@TxAop
public class OrderResource {

	private Logger logger = LoggerFactory.getLogger(OrderResource.class);

	@POST
	@Path("/create")
	@Logined
	public Response createOrder(@FormParam("goodsId") Long goodsId, @FormParam("goodsCount") Integer goodsCount,
								@FormParam("price") Integer price, @FormParam("payment") Integer payment,
								@FormParam("addressId") Long addressId) {
		String userId = null;
		try {
			// 查询该账户当前可用积分
			userId = TxJerseyRestContextFactory.getInstance().getUserId();
			//查询用户的头像
			String avatar = null;
			try {
				UserProfile profile = UserApiProvider.userProfileService.getUserProfileByUserId(userId);
				if (profile != null) {
					avatar = (String) profile.getUserInfo().get("avatar");
				}
			} catch (Exception e) {

			}finally{
				if(avatar == null){
					avatar = UserAvatar.getDefaultAvatar();
				}
			}

			String phone = TxJerseyRestContextFactory.getInstance().getPhone();
			Score score = ScoreProvider.scoreService.getScoreByUserId(userId);
			if (Score.ScoreUserType.SELLER.toString().equals(score.getUserType())) {
				return CustomAck.customError("不允许卖家消费!");
			}
			// 用户可用积分是否足够 兑换 该商品
			GoodsDO goods = ScoreProvider.goodsService.getEffectiveGoodsById(goodsId);
			Long userScore = score.getScore();
			if (userScore < goods.getPriceScore()) {
				logger.error("用户积分不足!用户可用积分=" + userScore + ",商品所需积分=" + goods.getPriceScore());
				return CustomAck.customError("96","用户积分不足!");
			}

			Auth auth = ScoreProvider.authService.getAuthByShopId(goods.getShopId());
			JSONObject jsonObject = new JSONObject();
			// 下单
			OrderDTO orderDTO = new OrderDTO();
			orderDTO.setAvatar(avatar);
			orderDTO.setGoodsId(goodsId);
			orderDTO.setUserId(userId);
			orderDTO.setSellerUserId(auth.getUserId());
			orderDTO.setUsername(phone);
			orderDTO.setSellerUsername(auth.getPhone());
			orderDTO.setPrice(price);
			orderDTO.setQuantity(goodsCount);
			orderDTO.setPayment(payment);
			orderDTO.setAddressId(addressId);
			orderDTO.setOrderState(OrderStatus.WAITING_FOR_DELIVERY);
			OrdersDO ordersDO = ScoreProvider.newOrderService.createOrder(orderDTO);
			jsonObject.put("id", ordersDO.getId());
			jsonObject.put("orderNum", ordersDO.getOrderNum());
			return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
		} catch (IllegalArgumentException e) {
			logger.error("====>createOrder exception", e);
			return CustomAck.customError(e.getMessage());
		} catch (AuthException e) {
			logger.error("====>createOrder exception", e);
			return CustomAck.customError(e.getMessage());
		} catch (ScoreException e) {
			logger.error("====>createOrder exception", e);
			if(ScoreException.BALANCE_NOT_ENOUGH.equals(e.getCode())){
				return CustomAck.customError("96",e.getMessage());
			}
			return CustomAck.customError(e.getMessage());
		} catch (BusinessException e) {
			logger.error("====>createOrder exception", e);
			return CustomAck.customError(e.getMessage());
		}  catch (CardCouponException e) {
			logger.error("====>createOrder exception", e);
			return CustomAck.customError(e.getMessage());
		} catch (GoodsException e) {
			logger.error("====>createOrder exception", e);
			return CustomAck.customError(e.getMessage());
		} catch (OrderException e) {
			logger.error(e.getMessage(), e);
			return CustomAck.customError(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
		}
	}

	@GET
	@Path("/list")
	@Logined
	public Response queryOrderList(@QueryParam("orderNum") String orderNum, @QueryParam("phone") String phone,
								   @QueryParam("orderState") Integer orderState, @QueryParam("startDate") Long startDate,
								   @QueryParam("endDate") Long endDate,
								   @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGEINDEX_STR) @QueryParam("pageIndex") String pageIndex,
								   @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGESIZE_STR) @QueryParam("pageSize") String pageSize) {
		try {
			String userId = TxJerseyRestContextFactory.getInstance().getUserId();
			OrdersDO order = new OrdersDO();
			order.setOrderNum(orderNum);
			if (StringUtils.isNotBlank(phone)) {
				UserDO userDO = UserApiProvider.userService.getUserDO(QueryType.Phone, phone);
				if (null == userDO) {
					return TxJerseyTools.returnSuccess();
				}
				order.setUserId(userDO.getUserId());
			}
			order.setUserId(userId);
			order.setOrderState(orderState);
			if (null != startDate) {
				order.setOperateTimeMin(new Date(startDate));
			}
			if (null != endDate) {
				order.setOperateTimeMax(new Date(endDate));
			}
			PageRequest<OrdersDO> pageRequest = new PageRequest<>();
			pageRequest.setCurPage(TxJerseyTools.paramsToInteger(pageIndex));
			pageRequest.setPageData(TxJerseyTools.paramsToInteger(pageSize));
			PageRequest<OrdersDO> result = ScoreProvider.newOrderService.queryOrdersDOListForPage(order, pageRequest);
			int quantityToBeDelivered = ScoreProvider.newOrderService.quantityToBeDelivered(order.getUserId(),null);
			int quantityToBeReceived = ScoreProvider.newOrderService.quantityToBeReceived(order.getUserId(),null);
			JSONObject jsonObject = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			if (ListUtils.isNotEmpty(result.getDataList())) {
				for (OrdersDO ordersDO : result.getDataList()) {
					JSONObject json = new JSONObject();
					json.put("id", ordersDO.getId());
					json.put("orderNum", ordersDO.getOrderNum());
					json.put("shopId", ordersDO.getShopId());
					json.put("shopName", ordersDO.getShopName());
					json.put("userId", ordersDO.getUserId());
					json.put("username", ordersDO.getUsername());
					json.put("goodsId", ordersDO.getGoodsId());
					json.put("goodsName", ordersDO.getGoodsName());
					json.put("goodsCount", ordersDO.getGoodsCount());
					json.put("price", ordersDO.getPrice());
					json.put("payment", ordersDO.getPayment());
					json.put("minImg", ordersDO.getMinImg());
					json.put("orderState", ordersDO.getOrderState());
					json.put("createTime", ordersDO.getCreateTime());
					json.put("deliveryTime", ordersDO.getDeliveryTime());
					json.put("confirmTime", ordersDO.getConfirmTime());
					json.put("updateTime", ordersDO.getUpdateTime());
					if (ordersDO.getOrderAddressDO() != null) {
						json.put("provinceCode", ordersDO.getOrderAddressDO().getProvinceCode());
						json.put("cityCode", ordersDO.getOrderAddressDO().getCityCode());
						json.put("areaCode", ordersDO.getOrderAddressDO().getAreaCode());
						json.put("province", ordersDO.getOrderAddressDO().getProvince());
						json.put("city", ordersDO.getOrderAddressDO().getCity());
						json.put("area", ordersDO.getOrderAddressDO().getArea());
						json.put("address", ordersDO.getOrderAddressDO().getAddress());
						json.put("receiverName", ordersDO.getOrderAddressDO().getReceiverName());
						json.put("receiverPhone", ordersDO.getOrderAddressDO().getPhone());
						json.put("postcode", ordersDO.getOrderAddressDO().getPostcode());
					}
					jsonArray.add(json);
				}
			}
			jsonObject.put("infos", jsonArray);
			jsonObject.put("pageIndex", ObjectUtils.convertVal(result.getCurPage(), 1));
			jsonObject.put("pageSize", ObjectUtils.convertVal(result.getPageData(), 10));
			jsonObject.put("totalData", ObjectUtils.convertVal(result.getTotalData(), 0));
			jsonObject.put("totalPage", ObjectUtils.convertVal(result.getTotalPage(), 0));
			jsonObject.put("quantityToBeDelivered",quantityToBeDelivered);
			jsonObject.put("quantityToBeReceived",quantityToBeReceived);
			return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
		} catch (OrderException e) {
			logger.error(e.getMessage(), e);
			return CustomAck.customError(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
		}
	}

	@GET
	@Path("/{id}")
	public Response selectOrder(@PathParam("id") Long id) {
		try {
			String userId = TxJerseyRestContextFactory.getInstance().getUserId();
			OrdersDO order = new OrdersDO();
			order.setId(id);
			order.setUserId(userId);
			OrdersDO result = ScoreProvider.newOrderService.selectByParams(order);
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
			jsonObject.put("couponCode", result.getCouponCode());
			if (result.getOrderAddressDO() != null) {
				jsonObject.put("provinceCode", result.getOrderAddressDO().getProvinceCode());
				jsonObject.put("cityCode", result.getOrderAddressDO().getCityCode());
				jsonObject.put("areaCode", result.getOrderAddressDO().getAreaCode());
				jsonObject.put("province", result.getOrderAddressDO().getProvince());
				jsonObject.put("city", result.getOrderAddressDO().getCity());
				jsonObject.put("area", result.getOrderAddressDO().getArea());
				jsonObject.put("address", result.getOrderAddressDO().getAddress());
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
			return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
		} catch (OrderException e) {
			logger.error(e.getMessage(), e);
			return CustomAck.customError(e.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
		}
	}

	@POST
	@Path("/updateState")
	public Response updateState(@FormParam("orderId") Long orderId, @FormParam("orderState") Integer orderState) {
		try {
			String userId = TxJerseyRestContextFactory.getInstance().getUserId();
			OrdersDO param = new OrdersDO();
			param.setId(orderId);
			OrdersDO order = ScoreProvider.newOrderService.selectByParams(param);
			if (!userId.equals(order.getUserId())) {
				return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_ILLEGAL_OPERATION);
			}
			OrdersDO ordersDO = new OrdersDO();
			ordersDO.setId(orderId);
			ordersDO.setOrderState(orderState);
			ScoreProvider.newOrderService.modifyOrderState(ordersDO);
			return TxJerseyTools.returnSuccess();
		} catch (OrderException e) {
			logger.error(e.getMessage(), e);
			return CustomAck.customError(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
		}
	}

	/*@GET
	@Path("/logisticsTracking")
	@Logined
	public Response logisticsTracking(@NotEmpty @QueryParam("shipperCode") String shipperCode,
									  @NotEmpty @QueryParam("logisticsNum") String logisticsNum) {
		LogisticAck logisticAck = ScoreProvider.newOrderService.logisticsTracking(shipperCode, logisticsNum);
		if (logisticAck.isSuccess()) {
			JSONObject json = new JSONObject();
			json.put("success", logisticAck.isSuccess());
			json.put("state", logisticAck.getState());
			json.put("traces", logisticAck.getTraces());
			return TxJerseyTools.returnSuccess(json.toJSONString());
		} else {
			logger.error(logisticAck.getReason());
			return CustomAck.customError(logisticAck.getReason());
		}
	}*/

	/*@GET
	@Path("/logisticsTracking/{id}")
	@Logined
	public Response logisticsTracking(@NotNull @PathParam("id") Long id ){
		try{
			String userId = TxJerseyRestContextFactory.getInstance().getUserId();
			OrdersDO param = new OrdersDO();
			param.setId(id);
			param.setUserId(userId);
			param.setOrderState(OrderStatus.WAITING_FOR_RECEIVING);
			OrdersDO order = ScoreProvider.newOrderService.selectByParams(param);
			LogisticsDO logistic = new LogisticsDO();
			logistic.setOrderId(id);
			LogisticsDO logisticsDO = ScoreProvider.newOrderService.getOrderLogistic(logistic);
			if(null==logisticsDO || StringUtils.isEmpty(logisticsDO.getShipperCode()) || StringUtils.isEmpty(logisticsDO.getLogisticsNum())){
				return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_CUSTOM);
			}
			LogisticAck logisticAck = ScoreProvider.newOrderService.logisticsTracking(logisticsDO.getShipperCode(), logisticsDO.getLogisticsNum());
			if(logisticAck.isSuccess()){
				JSONObject json = new JSONObject();
				json.put("success",logisticAck.isSuccess());
				json.put("state",logisticAck.getState());
				json.put("traces",logisticAck.getTraces());
				json.put("companyName", logisticsDO.getCompanyName());
				json.put("shipperCode", logisticsDO.getShipperCode());
				json.put("logisticsNum", logisticsDO.getLogisticsNum());
				return TxJerseyTools.returnSuccess(json.toJSONString());
			}else{
				logger.error(logisticAck.getReason());
				return CustomAck.customError(logisticAck.getReason());
			}

		}catch(AuthException e){
			logger.error("====>logisticsTracking exception", e);
			return CustomAck.customError(e.getMessage());
		}catch (Exception e) {
			return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
		}

	}*/

	@GET
	@Path("/pull")
	@Logined
	public Response pull(@NotEmpty @QueryParam("shipperCode") String shipperCode,
						 @NotEmpty @QueryParam("logisticsNum") String logisticsNum) {
		try {
			HttpBaseAck<TrcExpressDto> resultAck = ScoreProvider.newOrderService.pull(shipperCode, logisticsNum);
			if(resultAck.isSuccess() && null != resultAck.getData() && TrcExpressDto.SUCCESS_CODE.equals(resultAck.getData().getCode())){
				return TxJerseyTools.returnSuccess(JSON.toJSONString(resultAck.getData()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.error("物流查询服务不可用!");
		return CustomAck.customError("物流查询服务不可用!");
	}

}
