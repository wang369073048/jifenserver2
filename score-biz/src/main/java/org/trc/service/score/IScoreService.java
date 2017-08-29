package org.trc.service.score;

import java.util.List;

import org.trc.IBaseService;
import org.trc.domain.score.Score;
import org.trc.util.Pagenation;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/8
 */
public interface IScoreService extends IBaseService<Score,Long>{
	
	int updateScore(Score score);
	
	Score getScoreByUserId(String userId);
	
	int insertScore(Score score);
	
	List<Score> queryBuyerScore(Pagenation<Score> pageRequest);

	List<Score> queryScore(Pagenation<Score> pageRequest);
	
	Integer getScoreCountByType(String userType);

}
