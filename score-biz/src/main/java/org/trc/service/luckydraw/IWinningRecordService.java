package org.trc.service.luckydraw;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.dto.WinningRecordDTO;
import org.trc.domain.luckydraw.ActivityDetailDO;
import org.trc.domain.luckydraw.WinningRecordDO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
public interface IWinningRecordService extends IBaseService<WinningRecordDO,Long>{

    WinningRecordDO selectOneForUpdate(WinningRecordDO param);

    int insertWinningRecord(WinningRecordDO winningRecord);

    List<Long> limitedLottery(WinningRecordDO winningRecord);

    List<WinningRecordDTO> listWinningRecord(WinningRecordDTO param);

    Pagenation<WinningRecordDTO> queryWinningRecord(WinningRecordDTO param, Pagenation<WinningRecordDTO> pageRequest);

    List<ActivityDetailDO> listActivityDetail(ActivityDetailDO param);

    Pagenation<ActivityDetailDO> queryActivityDetail(ActivityDetailDO param, Pagenation<ActivityDetailDO> pageRequest);

    Integer countWinningRecord(WinningRecordDO param);

    WinningRecordDTO getWinningRecord(WinningRecordDO param);

    int updateState(WinningRecordDO param);
}
