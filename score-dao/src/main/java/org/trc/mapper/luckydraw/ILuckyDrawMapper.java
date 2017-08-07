package org.trc.mapper.luckydraw;

import org.trc.domain.luckydraw.LuckyDrawDO;
import org.trc.util.BaseMapper;

import java.util.List;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
public interface ILuckyDrawMapper extends BaseMapper<LuckyDrawDO>{

    LuckyDrawDO getLuckyDraw(LuckyDrawDO luckyDraw);

    int updateLuckyDraw(LuckyDrawDO luckyDraw);

    List<LuckyDrawDO> selectByParams(LuckyDrawDO luckyDraw);
}
