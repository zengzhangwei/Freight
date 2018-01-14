package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fuqianla.paysdk.FuQianLaPay;
import com.zl.freight.R;
import com.zl.zlibrary.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 18/1/14
 */
public class TopUpFragment extends BaseFragment {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_input_money)
    EditText etInputMoney;
    @BindView(R.id.tv_top_up_bt)
    TextView tvTopUpBt;
    Unbinder unbinder;

    public TopUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_up, container, false);
        unbinder = ButterKnife.bind(this, view);
        view.setClickable(true);
        initView();
        return view;
    }

    private void initView() {
        tvTitle.setText("充值");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_back, R.id.tv_top_up_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.tv_top_up_bt:
                topUp();
                break;
        }
    }

    /**
     * 充值
     */
    private void topUp() {
        String money = etInputMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            showToast("金额不能为空");
            return;
        }
        if (Integer.parseInt(money) > 0) {
            showToast("金额必须大于0");
            return;
        }
        //支付核心代码
        FuQianLaPay pay = new FuQianLaPay.Builder(mActivity)
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

    private OnPayListener onPayListener;

    public void setOnPayListener(OnPayListener onPayListener) {
        this.onPayListener = onPayListener;
    }

    public interface OnPayListener {
        void onPaySuccess(int money);

        void onPayError(String message);
    }

}
