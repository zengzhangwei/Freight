package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.ui.fragment.StoreFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/21
 * 积分商场
 */
public class JiFenStoreActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.store_rl)
    RelativeLayout storeRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ji_fen_store);
        ButterKnife.bind(this);
        tvTitle.setText("积分商城");
        getSupportFragmentManager().beginTransaction().replace(R.id.store_rl, StoreFragment.newInstance()).commit();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
