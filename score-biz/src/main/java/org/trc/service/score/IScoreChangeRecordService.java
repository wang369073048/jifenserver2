package org.trc.service.score;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.dto.FlowDTO;
import org.trc.domain.dto.ScoreChangeRecordQueryDTO;
import org.trc.domain.dto.ScoreChangeRecordsDTO;
import org.trc.domain.score.ScoreChange;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/23
 */
public interface IScoreChangeRecordService extends IBaseService<ScoreChange,Long>{

    int getTotalAmount(ScoreChangeRecordQueryDTO scoreChangeRecordQueryDto);

    ScoreChange getLastScoreChange(Map<String,Object> params);

    int correctScoreChange(ScoreChange scoreChange);

    ScoreChange getScoreChangeById(Long id);

    ScoreChange selectByParams(ScoreChange scoreChange);

    int insertScoreChange(ScoreChange scoreChange);

    Pagenation<ScoreChange> queryScoreChangeForUser(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pageRequest);

    Pagenation<ScoreChange> queryScoreChangeForShopAdmin(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pageRequest);

    Pagenation<ScoreChange> queryScoreChangeForPlatAdmin(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pageRequest);

    List<FlowDTO> queryScoreChangeForExport(ScoreChangeRecordQueryDTO queryDto);
    
    /**
     * @Description add by xab 用户积分流水明细报表
     * @param queryDto
     * @return
     */
    Pagenation<ScoreChangeRecordsDTO> queryScoreChangeRecordsForUser(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChangeRecordsDTO> pagenation);
    /**
     * @Description add by xab 用户积分流水明细报表-Excel导出
     * @param queryDto
     * @return
     */
    List<ScoreChangeRecordsDTO> queryScoreChangeRecordsForUserExport(ScoreChangeRecordQueryDTO queryDto);
}
