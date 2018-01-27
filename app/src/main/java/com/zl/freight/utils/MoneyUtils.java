package com.zl.freight.utils;

import android.content.Context;
import android.widget.Toast;

import com.zl.freight.mode.BaseUserEntity;

/**
 * Created by zhanglei on 2018\1\21 0021.
 * 积分工具类
 */

public class MoneyUtils {

    /**
     * 更新积分
     *
     * @param type  0增1减
     */
    public static void upDateMoney(Context context, int type, int money) {
        int jiFen = money * API.ratio;
        BaseUserEntity userData = SpUtils.getUserData(context);
        int i = Integer.parseInt(userData.getIntegral());
        if (type == 0) {
            userData.setIntegral((i + jiFen) + "");
        } else if (type == 1) {
            if (jiFen <= i) {
                userData.setIntegral((i - jiFen) + "");
            } else {
                userData.setIntegral(i + "");
                Toast.makeText(context, "发生异常", Toast.LENGTH_SHORT).show();
            }
        }

        SpUtils.setUserData(context, userData);
    }

}
