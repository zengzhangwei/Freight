package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;

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
    public static final String DRIVER = "车主";
    public static final String GOODSMASTER = "货主";

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
                startLogin(DRIVER);
                break;
            //货主入口
            case R.id.rc_goods_master:
                startLogin(GOODSMASTER);
                break;
        }
    }

    private void startLogin(String role) {
        Intent intent = new Intent(mActivity, LoginActivity.class);
        intent.putExtra("role", role);
        startActivity(intent);
        finish();
    }
}
