package org.trc.biz.luckydraw;

import com.txframework.core.jdbc.PageRequest;
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
public interface IWinningRecordBiz {

    int insertWinningRecord(WinningRecordDO winningRecord);

    List<Long> limitedLottery(WinningRecordDO winningRecord);

    Map<Long, Integer> selectPlatformActivityPrizesLimit(WinningRecordDO winningRecord);

    Map<Long, Integer> selectActivityPrizesLimit(WinningRecordDO winningRecord);

    List<WinningRecordDTO> listWinningRecord(WinningRecordDTO param);

    Pagenation<WinningRecordDTO> queryWinningRecord(WinningRecordDTO param, Pagenation<WinningRecordDTO> pageRequest);

    List<ActivityDetailDO> listActivityDetail(ActivityDetailDO param);

    Pagenation<ActivityDetailDO> queryActivityDetail(ActivityDetailDO param, Pagenation<ActivityDetailDO> pageRequest);

    Integer countWinningRecord(WinningRecordDO param);

    WinningRecordDTO getWinningRecord(WinningRecordDO param);

    int updateState(WinningRecordDO param);

    WinningRecordDO selectOne(WinningRecordDO param);
}
