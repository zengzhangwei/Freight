package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zhy.autolayout.AutoRelativeLayout;
import com.zl.freight.R;
import com.zl.freight.ui.activity.AddPathActivity;
import com.zl.freight.ui.activity.PathListActivity;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.view.MyListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 18/2/7
 * 司机添加常跑路线
 */
public class AddPathFragment extends BaseFragment {


    @BindView(R.id.linear_add_path)
    AutoRelativeLayout linearAddPath;
    @BindView(R.id.my_path_listview)
    MyListView myPathListview;
    Unbinder unbinder;
    private List<String> mList = new ArrayList<>();
    private UniversalAdapter<String> mAdapter;

    public AddPathFragment() {
        // Required empty public constructor
    }

    public static AddPathFragment newInstance() {

        Bundle args = new Bundle();

        AddPathFragment fragment = new AddPathFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_path, container, false);
        unbinder = ButterKnife.bind(this, view);
        view.setClickable(true);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        myPathListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(mActivity, PathListActivity.class));
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 5; i++) {
            mList.add("");
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        mAdapter = new UniversalAdapter<String>(mActivity, mList, R.layout.path_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, String s) {

            }
        };
        myPathListview.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.linear_add_path)
    public void onViewClicked() {
        startActivity(new Intent(mActivity, AddPathActivity.class));
    }
}
