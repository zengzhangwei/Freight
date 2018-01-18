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
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhangleI
 * @date 18/1/14
 * 提现Fragment
 */
public class TiXianFragment extends BaseFragment {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_input_money)
    EditText etInputMoney;
    @BindView(R.id.tv_ti_xian_bt)
    TextView tvTiXianBt;
    Unbinder unbinder;
    private int count;

    public TiXianFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ti_xian, container, false);
        unbinder = ButterKnife.bind(this, view);
        view.setClickable(true);
        initView();
        return view;
    }

    private void initView() {
        tvTitle.setText("提现");
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        count = Integer.parseInt(userData.getIntegral()) / 100;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_back, R.id.tv_ti_xian_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                getFragmentManager().popBackStack();
                break;
            case R.id.tv_ti_xian_bt:
                tiXian();
                break;
        }
    }

    /**
     * 提现的方法
     */
    private void tiXian() {
        String money = etInputMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            showToast("金额不能为空");
            return;
        }

        if (Integer.parseInt(money) < 100) {
            showToast("金额必须大于100");
            return;
        }

        if (Integer.parseInt(money) > count) {
            showToast("本次最多可以提现" + count + "元");
            return;
        }
//        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do","app_id","your private_key","json","GBK","alipay_public_key","RSA2");
    }

    private OnTiXianListener onTiXianListener;

    public void setOnTiXianListener(OnTiXianListener onTiXianListener) {
        this.onTiXianListener = onTiXianListener;
    }

    public interface OnTiXianListener {
        void tiXianSuccess(int money);
    }
}
