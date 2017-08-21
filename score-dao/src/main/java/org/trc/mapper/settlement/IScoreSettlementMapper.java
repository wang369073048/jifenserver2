package org.trc.mapper.settlement;

import java.util.Map;

import org.trc.domain.score.ScoreSettlement;
import org.trc.util.BaseMapper;

/**
 * 
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年8月21日 下午7:51:35
 */
public interface IScoreSettlementMapper extends BaseMapper<ScoreSettlement>{

    ScoreSettlement getScoreSettlementByUserIdAndAccountDay(Map params);

    ScoreSettlement getLastScoreSettlement(Long scoreId);

    int insert(ScoreSettlement scoreSettlement);

}
