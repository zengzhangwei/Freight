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
 * @date 17/12/21
 * 车源列表
 */
public class CheYuanListFragment extends BaseFragment {


    @BindView(R.id.cyl_mrrlv)
    MRefreshRecyclerView cylMrrlv;
    Unbinder unbinder;
    private RecyclerAdapter<String> mAdapter;
    private List<String> mList = new ArrayList<>();

    public CheYuanListFragment() {
        // Required empty public constructor
    }

    public static CheYuanListFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        CheYuanListFragment fragment = new CheYuanListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_che_yuan_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        return view;
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            mList.add("");
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mAdapter = new RecyclerAdapter<String>(mActivity, mList, R.layout.che_yuan_item_layout) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {

            }
        };
        cylMrrlv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        cylMrrlv.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
