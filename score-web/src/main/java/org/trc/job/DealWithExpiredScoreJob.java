package org.trc.job;


import com.trc.mall.util.GuidUtil;
import com.trc.mall.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trc.domain.score.Score;
import org.trc.domain.score.ScoreChange;
import org.trc.domain.score.ScoreChangeDetail;
import org.trc.domain.score.ScoreChild;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.ExpireException;
import org.trc.mapper.score.IScoreChangeMapper;

import org.trc.service.score.IScoreChangeDetailService;
import org.trc.service.score.IScoreChildService;
import org.trc.service.score.IScoreService;

import javax.annotation.Resource;
import java.net.SocketException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by george on 2016/12/5.
 */
public class DealWithExpiredScoreJob extends BaseJob{

    private Logger logger = LoggerFactory.getLogger(DealWithExpiredScoreJob.class);

    @Resource
    private IScoreService scoreService;

    @Resource
    private IScoreChildService scoreChildService;

    @Resource
    private IScoreChangeMapper scoreChangeMapper;

    @Resource
    private IScoreChangeDetailService scoreChangeDetailService;

    /**
     * @description 执行任务
     */
    @Transactional(propagation= Propagation.REQUIRED, rollbackFor=Exception.class)
    public void execute() {
        try {
            String realIp = IpUtil.getRealIp();
            if(!realIp.equals(this.getTaskIp())){
                logger.info("非任务补偿机，跳过任务!");
                return ;
            }
        } catch (SocketException e) {
            e.printStackTrace();
            logger.error("DealWithExpiredScoreJob获取Ip失败!跳过任务!");
            return ;
        }
        //当前没有冻结逻辑
        //1、获取过期时间
        Calendar now = Calendar.getInstance();
        now.set(Calendar.HOUR_OF_DAY,0);
        now.set(Calendar.MINUTE,0);
        now.set(Calendar.SECOND,0);
        now.set(Calendar.MILLISECOND,0);
        Date expirationTime = now.getTime();
        logger.info("--DealWithExpiredScoreJob start--:"+expirationTime);
        //2、查找过期子账户
        int total = scoreChildService.getCountOfScoreChildOutOfDate(expirationTime);
        logger.info("--DealWithExpiredScoreJob 待处理积分子账户个数:"+total);
        for(int i = 0 ; i < total/1000 + 1; i++ ) {
            List<ScoreChild> scoreChildList = scoreChildService.selectScoreChildOutOfDate(expirationTime);
            for (ScoreChild scoreChild : scoreChildList) {
                if (scoreChild.getScore() > 0) {
                    try{
                        expireScore(scoreChild,expirationTime);
                    }catch (Exception e){
                        break;
                    }
                }
            }
        }
        logger.info("--DealWithExpiredScoreJob end--:"+expirationTime);
    }

    @Transactional(propagation= Propagation.REQUIRED,rollbackFor=Exception.class)
    private void expireScore(ScoreChild scoreChild, Date expirationTime){
        Score score = scoreService.selectByPrimaryKey(scoreChild.getScoreId());
        score.setScore(score.getScore() - scoreChild.getScore());
        score.setUpdateTime(expirationTime);
        int result = scoreService.updateScore(score);
        if(result != 1){
            logger.error("更新score:"+scoreChild.getScoreId()+"过期积分失败!");
            throw new ExpireException(ExceptionEnum.UPDATE_EXCEPTION,"积分账户过期更新语句执行失败:对应的积分账户id为"+scoreChild.getScoreId());
        }
        ScoreChange scoreChange = new ScoreChange(score.getUserId(), null, null, null,score.getId(),scoreChild.getScore(),score.getScore(),score.getFreezingScore(), GuidUtil.getNextUid("expire"),
                null, "score","expire","expire","积分过期",null, expirationTime,expirationTime);
        scoreChangeMapper.insertScoreChange(scoreChange);
        ScoreChangeDetail scoreChangeDetail = new ScoreChangeDetail(score.getUserId(), GuidUtil.getNextUid("expire"),score.getId(),scoreChild.getId(),scoreChild.getScore(),score.getScore(),
                score.getFreezingScore(),"expire",expirationTime,expirationTime);
        scoreChangeDetailService.insertScoreChangeDetail(scoreChangeDetail);
    }


}
