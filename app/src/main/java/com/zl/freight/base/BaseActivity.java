package com.zl.freight.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;
import com.zl.freight.mode.BaseJPushEntity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/3.
 */

public class BaseActivity extends AutoLayoutActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected AppCompatActivity mActivity;
    protected ProgressDialog baseDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        baseDialog = new ProgressDialog(mActivity);
        baseDialog.setMessage("加载中...");
    }

    protected void showToast(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 找到控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    protected <T extends View> T getView(Integer viewId) {
        return (T) findViewById(viewId);
    }

    /**
     * 显示dialog 点击外部会消失
     *
     * @param Message
     */
    protected void showDialog(String Message) {
        baseDialog.setCanceledOnTouchOutside(true);
        baseDialog.setMessage(Message);
        baseDialog.show();
    }

    /**
     * 显示dialog 点击外部不会消失
     *
     * @param Message
     */
    protected void showNotTouchDialog(String Message) {
        baseDialog.setCanceledOnTouchOutside(false);
        baseDialog.setMessage(Message);
        baseDialog.show();
    }

    protected void showDialog() {
        baseDialog.setCanceledOnTouchOutside(true);
        baseDialog.show();
    }

    protected void showNotTouchDialog() {
        baseDialog.setCanceledOnTouchOutside(false);
        baseDialog.show();
    }

    protected void hideDialog() {
        baseDialog.dismiss();
    }

    protected void upLoadRegId() {
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        Map<String, String> params = new HashMap<>();
        BaseJPushEntity entity = new BaseJPushEntity();
        entity.setUserId(userData.getId());
        entity.setRegistrationId(SpUtils.getRegId(mActivity));
        entity.setMobile(userData.getUserName());
        params.put("baseJPushEntityJson", GsonUtils.toJson(entity));
        SoapUtils.Post(API.InsertJpush, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", "");
            }

            @Override
            public void onSuccess(String data) {
                Log.e("data", data);
            }
        });
    }

}
