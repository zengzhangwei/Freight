package com.zl.zlibrary.base;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Created by zhanglei on 2017\12\8 0008.
 * 弹窗的父类（可以通过此基类快速的创建一个自定义的弹窗）
 */

public class BaseDialog {

    protected Activity mActivity;
    protected PopupWindow mPopupWindow;

    public BaseDialog(Activity mActivity) {
        this.mActivity = mActivity;
    }

    /**
     * 初始化弹窗（自带弹出动画）
     * @param view
     */
    protected void initPopupWindow(View view) {
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setAnimationStyle(com.zl.zlibrary.R.style.WindowAnim);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1.0f);
            }
        });

    }

    /**
     * 初始化弹窗（需要自己设置弹出动画）
     * @param view
     */
    public void initPopupWindow(View view,int animationStyle) {
        mPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setAnimationStyle(animationStyle);

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setAlpha(1.0f);
            }
        });

    }

    public void showDialog(View view) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            setAlpha(0.6f);
        }
    }

    public void dismissDialog() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

    /**
     * 设置界面的透明度
     */
    protected void setAlpha(float f) {
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = f;
        window.setAttributes(attributes);
    }


}
