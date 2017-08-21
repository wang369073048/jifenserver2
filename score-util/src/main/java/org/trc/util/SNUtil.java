package org.trc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author dy
 * @since 2016- 05- 19  & JDK 1.8.0_91
 */
public class SNUtil {

    public final static String createSeriesNumber() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        StringBuilder sb = new StringBuilder(64);
        sb.append(df.format(new Date()));
        //sb.append(System.currentTimeMillis());
        sb.append((int) ((Math.random() * 9 + 1) * 10000));
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(SNUtil.createSeriesNumber().length());
    }
}
