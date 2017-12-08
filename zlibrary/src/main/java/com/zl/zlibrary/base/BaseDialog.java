package com.zl.zlibrary.base;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by zhanglei on 2017\12\8 0008.
 */

public class BaseDialog {

    protected Activity mActivity;

    public BaseDialog(Activity mActivity) {
        this.mActivity = mActivity;
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
