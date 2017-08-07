package org.trc.biz.luckydraw;

import com.txframework.core.jdbc.PageRequest;
import org.trc.domain.luckydraw.ActivityDetailDO;
import org.trc.domain.luckydraw.LuckyDrawDO;
import org.trc.util.Pagenation;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/27
 */
public interface ILuckyDrawBiz {

    LuckyDrawDO getLuckyDraw(LuckyDrawDO luckyDraw);

    void insertLuckyDraw(LuckyDrawDO luckyDraw);

    Pagenation<LuckyDrawDO> queryLuckyDraw(LuckyDrawDO luckyDraw, Pagenation<LuckyDrawDO> pageRequest);

    ActivityDetailDO slyderAdventures(LuckyDrawDO param, String userId, String phone) ;

    void updateLuckyDraw(LuckyDrawDO luckyDraw);

    void setDeliveryAddress(Long addressId, String userId, String orderNum);

    void deleteEntity(LuckyDrawDO luckyDraw);

    int freeLotteryTimes(Long luckyDrawId, String platform, String userId);
}
