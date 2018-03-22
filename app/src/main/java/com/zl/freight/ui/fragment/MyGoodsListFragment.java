package com.zl.freight.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.GoodsListBean;
import com.zl.freight.mode.RefreshGoodBean;
import com.zl.freight.ui.activity.MyMoneyActivity;
import com.zl.freight.ui.activity.UpdateSendActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
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
 */
public class MyGoodsListFragment extends BaseFragment {

    @BindView(R.id.my_goods_listView)
    ListView myGoodsListView;
    @BindView(R.id.my_goods_trl)
    TwinklingRefreshLayout myGoodsTrl;
    Unbinder unbinder;
    private int type;
    private List<GoodsListBean> mList = new ArrayList<>();
    private List<RefreshGoodBean> gList = new ArrayList<>();
    private UniversalAdapter<GoodsListBean> mAdapter;
    private int position;
    private AlertDialog alertDialog;

    public MyGoodsListFragment() {
        // Required empty public constructor
    }

    public static MyGoodsListFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        MyGoodsListFragment fragment = new MyGoodsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_goods_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        myGoodsTrl.startRefresh();
    }

    private void initListener() {
        myGoodsTrl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                getListData(true);
            }
        });
        myGoodsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mActivity, UpdateSendActivity.class);
                intent.putExtra("listBean", mList.get(i));
                startActivity(intent);
            }
        });
    }


    private void initView() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            type = arguments.getInt("type", 0);
        }
        mAdapter = new UniversalAdapter<GoodsListBean>(mActivity, mList, R.layout.my_goods_item_layout) {
            @Override
            public void convert(UniversalViewHolder holder, int position, GoodsListBean s) {
                handleData(holder, s, position);
            }
        };
        myGoodsListView.setAdapter(mAdapter);
        myGoodsTrl.setHeaderView(new ProgressLayout(mActivity));
        myGoodsTrl.setEnableLoadmore(false);
        alertDialog = new AlertDialog.Builder(mActivity).setMessage("账户余额不足请充值，建议充值100元以上")
                .setPositiveButton("充值", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(mActivity, MyMoneyActivity.class));
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 处理列表数据
     *
     * @param holder
     */
    private void handleData(UniversalViewHolder holder, final GoodsListBean s, final int position) {
        //出发地
        holder.setText(R.id.tv_origin, s.getStartPlace());
        //目的地
        holder.setText(R.id.tv_end_point, s.getEndPlace());
        //货物发布时间
        holder.setText(R.id.tv_goods_issue_time, s.getCreateAt());
        //货物描述
        String data = s.getCodeName1() + "米  " + s.getCodeName() + "/" + s.getGoodsWeight() + s.getWeightUnit() + " " + s.getCodeName4() + " " + s.getCodeName5()
                + (TextUtils.isEmpty(s.getGoDate()) ? "" : "\n装车时间" + s.getGoDate() + s.getGoTime()) + "  " + s.getCodeName3();
        holder.setText(R.id.tv_car_data, data);

        holder.getView(R.id.linear_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(s, position);
            }
        });
        holder.getView(R.id.linear_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh(s, position);
            }
        });
        holder.getView(R.id.linear_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close(s, position);
            }
        });
    }

    /**
     * 关闭货源
     *
     * @param s
     * @param position
     */
    private void close(GoodsListBean s, final int position) {
        if (s.getIsdelete().equals("2")) {
            showToast("该订单已关闭");
            return;
        }
        //TODO 在这里要加上一个判断，判断该订单是否被接，若被接则不能关闭
        Map<String, String> params = new HashMap<>();
        params.put("sendId", s.getId());
        SoapUtils.Post(mActivity, API.CloseSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                showToast("关闭成功");
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 重发货源
     *
     * @param s
     * @param position
     */
    private void refresh(final GoodsListBean s, int position) {
        Map<String, String> params = new HashMap<>();
        RefreshGoodBean goodBean = gList.get(position);
        goodBean.setCreateAt("");
        goodBean.setUpdateAt("");
        goodBean.setGoodsPic("");
        goodBean.setId("");
        params.put("sendJson", GsonUtils.toJson(goodBean));
        params.put("UserRole", SpUtils.getUserData(mActivity).getUserRole());

        showDialog("货物发布中...");
        SoapUtils.Post(mActivity, API.AddSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                hideDialog();
                if (error.equals("账户积分不足")) {
                    if (!s.getPayType().equals("0") && s.getPayType().equals("66")) {
                        alertDialog.setMessage("对不起您的账户余额不足，由于您选择了在线代收，需要预先将运费支付到货车多财务系统进行保管，" +
                                "当订单完成时会将运费发送至司机的账户上，请前去充值");
                    } else {
                        alertDialog.setMessage("账户余额不足请充值，建议充值100元以上");
                    }

                    alertDialog.show();
                    return;
                }
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                hideDialog();
                showToast("货物重发成功");
            }
        });
    }

    /**
     * 删除货源
     *
     * @param s
     * @param position
     */
    private void delete(GoodsListBean s, final int position) {
        if (s.getIsdelete().equals("1")) {
            showToast("该订单已删除");
            return;
        }
        //TODO 在这里要加上一个判断，判断该订单是否被接，若被接则不能关闭
        Map<String, String> params = new HashMap<>();
        params.put("SendId", s.getId());
        SoapUtils.Post(mActivity, API.DeleteSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                showToast("成功删除订单");
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 获取列表数据
     */
    private void getListData(final boolean b) {

        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        Map<String, String> params = new HashMap<>();
        params.put("UserRole", "2");
        params.put("UserId", userData.getId());
        params.put("SendState", type + "");
        SoapUtils.Post(mActivity, API.GetCarSend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                if (b) {
                    myGoodsTrl.finishRefreshing();
                } else {
                    myGoodsTrl.finishLoadmore();
                }
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                if (b) {
                    mList.clear();
                    gList.clear();
                    myGoodsTrl.finishRefreshing();
                } else {
                    myGoodsTrl.finishLoadmore();
                }
                try {
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        GoodsListBean sendEntity = GsonUtils.fromJson(array.optString(i), GoodsListBean.class);
                        RefreshGoodBean goodBean = GsonUtils.fromJson(array.optString(i), RefreshGoodBean.class);
                        mList.add(sendEntity);
                        gList.add(goodBean);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        });
    }
}
