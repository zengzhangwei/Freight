package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.freight.mode.BaseOpinionEntity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.GsonUtils;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 18/1/2
 * 意见反馈列表
 */
public class FeedbackListFragment extends BaseFragment {

    @BindView(R.id.feedback_listView)
    ListView feedbackListView;
    @BindView(R.id.feedback_trl)
    TwinklingRefreshLayout feedbackTrl;
    Unbinder unbinder;
    private List<BaseOpinionEntity> mList = new ArrayList<>();
    private UniversalAdapter<BaseOpinionEntity> mAdapter;
    private int mPostion;

    public FeedbackListFragment() {
        // Required empty public constructor
    }

    public static FeedbackListFragment newInstance() {

        Bundle args = new Bundle();

        FeedbackListFragment fragment = new FeedbackListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        feedbackTrl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                getListData();
            }
        });
        feedbackListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPostion = i;
                return false;
            }
        });
        registerForContextMenu(feedbackListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, R.id.menu_delete_news, Menu.NONE, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete_news:
                delete();
                break;
        }
        return true;
    }

    /**
     * 删除反馈意见
     */
    private void delete() {
        Map<String, String> params = new HashMap<>();
        params.put("Id", mList.get(mPostion).getId() + "");
        SoapUtils.Post(mActivity, API.DeleteOpinion, params, new SoapCallback() {
            @Override
            public void onError(String error) {

            }

            @Override
            public void onSuccess(String data) {
                mList.remove(mPostion);
                mAdapter.notifyDataSetChanged();
                showToast("删除成功");
            }
        });
    }

    private void initData() {
        getListData();
    }

    /**
     * 获取列表数据
     */
    private void getListData() {
        SoapUtils.Post(mActivity, API.GetOpinion, null, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                try {
                    mList.clear();
                    feedbackTrl.finishRefreshing();
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        BaseOpinionEntity entity = GsonUtils.fromJson(array.optString(i), BaseOpinionEntity.class);
                        mList.add(entity);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        });
    }

    private void initView() {
        mAdapter = new UniversalAdapter<BaseOpinionEntity>(mActivity, mList, R.layout.feedback_list_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, BaseOpinionEntity s) {
                holder.setText(R.id.tv_feedback_item_content, s.getOpinion());
                holder.setText(R.id.tv_feedback_issue_time, s.getCreatetime());
            }
        };
        feedbackListView.setAdapter(mAdapter);
        feedbackTrl.setHeaderView(new ProgressLayout(mActivity));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
