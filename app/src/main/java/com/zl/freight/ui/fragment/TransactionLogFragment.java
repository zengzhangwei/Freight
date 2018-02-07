package com.zl.freight.ui.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.freight.mode.BasePayLogEntity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.freight.utils.StringUtils;
import com.zl.zlibrary.adapter.RecyclerAdapter;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.GsonUtils;

import org.json.JSONArray;

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
    @BindView(R.id.log_rlv)
    RecyclerView logRlv;
    @BindView(R.id.log_trl)
    TwinklingRefreshLayout logTrl;
    Unbinder unbinder;
    private List<BasePayLogEntity> mList = new ArrayList<>();

    private RecyclerAdapter<BasePayLogEntity> mAdapter;

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
        logTrl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                getListData();
            }
        });
    }

    private void initData() {
        getListData();
    }

    private void getListData() {

        Map<String, String> params = new HashMap<>();
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("StartTime", "2018-01-20");
        params.put("EndTime", StringUtils.dateYYYY_MM_DD(System.currentTimeMillis()));
        SoapUtils.Post(mActivity, API.GetPayLog, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                logTrl.finishRefreshing();
                Log.e("error", error);
            }

            @Override
            public void onSuccess(String data) {
                Log.e("error", data);
                try {
                    mList.clear();
                    logTrl.finishRefreshing();
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        BasePayLogEntity entity = GsonUtils.fromJson(array.optString(i), BasePayLogEntity.class);
                        mList.add(entity);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        });
    }

    private void initView() {
        tvTitle.setText("交易明细");
        mAdapter = new RecyclerAdapter<BasePayLogEntity>(mActivity, mList, R.layout.deal_log_item_layout) {
            @Override
            protected void convert(ViewHolder holder, BasePayLogEntity s, int position) {
                TextView view = holder.getView(R.id.tv_deal_log_money);
                if (!TextUtils.isEmpty(s.getPayMoney() + "") && !(s.getPayMoney() + "").equals("0")) {
                    holder.setText(R.id.tv_deal_log_title, "充值");
                    view.setText("+ " + s.getPayMoney() + "元");
                    view.setTextColor(mActivity.getResources().getColor(R.color.green));
                } else {
                    holder.setText(R.id.tv_deal_log_title, "提现");
                    view.setText("- " + s.getCashValue() + "元");
                    view.setTextColor(mActivity.getResources().getColor(R.color.red));
                }
                holder.setText(R.id.tv_deal_log_time, s.getCreateTime());

            }
        };
        logRlv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        logRlv.setAdapter(mAdapter);
        logTrl.setHeaderView(new ProgressLayout(mActivity));
        logTrl.setEnableLoadmore(false);
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
