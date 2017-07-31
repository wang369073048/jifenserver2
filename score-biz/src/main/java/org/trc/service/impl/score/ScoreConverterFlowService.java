package org.trc.service.impl.score;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.score.ScoreConverterFlow;
import org.trc.mapper.score.IScoreConverterFlowMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.score.IScoreConverterFlowService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/26
 */
@Service("scoreConverterFlowService")
public class ScoreConverterFlowService extends BaseService<ScoreConverterFlow,Long> implements IScoreConverterFlowService{
    @Autowired
    private IScoreConverterFlowMapper scoreConverterFlowMapper;
    @Override
    public int insertScoreConverterFlow(ScoreConverterFlow scoreConverter) {
        return scoreConverterFlowMapper.insertScoreConverterFlow(scoreConverter);
    }
}
