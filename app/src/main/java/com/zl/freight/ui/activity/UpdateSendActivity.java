package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.GoodsListBean;
import com.zl.freight.ui.fragment.UpdateSendFragment;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;

import java.util.HashMap;
import java.util.Map;

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
    private GoodsListBean listBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_send);
        ButterKnife.bind(this);
        tvTitle.setText("货物详情");
        tvTitleRight.setText("关闭订单");
        listBean = (GoodsListBean) getIntent().getSerializableExtra("listBean");
        updateSendFragment = UpdateSendFragment.newInstance(listBean);
        getSupportFragmentManager().beginTransaction().replace(R.id.update_send_rl, updateSendFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateSendFragment.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.iv_back, R.id.tv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_title_right:
                deleteOrder();
                break;
        }
    }

    /**
     * 删除订单
     */
    private void deleteOrder() {
        if (listBean == null) return;
        if (listBean.getIsdelete().equals("2")) {
            showToast("该订单已关闭");
            return;
        }
        //TODO 在这里要加上一个判断，判断该订单是否被接，若被接则不能关闭
        Map<String, String> params = new HashMap<>();
        params.put("sendId", listBean.getId());
        SoapUtils.Post(mActivity, API.CloseSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                showToast("关闭成功");
                finish();
            }
        });
    }
}
