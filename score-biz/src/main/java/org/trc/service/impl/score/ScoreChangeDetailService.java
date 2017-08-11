package org.trc.service.impl.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.score.ScoreChangeDetail;
import org.trc.mapper.score.IScoreChangeDetailMapper;
import org.trc.mapper.score.IScoreChangeMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.score.IScoreChangeDetailService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/10
 */
@Service("scoreChangeDetailService")
public class ScoreChangeDetailService extends BaseService<ScoreChangeDetail,Long> implements IScoreChangeDetailService{
	@Autowired
    private IScoreChangeDetailMapper scoreChangeDetailMapper;
	
	@Override
	public int insertScoreChangeDetail(ScoreChangeDetail scoreChangeDetail) {
		return scoreChangeDetailMapper.insertScoreChangeDetail(scoreChangeDetail);
	}
}
