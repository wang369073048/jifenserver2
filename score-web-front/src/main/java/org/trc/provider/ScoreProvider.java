package org.trc.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.trc.biz.impl.order.NewOrderBiz;
import org.trc.biz.impl.score.ScoreBiz;
import org.trc.service.impl.auth.AuthService;
import org.trc.service.impl.consumer.AddressService;
import org.trc.service.impl.goods.CategoryService;
import org.trc.service.impl.goods.GoodsRecommendService;
import org.trc.service.impl.goods.GoodsService;
import org.trc.service.impl.goods.ShopClassificationService;
import org.trc.service.impl.luckydraw.LuckyDrawService;
import org.trc.service.impl.luckydraw.WinningRecordService;
import org.trc.service.impl.order.AreaService;
import org.trc.service.impl.pagehome.BannerContentService;
import org.trc.service.impl.score.ScoreChangeRecordService;
import org.trc.service.impl.score.ScoreChildService;
import org.trc.service.impl.score.ScoreConverterService;
import org.trc.service.impl.shop.ShopService;


public class ScoreProvider {

	@Autowired
    public static NewOrderBiz newOrderBiz;

    public static ShopService shopService;

    public static AuthService authService;

    public static AreaService areaService;

    public static GoodsService goodsService;

    public static AddressService addressService;

    public static CategoryService categoryService;

    @Autowired
    public static ScoreBiz scoreBiz;


    public static ScoreConverterService converterService;

    public static ScoreChangeRecordService scoreChangeRecordService;

    public static GoodsRecommendService goodsRecommendService;

    public static BannerContentService bannerContentService;

    public static ScoreChildService scoreChildService;
    
//    public static BarrageService barrageService;

    public static LuckyDrawService luckyDrawService;

    public static WinningRecordService winningRecordService;

    public static ShopClassificationService shopClassificationService;

//    public static ExchangeBiz exchangeBiz;

}
