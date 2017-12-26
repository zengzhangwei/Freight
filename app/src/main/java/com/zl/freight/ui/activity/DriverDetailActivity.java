package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.zlibrary.utils.SystemUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author zhanglei
 * @date 17/12/22
 * 司机详情页
 */
public class DriverDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.driver_icon)
    CircleImageView driverIcon;
    @BindView(R.id.tv_real_name)
    TextView tvRealName;
    @BindView(R.id.tv_driver_phone)
    TextView tvDriverPhone;
    @BindView(R.id.tv_driver_car_type)
    TextView tvDriverCarType;
    @BindView(R.id.tv_driver_location)
    TextView tvDriverLocation;
    @BindView(R.id.tv_driver_order)
    TextView tvDriverOrder;
    @BindView(R.id.tv_call)
    TextView tvCall;
    @BindView(R.id.tv_send_message)
    TextView tvSendMessage;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_detail);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    /**
     * 初始化数据，获取用户信息
     */
    private void initData() {
        username = getIntent().getStringExtra(API.USERNAME);
        if (TextUtils.isEmpty(username)) return;
        Map<String, String> params = new HashMap<>();
        params.put("UserName", username);
        SoapUtils.Post(mActivity, API.ShowUserInfo, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("String", error);
            }

            @Override
            public void onSuccess(String data) {
                Log.e("String", data);
            }
        });
    }

    private void initView() {
        tvTitle.setText("司机详情");
    }

    @OnClick({R.id.iv_back, R.id.tv_driver_location, R.id.tv_call, R.id.tv_send_message})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                finish();
                break;
            //查看司机位置
            case R.id.tv_driver_location:
                startActivity(new Intent(mActivity, LookDriverActivity.class));
                break;
            //给司机打电话
            case R.id.tv_call:
                SystemUtils.call(mActivity, "15075993917");
                break;
            //给司机发短信
            case R.id.tv_send_message:
                SystemUtils.sendSms(mActivity, "15075993917");
                break;
        }
    }
}