package org.trc.mapper.luckydraw;

import org.trc.domain.luckydraw.LuckyDrawDO;
import org.trc.util.BaseMapper;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
public interface ILuckyDrawMapper extends BaseMapper<LuckyDrawDO>{

    LuckyDrawDO getLuckyDraw(LuckyDrawDO luckyDraw);
}
