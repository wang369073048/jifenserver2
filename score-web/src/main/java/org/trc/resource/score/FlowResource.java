package org.trc.resource.score;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.service.UserService;
import com.tairanchina.md.api.QueryType;
import com.txframework.util.ListUtils;
import com.txframework.util.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.score.IScoreChangeRecordBiz;
import org.trc.biz.shop.IShopBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.constants.ScoreCst;
import org.trc.domain.auth.Auth;
import org.trc.domain.dto.ScoreChangeRecordQueryDTO;
import org.trc.domain.score.ScoreChange;
import org.trc.domain.shop.ManagerDO;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.FlowException;
import org.trc.util.Pagenation;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * Created by hzwzhen on 2017/6/16.
 */
@Produces(MediaType.APPLICATION_JSON)
@Path("{shopId}/" + ScoreAdminConstants.Route.Flow.ROOT)
public class FlowResource {

    @Autowired
    private IShopBiz shopBiz;
    @Autowired
    private IAuthBiz authBiz;
    @Autowired
    private UserService userService;
    @Autowired
    private IScoreChangeRecordBiz scoreChangeRecordBiz;
    @GET
    //@CustomerService
    public Response queryChangeRecord(@PathParam("shopId") Long shopId,
                                      @QueryParam("flowType") String flowType,
                                      @QueryParam("businessCode") String businessCodes,
                                      @QueryParam("userPhone") String userPhone,
                                      @QueryParam("startDate") Long startDate,
                                      @QueryParam("endDate") Long endDate,
                                      @BeanParam Pagenation<ScoreChange> page,
                                      @Context ContainerRequestContext requestContext) {
        //获取userId
        String userId = (String) requestContext.getProperty("userId");
        //判断数据权限
        ManagerDO manager = shopBiz.getManagerByUserId(userId);
        JSONObject jsonObject = new JSONObject();
        ScoreChangeRecordQueryDTO queryDto = new ScoreChangeRecordQueryDTO();
        if (StringUtils.isNotBlank(userPhone)) {
            UserDO userDO = userService.getUserDO(QueryType.Phone, userPhone);
            if (null == userDO) {
                throw new FlowException(ExceptionEnum.ORDER_QUERY_EXCEPTION,"手机号不存在");
            }
            queryDto.setUserId(userDO.getUserId());
        }
        queryDto.setShopId(manager.getShopId());
        Auth auth = authBiz.getAuthByShopId(manager.getShopId());
        queryDto.setExchangeCurrency(auth.getExchangeCurrency());
        queryDto.setFlowType(flowType);
        queryDto.calBusinessCodes(businessCodes);

        if (null != startDate) {
            queryDto.setOperateTimeMin(new Date(startDate));
        }
        if (null != endDate) {
            queryDto.setOperateTimeMax(new Date(endDate));
        }
        Pagenation<ScoreChange> scoreChangePge = scoreChangeRecordBiz.queryScoreChangeForShopAdmin(queryDto, page);
        List<ScoreChange> scoreChanges = scoreChangePge.getResult();
        JSONArray jsonArray = new JSONArray();
        if (ListUtils.isNotEmpty(scoreChanges)) {
            if (ScoreCst.BusinessCode.income.name().equals(businessCodes)) {
                for (ScoreChange scoreChange : scoreChanges) {
                    JSONObject json = new JSONObject();
                    json.put("orderCode", scoreChange.getOrderCode());
                    UserDO userDO = userService.getUserDO(QueryType.UserId, scoreChange.getTheOtherUserId());
                    if (null != userDO) {
                        json.put("userPhone", userDO.getPhone());
                    }
                    json.put("scoreId", scoreChange.getScoreId());
                    json.put("score", scoreChange.getScore());
                    json.put("scoreBalance", scoreChange.getScoreBalance());
                    json.put("freezingScoreBalance", scoreChange.getFreezingScoreBalance());
                    json.put("channelCode", scoreChange.getChannelCode());
                    json.put("shopId", scoreChange.getShopId());
                    json.put("businessCode", scoreChange.getBusinessCode());
                    json.put("flowType", scoreChange.getFlowType());
                    json.put("remark", ObjectUtils.convertVal(scoreChange.getRemark(), ""));
                    json.put("operationTime", scoreChange.getOperationTime());
                    json.put("expirationTime", ObjectUtils.convertVal(scoreChange.getExpirationTime(), 0));
                    jsonArray.add(json);
                }
            } else {
                for (ScoreChange scoreChange : scoreChanges) {
                    JSONObject json = new JSONObject();
                    json.put("orderCode", scoreChange.getOrderCode());
                    UserDO userDO = userService.getUserDO(QueryType.UserId, scoreChange.getUserId());
                    if (null != userDO) {
                        json.put("userPhone", userDO.getPhone());
                    }
                    json.put("scoreId", scoreChange.getScoreId());
                    json.put("score", scoreChange.getScore());
                    json.put("scoreBalance", scoreChange.getScoreBalance());
                    json.put("freezingScoreBalance", scoreChange.getFreezingScoreBalance());
                    json.put("channelCode", scoreChange.getChannelCode());
                    json.put("shopId", scoreChange.getShopId());
                    json.put("businessCode", scoreChange.getBusinessCode());
                    json.put("flowType", scoreChange.getFlowType());
                    json.put("remark", ObjectUtils.convertVal(scoreChange.getRemark(), ""));
                    json.put("operationTime", scoreChange.getOperationTime());
                    json.put("expirationTime", ObjectUtils.convertVal(scoreChange.getExpirationTime(), 0));
                    jsonArray.add(json);
                }
            }
        }
        return null;
    }
}
