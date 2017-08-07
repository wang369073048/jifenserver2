package org.trc.service.luckydraw;

import com.txframework.core.jdbc.PageRequest;
import org.trc.IBaseService;
import org.trc.domain.luckydraw.LuckyDrawDO;
import org.trc.util.Pagenation;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
public interface ILuckyDrawService extends IBaseService<LuckyDrawDO,Long>{

    LuckyDrawDO getLuckyDraw(LuckyDrawDO luckyDraw);

    int updateLuckyDraw(LuckyDrawDO luckyDraw);

    Pagenation<LuckyDrawDO> selectByParams(LuckyDrawDO luckyDraw, Pagenation<LuckyDrawDO> pageRequest);
}
