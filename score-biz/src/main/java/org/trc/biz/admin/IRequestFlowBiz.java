package org.trc.biz.admin;



import java.util.Date;
import java.util.List;

import org.trc.domain.admin.RequestFlow;

/**
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年8月22日 上午10:19:19
 */
public interface IRequestFlowBiz {

    RequestFlow insert(RequestFlow item);

    RequestFlow insert(String requester, String responder, String type, String requestNum, String status, String requestParam, String responseResult, Date requestTime, String remark);

    int modify(Long id, String status, String responseResult);

    int modify(RequestFlow item);

    RequestFlow getEntity(RequestFlow item);

    List<RequestFlow> listEntity(RequestFlow item);

    List<RequestFlow> listRequestFlowForExchangeTcoinCompensate();

    List<RequestFlow> listRequestFlowForGainLotteryScoreCompensate();

    List<RequestFlow> listRequestFlowForExchangeFinancialCardCompensate();

    List<RequestFlow> listRequestFlowForGainTcoinCompensate();

    List<RequestFlow> listRequestFlowForGainScoreCompensate();

    List<RequestFlow> listRequestFlowForGainFinancialCardCompensate();
}
