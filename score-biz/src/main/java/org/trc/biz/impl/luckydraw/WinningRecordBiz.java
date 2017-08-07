package org.trc.biz.impl.luckydraw;

import com.txframework.core.jdbc.PageRequest;
import org.springframework.stereotype.Service;
import org.trc.biz.luckydraw.IWinningRecordBiz;
import org.trc.domain.dto.WinningRecordDTO;
import org.trc.domain.luckydraw.ActivityDetailDO;
import org.trc.domain.luckydraw.WinningRecordDO;
import org.trc.util.Pagenation;

import java.util.List;
import java.util.Map;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/7
 */
@Service("winningRecordBiz")
public class WinningRecordBiz implements IWinningRecordBiz{
    @Override
    public int insertWinningRecord(WinningRecordDO winningRecord) {
        return 0;
    }

    @Override
    public List<Long> limitedLottery(WinningRecordDO winningRecord) {
        return null;
    }

    @Override
    public Map<Long, Integer> selectPlatformActivityPrizesLimit(WinningRecordDO winningRecord) {
        return null;
    }

    @Override
    public Map<Long, Integer> selectActivityPrizesLimit(WinningRecordDO winningRecord) {
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

    @Override//TODO
    public WinningRecordDTO getWinningRecord(WinningRecordDO param) {
        return null;
    }

    @Override
    public int updateState(WinningRecordDO param) {
        return 0;
    }

    @Override
    public WinningRecordDO selectOne(WinningRecordDO param) {
        return null;
    }
}
