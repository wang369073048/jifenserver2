package org.trc.service.impl.luckydraw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.luckydraw.LuckyDrawDO;
import org.trc.mapper.luckydraw.ILuckyDrawMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.luckydraw.ILuckyDrawService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
@Service("luckyDrawService")
public class LuckyDrawService extends BaseService<LuckyDrawDO,Long> implements ILuckyDrawService{
    @Autowired
    private ILuckyDrawMapper luckyDrawMapper;
    @Override
    public LuckyDrawDO getLuckyDraw(LuckyDrawDO luckyDraw) {
        return luckyDrawMapper.getLuckyDraw(luckyDraw);
    }
}
