package org.trc.biz.admin;

import java.util.Date;

/**
 * author: hzwzhen
 * JDK-version:  JDK1.8
 * comments:
 * since Date： 2017/8/29
 */
public interface ILimitBiz {

    String getKeySuffix(Date time);

    boolean enoughLimitInAmount(String exchangeCurrency, String orderCode, int amount, Date now);

    boolean rollbackLimitInAmount(String limitInKey, String orderCode, int amount);

    boolean enoughLimitOutAmount(String exchangeCurrency, String orderCode, int amount, Date now);

    boolean rollbackLimitOutAmount(String limitOutKey, String orderCode, int amount);

    boolean enoughPersonalLimitInAmount(String exchangeCurrency, String userId, String orderCode, int amount, Date now);

    boolean rollbackPersonalLimitInAmount(String personalLimitInKey, String userId, String orderCode, int amount);

    boolean enoughPersonalLimitOutAmount(String exchangeCurrency, String userId, String orderCode, int amount, Date now);

    boolean rollbackPersonalLimitOutAmount(String personalLimitOutKey, String userId, String orderCode, int amount);
}
