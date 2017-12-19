package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/7
 * 用户登录页
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_input_account)
    EditText etInputAccount;
    @BindView(R.id.et_input_password)
    EditText etInputPassword;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.tv_forget_password)
    TextView tvForgetPassword;
    private int role;
    private boolean isFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        Intent intent = getIntent();
        role = intent.getIntExtra("role", 0);
        isFinish = intent.getBooleanExtra(API.ISFINISH, false);
    }

    private void initView() {
        ivBack.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_clear_white));
        tvTitle.setText("登录");
        tvTitleRight.setText("注册");
    }

    @OnClick({R.id.iv_back, R.id.tv_login, R.id.tv_title_right, R.id.tv_see_see, R.id.tv_forget_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                finish();
                break;
            //登录
            case R.id.tv_login:
                startMain(true);
                break;
            //注册
            case R.id.tv_title_right:
                Intent intent;
                if (role == 0) {
                    intent = new Intent(mActivity, RegisterActivity.class);
                } else {
                    intent = new Intent(mActivity, GoodsRegisterActivity.class);
                }
                startActivity(intent);
                break;
            //随便看看
            case R.id.tv_see_see:
                startMain(false);
                break;
            //忘记密码
            case R.id.tv_forget_password:
                startActivity(new Intent(mActivity, ForgetPasswordActivity.class));
                break;
        }
    }

    /**
     * 根据角色的不同进入不同的主页
     */
    private void startMain(boolean isLogin) {
        SpUtils.setRole(mActivity, role);
        //现在是登录状态
        SpUtils.setIsLogin(mActivity, isLogin);

        if (isFinish) {
            setResult(RESULT_OK);
            finish();
            return;
        }
        if (role == 0) {
            startActivity(new Intent(mActivity, MainActivity.class));
        } else {
            startActivity(new Intent(mActivity, GoodsMainActivity.class));
        }
        finish();
    }
}
