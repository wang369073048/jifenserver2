package org.trc.service.impl.luckydraw;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.domain.goods.GoodsDO;
import org.trc.domain.luckydraw.LuckyDrawDO;
import org.trc.mapper.luckydraw.ILuckyDrawMapper;
import org.trc.service.impl.BaseService;
import org.trc.service.luckydraw.ILuckyDrawService;
import org.trc.util.Pagenation;

import java.util.List;

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

    @Override
    public int updateLuckyDraw(LuckyDrawDO luckyDraw) {
        return luckyDrawMapper.updateLuckyDraw(luckyDraw);
    }

    @Override
    public Pagenation<LuckyDrawDO> selectByParams(LuckyDrawDO luckyDraw, Pagenation<LuckyDrawDO> pagenation) {
        Page page = PageHelper.startPage(pagenation.getPageNo(), pagenation.getPageSize());
        List<LuckyDrawDO> list = luckyDrawMapper.selectByParams(luckyDraw);
        pagenation.setTotalCount(page.getTotal());
        pagenation.setResult(list);
        return pagenation;
    }
}
