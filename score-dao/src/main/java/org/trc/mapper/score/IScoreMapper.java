package org.trc.mapper.score;

import org.trc.domain.score.Score;
import org.trc.util.BaseMapper;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/8
 */
public interface IScoreMapper extends BaseMapper<Score> {
	int updateScore(Score score);
	
	Score getScoreByUserId(String userId);
	
	int insertScore(Score score);
}
