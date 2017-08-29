package org.trc.mapper.score;

import java.util.List;

import org.trc.domain.score.Score;
import org.trc.util.BaseMapper;
import org.trc.util.Pagenation;

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
	
	List<Score> queryBuyerScore(Pagenation<Score> pageRequest);
	
	Integer getScoreCountByType(String userType);

	List<Score> queryScore(Pagenation<Score> pageRequest);
}
