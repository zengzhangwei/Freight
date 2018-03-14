package com.zl.freight.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.freight.mode.GoodsListBean;
import com.zl.freight.ui.activity.LookDriverActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.freight.utils.SystemUtils;

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
 *         货主的运单页
 */
public class GoodsOrderListFragment extends BaseFragment {


    @BindView(R.id.my_order_trl)
    TwinklingRefreshLayout myOrderTrl;
    Unbinder unbinder;
    @BindView(R.id.my_order_listView)
    ListView myOrderListView;
    private List<GoodsListBean> mList = new ArrayList<>();
    private UniversalAdapter<GoodsListBean> mAdapter;
    private int type;
    private int mPosition;
    private AlertDialog alertDialog;

    public GoodsOrderListFragment() {
        // Required empty public constructor
    }

    public static GoodsOrderListFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        GoodsOrderListFragment fragment = new GoodsOrderListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        myOrderTrl.setOnRefreshListener(new RefreshListenerAdapter() {
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

    /**
     * 获取列表数据
     */
    private void getListData() {
        Map<String, String> params = new HashMap<>();
        params.put("UserRole", "2");
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("SendState", type + "");
        SoapUtils.Post(mActivity, API.GetSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", "");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    mList.clear();
                    myOrderTrl.finishRefreshing();
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        mList.add(GsonUtils.fromJson(array.optString(i), GoodsListBean.class));
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
                Log.e("error", "onSuccess");
            }
        });
    }

    private void initView() {
        type = getArguments().getInt("type", 0);
        mAdapter = new UniversalAdapter<GoodsListBean>(mActivity, mList, R.layout.goods_order_item_layout) {

            @Override
            public void convert(UniversalViewHolder holder, final int position, final GoodsListBean s) {
                TextView tvStatus = holder.getView(R.id.tv_order_status);

                String receiveId = s.getReceiveId();

                if (TextUtils.isEmpty(receiveId) || receiveId.equals("0")) {
                    tvStatus.setText("订单状态：未被接单");
                }else{
                    tvStatus.setText("订单状态：已被接单");
                }

                if (s.getIsOver().equals("99")) {
                    tvStatus.setText("订单状态：司机已装货");
                } else if (s.getIsOver().equals("1")) {
                    tvStatus.setText("订单状态：司机完成卸货");
                }

                holder.setText(R.id.tv_order_number, "运  单  号：" + s.getId());
                holder.setText(R.id.tv_order_time, "下单时间：" + s.getCreateAt());
                holder.setText(R.id.tv_order_start, s.getStartPlace());
                holder.setText(R.id.tv_order_end, s.getEndPlace());
                holder.setText(R.id.tv_order_goods_data, s.getGoodName() + "/" + s.getGoodsWeight() + s.getWeightUnit());

                //联系司机
                holder.getView(R.id.linear_call).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SystemUtils.call(mActivity, s.getUserName1());
                    }
                });
                //查看司机位置
                holder.getView(R.id.linear_location).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mActivity, LookDriverActivity.class);
                        intent.putExtra("id", s.getReceiveId());
                        intent.putExtra("sendId", s.getId());
                        startActivity(intent);
                    }
                });
                //确认收货
                holder.getView(R.id.linear_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (type == 2) {
                            showToast("该订单已完成");
                            return;
                        }
                        mPosition = position;
                        alertDialog.show();
                    }
                });

            }
        };
        myOrderListView.setAdapter(mAdapter);
        myOrderTrl.setHeaderView(new ProgressLayout(mActivity));
        myOrderTrl.setEnableLoadmore(false);
        alertDialog = new AlertDialog.Builder(mActivity)
                .setMessage("确定要完成该笔订单吗")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finishOrder();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }

    /**
     * 完成订单
     */
    private void finishOrder() {
        GoodsListBean goodsListBean = mList.get(mPosition);
        Map<String, String> params = new HashMap<>();
        params.put("SendId", goodsListBean.getId());
        params.put("IsOver", "2");
        SoapUtils.Post(mActivity, API.OverSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                mList.remove(mPosition);
                mAdapter.notifyDataSetChanged();
                showToast("成功完成订单");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
