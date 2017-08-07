package org.trc.mapper.luckydraw;

import org.trc.domain.dto.WinningRecordDTO;
import org.trc.domain.luckydraw.ActivityDetailDO;
import org.trc.domain.luckydraw.WinningRecordDO;
import org.trc.util.BaseMapper;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
public interface IWinningRecordMapper extends BaseMapper<WinningRecordDO>{

    WinningRecordDO selectOneForUpdate(WinningRecordDO param);

    int insertWinningRecord(WinningRecordDO winningRecord);

    int countWinningRecord(WinningRecordDO winningRecord);

    List<Long> personalLimit(WinningRecordDO winningRecord);

    List<Long> platformLimit(WinningRecordDO winningRecord);

    int updateState(WinningRecordDO winningRecord);

    List<WinningRecordDTO> selectByParams(WinningRecordDTO param);

    List<WinningRecordDTO> listByParams(WinningRecordDTO param);

    List<ActivityDetailDO> listActivityDetailByParams(ActivityDetailDO param);

    List<ActivityDetailDO> selectActivityDetailByParams(ActivityDetailDO param);

}
