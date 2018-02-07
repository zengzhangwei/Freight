package com.zl.freight.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.GoodsListBean;
import com.zl.freight.ui.fragment.TopUpFragment;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.MoneyUtils;
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
public class GoodsDetailActivity extends BaseActivity implements TopUpFragment.OnPayListener {

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
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    @BindView(R.id.tv_info_money)
    TextView tvInfoMoney;
    @BindView(R.id.linear_info_money)
    AutoLinearLayout linearInfoMoney;
    @BindView(R.id.linear_zhuang_xie)
    AutoLinearLayout linearZhuangXie;
    private GoodsListBean data;
    private AlertDialog alertDialog;
    private AlertDialog messageDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("货物详情");
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        //如果是司机，则显示接单按钮
        if (userData.getUserRole().equals("" + API.DRIVER)) {
            tvJieDan.setVisibility(View.VISIBLE);
        } else {
            tvJieDan.setVisibility(View.GONE);
        }
        data = (GoodsListBean) getIntent().getSerializableExtra("data");
        tvOrigin.setText(data.getStartPlace());
        tvEndPoint.setText(data.getEndPlace());
        tvGoodsTime.setText("发布时间:" + data.getCreateAt());
        tvGoodsType.setText(data.getCodeName5());
        tvGoodsWeight.setText(data.getGoodsWeight() + data.getWeightUnit());
        tvCarLength.setText(data.getCodeName1() + "米");
        tvGoodsIf.setText(data.getCodeName2());
        tvCarType.setText(data.getCodeName());
        ImageLoader.loadUserIcon(mActivity, data.getUserIcon(), ivGoodsUserIcon);
        ivGoodsUserName.setText(data.getRealName());
        tvGoodsName.setText(data.getGoodName());

        if (!TextUtils.isEmpty(data.getCodeName3())) {
            linearZhuangXie.setVisibility(View.VISIBLE);
            tvZhuangXieXuQiu.setText(data.getCodeName3());
        }

        if (Double.valueOf(data.getInfoMoney()) > 0) {
            tvInfoMoney.setText(data.getInfoMoney() + "元");
            linearInfoMoney.setVisibility(View.VISIBLE);
        }

        alertDialog = new AlertDialog.Builder(mActivity).setTitle("系统提示")
                .setMessage("该订单需要支付 " + data.getInfoMoney() + "元 信息费用是否要继续接单，点击继续后将从账户余额中扣除该笔费用。")
                .setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        next();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        messageDialog = new AlertDialog.Builder(mActivity).setMessage("余额不足请充值")
                .setPositiveButton("去充值", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        goTopUp();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }

    /**
     * 去充值
     */
    private void goTopUp() {
//        TopUpFragment topUpFragment = new TopUpFragment();
//        topUpFragment.setOnPayListener(this);
//        getSupportFragmentManager().beginTransaction().addToBackStack("topup")
//                .replace(R.id.goods_detail_rl, topUpFragment).commit();
        startActivity(new Intent(mActivity, MyMoneyActivity.class));
    }

    /**
     * 继续接单
     */
    private void next() {
        String integral = SpUtils.getUserData(mActivity).getIntegral();
        int i = Integer.parseInt(integral);
        //余额不足提示用户去充值
        if (i < Double.parseDouble(data.getInfoMoney()) * API.ratio) {
            messageDialog.show();
            return;
        }
        jieDan();
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
                Intent intent = new Intent(mActivity, LookGoodsLocationActivity.class);
                intent.putExtra("data",data);
                startActivity(intent);
                break;
            //联系货主
            case R.id.iv_call:
                SystemUtils.call(mActivity, data.getUserName());
                break;
            //接单
            case R.id.tv_jie_dan:
                //判断是否需要支付信息费
                if (Double.valueOf(data.getInfoMoney()) > 0) { //需要支付信息费
                    alertDialog.show();
                    return;
                }
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
        params.put("SendId", "" + data.getId());
        showDialog("获取订单中...");
        SoapUtils.Post(mActivity, API.ReceiveSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                hideDialog();
                showToast(error);
            }

            @Override
            public void onSuccess(String d) {
                hideDialog();
                showToast("接单成功");
                if (Double.valueOf(data.getInfoMoney()) > 0) { //修改本地帐户余额
                    int v = (int) Double.parseDouble(data.getInfoMoney());
                    MoneyUtils.upDateMoney(mActivity, 1, v);
                }
                finish();
            }
        });

    }

    @Override
    public void onPaySuccess(int money) {
        showToast("成功充值：" + money + "元");
    }

    @Override
    public void onPayError(String message) {
        showToast(message);
    }
}
