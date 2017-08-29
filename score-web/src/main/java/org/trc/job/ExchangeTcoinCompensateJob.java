package org.trc.job;


import com.trc.mall.util.IpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.admin.IRequestFlowBiz;
import org.trc.domain.admin.RequestFlow;
import org.trc.operation.ExchangeTcoin;
import org.trc.operation.GainLotteryScore;

import javax.annotation.Resource;
import java.net.SocketException;
import java.util.List;

/**
 * Created by george on 2017/7/19.
 */
public class ExchangeTcoinCompensateJob extends BaseJob{

    private Logger logger = LoggerFactory.getLogger(ExchangeTcoinCompensateJob.class);

    @Resource
    private IRequestFlowBiz requestFlowBiz;

    @Resource
    private ExchangeTcoin exchangeTcoin;

    @Resource
    private GainLotteryScore gainLotteryScore;

    private boolean _checkIp(){
        //ip检查，补偿任务暂时单机进行
        try {
            String realIp = IpUtil.getRealIp();
            if(!realIp.equals(this.getTaskIp())){
                logger.info("非任务补偿机，跳过任务!"+realIp+"!="+this.getTaskIp());
                return false;
            }
            return true;
        } catch (SocketException e) {
            e.printStackTrace();
            logger.error("ExchangeTcoinCompensateJob!跳过任务!");
            return false;
        }
    }

    @Transactional
    public void exchangeTcoin(){
        if(!_checkIp()){
            return ;
        }
        List<RequestFlow> requestFlowList = requestFlowBiz.listRequestFlowForExchangeTcoinCompensate();
        for(RequestFlow item : requestFlowList){
            exchangeTcoin.execute(item);
        }
    }

    @Transactional
    public void gainLotteryScore(){
        if(!_checkIp()){
            return ;
        }
        List<RequestFlow> requestFlowList = requestFlowBiz.listRequestFlowForGainLotteryScoreCompensate();
        for(RequestFlow item : requestFlowList){
            gainLotteryScore.execute(item);
        }
    }

}
