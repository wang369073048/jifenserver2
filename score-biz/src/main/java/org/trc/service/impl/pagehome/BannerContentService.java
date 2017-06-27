package org.trc.service.impl.pagehome;

import org.springframework.stereotype.Service;
import org.trc.domain.pagehome.BannerContent;
import org.trc.service.impl.BaseService;
import org.trc.service.pagehome.IBannerContentService;
/**
 * Created by hzwzhen on 2017/6/12.
 */
@Service("bannerContentService")
public class BannerContentService extends BaseService<BannerContent,Long> implements IBannerContentService{
}
