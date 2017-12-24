package com.zl.freight.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.zl.freight.ui.activity.LoginActivity;
import com.zl.freight.ui.activity.RoleChooseActivity;

/**
 * Created by Administrator on 2017/12/19.
 */

public class LoginUtils {

    public static void jumpToActivity(Context context, Intent intent) {
        //在这里判断是否登录
        boolean login = SpUtils.isLogin(context);
        if (!login) {
            //在这里进行身份判断
            int role = SpUtils.getRole(context);
            Intent i = null;
            switch (role) {
                case -1:
                    i = new Intent(context, RoleChooseActivity.class);
                    break;
                case 1:
                    i = new Intent(context, LoginActivity.class);
                    i.putExtra("role", role);
                    break;
                case 2:
                    i = new Intent(context, LoginActivity.class);
                    i.putExtra("role", role);
                    break;
            }
            context.startActivity(i);
            return;
        }
        context.startActivity(intent);
    }

    /**
     * 有返回值的
     *
     * @param context
     * @param intent
     */
    public static void jumpToActivityResult(Activity context, Intent intent, int requestCode) {
        //在这里判断是否登录
        if (!false) {
            // 在这里进行身份判断
            int role = SpUtils.getRole(context);
            Intent i = null;
            switch (role) {
                case -1:
                    i = new Intent(context, RoleChooseActivity.class);
                    break;
                case 0:
                    i = new Intent(context, LoginActivity.class);
                    i.putExtra("role", 0);
                    break;
                case 1:
                    i = new Intent(context, LoginActivity.class);
                    i.putExtra("role", 1);
                    break;
            }
            i.putExtra(API.ISFINISH, true);
            context.startActivityForResult(i, requestCode);
            return;
        }
        context.startActivityForResult(intent, requestCode);
    }

}
