package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.RegexUtils;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.CarUserBean;
import com.zl.freight.mode.UserBean;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.SendCodeUtils;
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
 * @date 17/12/15
 */
public class ForgetPasswordActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_input_phone)
    EditText etInputPhone;
    @BindView(R.id.et_input_code)
    EditText etInputCode;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.et_input_new_password)
    EditText etInputNewPassword;
    @BindView(R.id.tv_commit_update)
    TextView tvCommitUpdate;
    private String sendCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        tvTitle.setText(R.string.update_password);
    }

    @OnClick({R.id.iv_back, R.id.tv_send_code, R.id.tv_commit_update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_send_code:
                sendCode();
                break;
            case R.id.tv_commit_update:
                commit();
                break;
        }
    }

    private void sendCode() {
        String trim = etInputPhone.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            showToast("请输入手机号");
            return;
        }
        SendCodeUtils.sendCode(trim, tvSendCode, new SoapCallback() {

            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String data) {
                sendCode = data;
            }
        });
    }

    /**
     * 提交修改密码
     */
    private void commit() {
        String phone = etInputPhone.getText().toString().trim();
        String password = etInputNewPassword.getText().toString().trim();
        String code = etInputCode.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            showToast("请输入手机号");
            return;
        }

        if (!RegexUtils.isMobileExact(phone)) {
            showToast("手机号格式不对");
            return;
        }

        if (TextUtils.isEmpty(sendCode)) {
            showToast("验证码还未发送");
            return;
        }

        if (TextUtils.isEmpty(code)) {
            showToast("请输入验证码");
            return;
        }

        if (!sendCode.equals(code)) {
            showToast("验证码输入不正确");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            showToast("请输入密码");
            return;
        }

        if (password.length() != 6) {
            showToast("密码长度不符合标准");
            return;
        }

        showDialog("修改密码中...");
        Map<String, String> params = new HashMap<>();
        params.put("username", phone);
        params.put("newPassword", password);
        SoapUtils.Post(mActivity, API.ForgetPassword, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                hideDialog();
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                hideDialog();
                showToast("更新成功");
                finish();
            }
        });

    }
}
