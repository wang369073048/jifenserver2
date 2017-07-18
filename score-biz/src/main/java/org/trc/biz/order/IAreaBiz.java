package org.trc.biz.order;

import org.trc.domain.order.AreaDO;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/7/18
 */
public interface IAreaBiz {

    AreaDO getAreaByCode(String code);

    String getAreaDesc();

}
