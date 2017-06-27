package org.trc.biz.pagehome;

import org.trc.constants.ScoreAdminConstants;
import org.trc.domain.pagehome.Banner;
import org.trc.form.pagehome.BannerForm;
import org.trc.util.Pagenation;


/**
 * Created by hzwzhen on 2017/6/9.
 */
public interface IBannerBiz {

    /**
     * 保存
     * @param banner
     * @return
     * @throws Exception
     */
    int saveBanner(Banner banner);

    /**
     * 更新
     * @param banner
     * @return
     * @throws Exception
     */
    int updateBanner(Banner banner);

    /**
     * 查询唯一对象
     * @param banner
     * @return banner
     * @throws Exception
     */
    Banner selectOne(Banner banner);

    /**
     * banner分页查询
     * @param form
     * @param page
     * @return
     * @throws Exception
     */
    Pagenation<Banner> bannerPage(BannerForm form,Pagenation<Banner> page);

    /**
     * 根据bannerId和shopId查询,判断是否有操作权限
     * @param form
     * @return
     * @throws Exception
     */
    Banner selectByIdAndShopId(BannerForm form);

    /**
     * 交换排序
     * @param bannerA
     * @param bannerB
     * @return
     * @throws Exception
     */
    int exchangeSort(Banner bannerA,Banner bannerB);

    int deleteById(Long id);
}
