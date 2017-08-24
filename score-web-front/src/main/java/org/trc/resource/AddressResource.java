package org.trc.resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tairanchina.constants.CommonConstants;
import com.tairanchina.context.TxJerseyRestContextFactory;
import com.tairanchina.engine.jersey.TxJerseyTools;
import com.trc.mall.constants.ScoreConstants;
import com.trc.mall.exception.AddressException;
import com.trc.mall.interceptor.Logined;
import com.trc.mall.model.AddressDO;
import com.trc.mall.model.OrdersDO;
import com.trc.mall.provider.ScoreProvider;
import com.trc.mall.util.CustomAck;
import com.trc.mall.util.JSONUtil;
import com.txframework.core.jdbc.PageRequest;
import com.txframework.interceptor.api.annotation.TxAop;
import com.txframework.util.ListUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreConstants.Route.Address.ROOT)
@TxAop
public class AddressResource {

    private Logger logger = LoggerFactory.getLogger(AddressResource.class);

    @POST
    @Path(ScoreConstants.Route.Address.ADD)
    @Logined
    public Response addAddress(@NotEmpty @FormParam("provinceCode") String provinceCode, @NotEmpty @FormParam("cityCode") String cityCode,
                               @FormParam("areaCode") String areaCode, @NotEmpty @FormParam("address") String address,
                               @NotEmpty @FormParam("receiverName") String receiverName, @NotEmpty @FormParam("phone") String phone,
                               @FormParam("postcode") String postcode, @FormParam("isDefault") boolean isDefault){
        String userId = null;
        try {
            userId = TxJerseyRestContextFactory.getInstance().getUserId();
            AddressDO addressDO = new AddressDO();
            addressDO.setUserId(userId);
            addressDO.setIsDeleted(false);
            addressDO.setAddress(address);
            addressDO.setProvinceCode(provinceCode);
            addressDO.setCityCode(cityCode);
            addressDO.setAreaCode(areaCode);
            addressDO.setReceiverName(receiverName);
            addressDO.setPhone(phone);
            addressDO.setPostcode(postcode);
            addressDO.setIsDefault(isDefault);
            Date time = Calendar.getInstance().getTime();
            addressDO.setCreateTime(time);
            addressDO.setUpdateTime(time);
            ScoreProvider.addressService.addAddressDO(addressDO);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("addressId", addressDO.getId());
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch (AddressException e){
            logger.error("====>addAddress exception", e.getCode());
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error("====>addAddress exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }

    }

    /**
     * 根据id查询address
     *
     * @return Response
     */
    @GET
    @Path("/{id}")
    @Logined
    public Response getAddressById(@PathParam("id") Long id) {
        try {
            AddressDO addressDO = ScoreProvider.addressService.getAddressDOById(id);
            String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            if(!userId.equals(addressDO.getUserId())){
                return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_ILLEGAL_OPERATION);
            }
            JSONObject json = new JSONObject();
            json.put("id", addressDO.getId());
            json.put("userId", addressDO.getUserId());
            json.put("provinceCode", addressDO.getProvinceCode());
            json.put("cityCode", addressDO.getCityCode());
            json.put("areaCode", addressDO.getAreaCode());
            json.put("province", addressDO.getProvince());
            json.put("city", addressDO.getCity());
            json.put("area", addressDO.getArea());
            json.put("address", addressDO.getAddress());
            json.put("receiverName", addressDO.getReceiverName());
            json.put("phone", addressDO.getPhone());
            json.put("postcode", addressDO.getPostcode());
            json.put("isDefault", addressDO.getIsDefault());
            json.put("createTime", addressDO.getCreateTime());
            json.put("updateTime", addressDO.getUpdateTime());
            return TxJerseyTools.returnSuccess(json.toString());
        } catch (AddressException e){
            logger.error("====>getAddressById exception", e.getCode());
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error("====>getAddressById exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    /**
     * 修改Address
     *
     * @return Response
     */
    @POST
    @Path("/{id}")
    @Logined
    public Response modifyAddress(@PathParam("id") Long id, @NotEmpty @FormParam("provinceCode") String provinceCode, @NotEmpty @FormParam("cityCode") String cityCode,
                                  @FormParam("areaCode") String areaCode, @NotEmpty @FormParam("address") String address,
                                  @NotEmpty @FormParam("receiverName") String receiverName, @NotEmpty @FormParam("phone") String phone,
                                  @FormParam("postcode") String postcode, @FormParam("isDefault") boolean isDefault) {
        String userId = null;
        try {
            userId = TxJerseyRestContextFactory.getInstance().getUserId();
            AddressDO result = ScoreProvider.addressService.getAddressDOById(id);
            if(!userId.equals(result.getUserId())){
                return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_ILLEGAL_OPERATION);
            }
            AddressDO addressDO = new AddressDO();
            addressDO.setId(id);
            addressDO.setUserId(userId);
            addressDO.setIsDeleted(false);
            addressDO.setAddress(address);
            addressDO.setProvinceCode(provinceCode);
            addressDO.setCityCode(cityCode);
            addressDO.setAreaCode(areaCode);
            addressDO.setReceiverName(receiverName);
            addressDO.setPhone(phone);
            addressDO.setPostcode(postcode);
            addressDO.setIsDefault(isDefault);
            Date time = Calendar.getInstance().getTime();
            addressDO.setUpdateTime(time);
            ScoreProvider.addressService.modifyAddressDO(addressDO);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("addressId", addressDO.getId());
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch (AddressException e){
            logger.error("====>modifyAddress exception", e.getCode());
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error("====>modifyAddress exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    /**
     * 设置默认Address
     *
     * @return Response
     */
    @GET
    @Path(ScoreConstants.Route.Address.DEFAULT+"/{id}")
    @Logined
    public Response defaultAddress(@PathParam("id") Long id) {
        String userId = null;
        try {
            userId = TxJerseyRestContextFactory.getInstance().getUserId();
            AddressDO result = ScoreProvider.addressService.getAddressDOById(id);
            if(!userId.equals(result.getUserId())){
                return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_ILLEGAL_OPERATION);
            }
            AddressDO addressDO = new AddressDO();
            addressDO.setId(id);
            addressDO.setUserId(userId);
            addressDO.setIsDefault(true);
            Date time = Calendar.getInstance().getTime();
            addressDO.setUpdateTime(time);
            ScoreProvider.addressService.defaultAddressDO(addressDO);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("addressId", addressDO.getId());
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch (AddressException e){
            logger.error("====>defaultAddress exception", e.getCode());
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error("====>defaultAddress exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    /**
     * 删除Address
     * @return Response
     */
    @DELETE
    @Path("/{id}")
    @Logined
    public Response deleteAddress(@PathParam("id") Long id) {
        String userId = null;
        try {
            userId = TxJerseyRestContextFactory.getInstance().getUserId();
            AddressDO result = ScoreProvider.addressService.getAddressDOById(id);
            if(!userId.equals(result.getUserId())){
                return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_ILLEGAL_OPERATION);
            }
            ScoreProvider.addressService.deleteAddressDO(id);
            return TxJerseyTools.returnSuccess();
        } catch (AddressException e){
            logger.error("====>deleteAddress exception", e.getCode());
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    @GET
    @Logined
    public Response queryAddress(@DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGEINDEX_STR) @QueryParam("pageIndex") String pageIndex,
                                 @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGESIZE_STR) @QueryParam("pageSize") String pageSize){
        PageRequest<AddressDO> pageRequest = new PageRequest<AddressDO>();
        pageRequest.setCurPage(TxJerseyTools.paramsToInteger(pageIndex));
        pageRequest.setPageData(TxJerseyTools.paramsToInteger(pageSize));
        JSONObject jsonObject = new JSONObject();
        try {
            String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            AddressDO param = new AddressDO();
            param.setUserId(userId);
            PageRequest<AddressDO> addressPage = ScoreProvider.addressService.queryAddressDOListForPage(param,pageRequest);
            List<AddressDO> addressDOs = addressPage.getDataList();
            JSONArray jsonArray = new JSONArray();
            if (ListUtils.isNotEmpty(addressDOs)) {
                for (AddressDO addressDO : addressDOs) {
                    JSONObject json = new JSONObject();
                    json.put("id", addressDO.getId());
                    json.put("userId", addressDO.getUserId());
                    json.put("provinceCode", addressDO.getProvinceCode());
                    json.put("cityCode", addressDO.getCityCode());
                    json.put("areaCode", addressDO.getAreaCode());
                    json.put("province", addressDO.getProvince());
                    json.put("city", addressDO.getCity());
                    json.put("area", addressDO.getArea());
                    json.put("address", addressDO.getAddress());
                    json.put("receiverName", addressDO.getReceiverName());
                    json.put("phone", addressDO.getPhone());
                    json.put("postcode", addressDO.getPostcode());
                    json.put("isDefault", addressDO.getIsDefault());
                    json.put("createTime", addressDO.getCreateTime());
                    json.put("updateTime", addressDO.getUpdateTime());
                    jsonArray.add(json);
                }
            }
            JSONUtil.putParam(jsonArray, addressPage, jsonObject);
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch (AddressException e){
            logger.error("====>queryAddress exception", e.getCode());
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error("====>queryAddress", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

    @GET
    @Path("/get")
    @Logined
    public Response selectOrder(@QueryParam("id") Long id) {
        try {
            OrdersDO order = new OrdersDO();
            order.setId(id);
            AddressDO address = ScoreProvider.addressService.getAddressDOById(id);
            String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            if(!userId.equals(address.getUserId())){
                return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_ILLEGAL_OPERATION);
            }
            return TxJerseyTools.returnSuccess(JSONObject.toJSONString(address));
        } catch (AddressException e){
            logger.error("====>selectOrder exception", e.getCode());
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

}
