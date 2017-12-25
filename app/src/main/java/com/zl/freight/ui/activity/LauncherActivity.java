package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.blankj.utilcode.util.SPUtils;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.service.LocationService;
import com.zl.freight.utils.SpUtils;

/**
 * @author zhanglei
 * @date 17/12/6
 * 应用启动页
 */
public class LauncherActivity extends BaseActivity {
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                go();
            }
        }, 2000);
        String regId = SpUtils.getRegId(mActivity);
    }

    private void go() {
        boolean login = SpUtils.isLogin(mActivity);
        if (!login) { //没有登录时，根据role跳转到用户角色选择页或登录页
            int role = SpUtils.getRole(mActivity);
            Intent intent = new Intent(mActivity, LoginActivity.class);
            intent.putExtra("role", role);
            startActivity(intent);
            finish();
        } else { //已经是登录状态时直接进入主页
            int role = SpUtils.getRole(mActivity);
            Intent intent = null;
            switch (role) {
                case 1:
                    intent = new Intent(mActivity, MainActivity.class);
                    break;
                default:
                    intent = new Intent(mActivity, GoodsMainActivity.class);
                    break;
            }
            startActivity(intent);
            finish();
        }
    }
}
