package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zl.freight.R;
import com.zl.zlibrary.adapter.RecyclerAdapter;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.view.MRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 17/12/12
 * 用户审核列表页
 */
public class UserCheckListFragment extends BaseFragment {


    @BindView(R.id.fucl_mrlv)
    MRefreshRecyclerView fuclMrlv;
    Unbinder unbinder;
    private RecyclerAdapter<String> mAdapter;
    private List<String> mList = new ArrayList<>();

    public UserCheckListFragment() {
        // Required empty public constructor
    }

    public static UserCheckListFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        UserCheckListFragment fragment = new UserCheckListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_check_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initData() {
        if (mList != null) {
            mList.clear();
        }
        for (int i = 0; i < 10; i++) {
            mList.add("");
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mAdapter = new RecyclerAdapter<String>(mActivity, mList, R.layout.user_query_item) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        fuclMrlv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        fuclMrlv.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
