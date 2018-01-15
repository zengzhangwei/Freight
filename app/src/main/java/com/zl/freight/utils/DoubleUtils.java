package com.zl.freight.utils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018\1\15 0015.
 */

public class DoubleUtils {

    public static String format0_00(double d) {
        DecimalFormat df = new DecimalFormat("######0.00");
        return df.format(d);
    }

}
