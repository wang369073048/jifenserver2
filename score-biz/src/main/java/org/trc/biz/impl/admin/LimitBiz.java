package org.trc.biz.impl.admin;

import com.txframework.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.trc.biz.admin.ILimitBiz;
import org.trc.biz.score.IScoreChangeRecordBiz;
import org.trc.constants.ScoreCst;
import org.trc.domain.score.ScoreConverter;
import org.trc.service.score.IScoreConverterService;
import org.trc.util.RedisUtil;

import javax.annotation.Resource;
import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/29
 */
@Service("limitBiz")
public class LimitBiz implements ILimitBiz{
    private Logger logger = LoggerFactory.getLogger(LimitBiz.class);

    @Resource
    private IScoreConverterService scoreConverterService;

    @Resource
    private IScoreChangeRecordBiz scoreChangeRecordBiz;

    public static final String PLAT_PRE = "plat_";

    public static final String PERSONAL_PRE = "personal_";

    public static final String IN = "in_";

    public static final String OUT ="out_";

    public String getKeySuffix(Date time){
        return DateUtils.format(time, DateUtils.DATE_PATTERN);
    }

    private void initLimitInAmount(String exchangeCurrency, Date now){
        String limitInKey = PLAT_PRE + IN + exchangeCurrency + this.getKeySuffix(now);
        if(!RedisUtil.exists(limitInKey)){
            ScoreConverter scoreConverter = new ScoreConverter();
            scoreConverter.setIsDeleted(0);
            scoreConverter.setExchangeCurrency(exchangeCurrency);
            scoreConverter = scoreConverterService.selectOne(scoreConverter);
            //兑换规则不为空!
            if(null!=scoreConverter){
                //不允许兑入
                if(scoreConverter.getDirection().equals(ScoreCst.Direction.exitOnly)) {
                    RedisUtil.set(limitInKey, String.valueOf(0));
                }
                //允许兑入,设置兑入限额
                else if(null!=scoreConverter.getChannelEverydayInLimit()){
                    int totalAmount = scoreChangeRecordBiz.getTotalAmountByCurrency(exchangeCurrency, ScoreCst.BusinessCode.exchangeIn);
                    RedisUtil.set(limitInKey, String.valueOf(scoreConverter.getChannelEverydayInLimit() - totalAmount));
                }
            }
        }
    }

    private void initLimitOutAmount(String exchangeCurrency, Date now){
        String limitOutKey = PLAT_PRE + OUT + exchangeCurrency + this.getKeySuffix(now);
        if(!RedisUtil.exists(limitOutKey)){
            ScoreConverter scoreConverter = new ScoreConverter();
            scoreConverter.setIsDeleted(0);
            scoreConverter.setExchangeCurrency(exchangeCurrency);
            scoreConverter = scoreConverterService.selectOne(scoreConverter);
            //兑换规则不为空!
            if(null!=scoreConverter){
                //不允许兑出
                if(scoreConverter.getDirection().equals(ScoreCst.Direction.entranceOnly)) {
                    RedisUtil.set(limitOutKey, String.valueOf(0));
                }
                //允许兑出,设置兑出限额
                else if(null!=scoreConverter.getChannelEverydayOutLimit()){
                    int totalAmount = scoreChangeRecordBiz.getTotalAmountByCurrency(exchangeCurrency, ScoreCst.BusinessCode.exchangeOut);
                    RedisUtil.set(limitOutKey, String.valueOf(scoreConverter.getChannelEverydayOutLimit()-totalAmount));
                }
            }
        }
    }

    @Override
    public boolean enoughLimitInAmount(String exchangeCurrency, String orderCode, int amount, Date now) {
        initLimitInAmount(exchangeCurrency, now);
        String limitInKey = PLAT_PRE + IN + exchangeCurrency + this.getKeySuffix(now);
        Long remainderAmount = RedisUtil.decrBy(limitInKey, amount);
        logger.info( "扣款后额度为：" + remainderAmount);
        if(remainderAmount<0){
            logger.warn(limitInKey+"平台兑入额度不充足！");
            return false;
        }
        logger.info(limitInKey+"积分兑入额度扣减成功，单据号:"+orderCode+",额度:"+amount);
        return true;
    }

    @Override
    public boolean rollbackLimitInAmount(String limitInKey, String orderCode, int amount) {
        if(!RedisUtil.exists(limitInKey)){
            logger.error(limitInKey+"积分兑入额度获取失败!");
            return false;
        }
        Long remainderAmount = RedisUtil.incrBy(limitInKey, amount);
        logger.warn("回滚后"+limitInKey+"积分兑入额度为:"+remainderAmount+",单据号:"+orderCode+",额度:"+amount);
        return true;
    }

    @Override
    public boolean enoughLimitOutAmount(String exchangeCurrency, String orderCode, int amount, Date now) {
        initLimitOutAmount(exchangeCurrency, now);
        String limitOutKey = PLAT_PRE + OUT + exchangeCurrency + this.getKeySuffix(now);
        Long remainderAmount = RedisUtil.decrBy(limitOutKey, amount);
        logger.info( "扣款后额度为：" + remainderAmount);
        if(remainderAmount<0){
            logger.warn(limitOutKey+"平台兑出额度不充足！");
            return false;
        }
        logger.info(limitOutKey+"积分兑出额度扣减成功，单据号:"+orderCode+",额度:"+amount);
        return true;
    }

    @Override
    public boolean rollbackLimitOutAmount(String limitOutKey, String orderCode, int amount) {
        if(!RedisUtil.exists(limitOutKey)){
            logger.error(limitOutKey+"积分兑出额度获取失败!");
            return false;
        }
        Long remainderAmount = RedisUtil.incrBy(limitOutKey, amount);
        logger.warn("回滚后"+limitOutKey+"积分兑出额度为:"+remainderAmount+",单据号:"+orderCode+",额度:"+amount);
        return true;
    }

    private void initPersonalLimitInAmount(String exchangeCurrency, String userId, Date now){
        String personalLimitInKey = PERSONAL_PRE + userId + IN + exchangeCurrency + this.getKeySuffix(now);
        if(!RedisUtil.exists(personalLimitInKey)){
            ScoreConverter scoreConverter = new ScoreConverter();
            scoreConverter.setIsDeleted(0);
            scoreConverter.setExchangeCurrency(exchangeCurrency);
            scoreConverter = scoreConverterService.selectOne(scoreConverter);
            //兑换规则不为空!
            if(null!=scoreConverter){
                //不允许兑入
                if(scoreConverter.getDirection().equals(ScoreCst.Direction.exitOnly)) {
                    RedisUtil.set(personalLimitInKey, String.valueOf(0));
                }
                //允许兑入,设置兑入限额
                else if(null!=scoreConverter.getPersonEverydayInLimit()){
                    int totalAmount = scoreChangeRecordBiz.getTotalAmountByCurrencyAndUserId(exchangeCurrency, userId, ScoreCst.BusinessCode.exchangeIn);
                    RedisUtil.set(personalLimitInKey, String.valueOf(scoreConverter.getPersonEverydayInLimit()-totalAmount));
                }
            }
        }
    }

    private void initPersonalLimitOutAmount(String exchangeCurrency, String userId, Date now){
        String personalLimitOutKey = PERSONAL_PRE + userId + OUT + exchangeCurrency + this.getKeySuffix(now);
        if(!RedisUtil.exists(personalLimitOutKey)){
            ScoreConverter scoreConverter = new ScoreConverter();
            scoreConverter.setIsDeleted(0);
            scoreConverter.setExchangeCurrency(exchangeCurrency);
            scoreConverter = scoreConverterService.selectOne(scoreConverter);
            //兑换规则不为空!
            if(null!=scoreConverter){
                //不允许兑出
                if(scoreConverter.getDirection().equals(ScoreCst.Direction.entranceOnly)) {
                    RedisUtil.set(personalLimitOutKey, String.valueOf(0));
                }
                //允许兑出,设置兑出限额
                else if(null!=scoreConverter.getPersonEverydayOutLimit()){
                    int totalAmount = scoreChangeRecordBiz.getTotalAmountByCurrencyAndUserId(exchangeCurrency, userId, ScoreCst.BusinessCode.exchangeOut);
                    RedisUtil.set(personalLimitOutKey, String.valueOf(scoreConverter.getPersonEverydayOutLimit()-totalAmount));
                }
            }
        }
    }

    @Override
    public boolean enoughPersonalLimitInAmount(String exchangeCurrency, String userId, String orderCode, int amount, Date now) {
        initPersonalLimitInAmount(exchangeCurrency, userId, now);
        String personalLimitInKey = PERSONAL_PRE + userId + IN + exchangeCurrency + this.getKeySuffix(now);
        Long remainderAmount = RedisUtil.decrBy(personalLimitInKey, amount);
        logger.info( "扣款后个人限制额度为：" + remainderAmount);
        if(remainderAmount<0){
            logger.warn(personalLimitInKey+"个人兑入额度不充足！"+userId);
            return false;
        }
        logger.info(personalLimitInKey+"积分兑入额度扣减成功，单据号:"+orderCode+",额度:"+amount);
        return true;
    }

    @Override
    public boolean rollbackPersonalLimitInAmount(String personalLimitInKey, String userId, String orderCode, int amount) {
        if(!RedisUtil.exists(personalLimitInKey)){
            logger.error(personalLimitInKey+"积分兑入额度获取失败!");
            return false;
        }
        Long remainderAmount = RedisUtil.incrBy(personalLimitInKey, amount);
        logger.warn("回滚后"+personalLimitInKey+"积分兑入额度为:"+remainderAmount+",单据号:"+orderCode+",额度:"+amount);
        return true;
    }

    @Override
    public boolean enoughPersonalLimitOutAmount(String exchangeCurrency, String userId, String orderCode, int amount, Date now) {
        initPersonalLimitOutAmount(exchangeCurrency, userId, now);
        String personalLimitOutKey = PERSONAL_PRE + userId + OUT + exchangeCurrency + this.getKeySuffix(now);
        Long remainderAmount = RedisUtil.decrBy(personalLimitOutKey, amount);
        logger.info( "扣款后额度为：" + remainderAmount);
        if(remainderAmount<0){
            logger.warn(personalLimitOutKey+"平台兑出额度不充足！");
            return false;
        }
        logger.info(personalLimitOutKey+"积分兑出额度扣减成功，单据号:"+orderCode+",额度:"+amount);
        return true;
    }

    @Override
    public boolean rollbackPersonalLimitOutAmount(String personalLimitOutKey, String userId, String orderCode, int amount) {
        if(!RedisUtil.exists(personalLimitOutKey)){
            logger.error(personalLimitOutKey+"积分兑出额度获取失败!");
            return false;
        }
        Long remainderAmount = RedisUtil.incrBy(personalLimitOutKey, amount);
        logger.warn("回滚后"+personalLimitOutKey+"积分兑出额度为:"+remainderAmount+",单据号:"+orderCode+",额度:"+amount);
        return true;
    }
}
