package org.trc.mapper.score;

import java.util.List;
import java.util.Map;

import org.trc.domain.dto.FlowDTO;
import org.trc.domain.dto.ScoreChangeRecordQueryDTO;
import org.trc.domain.dto.ScoreChangeRecordsDTO;
import org.trc.domain.score.ScoreChange;
import org.trc.util.BaseMapper;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/23
 */
public interface IScoreChangeMapper extends BaseMapper<ScoreChange>{
	
	int insertScoreChange(ScoreChange scoreChange);

    List<ScoreChange> queryScoreChangeForUser(ScoreChangeRecordQueryDTO queryDto);

    List<ScoreChange> queryScoreChangeForShopAdmin(ScoreChangeRecordQueryDTO queryDto);

    List<ScoreChange> queryScoreChangeForPlatAdmin(ScoreChangeRecordQueryDTO queryDto);

    List<FlowDTO> queryScoreChangeForExport(ScoreChangeRecordQueryDTO queryDto);

    int correctScoreChange(ScoreChange scoreChange);
    
    /**
     * 
     * @Description add by xab 用户积分流水明细报表
     * @param queryDto
     * @return
     */
    List<ScoreChangeRecordsDTO> queryScoreChangeRecordsForUser(ScoreChangeRecordQueryDTO queryDto);

    int getTotalAmount(ScoreChangeRecordQueryDTO scoreChangeRecordQueryDto);

    ScoreChange getLastScoreChange(Map<String,Object> params);
}
