package org.trc.biz.impl.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.trc.annotation.cache.Cacheable;
import org.trc.biz.order.IAreaBiz;
import org.trc.domain.order.AreaDO;
import org.trc.service.order.IAreaService;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/18
 */
@Service("areaBiz")
public class AreaBiz implements IAreaBiz {

    @Autowired
    private IAreaService areaService;
    @Override
    @Cacheable(key="#code")
    public AreaDO getAreaByCode(String code) {
        AreaDO areaDO = new AreaDO();
        areaDO.setCode(code);
        return areaService.selectOne(areaDO);
    }

    @Override
    public String getAreaDesc() {
        return null;
    }
}
