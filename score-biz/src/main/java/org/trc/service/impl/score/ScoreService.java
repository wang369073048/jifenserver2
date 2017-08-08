package org.trc.service.impl.score;

import org.springframework.stereotype.Service;
import org.trc.domain.score.Score;
import org.trc.service.impl.BaseService;
import org.trc.service.score.IScoreService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/8
 */
@Service("scoreService")
public class ScoreService extends BaseService<Score,Long> implements IScoreService{
}
