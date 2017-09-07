package org.trc.util;

import com.txframework.util.HmacUtils;
import org.apache.commons.lang.StringUtils;

/**
 * 枚举类型工具类
 * Created by huyan on 2016/7/13
 */

public class EnumUtil {

    public static <T extends Enum<T>> boolean isMember(Class<T> enumClass, String memberParam) {
        if (StringUtils.isBlank(memberParam)) {
            return false;
        }
        T[] enumConstants = enumClass.getEnumConstants();
        int size = enumConstants.length;
        if (size>0) {
            for (int i=0;i<size;i++) {
                if (memberParam.equals(enumConstants[i].name())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        for (int i=0;i<10;i++) {
            try {
                System.out.println(HmacUtils.generateSecretKey(HmacUtils.Algorithm.HmacMD5));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
