package org.trc.mapper.score;

import org.trc.domain.score.ScoreChild;
import org.trc.util.BaseMapper;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/10
 */
public interface IScoreChildMapper extends BaseMapper<ScoreChild> {

    int updateScoreChild(ScoreChild scoreChild);
}
