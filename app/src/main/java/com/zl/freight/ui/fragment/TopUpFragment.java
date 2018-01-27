package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.ui.dialog.PayTypeDialog;
import com.zl.freight.utils.API;
import com.zl.freight.utils.MoneyUtils;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
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
    private String money;
    private PayTypeDialog typeDialog;

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

    /**
     * 更新余额
     */
    private void updateMoney() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("Moreorless", "0");
        params.put("Value", "" + money);

        SoapUtils.Post(mActivity, API.IntegralChange, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                if (onPayListener != null) {
                    //更新本地存储的积分
                    MoneyUtils.upDateMoney(mActivity, 0, Integer.parseInt(money) * 100);
                    getFragmentManager().popBackStack();
                    onPayListener.onPaySuccess(Integer.parseInt(money));
                }
            }
        });
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

        hideKeyboard(etInputMoney);

        money = etInputMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            showToast("金额不能为空");
            return;
        }
        if (Integer.parseInt(money) < 0) {
            showToast("金额必须大于0");
            return;
        }
        typeDialog = new PayTypeDialog(mActivity, Double.parseDouble(money));
        //这里的回调就只是支付宝的回调
        typeDialog.setOnReturnPayListener(new PayTypeDialog.OnReturnPayListener() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess() {
                showToast("支付成功");
                updateMoney();
            }
        });
        typeDialog.showDialog(tvTopUpBt);
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
