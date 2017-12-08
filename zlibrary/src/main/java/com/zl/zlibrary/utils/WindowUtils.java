package com.zl.zlibrary.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by zhanglei on 2017\12\8 0008.
 * 窗口工具类
 */

public class WindowUtils {

    /**
     * 设置界面的透明度
     */
    public static void setAlpha(Activity activity,float f) {
        Window window = activity.getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.alpha = f;
        window.setAttributes(attributes);
    }

}
