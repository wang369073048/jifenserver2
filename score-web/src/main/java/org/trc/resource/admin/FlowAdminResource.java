package org.trc.resource.admin;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.service.UserService;
import com.tairanchina.md.api.QueryType;
import com.trc.mall.util.CustomAck;
import com.txframework.core.jdbc.PageRequest;
import com.txframework.util.ListUtils;
import com.txframework.util.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.score.IScoreChangeRecordBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.constants.ScoreCst;
import org.trc.constants.TemporaryContext;
import org.trc.domain.auth.Auth;
import org.trc.domain.dto.ScoreChangeDTO;
import org.trc.domain.dto.ScoreChangeRecordQueryDTO;
import org.trc.domain.score.ScoreChange;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.FlowException;
import org.trc.util.FatherToChildUtils;
import org.trc.util.JSONUtil;
import org.trc.util.Pagenation;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
@Component
@Produces(MediaType.APPLICATION_JSON)
@Path(ScoreAdminConstants.Route.Flow.ROOT)
public class FlowAdminResource {
    @Autowired
    private UserService userService;
    @Autowired
    private IScoreChangeRecordBiz scoreChangeRecordBiz;
    @Autowired
    private IAuthBiz authBiz;
    @GET
    @Path(ScoreAdminConstants.Route.Flow.RECORD)
    public Pagenation<ScoreChange> queryChangeRecord(@QueryParam("shopId") Long shopId,
                                      @QueryParam("userPhone") String userPhone,
                                      @QueryParam("businessCode") String businessCodes,
                                      @QueryParam("startTime") Long startTime,
                                      @QueryParam("endTime") Long endTime,
                                      @BeanParam Pagenation<ScoreChange> page,
                                      @Context ContainerRequestContext requestContext) {
            ScoreChangeRecordQueryDTO queryDto = new ScoreChangeRecordQueryDTO();
            if(StringUtils.isNotBlank(userPhone)) {
                UserDO userDO = userService.getUserDO(QueryType.Phone, userPhone);
                if(null==userDO){
                    throw new FlowException(ExceptionEnum.ORDER_QUERY_EXCEPTION,"手机号不存在");
                }
                queryDto.setUserId(userDO.getUserId());
            }
            if(null!=shopId){
                queryDto.setShopId(shopId);
                Auth auth = authBiz.getAuthByShopId(shopId);
                if(StringUtils.isNotBlank(auth.getExchangeCurrency())){
                    queryDto.setExchangeCurrency(auth.getExchangeCurrency());
                }
            }
            queryDto.calBusinessCodes(businessCodes);
            if (null != startTime) {
                queryDto.setOperateTimeMin(new Date(startTime));
            }
            if (null != endTime) {
                queryDto.setOperateTimeMax(new Date(endTime));
            }
            Pagenation<ScoreChange> scoreChangePage = scoreChangeRecordBiz.queryScoreChangeForPlatAdmin(queryDto, page);
            List<ScoreChange> scoreChanges = scoreChangePage.getResult();
            if (ListUtils.isNotEmpty(scoreChanges)) {
                for(int i = 0; i < scoreChanges.size(); i++){
                    ScoreChange scoreChange = scoreChanges.get(i);
                    ScoreChangeDTO scoreChangeDTO = new ScoreChangeDTO();
                    FatherToChildUtils.fatherToChild(scoreChange,scoreChangeDTO);
                    UserDO userDO = userService.getUserDO(QueryType.UserId, scoreChange.getUserId());
                    if(null!=userDO) {
                        scoreChangeDTO.setUserPhone(userDO.getPhone());
                    }
                    if(null!=scoreChange.getExchangeCurrency()) {
                        ScoreCst.ExchangeCurrency exchangeCurrency = Enum.valueOf(ScoreCst.ExchangeCurrency.class, scoreChange.getExchangeCurrency());
                        scoreChangeDTO.setExchangeCurrency(exchangeCurrency.getValue());
                    }
                    scoreChangeDTO.setShopName(TemporaryContext.getShopNameByExchangeCurrency(scoreChange.getExchangeCurrency()));
                    scoreChanges.set(i,scoreChangeDTO);
                }
            }
            return scoreChangePage;
    }
    //TODO 导出
}
