package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.utils.SystemUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author zhanglei
 * @date 17/12/15
 * 货源详情页
 */
public class GoodsDetailActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_origin)
    TextView tvOrigin;
    @BindView(R.id.iv_arrows)
    ImageView ivArrows;
    @BindView(R.id.tv_end_point)
    TextView tvEndPoint;
    @BindView(R.id.tv_goods_location)
    TextView tvGoodsLocation;
    @BindView(R.id.tv_goods_time)
    TextView tvGoodsTime;
    @BindView(R.id.tv_goods_type)
    TextView tvGoodsType;
    @BindView(R.id.tv_goods_weight)
    TextView tvGoodsWeight;
    @BindView(R.id.tv_goods_if)
    TextView tvGoodsIf;
    @BindView(R.id.tv_car_length)
    TextView tvCarLength;
    @BindView(R.id.tv_car_type)
    TextView tvCarType;
    @BindView(R.id.tv_zhuang_xie_xu_qiu)
    TextView tvZhuangXieXuQiu;
    @BindView(R.id.iv_goods_user_icon)
    CircleImageView ivGoodsUserIcon;
    @BindView(R.id.iv_goods_user_name)
    TextView ivGoodsUserName;
    @BindView(R.id.iv_call)
    ImageView ivCall;
    @BindView(R.id.tv_jie_dan)
    TextView tvJieDan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        //如果是司机，则显示接单按钮
        if (userData.getUserRole().equals("" + API.DRIVER)) {
            tvJieDan.setVisibility(View.VISIBLE);
        } else {
            tvJieDan.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_goods_location, R.id.iv_call, R.id.tv_jie_dan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                finish();
                break;
            //查看货源位置
            case R.id.tv_goods_location:
                startActivity(new Intent(mActivity, LookGoodsLocationActivity.class));
                break;
            //联系货主
            case R.id.iv_call:
                SystemUtils.call(mActivity, "15075993917");
                break;
            //接单
            case R.id.tv_jie_dan:
                jieDan();
                break;
        }
    }

    /**
     * 接单
     */
    private void jieDan() {
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        Map<String, String> params = new HashMap<>();
        params.put("UserId", userData.getId());
        params.put("SendId", "");
        showDialog("获取订单中...");
        SoapUtils.Post(mActivity, API.ReceiveSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                hideDialog();
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                hideDialog();
                showToast("接单成功");
            }
        });

    }
}
