package com.zl.freight.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Toast;

import com.zhy.autolayout.AutoLayoutActivity;

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

    protected void showDialog(String Message) {
        baseDialog.setMessage(Message);
        baseDialog.show();
    }

    protected void showDialog() {
        baseDialog.show();
    }

    protected void hideDialog() {
        baseDialog.dismiss();
    }

}
