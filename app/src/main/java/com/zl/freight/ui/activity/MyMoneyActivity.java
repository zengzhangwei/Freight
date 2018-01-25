package com.zl.freight.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BasePayLogEntity;
import com.zl.freight.ui.fragment.TiXianFragment;
import com.zl.freight.ui.fragment.TopUpFragment;
import com.zl.freight.ui.fragment.TransactionLogFragment;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/11
 * 我的钱包
 */
public class MyMoneyActivity extends BaseActivity implements TopUpFragment.OnPayListener, TiXianFragment.OnTiXianListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_money_count)
    TextView tvMoneyCount;
    @BindView(R.id.tv_top_up_bt)
    TextView tvTopUpBt;
    @BindView(R.id.tv_ti_xian_bt)
    TextView tvTiXianBt;
    private TransactionLogFragment logFragment;
    private AlertDialog helperDialog;
    private TopUpFragment topUpFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_money);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        tvMoneyCount.setText(SpUtils.getUserData(mActivity).getIntegral());
    }

    private void initView() {
        tvTitle.setText(R.string.my_money);
        tvTitleRight.setText("充值提现记录");
        logFragment = TransactionLogFragment.newInstance();
        helperDialog = new AlertDialog.Builder(mActivity).setTitle("使用帮助")
                .setMessage("1元 == 100积分，提现时需要收取手续费，最低提现额为100元也就是10000积分，提现时请输入大于等于100的整数")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //返回结果
        if (topUpFragment != null) {
            topUpFragment.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.iv_back, R.id.tv_title_right, R.id.tv_top_up_bt, R.id.tv_helper, R.id.tv_ti_xian_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                finish();
                break;
            //交易记录
            case R.id.tv_title_right:
                getSupportFragmentManager().beginTransaction().addToBackStack("132").replace(R.id.my_money_rl, logFragment).commit();
                break;
            //充值
            case R.id.tv_top_up_bt:
                topUp();
                break;
            //提现
            case R.id.tv_ti_xian_bt:
                tiXian();
                break;
            //使用帮助说明
            case R.id.tv_helper:
                helperDialog.show();
                break;
        }
    }

    /**
     * 提现
     */
    private void tiXian() {
        TiXianFragment tiXianFragment = new TiXianFragment();
        tiXianFragment.setOnTiXianListener(this);
        getSupportFragmentManager().beginTransaction().addToBackStack("topup")
                .replace(R.id.my_money_rl, tiXianFragment).commit();
    }

    /**
     * 充值
     */
    private void topUp() {
        topUpFragment = new TopUpFragment();
        topUpFragment.setOnPayListener(this);
        getSupportFragmentManager().beginTransaction().addToBackStack("topup")
                .replace(R.id.my_money_rl, topUpFragment).commit();
    }

    /**
     * 支付成功
     *
     * @param money
     */
    @Override
    public void onPaySuccess(int money) {
        tvMoneyCount.setText(SpUtils.getUserData(mActivity).getIntegral());
        insertPayLog(money);
    }

    /**
     * 支付失败
     *
     * @param message
     */
    @Override
    public void onPayError(String message) {
        showToast(message);
    }

    /**
     * 提现的回调方法
     *
     * @param money
     */
    @Override
    public void tiXianSuccess(int money) {
        
    }

    private void insertPayLog(int money) {
        BasePayLogEntity entity = new BasePayLogEntity();
        entity.setPayMoney(money);
        entity.setUserId(Integer.parseInt(SpUtils.getUserData(mActivity).getId()));
        entity.setUserName(SpUtils.getUserData(mActivity).getUserName());
        Map<String, String> params = new HashMap<>();
        params.put("PayLogJson", GsonUtils.toJson(entity));
        SoapUtils.Post(mActivity, API.InsertPayLog, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", error);
            }

            @Override
            public void onSuccess(String data) {
                Log.e("error", data);
            }
        });
    }
}
