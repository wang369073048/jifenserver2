package org.trc.resource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tairanchina.constants.CommonConstants;
import com.tairanchina.context.TxJerseyRestContextFactory;
import com.tairanchina.engine.jersey.TxJerseyTools;
import com.trc.mall.base.BaseResource;
import com.trc.mall.constants.ScoreConstants;
import com.trc.mall.dto.ScoreChangeRecordQueryDto;
import com.trc.mall.exception.ScoreChangeRecordException;
import com.trc.mall.interceptor.Logined;
import com.trc.mall.model.ScoreChange;
import com.trc.mall.provider.ScoreProvider;
import com.trc.mall.util.CustomAck;
import com.trc.mall.util.JSONUtil;
import com.txframework.core.jdbc.PageRequest;
import com.txframework.util.ListUtils;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * 积分流水
 * Created by huyan on 2016/11/21.
 */
@Path(ScoreConstants.Route.Flow.ROOT)
@Produces(MediaType.APPLICATION_JSON)
public class FlowResource extends BaseResource {

    private Logger logger = LoggerFactory.getLogger(FlowResource.class);

    /**
     * 根据用户查询积分流水
     *
     * @param
     * @return
     */
    @GET
    @Logined
    public Response getFlowList(@NotEmpty @QueryParam("businessCodes") String businessCodes,
                                @QueryParam("startDate") Long startDate,
                                @QueryParam("endDate") Long endDate,
                                @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGEINDEX_STR) @QueryParam("pageIndex") String pageIndex,
                                @DefaultValue(ScoreConstants.BaseParam.DEFAULT_PAGESIZE_STR) @QueryParam("pageSize") String pageSize) {
        try {
            JSONObject jsonObject = new JSONObject();
            ScoreChangeRecordQueryDto queryDto = new ScoreChangeRecordQueryDto();
            queryDto.calBusinessCodes(businessCodes);
            if(null!=startDate) {
                queryDto.setOperateTimeMin(new Date(startDate));
            }
            if(null!=endDate) {
                queryDto.setOperateTimeMax(new Date(endDate));
            }
            String userId = TxJerseyRestContextFactory.getInstance().getUserId();
            queryDto.setUserId(userId);

            PageRequest<ScoreChange> pageRequest = new PageRequest<>();
            pageRequest.setCurPage(TxJerseyTools.paramsToInteger(pageIndex));
            pageRequest.setPageData(TxJerseyTools.paramsToInteger(pageSize));

            PageRequest<ScoreChange> scoreChangePage = ScoreProvider.scoreChangeRecordService.queryScoreChangeForUser(queryDto, pageRequest);
            List<ScoreChange> scoreChanges = scoreChangePage.getDataList();
            JSONArray jsonArray = new JSONArray();
            if (ListUtils.isNotEmpty(scoreChanges)) {
                for (ScoreChange scoreChange : scoreChanges) {
                    JSONObject json = new JSONObject();
                    json.put("score", scoreChange.getScore());
                    json.put("scoreBalance", scoreChange.getScoreBalance());
                    json.put("freezingScoreBalance", scoreChange.getFreezingScoreBalance());
                    json.put("channelCode", scoreChange.getChannelCode());
                    json.put("businessCode", scoreChange.getBusinessCode());
                    json.put("flowType", scoreChange.getFlowType());
                    json.put("remark", scoreChange.getRemark());
                    json.put("operationTime", scoreChange.getOperationTime().getTime());
                    jsonArray.add(json);
                }
            }
            JSONUtil.putParam(jsonArray, scoreChangePage, jsonObject);
            return TxJerseyTools.returnSuccess(jsonObject.toJSONString());
        } catch (ScoreChangeRecordException e){
            return CustomAck.customError(e.getMessage());
        } catch (Exception e) {
            logger.error("=====>getFlowList exception", e);
            return TxJerseyTools.returnAbort(CommonConstants.ErrorCode.ERROR_SERVICE_IN_REST);
        }
    }

}
