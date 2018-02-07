package com.zl.freight.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BasePayLogEntity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.CarUserBean;
import com.zl.freight.ui.fragment.TiXianFragment;
import com.zl.freight.ui.fragment.TopUpFragment;
import com.zl.freight.ui.fragment.TransactionLogFragment;
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        final BaseUserEntity userData = SpUtils.getUserData(mActivity);
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
                    JSONArray array = new JSONArray(data);
                    CarUserBean carUserBean = GsonUtils.fromJson(array.optString(0), CarUserBean.class);
                    tvMoneyCount.setText(carUserBean.getIntegral());
                    userData.setIntegral(carUserBean.getIntegral());
                    SpUtils.setUserData(mActivity, userData);
                } catch (Exception e) {

                }
            }
        });

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
        insertTiXianLog(money);
    }

    /**
     * 插入提现记录
     *
     * @param money
     */
    private void insertTiXianLog(int money) {
        int count = money * 100;
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        int i = Integer.parseInt(userData.getIntegral());
        int m = i - count;
        //更新显示
        tvMoneyCount.setText(m + "");
        userData.setIntegral(m + "");
        //更新本地储存的余额
        SpUtils.setUserData(mActivity, userData);

        BasePayLogEntity entity = new BasePayLogEntity();
        entity.setCashValue(money);
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

    /**
     * 插入充值记录
     *
     * @param money
     */
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
