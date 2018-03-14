package com.zl.freight.ui.fragment;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.freight.mode.GoodsListBean;
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
 * @date 18/1/6
 */
public class OrderListFragment extends BaseFragment {


    @BindView(R.id.my_order_trl)
    TwinklingRefreshLayout myOrderTrl;
    Unbinder unbinder;
    @BindView(R.id.my_order_listView)
    ListView myOrderListView;
    private List<GoodsListBean> mList = new ArrayList<>();
    private UniversalAdapter<GoodsListBean> mAdapter;
    private int type;

    public OrderListFragment() {
        // Required empty public constructor
    }

    public static OrderListFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        OrderListFragment fragment = new OrderListFragment();
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
        myOrderTrl.startRefresh();
    }


    private void getListData() {
        Map<String, String> params = new HashMap<>();
        params.put("UserRole", "1");
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("SendState", "" + type);
        SoapUtils.Post(mActivity, API.GetSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                myOrderTrl.finishRefreshing();
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
        mAdapter = new UniversalAdapter<GoodsListBean>(mActivity, mList, R.layout.order_list_item) {


            @Override
            public void convert(UniversalViewHolder holder, int position, final GoodsListBean s) {
                holder.setText(R.id.tv_order_number, "运  单  号：" + s.getId());
                holder.setText(R.id.tv_order_time, "下单时间：" + s.getCreateAt());
                holder.setText(R.id.tv_order_start, s.getStartPlace());
                holder.setText(R.id.tv_order_end, s.getEndPlace());
                holder.setText(R.id.tv_order_goods_data, s.getGoodName() + "/" + s.getGoodsWeight() + s.getWeightUnit());
                //联系货主
                holder.getView(R.id.linear_call).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        SystemUtils.call(mActivity, s.getUserName1());
                    }
                });
                //我已装货
                holder.getView(R.id.linear_finish).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (type == 2) {
                            showToast("订单已完成");
                            return;
                        }
                        new AlertDialog.Builder(mActivity)
                                .setMessage("确认提醒货主我已装货吗")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        uploadGoods(s);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }
                });
                //请求收货
                holder.getView(R.id.linear_please).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (type == 2) {
                            showToast("订单已完成");
                            return;
                        }
                        new AlertDialog.Builder(mActivity)
                                .setMessage("确认提醒货主收货吗")
                                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        remindGoods(s);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();
                    }
                });
            }
        };
        myOrderListView.setAdapter(mAdapter);
        myOrderTrl.setHeaderView(new ProgressLayout(mActivity));
        myOrderTrl.setEnableLoadmore(false);
    }

    /**
     * 提醒货主收货
     *
     * @param s
     */
    private void remindGoods(GoodsListBean s) {
        Map<String, String> params = new HashMap<>();
        params.put("SendId", s.getId());
        SoapUtils.Post(mActivity, API.RequsetOverSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                showToast("已成功提醒");
            }
        });
    }


    /**
     * 我已装货
     *
     * @param s
     */
    private void uploadGoods(GoodsListBean s) {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("SendId", s.getId());
        SoapUtils.Post(mActivity, API.ReceiveGoods, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                showToast("成功装货");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
