package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.zlibrary.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 18/1/2
 */
public class MyGoodsListFragment extends BaseFragment {

    @BindView(R.id.my_goods_listView)
    ListView myGoodsListView;
    @BindView(R.id.my_goods_trl)
    TwinklingRefreshLayout myGoodsTrl;
    Unbinder unbinder;

    public MyGoodsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_goods_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initData() {

    }

    private void initListener() {
        myGoodsTrl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                getListData(true);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                getListData(false);
            }
        });
    }

    private void initView() {
        myGoodsTrl.setHeaderView(new ProgressLayout(mActivity));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 获取列表数据
     */
    private void getListData(boolean b) {
        if (b) {
            myGoodsTrl.finishRefreshing();
        } else {
            myGoodsTrl.finishLoadmore();
        }
    }
}
