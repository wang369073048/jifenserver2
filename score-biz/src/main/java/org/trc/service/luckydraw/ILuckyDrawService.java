package org.trc.service.luckydraw;

import org.trc.IBaseService;
import org.trc.domain.luckydraw.LuckyDrawDO;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
public interface ILuckyDrawService extends IBaseService<LuckyDrawDO,Long>{

    LuckyDrawDO getLuckyDraw(LuckyDrawDO luckyDraw);
}
