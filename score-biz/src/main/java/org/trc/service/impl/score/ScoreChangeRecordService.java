package org.trc.service.impl.score;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.dto.FlowDTO;
import org.trc.domain.dto.ScoreChangeRecordQueryDTO;
import org.trc.domain.dto.ScoreChangeRecordsDTO;
import org.trc.domain.score.ScoreChange;
import org.trc.mapper.score.IScoreChangeMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.score.IScoreChangeRecordService;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/23
 */
@Service("scoreChangeRecordService")
public class ScoreChangeRecordService extends BaseService<ScoreChange,Long> implements IScoreChangeRecordService{

    @Autowired
    private IScoreChangeMapper scoreChangeMapper;
    @Override
    public int getTotalAmount(ScoreChangeRecordQueryDTO scoreChangeRecordQueryDto) {
        return scoreChangeMapper.getTotalAmount(scoreChangeRecordQueryDto);
    }

    @Override
    public ScoreChange getLastScoreChange(Map<String, Object> params) {
        return scoreChangeMapper.getLastScoreChange(params);
    }

    @Override
    public int correctScoreChange(ScoreChange scoreChange) {
        return scoreChangeMapper.correctScoreChange(scoreChange);
    }

    @Override
    public ScoreChange getScoreChangeById(Long id) {
        return null;
    }

    @Override
    public ScoreChange selectByParams(ScoreChange scoreChange) {
        return null;
    }

    @Override
    public int insertScoreChange(ScoreChange scoreChange) {
        return scoreChangeMapper.insertScoreChange(scoreChange);
    }

    @Override
    public Pagenation<ScoreChange> queryScoreChangeForUser(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pagenation) {
    	Page page = PageHelper.startPage(pagenation.getPageIndex(), pagenation.getPageSize());
        List<ScoreChange> list = scoreChangeMapper.queryScoreChangeForUser(queryDto);
        pagenation.setTotalData(page.getTotal());
        pagenation.setInfos(list);
        return pagenation;
    }

    @Override
    public Pagenation<ScoreChange> queryScoreChangeForShopAdmin(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageIndex(), pagenation.getPageSize());
        List<ScoreChange> list = scoreChangeMapper.queryScoreChangeForShopAdmin(queryDto);
        pagenation.setTotalData(page.getTotal());
        pagenation.setInfos(list);
        return pagenation;
    }

    @Override
    public Pagenation<ScoreChange> queryScoreChangeForPlatAdmin(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageIndex(), pagenation.getPageSize());
        List<ScoreChange> list = scoreChangeMapper.queryScoreChangeForPlatAdmin(queryDto);
        pagenation.setTotalData(page.getTotal());
        pagenation.setInfos(list);
        return pagenation;
    }
    
    @Override
    public List<FlowDTO> queryScoreChangeForExport(ScoreChangeRecordQueryDTO queryDto) {
        return scoreChangeMapper.queryScoreChangeForExport(queryDto);
    }
    
    @Override
    public Pagenation<ScoreChangeRecordsDTO> queryScoreChangeRecordsForUser(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChangeRecordsDTO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageIndex(), pagenation.getPageSize());
        List<ScoreChangeRecordsDTO> list = scoreChangeMapper.queryScoreChangeRecordsForUser(queryDto);
        pagenation.setTotalData(page.getTotal());
        pagenation.setInfos(list);
        return pagenation;
    }
    
    @Override
    public List<ScoreChangeRecordsDTO> queryScoreChangeRecordsForUserExport(ScoreChangeRecordQueryDTO queryDto) {
        return scoreChangeMapper.queryScoreChangeRecordsForUser(queryDto);
    }
}
