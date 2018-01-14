package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.freight.ui.activity.LookDriverActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.adapter.RecyclerAdapter;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * 货主的运单页
 */
public class GoodsOrderListFragment extends BaseFragment {


    @BindView(R.id.my_order_mrlv)
    RecyclerView myOrderMrlv;
    @BindView(R.id.my_order_trl)
    TwinklingRefreshLayout myOrderTrl;
    Unbinder unbinder;
    private List<String> mList = new ArrayList<>();
    private RecyclerAdapter<String> mAdapter;

    public GoodsOrderListFragment() {
        // Required empty public constructor
    }

    public static GoodsOrderListFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        GoodsOrderListFragment fragment = new GoodsOrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        myOrderTrl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            mList.add("");
        }
        mAdapter.notifyDataSetChanged();
        getListData();
    }

    /**
     * 获取列表数据
     */
    private void getListData() {
        Map<String, String> params = new HashMap<>();
        params.put("UserRole", "2");
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("SendState", "0");
        SoapUtils.Post(mActivity, API.GetSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error","");
            }

            @Override
            public void onSuccess(String data) {
                Log.e("error","onSuccess");
            }
        });
    }

    private void initView() {
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
        myOrderTrl.setHeaderView(new ProgressLayout(mActivity));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
