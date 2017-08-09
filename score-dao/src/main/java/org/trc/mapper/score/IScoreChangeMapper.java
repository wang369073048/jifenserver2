package org.trc.mapper.score;

import org.trc.domain.dto.FlowDTO;
import org.trc.domain.dto.ScoreChangeRecordQueryDTO;
import org.trc.domain.score.ScoreChange;
import org.trc.util.BaseMapper;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/23
 */
public interface IScoreChangeMapper extends BaseMapper<ScoreChange>{

    List<ScoreChange> queryScoreChangeForUser(ScoreChangeRecordQueryDTO queryDto);

    List<ScoreChange> queryScoreChangeForShopAdmin(ScoreChangeRecordQueryDTO queryDto);

    List<ScoreChange> queryScoreChangeForPlatAdmin(ScoreChangeRecordQueryDTO queryDto);

    List<FlowDTO> queryScoreChangeForExport(ScoreChangeRecordQueryDTO queryDto);

    int correctScoreChange(ScoreChange scoreChange);

}
