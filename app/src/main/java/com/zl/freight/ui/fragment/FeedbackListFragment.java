package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

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

import java.util.ArrayList;
import java.util.List;

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
        return view;
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            BaseOpinionEntity entity = new BaseOpinionEntity();
            mList.add(entity);
        }
        mAdapter.notifyDataSetChanged();
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

            }
        });
    }

    private void initView() {
        mAdapter = new UniversalAdapter<BaseOpinionEntity>(mActivity, mList, R.layout.feedback_list_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, BaseOpinionEntity s) {
                holder.setText(R.id.tv_feedback_item_content, s.getOpinion());
                holder.setText(R.id.tv_feedback_issue_time, s.getCreateTime());
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
