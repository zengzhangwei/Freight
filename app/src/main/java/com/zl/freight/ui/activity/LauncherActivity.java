package com.zl.freight.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.zl.freight.R;

/**
 * @author zhanglei
 * @date 17/12/6
 * 应用启动页
 */
public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
    }
}
