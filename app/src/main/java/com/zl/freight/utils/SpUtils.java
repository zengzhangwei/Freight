package com.zl.freight.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.zl.freight.mode.BaseUserEntity;

import java.util.HashMap;
import java.util.Map;

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
        return sp.getInt("role", 1);
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

    //储存用户信息
    public static void setUserData(Context context, BaseUserEntity userEntity) {
        SharedPreferences sp = context.getSharedPreferences("userEntity", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("RealName", userEntity.getRealName());
        edit.putString("UserName", userEntity.getUserName());
        edit.putString("UserIcon", userEntity.getUserIcon());
        edit.putString("PassWord", userEntity.getPassWord());
        edit.putString("IsCheck", userEntity.getIsCheck());
        edit.putString("id", userEntity.getId());
        edit.putString("UserRole", userEntity.getUserRole());
        edit.putString("CarLong", userEntity.getCarLong());
        edit.putString("CarType", userEntity.getCarType());
        edit.putString("Integral", userEntity.getIntegral());
        edit.putString("Bankaccount", userEntity.getBankaccount());
        edit.commit();
    }

    //获取用户信息
    public static BaseUserEntity getUserData(Context context) {
        BaseUserEntity user = new BaseUserEntity();
        SharedPreferences sp = context.getSharedPreferences("userEntity", Context.MODE_PRIVATE);
        user.setRealName(sp.getString("RealName", ""));
        user.setUserName(sp.getString("UserName", ""));
        user.setUserIcon(sp.getString("UserIcon", ""));
        user.setPassWord(sp.getString("PassWord", ""));
        user.setId(sp.getString("id", "-1"));
        user.setUserRole(sp.getString("UserRole", "-1"));
        user.setCarLong(sp.getString("CarLong", "31"));
        user.setCarType(sp.getString("CarType", "33"));
        user.setIntegral(sp.getString("Integral", "0"));
        user.setIsCheck(sp.getString("IsCheck", "0"));
        user.setBankaccount(sp.getString("Bankaccount", ""));
        return user;
    }

    /**
     * 储存定位信息
     */
    public static void setLocation(Context context, double x, double y) {
        SharedPreferences sp = context.getSharedPreferences("location", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("x", String.valueOf(x));
        edit.putString("y", String.valueOf(y));
        edit.commit();
    }

    /**
     * 获取定位信息
     *
     * @param context
     * @return
     */
    public static Map<String, String> getLocation(Context context) {
        Map<String, String> map = new HashMap<>();
        SharedPreferences sp = context.getSharedPreferences("location", Context.MODE_PRIVATE);
        map.put("x", sp.getString("x", ""));
        map.put("y", sp.getString("y", ""));
        return map;
    }

    //储存登录信息
    public static void setIsReal(Context context, boolean isReal) {
        SharedPreferences sp = context.getSharedPreferences("isReal", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isLogin", isReal);
        edit.commit();
    }

    //获取登录信息（是否登录）
    public static boolean isReal(Context context) {
        SharedPreferences sp = context.getSharedPreferences("isReal", Context.MODE_PRIVATE);
        return sp.getBoolean("isLogin", false);
    }

}
