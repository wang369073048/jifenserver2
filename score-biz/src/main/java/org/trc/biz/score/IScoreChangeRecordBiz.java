package org.trc.biz.score;

import com.txframework.core.jdbc.PageRequest;
import org.trc.constants.ScoreCst;
import org.trc.domain.dto.FlowDTO;
import org.trc.domain.dto.ScoreChangeRecordQueryDTO;
import org.trc.domain.score.ScoreChange;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * since Date： 2017/7/13
 */
public interface IScoreChangeRecordBiz {

    int getTotalAmountByCurrency(String exchangeCurrency, ScoreCst.BusinessCode businessCode);

    int getTotalAmountByCurrencyAndUserId(String exchangeCurrency, String userId, ScoreCst.BusinessCode businessCode);

    Pagenation<ScoreChange> queryScoreChangeForUser(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pageRequest);

    Pagenation<ScoreChange> queryScoreChangeForShopAdmin(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pageRequest);

    Pagenation<ScoreChange> queryScoreChangeForPlatAdmin(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pageRequest);

    List<FlowDTO> queryScoreChangeForExport(ScoreChangeRecordQueryDTO queryDto);
}
