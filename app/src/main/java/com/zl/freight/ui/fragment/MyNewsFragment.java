package com.zl.freight.ui.fragment;


import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.CarUserBean;
import com.zl.freight.mode.TopNewsBean;
import com.zl.freight.ui.activity.MyMoneyActivity;
import com.zl.freight.ui.activity.NewsDetailActivity;
import com.zl.freight.ui.activity.WebActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
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
 * @date 17/12/30
 */
public class MyNewsFragment extends BaseFragment {


    @BindView(R.id.news_listView)
    ListView newsListView;
    @BindView(R.id.news_trl)
    TwinklingRefreshLayout newsTrl;
    Unbinder unbinder;

    private UniversalAdapter<TopNewsBean> mAdapter;
    private List<TopNewsBean> mList = new ArrayList<>();
    private int type;
    private AlertDialog alertDialog;

    public MyNewsFragment() {
        // Required empty public constructor
    }

    public static MyNewsFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type", type);
        MyNewsFragment fragment = new MyNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_news, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        initData();
        initListener();
        return view;
    }

    private void initListener() {
        newsTrl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                getListData();
            }
        });
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View img = view.findViewById(R.id.iv_top_icon);
                TopNewsBean topNewsBean = mList.get(i);
                if (TextUtils.isEmpty(topNewsBean.getInfoLink())) {
                    Intent intent = new Intent(mActivity, NewsDetailActivity.class);
                    intent.putExtra("data", topNewsBean);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(mActivity, img, NewsDetailActivity.PICTURE);
                        startActivity(intent, options.toBundle());
                    } else {
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(mActivity, WebActivity.class);
                    intent.putExtra("title", "文章详情");
                    intent.putExtra("url", topNewsBean.getInfoLink());
                    startActivity(intent);
                }
            }
        });
    }

    private void initData() {
        Bundle arguments = getArguments();
        type = arguments.getInt("type");
        getListData();
    }

    /**
     * 获取列表数据
     */
    private void getListData() {
        Map<String, String> params = new HashMap<>();
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("Ispay", "" + type);
        SoapUtils.Post(mActivity, API.GetCarInfo, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", "");
                newsTrl.finishRefreshing();
            }

            @Override
            public void onSuccess(String data) {
                Log.e("error", "");
                try {
                    newsTrl.finishRefreshing();
                    mList.clear();
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        mList.add(GsonUtils.fromJson(array.getString(i), TopNewsBean.class));
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        });
    }

    private void initView() {
        mAdapter = new UniversalAdapter<TopNewsBean>(mActivity, mList, R.layout.top_item) {
            @Override
            public void convert(UniversalViewHolder holder, final int position, final TopNewsBean s) {
                ImageView icon = holder.getView(R.id.iv_top_icon);
                ImageLoader.loadImageUrl(mActivity, s.getInfoPic(), icon);
                holder.setText(R.id.tv_news_item_title, s.getInfoTitle());
                holder.setText(R.id.tv_news_item_user, s.getCreateAt());
                holder.getView(R.id.arl_type).setVisibility(View.VISIBLE);
                View view = holder.getView(R.id.tv_to_pay);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toPay(s, position);
                    }
                });
                Spanned spanned;
                if (type == 0) {
                    view.setVisibility(View.VISIBLE);
                    spanned = Html.fromHtml("文章状态：<font color=\"#ce2538\">未支付</font>");
                } else {
                    view.setVisibility(View.GONE);
                    spanned = Html.fromHtml("文章状态：<font color=\"#079605\">已支付</font>");
                }
                holder.setText(R.id.tv_news_type, spanned);
            }
        };
        newsListView.setAdapter(mAdapter);
        newsTrl.setHeaderView(new ProgressLayout(mActivity));
        newsTrl.setEnableLoadmore(false);
        alertDialog = new AlertDialog.Builder(mActivity).setMessage("余额不足请充值，建议充值100元").setPositiveButton("继续", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                if (onOpenMyNewsListener != null) {
//                    onOpenMyNewsListener.openMyNews();
//                }
                startActivity(new Intent(mActivity, MyMoneyActivity.class));
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create();
    }

    /**
     * 去支付
     *
     * @param s
     */
    private void toPay(final TopNewsBean s, int position) {
        goPay(s, position);
    }

    /**
     * 去支付
     *
     * @param s
     */
    private void goPay(TopNewsBean s, final int position) {
        showDialog("支付中...");
        Map<String, String> params = new HashMap<>();
        params.put("InfoId", s.getId());
        SoapUtils.Post(mActivity, API.InfoPayResult, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                hideDialog();
                alertDialog.show();
            }

            @Override
            public void onSuccess(String data) {
                hideDialog();
                showToast("支付成功");
                mList.remove(position);
                mAdapter.notifyDataSetChanged();
                updateInternal();
                Log.e("error", "");
            }
        });
    }

    /**
     * 更新积分
     */
    private void updateInternal() {
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        Map<String, String> params = new HashMap<>();
        params.put("UserId", userData.getId());
        params.put("UserRole", userData.getUserRole());
        SoapUtils.Post(mActivity, API.ShowUserInfo, params, new SoapCallback() {

            @Override
            public void onError(String error) {
                Log.e("error", "获取用户信息失败");
            }

            @Override
            public void onSuccess(String data) {
                try {
                    JSONArray array = new JSONArray(data);
                    CarUserBean carUserBean = GsonUtils.fromJson(array.optString(0), CarUserBean.class);
                    BaseUserEntity userEntity = SpUtils.getUserData(mActivity);
                    userEntity.setIntegral(carUserBean.getIntegral());
                    SpUtils.setUserData(mActivity, userEntity);
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private OnOpenMyNewsListener onOpenMyNewsListener;

    public void setOnOpenMyNewsListener(OnOpenMyNewsListener onOpenMyNewsListener) {
        this.onOpenMyNewsListener = onOpenMyNewsListener;
    }

    public interface OnOpenMyNewsListener {
        void openMyNews();
    }

}
