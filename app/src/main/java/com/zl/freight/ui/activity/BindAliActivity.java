package com.zl.freight.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 18/1/25
 * 绑定支付宝账号
 */
public class BindAliActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_input_ali_account)
    EditText etInputAliAccount;
    @BindView(R.id.tv_bind_ali)
    TextView tvBindAli;
    @BindView(R.id.tv_current_ali_account)
    TextView tvCurrentAliAccount;
    private AlertDialog etDialog;
    private String aliAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_ali);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        String ali = getIntent().getStringExtra("ali");
        if (!TextUtils.isEmpty(ali)) {
            tvCurrentAliAccount.setText("* " + ali);
        } else {
            tvCurrentAliAccount.setText("* 无");
        }
        tvTitle.setText("绑定支付宝");
        View view = LayoutInflater.from(mActivity).inflate(R.layout.et_layout, null);
        final EditText et = view.findViewById(R.id.et_input);
        et.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        et.setHint("请输入登录密码");
        etDialog = new AlertDialog.Builder(this).setTitle("密码验证")
                .setView(view)
                .setPositiveButton("提交", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String data = et.getText().toString().trim();
                        if (TextUtils.isEmpty(data)) {
                            showToast("密码不能为空");
                            return;
                        }

                        if (!SpUtils.getUserData(mActivity).getPassWord().equals(data)) {
                            showToast("密码输入不正确");
                            return;
                        }

                        bindAli();

                    }
                })
                .setNegativeButton("取消", null)
                .create();
    }

    /**
     * 绑定支付宝账号
     */
    private void bindAli() {

        Map<String, String> params = new HashMap<>();
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("BankCard", aliAccount);
        SoapUtils.Post(mActivity, API.AddBankCard, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
                finish();
            }

            @Override
            public void onSuccess(String data) {
                showToast("绑定成功");
                finish();
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_bind_ali})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_bind_ali:
                aliAccount = etInputAliAccount.getText().toString().trim();
                if (TextUtils.isEmpty(aliAccount)) {
                    showToast("账号不能为空");
                    return;
                }
                etDialog.show();
                break;
        }
    }
}
