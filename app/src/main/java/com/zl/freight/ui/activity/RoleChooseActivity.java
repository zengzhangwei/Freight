package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.NavigationUtils;
import com.zl.freight.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/7
 * 用户角色选择页（货主、车主）
 */
public class RoleChooseActivity extends BaseActivity {

    @BindView(R.id.rc_driver)
    CardView rcDriver;
    @BindView(R.id.rc_goods_master)
    CardView rcGoodsMaster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_choose);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rc_iv_finish, R.id.rc_driver, R.id.rc_goods_master})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //退出
            case R.id.rc_iv_finish:
                finish();
                break;
            //司机入口
            case R.id.rc_driver:
                startLogin(API.DRIVER);
                break;
            //货主入口
            case R.id.rc_goods_master:
                startLogin(API.GOODS);
                break;
        }
    }

    private void startLogin(int role) {
        Intent intent = new Intent();
        intent.putExtra("role", role);
        setResult(RESULT_OK, intent);
        finish();
    }
}
