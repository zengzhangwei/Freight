package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.GoodsListBean;
import com.zl.freight.ui.fragment.UpdateSendFragment;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateSendActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    private UpdateSendFragment updateSendFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_send);
        ButterKnife.bind(this);
        tvTitle.setText("货物详情");
        GoodsListBean listBean = (GoodsListBean) getIntent().getSerializableExtra("listBean");
        updateSendFragment = UpdateSendFragment.newInstance(listBean);
        getSupportFragmentManager().beginTransaction().replace(R.id.update_send_rl, updateSendFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateSendFragment.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
