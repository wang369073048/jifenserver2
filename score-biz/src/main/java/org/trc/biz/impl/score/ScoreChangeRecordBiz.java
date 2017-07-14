package org.trc.biz.impl.score;

import org.trc.biz.score.IScoreChangeRecordBiz;
import org.trc.constants.ScoreCst;
import org.trc.domain.dto.FlowDTO;
import org.trc.domain.dto.ScoreChangeRecordQueryDTO;
import org.trc.domain.score.ScoreChange;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/13
 */
public class ScoreChangeRecordBiz implements IScoreChangeRecordBiz{
    @Override
    public int getTotalAmountByCurrency(String exchangeCurrency, ScoreCst.BusinessCode businessCode) {
        return 0;
    }

    @Override
    public int getTotalAmountByCurrencyAndUserId(String exchangeCurrency, String userId, ScoreCst.BusinessCode businessCode) {
        return 0;
    }

    @Override
    public Pagenation<ScoreChange> queryScoreChangeForUser(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pageRequest) {
        return null;
    }

    @Override
    public Pagenation<ScoreChange> queryScoreChangeForShopAdmin(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pageRequest) {
        return null;
    }

    @Override
    public Pagenation<ScoreChange> queryScoreChangeForPlatAdmin(ScoreChangeRecordQueryDTO queryDto, Pagenation<ScoreChange> pageRequest) {
        return null;
    }

    @Override
    public List<FlowDTO> queryScoreChangeForExport(ScoreChangeRecordQueryDTO queryDto) {
        return null;
    }
}
