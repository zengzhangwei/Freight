package com.zl.freight.ui.fragment;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.freight.ui.activity.NewsDetailActivity;
import com.zl.freight.ui.activity.PublishNewsActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.zlibrary.adapter.RecyclerAdapter;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.view.MRefreshRecyclerView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 17/12/7
 * 头条页面
 */
public class TopLineFragment extends BaseFragment {

    @BindView(R.id.top_rlv)
    RecyclerView topRlv;
    Unbinder unbinder;
    @BindView(R.id.ft_fab)
    FloatingActionButton ftFab;
    @BindView(R.id.top_line_trl)
    TwinklingRefreshLayout topLineTrl;
    private int page = 1;
    private List<String> mList = Arrays.asList("http://image.3761.com/pic/85241434675216.jpg",
            "http://image.3761.com/pic/5111434675216.jpg",
            "http://image.3761.com/pic/58601434675217.jpg",
            "http://image.3761.com/pic/43701434675217.jpg",
            "http://image.3761.com/pic/1191434675217.jpg",
            "http://image.3761.com/pic/34951434675217.jpg");
    private RecyclerAdapter<String> mAdapter;

    public TopLineFragment() {
        // Required empty public constructor
    }

    public static TopLineFragment newInstance() {

        Bundle args = new Bundle();

        TopLineFragment fragment = new TopLineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_top_line, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }


    private void initData() {
        getListData(true);
    }

    /**
     * 获取列表数据
     */
    private void getListData(boolean b) {
        if (b) {
            page = 1;
        } else {
            page++;
        }
        Map<String, String> params = new HashMap<>();
        params.put("PageIndex", page + "");
        params.put("PageSize", "10");
        //TODO 等待数据接入
        SoapUtils.Post(mActivity, API.GetInfo, null, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", "error");
            }

            @Override
            public void onSuccess(String data) {
                Log.e("data", data);
            }
        });
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                View img = view.findViewById(R.id.iv_top_icon);
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", mList.get(position));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, img, NewsDetailActivity.PICTURE);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });

        topLineTrl.setOnRefreshListener(new RefreshListenerAdapter() {
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

    private void initView() {
        mAdapter = new RecyclerAdapter<String>(mActivity, mList, R.layout.top_item) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                ImageView view = holder.getView(R.id.iv_top_icon);
                ImageLoader.loadImageUrl(TopLineFragment.this, s, view);
                holder.setText(R.id.tv_news_item_title, "我就是标题，不一样的标题");
                holder.setText(R.id.tv_news_item_user, "这里是用户名");
            }
        };
        topRlv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        topRlv.setAdapter(mAdapter);
        topLineTrl.setHeaderView(new ProgressLayout(mActivity));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.ft_fab)
    public void onViewClicked() {
        startActivity(new Intent(mActivity, PublishNewsActivity.class));
    }
}
