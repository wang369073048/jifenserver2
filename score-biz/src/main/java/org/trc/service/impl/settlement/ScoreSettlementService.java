package org.trc.service.impl.settlement;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.score.ScoreSettlement;
import org.trc.mapper.settlement.IScoreSettlementMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.settlement.IScoreSettlementService;

/**
 * 
 * @JDK_version:  JDK1.8
 * @author hzxab
 * @date 2017年8月21日 下午7:51:35
 */
@Service("scoreSettlementService")
public class ScoreSettlementService extends BaseService<ScoreSettlement,Long> implements IScoreSettlementService{

	@Autowired
    private IScoreSettlementMapper soreSettlementMapper;
	
	@Override
	public ScoreSettlement getScoreSettlementByUserIdAndAccountDay(Map params) {
		// TODO Auto-generated method stub
		return soreSettlementMapper.getScoreSettlementByUserIdAndAccountDay(params);
	}

	@Override
	public ScoreSettlement getLastScoreSettlement(Long scoreId) {
		// TODO Auto-generated method stub
		return soreSettlementMapper.getLastScoreSettlement(scoreId);
	}

   

}
