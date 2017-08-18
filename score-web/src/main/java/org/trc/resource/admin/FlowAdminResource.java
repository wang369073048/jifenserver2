package org.trc.resource.admin;

import com.tairanchina.md.account.user.model.UserDO;
import com.tairanchina.md.account.user.service.UserService;
import com.tairanchina.md.api.QueryType;
import com.txframework.util.DateUtils;
import com.txframework.util.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.trc.biz.auth.IAuthBiz;
import org.trc.biz.score.IScoreChangeRecordBiz;
import org.trc.constants.ScoreAdminConstants;
import org.trc.constants.ScoreCst;
import org.trc.constants.TemporaryContext;
import org.trc.domain.auth.Auth;
import org.trc.domain.dto.FlowDTO;
import org.trc.domain.dto.ScoreChangeDTO;
import org.trc.domain.dto.ScoreChangeRecordQueryDTO;
import org.trc.domain.score.ScoreChange;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.FlowException;
import org.trc.util.CellDefinition;
import org.trc.util.ExportExcel;
import org.trc.util.FatherToChildUtils;
import org.trc.util.Pagenation;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
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
            handleQueryDTO(userPhone,queryDto,shopId);
            queryDto.calBusinessCodes(businessCodes);
            if (null != startTime) {
                queryDto.setOperateTimeMin(new Date(startTime));
            }
            if (null != endTime) {
                queryDto.setOperateTimeMax(new Date(endTime));
            }
            Pagenation<ScoreChange> scoreChangePage = scoreChangeRecordBiz.queryScoreChangeForPlatAdmin(queryDto, page);
            List<ScoreChange> scoreChanges = scoreChangePage.getInfos();
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

    /**
     * @param shopId        各业务方
     * @param userPhone     会员手机号
     * @param businessCodes 业务号('exchangeIn','exchangeOut','consume','freeze','expire')
     * @param startTime
     * @param endTime
     * @return
     */
    @GET
    @Path(ScoreAdminConstants.Route.Flow.EXPORT)
    public Response exportChangeRecord(@QueryParam("shopId") Long shopId,
                                       @QueryParam("userPhone") String userPhone,
                                       @QueryParam("businessCode") String businessCodes,
                                       @QueryParam("startTime") Long startTime,
                                       @QueryParam("endTime") Long endTime) throws Exception{
        ScoreChangeRecordQueryDTO queryDto = new ScoreChangeRecordQueryDTO();
        handleQueryDTO(userPhone,queryDto,shopId);
        queryDto.calBusinessCodes(businessCodes);
        if (null != startTime) {
            queryDto.setOperateTimeMin(new Date(startTime));
        }
        if (null != endTime) {
            queryDto.setOperateTimeMax(new Date(endTime));
        }
        List<FlowDTO> flowDTOList = scoreChangeRecordBiz.queryScoreChangeForExport(queryDto);
        //导出文件
        CellDefinition orderDate = new CellDefinition("serialDate", "日期", CellDefinition.DATE, 4000);
        CellDefinition serialNum = new CellDefinition("serialNum", "订单编号", CellDefinition.TEXT, 8000);
        CellDefinition shopName = new CellDefinition("shopName", "店铺名称", CellDefinition.TEXT, 4000);
        CellDefinition exchangeCurrency = new CellDefinition("exchangeCurrency", "兑出币种", CellDefinition.TEXT, 4000);
        CellDefinition foreignCurrency = new CellDefinition("foreignCurrency", "兑出数量", CellDefinition.NUM_0, 4000);
        CellDefinition score = new CellDefinition("score", "兑入积分数量", CellDefinition.NUM_0, 4000);
        CellDefinition phone = new CellDefinition("phone", "会员手机号", CellDefinition.TEXT, 4000);
        List<CellDefinition> cellDefinitionList = new ArrayList<>();
        cellDefinitionList.add(orderDate);
        cellDefinitionList.add(serialNum);
        cellDefinitionList.add(shopName);
        cellDefinitionList.add(exchangeCurrency);
        cellDefinitionList.add(foreignCurrency);
        cellDefinitionList.add(score);
        cellDefinitionList.add(phone);
        String sheetName = "兑入结算";
        String fileName = "兑入结算-" + DateUtils.formatDate(queryDto.getOperateTimeMin(), DateUtils.DATE_PATTERN) + "-" + DateUtils.formatDate(queryDto.getOperateTimeMax(), DateUtils.DATE_PATTERN) + ".xls";
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        HSSFWorkbook hssfWorkbook = ExportExcel.generateExcel(flowDTOList, cellDefinitionList, sheetName);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        hssfWorkbook.write(stream);
        /**模拟数据结束*/
        return Response.ok(stream.toByteArray()).header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8'zh_cn'" + fileName).type(MediaType.APPLICATION_OCTET_STREAM)
                .header("Cache-Control", "no-cache").build();

    }

    private void handleQueryDTO(String userPhone,ScoreChangeRecordQueryDTO queryDto,Long shopId){
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
    }
}
