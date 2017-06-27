package org.trc.biz.pagehome;

import org.trc.domain.pagehome.Banner;
import org.trc.domain.pagehome.BannerContent;
import org.trc.form.pagehome.BannerContentForm;
import org.trc.form.pagehome.BannerForm;
import org.trc.util.Pagenation;

/**
 * Created by hzwzhen on 2017/6/12.
 */
public interface IBannerContentBiz {

    /**
     * 保存
     * @param bannerContent
     * @return
     */
    int saveBannerContent(BannerContent bannerContent);

    /**
     * 更新
     * @param bannerContent
     * @return
     */
    int updateBannerContent(BannerContent bannerContent);

    /**
     * 根据主键和shopId查询,判断是否有操作权限
     * @param bannerContent
     * @return
     */
    BannerContent selectByIdAndShopId(BannerContent bannerContent);

    /**
     * 删除bannerContent
     * @param bannerContent
     * @return
     */
    int deleteBannerContent(BannerContent bannerContent);

    /**
     * BannerContent分页查询
     * @param form
     * @param page
     * @return
     * @throws Exception
     */
    Pagenation<BannerContent> bannerContentPage(BannerContentForm form, Pagenation<BannerContent> page);
}
