package com.zl.freight.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.zl.freight.mode.BaseUserEntity;
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
            Intent i = new Intent(context, LoginActivity.class);
            i.putExtra("role", role);
            i.putExtra(API.ISFINISH, true);
            context.startActivity(i);
            return;
        }

        BaseUserEntity userData = SpUtils.getUserData(context);
        if (!userData.getIsCheck().equals("1")) {
            new AlertDialog.Builder(context)
                    .setMessage("您的实名认证还未通过无法查看")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
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
            Intent i = new Intent(context, LoginActivity.class);
            i.putExtra("role", role);
            i.putExtra(API.ISFINISH, true);
            context.startActivityForResult(i, requestCode);
            return;
        }
        context.startActivityForResult(intent, requestCode);
    }

}
