package com.zl.freight.ui.activity;

import android.app.Activity;
import android.os.Bundle;

import com.zl.freight.R;

/**
 * @author zhanglei
 * @date 17/12/7
 * 用户角色选择页（货主、车主）
 */
public class RoleChooseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_choose);
    }
}
