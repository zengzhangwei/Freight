package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zl.freight.R;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.ui.activity.UserCheckDetailActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.adapter.RecyclerAdapter;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.view.MRefreshRecyclerView;

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
 * @date 17/12/12
 * 用户审核列表页
 */
public class UserCheckListFragment extends BaseFragment {


    @BindView(R.id.fucl_mrlv)
    MRefreshRecyclerView fuclMrlv;
    Unbinder unbinder;
    private RecyclerAdapter<BaseUserEntity> mAdapter;
    private List<BaseUserEntity> mList = new ArrayList<>();
    private int type;

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
        initListener();
        return view;
    }

    private void initListener() {
        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(mActivity, UserCheckDetailActivity.class));
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

        Map<String, String> params = new HashMap<>();
        params.put("IsCheck", type + "");
        SoapUtils.Post(mActivity, API.CheckList, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        mList.add(GsonUtils.fromJson(array.optString(i), BaseUserEntity.class));
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        });
    }

    private void initView() {
        type = getArguments().getInt("type", 0);
        mAdapter = new RecyclerAdapter<BaseUserEntity>(mActivity, mList, R.layout.user_query_item) {
            @Override
            protected void convert(ViewHolder holder, BaseUserEntity s, int position) {
                holder.setText(R.id.tv_item_user_name, s.getRealName());
                holder.setText(R.id.tv_item_time, "于" + s.getCreateAt() + "发布");
                TextView view = holder.getView(R.id.tv_check_status);
                switch (type) {
                    case 0:
                        view.setText("未审核");
                        view.setTextColor(getResources().getColor(R.color.text_grey));
                        break;
                    case 1:
                        view.setText("审核通过");
                        view.setTextColor(getResources().getColor(R.color.green));
                        break;
                    case 2:
                        view.setText("审核未通过");
                        view.setTextColor(getResources().getColor(R.color.colorAccent));
                        break;
                }
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
