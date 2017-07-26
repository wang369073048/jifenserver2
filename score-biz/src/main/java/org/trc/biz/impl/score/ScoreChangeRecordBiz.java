package org.trc.biz.impl.score;

import com.txframework.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.biz.score.IScoreChangeRecordBiz;
import org.trc.constants.ScoreCst;
import org.trc.domain.dto.FlowDTO;
import org.trc.domain.dto.ScoreChangeRecordQueryDTO;
import org.trc.domain.score.ScoreChange;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.ScoreChangeRecordException;
import org.trc.service.score.IScoreChangeRecordService;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/13
 */
@Service("scoreChangeRecordBiz")
public class ScoreChangeRecordBiz implements IScoreChangeRecordBiz{

    Logger logger = LoggerFactory.getLogger(ScoreConverterBiz.class);
    @Autowired
    private IScoreChangeRecordService scoreChangeRecordService;
    @Override
    public int getTotalAmountByCurrency(String exchangeCurrency, ScoreCst.BusinessCode businessCode) {
        return 0;
    }

    @Override
    public int getTotalAmountByCurrencyAndUserId(String exchangeCurrency, String userId, ScoreCst.BusinessCode businessCode) {
        return 0;
    }

    @Override
    public Pagenation<ScoreChange> queryScoreChangeForUser(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pageRequest) {
        return null;
    }

    @Override
    public Pagenation<ScoreChange> queryScoreChangeForShopAdmin(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pageRequest) {
        try {
            Assert.notNull(pageRequest, "分页参数不能为空");
            Assert.notNull(queryDto, "查询参数不能为空");
            return scoreChangeRecordService.queryScoreChangeForShopAdmin(queryDto, pageRequest);
        } catch (IllegalArgumentException e){
            logger.error("查询积分流水校验参数异常!", e);
            throw new ScoreChangeRecordException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "查询积分流水校验参数异常!");
        } catch (Exception e) {
            logger.error("查询积分流水失败!", e);
            throw new ScoreChangeRecordException(ExceptionEnum.SCORECHANGGE_QUERY_EXCEPTION, "查询积分流水失败!");
        }
    }

    @Override
    public Pagenation<ScoreChange> queryScoreChangeForPlatAdmin(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pageRequest) {
        try {
            Assert.notNull(pageRequest, "分页参数不能为空");
            Assert.notNull(queryDto, "查询参数不能为空");
            return scoreChangeRecordService.queryScoreChangeForPlatAdmin(queryDto, pageRequest);
        } catch (IllegalArgumentException e){
            logger.error("查询积分流水校验参数异常!", e);
            throw new ScoreChangeRecordException(ExceptionEnum.PARAM_CHECK_EXCEPTION, "查询积分流水校验参数异常!");
        } catch (Exception e) {
            logger.error("查询积分流水失败!", e);
            throw new ScoreChangeRecordException(ExceptionEnum.SCORECHANGGE_QUERY_EXCEPTION, "查询积分流水失败!");
        }
    }

    @Override
    public List<FlowDTO> queryScoreChangeForExport(ScoreChangeRecordQueryDTO queryDto) {
        return null;
    }
}
