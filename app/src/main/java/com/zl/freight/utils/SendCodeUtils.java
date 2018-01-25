package com.zl.freight.utils;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

/**
 * Created by Administrator on 2018/1/24.
 */

public class SendCodeUtils {

    public static void sendCode(String phone, final TextView textView, final SoapCallback callback) {
        textView.setEnabled(false);
        new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                textView.setText(l / 1000 + "s后可重发");
            }

            @Override
            public void onFinish() {
                textView.setText("重新发送");
                textView.setEnabled(true);
            }
        }.start();
        Map<String, String> params = new HashMap<>();
        params.put("TelphoneNumber", phone);
        SoapUtils.Post(API.MessageCheck, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                if (callback != null) {
                    callback.onError(error);
                }
            }

            @Override
            public void onSuccess(String data) {
                if (callback != null) {
                    callback.onSuccess(data);
                }
            }
        });
    }

}
