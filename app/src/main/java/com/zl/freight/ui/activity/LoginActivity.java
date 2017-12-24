package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Request;

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
        role = intent.getIntExtra("role", -1);
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
    private void startMain(final boolean isLogin) {

        //如果是随便看看
        if (!isLogin) {
            if (isFinish) {
                setResult(RESULT_OK);
                finish();
                return;
            }

            if (role == 1) {
                startActivity(new Intent(mActivity, MainActivity.class));
            } else {
                startActivity(new Intent(mActivity, GoodsMainActivity.class));
            }

            finish();
            return;
        }

        String userName = etInputAccount.getText().toString().trim();
        String password = etInputPassword.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            showToast("手机号不能为空");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("密码不能为空");
            return;
        }

//        if (password.length() != 6) {
//            showToast("密码不正确");
//            return;
//        }

        Map<String, String> params = new HashMap<>();
        params.put("UserName", userName);
        params.put("PassWord", password);
        showDialog("登录中...");
        SoapUtils.Post(mActivity, API.Login, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                hideDialog();
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                try {
                    hideDialog();
                    JSONArray array = new JSONArray(data);
                    BaseUserEntity baseUserEntity = GsonUtils.fromJson(array.optString(0), BaseUserEntity.class);
                    //登录成功，储存用户信息
                    SpUtils.setUserData(mActivity, baseUserEntity);
                    //储存用户角色信息
                    SpUtils.setRole(mActivity, role);
                    //储存登录状态
                    SpUtils.setIsLogin(mActivity, isLogin);
                    //储存账号密码

                    if (isFinish) {
                        setResult(RESULT_OK);
                        finish();
                        return;
                    }

                    if (role == 1) {
                        startActivity(new Intent(mActivity, MainActivity.class));
                    } else {
                        startActivity(new Intent(mActivity, GoodsMainActivity.class));
                    }

                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast("登录出现异常，请重试");
                }

            }
        });
    }
}
