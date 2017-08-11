package org.trc.mapper.score;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
    
    int insertScoreChild(ScoreChild scoreChild);

    ScoreChild getFirstScoreChildByUserId(String userId);

    ScoreChild getLastScoreChildByUserId(String userId);

    ScoreChild getScoreChildByUserIdAndExpirationTime(Map params);

    //默认按过期时间升序排列
    List<ScoreChild> queryScoreChildByUserId(String userId);

    //默认查找当前过期的积分子账户按子账户创建时间顺序获取一次1000
    List<ScoreChild> selectScoreChildOutOfDate(Date expirationTime);

    //默认查找当前时间未处理过期积分子账户总数
    int getCountOfScoreChildOutOfDate(Date expirationTime);
    //获取积分总数量
    Long getScoreCount(ScoreChild scoreChild);
}
