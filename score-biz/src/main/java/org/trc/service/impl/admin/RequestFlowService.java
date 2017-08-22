package org.trc.service.impl.admin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.admin.RequestFlow;
import org.trc.mapper.admin.IRequestFlowMapper;
import org.trc.service.admin.IRequestFlowService;
import org.trc.service.impl.BaseService;

/**
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年8月22日 上午10:30:35
 */
@Service("requestFlowService")
public class RequestFlowService extends BaseService<RequestFlow,Long> implements IRequestFlowService{
	
	@Autowired
    private IRequestFlowMapper requestFlowMapper;

	@Override
	public int modify(RequestFlow item) {
		return requestFlowMapper.modify(item);
	}

	@Override
	public RequestFlow getEntity(RequestFlow item) {
		return requestFlowMapper.getEntity(item);
	}

	@Override
	public List<RequestFlow> listEntity(RequestFlow item) {
		return requestFlowMapper.listEntity(item);
	}

	@Override
	public List<RequestFlow> listRequestFlowForExchangeTcoinCompensate() {
		return requestFlowMapper.listRequestFlowForExchangeTcoinCompensate();
	}

	@Override
	public List<RequestFlow> listRequestFlowForExchangeFinancialCardCompensate() {
		return requestFlowMapper.listRequestFlowForExchangeFinancialCardCompensate();
	}

	@Override
	public List<RequestFlow> listRequestFlowForGainTcoinCompensate() {
		return requestFlowMapper.listRequestFlowForGainTcoinCompensate();
	}

	@Override
	public List<RequestFlow> listRequestFlowForGainScoreCompensate() {
		return requestFlowMapper.listRequestFlowForGainScoreCompensate();
	}

	@Override
	public List<RequestFlow> listRequestFlowForGainFinancialCardCompensate() {
		return requestFlowMapper.listRequestFlowForGainFinancialCardCompensate();
	}

	@Override
	public List<RequestFlow> listRequestFlowForGainLotteryScoreCompensate() {
		return requestFlowMapper.listRequestFlowForGainLotteryScoreCompensate();
	}

}
