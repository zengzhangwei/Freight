package com.zl.freight.ui.fragment;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

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
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
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
    ListView topRlv;
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
    private UniversalAdapter<String> mAdapter;
    private int mPostion;

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
        topRlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View img = view.findViewById(R.id.iv_top_icon);
                Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                intent.putExtra("url", mList.get(i));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, img, NewsDetailActivity.PICTURE);
                    startActivity(intent, options.toBundle());
                } else {
                    startActivity(intent);
                }
            }
        });
        topRlv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPostion = i;
                return false;
            }
        });
    }

    private void initView() {
        mAdapter = new UniversalAdapter<String>(mActivity, mList, R.layout.top_item) {

            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {
                ImageView view = holder.getView(R.id.iv_top_icon);
                ImageLoader.loadImageUrl(TopLineFragment.this, s, view);
                holder.setText(R.id.tv_news_item_title, "我就是标题，不一样的标题");
                holder.setText(R.id.tv_news_item_user, "这里是用户名");
            }
        };
        topRlv.setAdapter(mAdapter);
        topLineTrl.setHeaderView(new ProgressLayout(mActivity));
        registerForContextMenu(topRlv);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, R.id.menu_delete_news, Menu.NONE, "删除文章");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_news:
                deleteNews();
                break;
        }
        return true;
    }

    /**
     * 删除文章
     */
    private void deleteNews() {
        Map<String, String> params = new HashMap<>();
        params.put("InfoId", "");
        showDialog("删除文章中...");
        SoapUtils.Post(mActivity, API.DeleteInfo, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                hideDialog();
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                hideDialog();
                mList.remove(mPostion);
                mAdapter.notifyDataSetChanged();
                showToast("删除成功");
            }
        });
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
