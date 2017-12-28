package com.zl.freight.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.BDLocation;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.CarUserBean;
import com.zl.freight.ui.activity.GoodsDetailActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.LocationUtils;
import com.zl.freight.utils.LoginUtils;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.adapter.RecyclerAdapter;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.ImageLoader;
import com.zl.zlibrary.utils.SystemUtils;
import com.zl.zlibrary.view.MRefreshRecyclerView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author zhanglei
 * @date 17/12/7
 * 找货界面
 */
public class FindGoodsFragment extends BaseFragment {


    @BindView(R.id.find_goods_rlv)
    RecyclerView findGoodsRlv;
    Unbinder unbinder;
    @BindView(R.id.find_goods_trl)
    TwinklingRefreshLayout findGoodsTrl;

    private RecyclerAdapter<String> mAdapter;
    private List<String> mList = new ArrayList<>();
    private LocationUtils locationUtils;
    private BDLocation bdLocation;
    private int page = 1;
    private BaseUserEntity userData;

    public FindGoodsFragment() {
        // Required empty public constructor
    }

    public static FindGoodsFragment newInstance() {

        Bundle args = new Bundle();

        FindGoodsFragment fragment = new FindGoodsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_goods, container, false);
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
                Intent intent = new Intent(mActivity, GoodsDetailActivity.class);
                LoginUtils.jumpToActivity(mActivity, intent);
            }
        });
        findGoodsTrl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                getDataList(true);
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                getDataList(false);
            }
        });
        locationUtils.setOnLocationListener(new LocationUtils.OnLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                bdLocation = location;
                getDataList(true);
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
    }

    private void initData() {

    }

    /**
     * 获取列表数据
     */
    private void getDataList(boolean b) {
        if (bdLocation == null) return;
        userData = SpUtils.getUserData(mActivity);
        Map<String, String> params = new HashMap<>();
        //如果角色是司机，则判断司机的车长和车型是否为空，若为空则调用获取用户信息的接口
        if (userData.getUserRole().equals("" + API.DRIVER)) {
            if (TextUtils.isEmpty(userData.getCarLong())) {
                getUserData();
                return;
            }
            params.put("CarLong", userData.getCarLong());
            params.put("CarType", userData.getCarType());
        }
        if (b) {
            page = 1;
        } else {
            page++;
        }

        params.put("UserRole", userData.getUserRole());
        params.put("CarX", bdLocation.getLatitude() + "");
        params.put("CarY", bdLocation.getLongitude() + "");
        params.put("PageIndex", page + "");
        params.put("PageSize", "10");

        SoapUtils.Post(mActivity, API.GetNearBySend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                findGoodsTrl.finishLoadmore();
                findGoodsTrl.finishRefreshing();
                Log.e("error", "error");
            }

            @Override
            public void onSuccess(String data) {
                Log.e("data", data);
                findGoodsTrl.finishLoadmore();
                findGoodsTrl.finishRefreshing();
            }
        });
    }

    /**
     * 获取用户信息
     */
    private void getUserData() {
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
                    userData.setCarLong(carUserBean.getCarLong());
                    userData.setCarType(carUserBean.getCarType());
                    //更新用户信息
                    SpUtils.setUserData(mActivity, userData);
                    //获取界面数据
                    getDataList(true);
                } catch (Exception e) {

                }
            }
        });
    }

    private void initView() {
        mAdapter = new RecyclerAdapter<String>(mActivity, mList, R.layout.goods_item) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                handleData(holder);
            }
        };
        findGoodsRlv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        findGoodsRlv.setAdapter(mAdapter);
        findGoodsTrl.setHeaderView(new ProgressLayout(mActivity));
        locationUtils = new LocationUtils(mActivity);
        locationUtils.startLocation();
    }

    /**
     * 处理列表数据
     *
     * @param holder
     */
    private void handleData(ViewHolder holder) {
        CircleImageView imageView = holder.getView(R.id.iv_user_icon);
        ImageLoader.loadImageUrl(mActivity, "http://image.3761.com/pic/5111434675216.jpg", imageView);
        holder.getView(R.id.iv_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemUtils.call(mActivity, "15075993917");
            }
        });
        //出发地
        holder.setText(R.id.tv_origin, "邢台");
        //目的地
        holder.setText(R.id.tv_end_point, "广宗");
        //发布人
        holder.setText(R.id.tv_user_name, "张磊");
        //货物发布时间
        holder.setText(R.id.tv_goods_issue_time, "刚刚");
        //货物描述
        holder.setText(R.id.tv_car_data, getResources().getString(R.string.data));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
