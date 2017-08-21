package org.trc.biz.impl.settlement;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.trc.biz.settlement.IScoreSettlementBiz;
import org.trc.domain.score.ScoreSettlement;
import org.trc.service.settlement.IScoreSettlementService;

/**
 * 
 * @Description:积分结算
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年8月21日 下午7:49:36
 */
@Service("scoreSettlementBiz")
public class ScoreSettlementBiz implements IScoreSettlementBiz {

    @Resource
    private IScoreSettlementService scoreSettlementService;

    @Override
    public ScoreSettlement getScoreSettlementByUserIdAndAccountDay(String userId, String accountDay) {
        Map param = new HashMap();
        param.put("userId",userId);
        param.put("accountDay",accountDay);
        return scoreSettlementService.getScoreSettlementByUserIdAndAccountDay(param);
    }

    @Override
    public int insert(ScoreSettlement scoreSettlement) {
        return scoreSettlementService.insert(scoreSettlement);
    }
}
