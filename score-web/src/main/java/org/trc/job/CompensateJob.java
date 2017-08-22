package org.trc.job;

import com.trc.mall.model.RequestFlow;
import com.trc.mall.operation.*;
import com.trc.mall.service.RequestFlowService;
import org.trc.util.IpUtil;
import com.trc.mall.util.ThreadPoolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.net.SocketException;
import java.util.List;

/**
 * Created by george on 2017/7/20.
 */
public class CompensateJob extends BaseJob{

    private Logger logger = LoggerFactory.getLogger(CompensateJob.class);

    @Resource
    private RequestFlowService requestFlowService;

    @Resource
    private ExchangeFinancialCard exchangeFinancialCard;

    @Resource
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
        List<RequestFlow> requestFlowList = requestFlowService.listRequestFlowForGainTcoinCompensate();
        for(RequestFlow item : requestFlowList){
            ThreadPoolUtil.execute(new GainTcoin(item));
        }
    }

    @Transactional
    public void gainScore(){
        if(!_checkIp()){
            return ;
        }
        List<RequestFlow> requestFlowList = requestFlowService.listRequestFlowForGainScoreCompensate();
        for(RequestFlow item : requestFlowList){
            ThreadPoolUtil.execute(new GainScore(item));
        }
    }

    @Transactional
    public void exchangeFinancialCard(){
        if(!_checkIp()){
            return ;
        }
        List<RequestFlow> requestFlowList = requestFlowService.listRequestFlowForExchangeFinancialCardCompensate();
        for(RequestFlow item : requestFlowList){
            exchangeFinancialCard.execute(item);
        }
    }

    @Transactional
    public void gainFinancialCard(){
        if(!_checkIp()){
            return ;
        }
        List<RequestFlow> requestFlowList = requestFlowService.listRequestFlowForGainFinancialCardCompensate();
        for(RequestFlow item : requestFlowList){
            gainFinancialCard.execute(item);
        }
    }

}
