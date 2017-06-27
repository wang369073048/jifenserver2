package org.trc.service.impl.pagehome;

import org.springframework.stereotype.Service;
import org.trc.domain.pagehome.Banner;
import org.trc.service.impl.BaseService;
import org.trc.service.pagehome.IBannerService;

/**
 * Created by hzwzhen on 2017/6/9.
 */
@Service("bannerService")
public class BannerService extends BaseService<Banner,Long> implements IBannerService{

}
