package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * @date 17/12/30
 */
public class MyNewsFragment extends BaseFragment {


    @BindView(R.id.news_listView)
    ListView newsListView;
    @BindView(R.id.news_trl)
    TwinklingRefreshLayout newsTrl;
    Unbinder unbinder;

    private UniversalAdapter<String> mAdapter;
    private List<String> mList = new ArrayList<>();

    public MyNewsFragment() {
        // Required empty public constructor
    }

    public static MyNewsFragment newInstance() {

        Bundle args = new Bundle();

        MyNewsFragment fragment = new MyNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        newsTrl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                newsTrl.finishRefreshing();
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            mList.add("");
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mAdapter = new UniversalAdapter<String>(mActivity, mList, R.layout.top_item) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                holder.getView(R.id.arl_type).setVisibility(View.VISIBLE);
                Spanned spanned1 = Html.fromHtml("文章状态：<font color=\"#ce2538\">未支付</font>");
                Spanned spanned2 = Html.fromHtml("文章状态：<font color=\"#079605\">已支付</font>");
                holder.setText(R.id.tv_news_type, spanned1);
            }
        };
        newsListView.setAdapter(mAdapter);
        newsTrl.setHeaderView(new ProgressLayout(mActivity));
        newsTrl.setEnableLoadmore(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
