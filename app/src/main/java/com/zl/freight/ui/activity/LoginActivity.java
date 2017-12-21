package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.HttpUtils;

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

        new Thread() {
            @Override
            public void run() {
                // 命名空间
                String nameSpace = "http://tempuri.org/";
                // 调用的方法名称
                String methodName = "Login";
                // EndPoint
                String endPoint = "http://172.16.18.17/WebService1.asmx";
                // SOAP Action
                String soapAction = nameSpace + methodName;

                // 指定WebService的命名空间和调用的方法名
                SoapObject rpc = new SoapObject(nameSpace, methodName);

                // 设置需调用WebService接口需要传入的两个参数mobileCode、userId
                rpc.addProperty("UserName", "admin");
                rpc.addProperty("PassWord", "1");

                // 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.bodyOut = rpc;
                // 设置是否调用的是dotNet开发的WebService
                envelope.dotNet = true;

                HttpTransportSE transport = new HttpTransportSE(endPoint);
                try {
                    // 调用WebService
                    transport.call(soapAction, envelope);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JSONObject object = new JSONObject();
                // 获取返回的数据
                try {
                    SoapPrimitive response = (SoapPrimitive) envelope.getResponse();
                    String result = response.toString();
                    Log.e("result", result);
                } catch (Exception e) {

                }
                // 获取返回的结果
            }
        }.start();


//        SpUtils.setRole(mActivity, role);
//        //现在是登录状态
//        SpUtils.setIsLogin(mActivity, isLogin);
//
//        if (isFinish) {
//            setResult(RESULT_OK);
//            finish();
//            return;
//        }
//
//        finish();
    }
}
