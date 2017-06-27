package org.trc.service.impl.score;

import org.springframework.stereotype.Service;
import org.trc.domain.score.ScoreConverter;
import org.trc.service.impl.BaseService;
import org.trc.service.score.IScoreConverterService;

/**
 * Created by hzwzhen on 2017/6/14.
 */
@Service("scoreConverterService")
public class ScoreConverterService extends BaseService<ScoreConverter,Long> implements IScoreConverterService{
}
