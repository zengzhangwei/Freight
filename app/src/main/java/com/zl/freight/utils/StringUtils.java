package com.zl.freight.utils;

/**
 * Created by Administrator on 2017/12/24.
 */

public class StringUtils {

    /**
     * 将身份证号码加密显示
     *
     * @param idCardNumber
     * @return
     */
    public static String handleIdCardNumber(String idCardNumber) {
        String head = idCardNumber.substring(0, 4);
        String footer = idCardNumber.substring(14, 18);
        return head + "**********" + footer;
    }

    /**
     * 通过身份证计算出性别
     *
     * @param idCardNumber
     * @return
     */
    public static String countSex(String idCardNumber) {
        String number = idCardNumber.substring(16, 17);
        int i = Integer.parseInt(number);
        if (i % 2 == 0) {
            return "女";
        } else {
            return "男";
        }
    }

}
