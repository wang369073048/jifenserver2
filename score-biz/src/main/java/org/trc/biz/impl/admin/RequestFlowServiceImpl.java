package org.trc.biz.impl.admin;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.trc.biz.admin.IRequestFlowBiz;
import org.trc.domain.admin.RequestFlow;
import org.trc.enums.ExceptionEnum;
import org.trc.exception.BusinessException;
import org.trc.service.admin.IRequestFlowService;

import com.txframework.util.Assert;

/**
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年8月22日 上午10:28:32
 */
@Service("requestFlowBiz")
public class RequestFlowServiceImpl implements IRequestFlowBiz {

    private Logger logger = LoggerFactory.getLogger(RequestFlowServiceImpl.class);

    @Autowired
    private IRequestFlowService requestFlowService;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RequestFlow insert(RequestFlow item) {
        _validate(item);
        requestFlowService.insert(item);
        return item;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RequestFlow insert(String requester, String responder, String type, String requestNum, String status, String requestParam, String responseResult, Date requestTime, String remark) {
        RequestFlow item = new RequestFlow(requester, responder, type, requestNum, status, requestParam, responseResult, requestTime, remark);
        return insert(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int modify(Long id, String status, String responseResult) {
        RequestFlow item = new RequestFlow();
        item.setId(id);
        item.setStatus(status);
        item.setResponseResult(responseResult);
        _validate(item);
        return requestFlowService.modify(item);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int modify(RequestFlow item) {
        _validate(item);
        return requestFlowService.modify(item);
    }

    @Override
    public RequestFlow getEntity(RequestFlow item) {
        return requestFlowService.getEntity(item);
    }

    @Override
    public List<RequestFlow> listEntity(RequestFlow item) {
        return requestFlowService.listEntity(item);
    }

    @Override
    public List<RequestFlow> listRequestFlowForExchangeTcoinCompensate() {
        return requestFlowService.listRequestFlowForExchangeTcoinCompensate();
    }

    @Override
    public List<RequestFlow> listRequestFlowForGainLotteryScoreCompensate() {
        return requestFlowService.listRequestFlowForGainLotteryScoreCompensate();
    }

    @Override
    public List<RequestFlow> listRequestFlowForExchangeFinancialCardCompensate() {
        return requestFlowService.listRequestFlowForExchangeFinancialCardCompensate();
    }

    @Override
    public List<RequestFlow> listRequestFlowForGainTcoinCompensate() {
        return requestFlowService.listRequestFlowForGainTcoinCompensate();
    }

    @Override
    public List<RequestFlow> listRequestFlowForGainScoreCompensate() {
        return requestFlowService.listRequestFlowForGainScoreCompensate();
    }

    @Override
    public List<RequestFlow> listRequestFlowForGainFinancialCardCompensate() {
        return requestFlowService.listRequestFlowForGainFinancialCardCompensate();
    }

    private boolean _validate(RequestFlow item){
        try{
            Assert.isTrue(null != item, "请求流水不能为空！");
            Assert.isTrue(StringUtils.isNotBlank(item.getStatus()), "状态不能为空！");
            if(null==item.getId()) {
                Assert.isTrue(StringUtils.isNotBlank(item.getRequester()), "请求方不能为空！");
                Assert.isTrue(StringUtils.isNotBlank(item.getResponder()), "响应方不能为空！");
                Assert.isTrue(StringUtils.isNotBlank(item.getType()), "业务类型不能为空！");
                Assert.isTrue(StringUtils.isNotBlank(item.getRequestNum()), "请求号不能为空！");
                Assert.isTrue(StringUtils.isNotBlank(item.getRequestParam()), "请求参数不能为空！");
                Assert.isTrue(null!=item.getRequestTime(), "请求时间不能为空！");
                Assert.isTrue(null!=item.getCreateTime(), "创建时间不能为空！");
                Assert.isTrue(null!=item.getUpdateTime(), "更新时间不能为空！");
            }
            return true;
        } catch (IllegalArgumentException e) {
            logger.error("流水处理参数校验错误:"+item);
            throw new BusinessException(ExceptionEnum.PARAM_CHECK_EXCEPTION, e.getMessage());
        }

    }

}
