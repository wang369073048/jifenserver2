package org.trc.util;

import org.trc.enums.ExceptionEnum;
import org.trc.exception.BannerException;

/**
 * Created by hzwzhen on 2017/6/10.
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(new BannerException(ExceptionEnum.BANNER_QUERY_EXCEPTION,"") instanceof  Exception);

    }
}
