package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 17/12/28
 */
public class OftenFragment extends BaseFragment {


    @BindView(R.id.often_list_view)
    ListView oftenListView;
    @BindView(R.id.often_trl)
    TwinklingRefreshLayout oftenTrl;
    Unbinder unbinder;
    private List<String> mList = new ArrayList<>();
    private UniversalAdapter<String> mAdapter;

    public OftenFragment() {
        // Required empty public constructor
    }

    public static OftenFragment newInstance() {

        Bundle args = new Bundle();

        OftenFragment fragment = new OftenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_often, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initListener();
        return view;
    }

    private void initListener() {
        oftenTrl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
            }
        });

        oftenListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private void initView() {
        mAdapter = new UniversalAdapter<String>(mActivity, mList, R.layout.driver_list_location_item) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {

            }
        };
        oftenListView.setAdapter(mAdapter);
        oftenTrl.setHeaderView(new ProgressLayout(mActivity));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
