package org.trc.biz.score;

import com.txframework.core.jdbc.PageRequest;
import org.trc.domain.dto.ScoreAck;
import org.trc.domain.dto.ScoreReq;
import org.trc.domain.score.Score;

import java.util.Date;
import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/8
 */
public interface IScoreBiz {
	
	Score getScoreByUserId2(String userId);

    Score getScoreByUserId(String userId);

    ScoreAck produceScore(ScoreReq scoreReq);

    ScoreAck deductScore(ScoreReq scoreReq);

    ScoreAck consumeScore(ScoreReq scoreReq);

    ScoreAck lotteryConsumeScore(ScoreReq scoreReq);

    ScoreAck consumeCorrectScore(ScoreReq scoreReq);

    //ScoreAck exchangeOut(ScoreReq scoreReq);

    List<Score> queryBuyerScore(PageRequest<Score> pageRequest);

    ScoreAck returnScore(String orderNum, Date returnTime);

}
