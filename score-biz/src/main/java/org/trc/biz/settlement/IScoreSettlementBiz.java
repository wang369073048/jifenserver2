package org.trc.biz.settlement;

import org.trc.domain.score.ScoreSettlement;

/**
 * 
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年8月21日 下午8:12:41
 */
public interface IScoreSettlementBiz {

    ScoreSettlement getScoreSettlementByUserIdAndAccountDay(String userId, String accountDay);

    int insert(ScoreSettlement scoreSettlement);

}
