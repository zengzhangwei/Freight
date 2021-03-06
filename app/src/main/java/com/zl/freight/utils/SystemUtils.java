package com.zl.freight.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.Toast;

import com.zl.freight.mode.BaseUserEntity;

/**
 * Created by zhanglei on 2017\12\8 0008.
 * 系统功能工具类
 */

public class SystemUtils {

    /**
     * 调用拨号界面
     *
     * @param phone 电话号码
     */
    public static void call(Context context, String phone) {

        BaseUserEntity userData = SpUtils.getUserData(context);
        if (!userData.getIsCheck().equals("1")) {
            new AlertDialog.Builder(context)
                    .setMessage("您的实名认证还未通过无法拨打电话")
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).show();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(context, "无法拨打电话，手机号未被获取", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 调用发短信界面
     *
     * @param context
     * @param phone
     */
    public static void sendSms(Context context, String phone) {
        Uri uri2 = Uri.parse("smsto:" + phone);
        Intent intentFinalMessage = new Intent(Intent.ACTION_VIEW, uri2);
//        intentFinalMessage.setType("vnd.android-dir/mms-sms");
        context.startActivity(intentFinalMessage);
    }

}
