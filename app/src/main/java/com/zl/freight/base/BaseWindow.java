package com.zl.freight.base;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/7/28.
 */

public class BaseWindow {

    protected Activity mActivity;

    public BaseWindow(Activity mActivity) {
        this.mActivity = mActivity;
    }

    protected void setAlpha(float alpha) {
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = alpha;
        window.setAttributes(params);
    }

    protected OnDismissListener onDismissListener;

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public interface OnDismissListener {
        void onDismiss();
    }

}
