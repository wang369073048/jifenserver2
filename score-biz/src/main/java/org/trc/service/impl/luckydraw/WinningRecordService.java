package org.trc.service.impl.luckydraw;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.dto.WinningRecordDTO;
import org.trc.domain.luckydraw.ActivityDetailDO;
import org.trc.domain.luckydraw.LuckyDrawDO;
import org.trc.domain.luckydraw.WinningRecordDO;
import org.trc.mapper.luckydraw.IWinningRecordMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.luckydraw.IWinningRecordService;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
@Service(value = "winningRecordService")
public class WinningRecordService extends BaseService<WinningRecordDO,Long> implements IWinningRecordService{
    @Autowired
    private IWinningRecordMapper winningRecordMapper;
    @Override
    public WinningRecordDO selectOneForUpdate(WinningRecordDO param) {
        return winningRecordMapper.selectOneForUpdate(param);
    }

    @Override
    public int insertWinningRecord(WinningRecordDO winningRecord) {
        return 0;
    }

    @Override
    public List<Long> limitedLottery(WinningRecordDO winningRecord) {
        return null;
    }

    @Override
    public List<WinningRecordDTO> listWinningRecord(WinningRecordDTO param) {
        return null;
    }

    @Override
    public Pagenation<WinningRecordDTO> queryWinningRecord(WinningRecordDTO param, Pagenation<WinningRecordDTO> pageRequest) {
        return null;
    }

    @Override
    public List<ActivityDetailDO> listActivityDetail(ActivityDetailDO param) {
        return null;
    }

    @Override
    public Pagenation<ActivityDetailDO> queryActivityDetail(ActivityDetailDO param, Pagenation<ActivityDetailDO> pageRequest) {
        return null;
    }

    @Override
    public Integer countWinningRecord(WinningRecordDO param) {
        return null;
    }

    @Override
    public WinningRecordDTO getWinningRecord(WinningRecordDO param) {
        return null;
    }

    @Override
    public int updateState(WinningRecordDO param) {
        return winningRecordMapper.updateState(param);
    }

    @Override
    public List<ActivityDetailDO> listActivityDetailByParams(ActivityDetailDO param) {
        return winningRecordMapper.listActivityDetailByParams(param);
    }

    @Override
    public Pagenation<ActivityDetailDO> selectActivityDetailByParams(ActivityDetailDO param, Pagenation<ActivityDetailDO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
        List<ActivityDetailDO> list = winningRecordMapper.selectActivityDetailByParams(param);
        pagenation.setTotalCount(page.getTotal());
        pagenation.setResult(list);
        return pagenation;
    }

    @Override
    public List<WinningRecordDTO> listByParams(WinningRecordDTO param) {
        return winningRecordMapper.listByParams(param);
    }

    @Override
    public Pagenation<WinningRecordDTO> selectByParams(WinningRecordDTO param, Pagenation<WinningRecordDTO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
        List<WinningRecordDTO> list = winningRecordMapper.selectByParams(param);
        pagenation.setTotalCount(page.getTotal());
        pagenation.setResult(list);
        return pagenation;
    }
}
