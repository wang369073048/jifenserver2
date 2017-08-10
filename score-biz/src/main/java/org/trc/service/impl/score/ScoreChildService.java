package org.trc.service.impl.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.score.ScoreChild;
import org.trc.mapper.score.IScoreChildMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.score.IScoreChildService;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/10
 */
@Service("scoreChildService")
public class ScoreChildService extends BaseService<ScoreChild,Long> implements IScoreChildService{

    @Autowired
    private IScoreChildMapper scoreChildMapper;
    @Override
    public int insertScoreChild(ScoreChild scoreChild) {
        return 0;
    }

    @Override
    public int updateScoreChild(ScoreChild scoreChild) {
        return scoreChildMapper.updateScoreChild(scoreChild);
    }

    @Override
    public ScoreChild getFirstScoreChildByUserId(String userId) {
        return null;
    }

    @Override
    public ScoreChild getLastScoreChildByUserId(String userId) {
        return null;
    }

    @Override
    public ScoreChild getScoreChildByUserIdAndExpirationTime(Map params) {
        return null;
    }

    @Override
    public List<ScoreChild> queryScoreChildByUserId(String userId) {
        return scoreChildMapper.queryScoreChildByUserId(userId);
    }

    @Override
    public List<ScoreChild> selectScoreChildOutOfDate(Date expirationTime) {
        return null;
    }

    @Override
    public int getCountOfScoreChildOutOfDate(Date expirationTime) {
        return 0;
    }

    @Override
    public Long getScoreCount(ScoreChild scoreChild) {
        return null;
    }
}
