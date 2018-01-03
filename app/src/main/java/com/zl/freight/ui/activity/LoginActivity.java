package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuqianla.paysdk.FuQianLaPay;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.CarUserBean;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.utils.GsonUtils;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

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
    private int role = API.DRIVER;
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
        role = intent.getIntExtra("role", API.DRIVER);
        isFinish = intent.getBooleanExtra(API.ISFINISH, false);
        setRole();
    }

    private void initView() {
        ivBack.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_clear_white));
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        if (!TextUtils.isEmpty(userData.getUserName()) && !TextUtils.isEmpty(userData.getPassWord())) {
            etInputAccount.setText(userData.getUserName());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 147:
                    role = data.getIntExtra("role", API.DRIVER);
                    setRole();
                    break;
            }
        }
    }

    private void setRole() {
        if (role == API.DRIVER) {
            tvTitle.setText("司机登录");
            tvTitleRight.setText("司机注册");
        } else {
            tvTitle.setText("货主登录");
            tvTitleRight.setText("货主注册");
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_login, R.id.tv_title_right, R.id.tv_see_see, R.id.tv_forget_password, R.id.tv_choose_role})
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
                Intent intent = new Intent(mActivity, URegisterActivity.class);
                intent.putExtra("role", role);
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
            //点击切换身份
            case R.id.tv_choose_role:
                if (isFinish) {
                    showToast("现在无法进行身份切换");
                    return;
                }
                startActivityForResult(new Intent(mActivity, RoleChooseActivity.class), 147);
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

            if (role == API.DRIVER) {
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
                    JSONArray array = new JSONArray(data);
                    BaseUserEntity baseUserEntity = GsonUtils.fromJson(array.optString(0), BaseUserEntity.class);
                    //根据用户id获取用户信息，如果是司机的话需要储存车长和车型
                    getUserData(baseUserEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                    hideDialog();
                    showToast("登录出现异常，请重试");
                }

            }
        });
    }

    /**
     * 页面跳转逻辑
     *
     * @param baseUserEntity
     */
    private void goToUi(BaseUserEntity baseUserEntity) {
        if (TextUtils.isEmpty(baseUserEntity.getUserRole())) {
            baseUserEntity.setUserRole("0");
        }
        //在这里判断是否越权登录(司机账号无法登录货主，反之则反之，管理员随便)
        if (!baseUserEntity.getUserRole().equals("0")) {
            if (!baseUserEntity.getUserRole().equals("" + role)) {
                showToast("不能越权登录，请选择正确身份");
                return;
            }
        }
        //判断是销毁页面还是跳转页面
        if (isFinish) {
            setResult(RESULT_OK);
            finish();
            return;
        }

        if (role == API.DRIVER) {
            startActivity(new Intent(mActivity, MainActivity.class));
        } else {
            startActivity(new Intent(mActivity, GoodsMainActivity.class));
        }

        finish();
        return;
    }

    /**
     * 获取用户信息
     */
    private void getUserData(final BaseUserEntity userData) {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", userData.getId());
        params.put("UserRole", userData.getUserRole());
        SoapUtils.Post(mActivity, API.ShowUserInfo, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", "获取用户信息失败");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    hideDialog();
                    JSONArray array = new JSONArray(data);
                    CarUserBean carUserBean = GsonUtils.fromJson(array.optString(0), CarUserBean.class);
                    //储存用户角色信息
                    SpUtils.setRole(mActivity, role);
                    //储存登录状态
                    SpUtils.setIsLogin(mActivity, true);
                    if (userData.getUserRole().equals(API.DRIVER + "")) {
                        userData.setCarLong(carUserBean.getCarLong());
                        userData.setCarType(carUserBean.getCarType());
                    }
                    if (TextUtils.isEmpty(userData.getUserRole())) {
                        userData.setUserRole("0");
                    }
                    //登录成功，储存用户信息
                    SpUtils.setUserData(mActivity, userData);
                    //进行界面的跳转逻辑
                    goToUi(userData);
                } catch (Exception e) {

                }
            }
        });
    }

}
