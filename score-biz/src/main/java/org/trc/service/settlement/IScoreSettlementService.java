package org.trc.service.settlement;

import java.util.Map;

import org.trc.IBaseService;
import org.trc.domain.score.ScoreSettlement;

/**
 * 
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年8月21日 下午7:51:35
 */
public interface IScoreSettlementService extends IBaseService<ScoreSettlement,Long>{

    ScoreSettlement getScoreSettlementByUserIdAndAccountDay(Map params);

    ScoreSettlement getLastScoreSettlement(Long scoreId);

    int insert(ScoreSettlement scoreSettlement);

}
