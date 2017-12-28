package com.zl.zlibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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
