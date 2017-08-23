package org.trc.mapper.admin;

import java.util.List;

import org.trc.domain.admin.RequestFlow;
import org.trc.domain.auth.Auth;
import org.trc.util.BaseMapper;

/**
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年8月22日 上午10:34:01
 */
public interface IRequestFlowMapper extends BaseMapper<RequestFlow> {

    int insert(RequestFlow item);

    int modify(RequestFlow item);

    RequestFlow getEntity(RequestFlow item);

    List<RequestFlow> listEntity(RequestFlow item);

    List<RequestFlow> listRequestFlowForExchangeTcoinCompensate();

    List<RequestFlow> listRequestFlowForExchangeFinancialCardCompensate();

    List<RequestFlow> listRequestFlowForGainTcoinCompensate();

    List<RequestFlow> listRequestFlowForGainScoreCompensate();

    List<RequestFlow> listRequestFlowForGainFinancialCardCompensate();

    List<RequestFlow> listRequestFlowForGainLotteryScoreCompensate();
}
