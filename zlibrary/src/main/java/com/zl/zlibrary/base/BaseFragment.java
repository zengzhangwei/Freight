package com.zl.zlibrary.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatDelegate;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by Administrator on 2017/8/25.
 */

public class BaseFragment extends Fragment {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    protected Activity mActivity;
    protected Fragment mFragment;
    protected Handler handler = new Handler();
    private ProgressDialog baseDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
        baseDialog = new ProgressDialog(mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragment = this;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void showToast(String massage) {
        Toast.makeText(mActivity, massage, Toast.LENGTH_SHORT).show();
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

    /**
     * 隐藏软键盘
     *
     * @param view
     */
    protected void hideKeyboard(View view) {
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 获取软键盘的显示状态
        boolean isOpen = imm.isActive();
        if (isOpen) {
            // 隐藏软键盘
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
