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
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.base.BaseFragment;

import java.util.HashMap;
import java.util.Map;

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
    private String myMoney;

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
        initData();
        return view;
    }

    private void initData() {

    }

    private void initView() {
        tvTitle.setText("提现");
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
     * 提现的方法,这个方法主要是为了验证
     */
    private void tiXian() {
        //TODO 在这里判断是否绑定了支付宝

        final String money = etInputMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            showToast("金额不能为空");
            return;
        }

        if (Integer.parseInt(money) < 100) {
            showToast("金额必须大于100");
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("Id", SpUtils.getUserData(mActivity).getId());
        SoapUtils.Post(mActivity, API.AbleCasch, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                myMoney = data;
                if (TextUtils.isEmpty(myMoney)) {
                    showToast("未获取到账户余额");
                    return;
                }

                try {
                    int i = Integer.parseInt(myMoney);
                    int m = Integer.parseInt(money);
                    if (i >= 100) {
                        if (m > i) {
                            showToast("本次最多可以提现" + i + "元");
                            return;
                        }

                        commit(m);

                    } else {
                        showToast("账户余额为" + i + ",满100元才可提现");
                    }
                } catch (Exception e) {
                    showToast("获取账户余额时出错");
                    return;
                }
            }
        });

    }

    /**
     * 在这里进行真正的提现
     */
    private void commit(final int money) {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("Money", money + "");
        SoapUtils.Post(mActivity, API.DoCasch, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                showToast("提现成功，请注意查收");
                if (onTiXianListener != null) {
                    onTiXianListener.tiXianSuccess(money);
                }
            }
        });
    }

    private OnTiXianListener onTiXianListener;

    public void setOnTiXianListener(OnTiXianListener onTiXianListener) {
        this.onTiXianListener = onTiXianListener;
    }

    public interface OnTiXianListener {
        void tiXianSuccess(int money);
    }
}
