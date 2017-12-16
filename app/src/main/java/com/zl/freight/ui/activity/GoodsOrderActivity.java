package com.zl.freight.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.utils.API;
import com.zl.zlibrary.adapter.RecyclerAdapter;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.view.MRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/15
 * 货主的订单页
 */
public class GoodsOrderActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.my_order_mrlv)
    MRefreshRecyclerView myOrderMrlv;
    private List<String> mList = new ArrayList<>();
    private RecyclerAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            mList.add("");
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        tvTitle.setText(R.string.my_order);
        mAdapter = new RecyclerAdapter<String>(mActivity, mList, R.layout.goods_order_item_layout) {

            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                holder.getView(R.id.tv_look_location).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mActivity, LookDriverActivity.class);
                        intent.putExtra(API.LATITUDE, 0.00);
                        intent.putExtra(API.LONGITUDE, 0.00);
                        startActivity(intent);
                    }
                });
            }
        };
        myOrderMrlv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        myOrderMrlv.setAdapter(mAdapter);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
