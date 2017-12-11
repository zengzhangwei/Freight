package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.ui.fragment.TransactionLogFragment;

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
                break;
            //提现
            case R.id.tv_ti_xian_bt:

                break;
        }
    }
}
