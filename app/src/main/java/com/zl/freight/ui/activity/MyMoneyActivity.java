package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuqianla.paysdk.FuQianLa;
import com.fuqianla.paysdk.FuQianLaPay;
import com.fuqianla.paysdk.bean.FuQianLaResult;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.ui.fragment.TransactionLogFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/11
 * 我的钱包
 */
public class MyMoneyActivity extends BaseActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_money);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {

    }

    private void initView() {
        tvTitle.setText(R.string.my_money);
        tvTitleRight.setText("交易记录");
        logFragment = TransactionLogFragment.newInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //返回结果
        if (requestCode == FuQianLa.REQUESTCODE
                && resultCode == FuQianLa.RESULTCODE
                && data != null) {
            //result结果包括code和message
            FuQianLaResult result = data.getParcelableExtra(FuQianLa.PAYRESULT_KEY);
            //支付成功
            if (result.payCode.equals("9000")) {

            }
            showToast(result.payMessage);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.iv_back, R.id.tv_title_right, R.id.tv_top_up_bt, R.id.tv_ti_xian_bt})
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

                break;
        }
    }

    /**
     * 充值
     */
    private void topUp() {
        //支付核心代码
        FuQianLaPay pay = new FuQianLaPay.Builder(this)
                .alipay(true)//支付宝通道
                .wxpay(true)//微信通道
                .orderID(getOutTradeNo())//订单号
                .amount(0.01)//金额
                .subject("商品名称")
                .notifyUrl("https://api.fuqian.la/pay-adapter/services/notify")
                .build();
        pay.startPay();
    }

    /**
     * 要求外部订单号必须唯一。
     *
     * @return
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
//        key = key + r.nextInt();
//        key = key.substring(0, 15);
        key = key + r.nextInt(100000);
        return key;
    }
}
