package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.adapter.RecyclerAdapter;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.view.MRefreshRecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @author zhanglei
 * @date 17/12/11
 * 交易记录
 */
public class TransactionLogFragment extends BaseFragment {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.log_mrl)
    MRefreshRecyclerView logMrl;
    Unbinder unbinder;

    private List<String> mList = new ArrayList<>();

    private RecyclerAdapter<String> mAdapter;

    public TransactionLogFragment() {
        // Required empty public constructor
    }

    public static TransactionLogFragment newInstance() {

        Bundle args = new Bundle();

        TransactionLogFragment fragment = new TransactionLogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_log, container, false);
        view.setClickable(true);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        logMrl.setOnMRefreshListener(new MRefreshRecyclerView.OnMRefreshListener() {
            @Override
            public void onRefresh() {
                logMrl.setRefreshing(false);
            }

            @Override
            public void onLoadMore() {
                logMrl.setRefreshing(false);
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            mList.add("");
        }
        mAdapter.notifyDataSetChanged();
        getListData();
    }

    private void getListData() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("StartTime", "");
        params.put("EndTime", "");
        SoapUtils.Post(mActivity, API.GetPayLog, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", error);
            }

            @Override
            public void onSuccess(String data) {
                Log.e("error", data);
            }
        });
    }

    private void initView() {
        tvTitle.setText("交易明细");
        mAdapter = new RecyclerAdapter<String>(mActivity, mList, R.layout.deal_log_item_layout) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                TextView view = holder.getView(R.id.tv_deal_log_money);
                if (position > 3 && position < 6) {
                    view.setTextColor(mActivity.getResources().getColor(R.color.green));
                } else {
                    view.setTextColor(mActivity.getResources().getColor(R.color.black));
                }
            }
        };
        logMrl.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        logMrl.setAdapter(mAdapter);
        logMrl.setColorSchemeResources(R.color.colorPrimary);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        getFragmentManager().popBackStack();
    }
}
