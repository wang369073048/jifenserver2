package org.trc.job;

import java.net.SocketException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.admin.IRequestFlowBiz;
import org.trc.domain.admin.RequestFlow;
import org.trc.operation.ExchangeFinancialCard;
import org.trc.operation.GainFinancialCard;
import org.trc.operation.GainScore;
import org.trc.operation.GainTcoin;
import org.trc.util.IpUtil;
import org.trc.util.ThreadPoolUtil;

/**
 * Created by wangzhen
 */
public class CompensateJob extends BaseJob{

    private Logger logger = LoggerFactory.getLogger(CompensateJob.class);

    @Autowired
    private IRequestFlowBiz requestFlowBiz;

    @Autowired
    private ExchangeFinancialCard exchangeFinancialCard;

    @Autowired
    private GainFinancialCard gainFinancialCard;

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
            logger.error("CompensateJob!跳过任务!");
            return false;
        }
    }

    @Transactional
    public void gainTcoin(){
        if(!_checkIp()){
            return ;
        }
        List<RequestFlow> requestFlowList = requestFlowBiz.listRequestFlowForGainTcoinCompensate();
        for(RequestFlow item : requestFlowList){
            ThreadPoolUtil.execute(new GainTcoin(item));
        }
    }

    @Transactional
    public void gainScore(){
        if(!_checkIp()){
            return ;
        }
        List<RequestFlow> requestFlowList = requestFlowBiz.listRequestFlowForGainScoreCompensate();
        for(RequestFlow item : requestFlowList){
            ThreadPoolUtil.execute(new GainScore(item));
        }
    }

    @Transactional
    public void exchangeFinancialCard(){
        if(!_checkIp()){
            return ;
        }
        List<RequestFlow> requestFlowList = requestFlowBiz.listRequestFlowForExchangeFinancialCardCompensate();
        for(RequestFlow item : requestFlowList){
            exchangeFinancialCard.execute(item);
        }
    }

    @Transactional
    public void gainFinancialCard(){
        if(!_checkIp()){
            return ;
        }
        List<RequestFlow> requestFlowList = requestFlowBiz.listRequestFlowForGainFinancialCardCompensate();
        for(RequestFlow item : requestFlowList){
            gainFinancialCard.execute(item);
        }
    }

}
