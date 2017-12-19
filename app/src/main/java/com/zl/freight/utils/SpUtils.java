package com.zl.freight.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhanglei on 2017/12/19.
 */

public class SpUtils {

    //储存regId
    public static void setRegId(Context context, String regId) {
        SharedPreferences sp = context.getSharedPreferences("regId", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("regId", regId);
        edit.commit();
    }

    //获取regId
    public static String getRegId(Context context) {
        SharedPreferences sp = context.getSharedPreferences("regId", Context.MODE_PRIVATE);
        return sp.getString("regId", "");
    }

    //储存角色信息
    public static void setRole(Context context, int role) {
        SharedPreferences sp = context.getSharedPreferences("role", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putInt("role", role);
        edit.commit();
    }

    //获取角色信息
    public static int getRole(Context context) {
        SharedPreferences sp = context.getSharedPreferences("role", Context.MODE_PRIVATE);
        return sp.getInt("role", -1);
    }

    //储存登录信息
    public static void setIsLogin(Context context, boolean isLogin) {
        SharedPreferences sp = context.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isLogin", isLogin);
        edit.commit();
    }

    //获取登录信息（是否登录）
    public static boolean isLogin(Context context) {
        SharedPreferences sp = context.getSharedPreferences("isLogin", Context.MODE_PRIVATE);
        return sp.getBoolean("isLogin", false);
    }

}
