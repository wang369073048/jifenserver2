package org.trc.service.impl.score;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.score.Score;
import org.trc.mapper.score.IScoreMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.score.IScoreService;
import org.trc.util.Pagenation;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/8
 */
@Service("scoreService")
public class ScoreService extends BaseService<Score,Long> implements IScoreService{

	@Autowired
	public IScoreMapper scoreMapper;
	
	@Override
	public int updateScore(Score score) {
		return scoreMapper.updateScore(score);
	}

	@Override
	public Score getScoreByUserId(String userId) {
		return scoreMapper.getScoreByUserId(userId);
	}

	@Override
	public int insertScore(Score score) {
		return scoreMapper.insertScore(score);
	}

	@Override
	public Integer getScoreCountByType(String userType) {
		return scoreMapper.getScoreCountByType(userType);
	}

	@Override
	public List<Score> queryBuyerScore(Pagenation<Score> pageRequest) {
		return scoreMapper.queryBuyerScore(pageRequest);
	}

	@Override
	public List<Score> queryScore(Pagenation<Score> pageRequest) {
		return scoreMapper.queryScore(pageRequest);
	}
}
