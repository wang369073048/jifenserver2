package org.trc.service.impl.score;

import org.springframework.stereotype.Service;
import org.trc.domain.score.ScoreChangeDetail;
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
}
