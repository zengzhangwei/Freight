package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.CarUserBean;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.zlibrary.utils.SystemUtils;

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
    private CarUserBean carUser;

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
        carUser = (CarUserBean) getIntent().getSerializableExtra(API.CARUSER);
        if (carUser != null) {
            tvRealName.setText("真实姓名：" + carUser.getRealName());
            tvDriverPhone.setText(carUser.getUserName());
            tvDriverCarType.setText(carUser.getCodeName() + "米/" + carUser.getCodeName1());
            ImageLoader.loadUserIcon(mActivity, carUser.getUserIcon(), driverIcon);
        }
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
                Intent intent = new Intent(mActivity, LookDriverActivity.class);
                intent.putExtra(API.LATITUDE, carUser.getCarX());
                intent.putExtra(API.LONGITUDE, carUser.getCarY());
                intent.putExtra(API.USERID, carUser.getUserId());
                startActivity(intent);
                break;
            //给司机打电话
            case R.id.tv_call:
                if (carUser == null) return;
                SystemUtils.call(mActivity, carUser.getUserName());
                break;
            //给司机发短信
            case R.id.tv_send_message:
                if (carUser == null) return;
                SystemUtils.sendSms(mActivity, carUser.getUserName());
                break;
        }
    }
}
