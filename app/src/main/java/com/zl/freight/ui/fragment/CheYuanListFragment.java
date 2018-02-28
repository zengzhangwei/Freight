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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zhy.autolayout.AutoLinearLayout;
import com.zl.freight.R;
import com.zl.freight.mode.CarUserBean;
import com.zl.freight.ui.activity.DriverDetailActivity;
import com.zl.freight.ui.activity.LookDriverActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.LocationUtils;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.adapter.UniversalAdapter;
import com.zl.zlibrary.adapter.UniversalViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.SystemUtils;
import com.zl.zlibrary.view.MRefreshRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author zhanglei
 * @date 17/12/21
 * 车源列表
 */
public class CheYuanListFragment extends BaseFragment {

    @BindView(R.id.cyl_mrrlv)
    ListView cylMrrlv;
    Unbinder unbinder;
    @BindView(R.id.et_search_data)
    EditText etSearchData;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.search_linear)
    AutoLinearLayout searchLinear;
    @BindView(R.id.che_yuan_trl)
    TwinklingRefreshLayout cheYuanTrl;
    private UniversalAdapter<CarUserBean> mAdapter;
    private List<CarUserBean> mList = new ArrayList<>();
    private int type; // 0 为熟车列表  1 为找车列表
    private AlertDialog addDialog, removeDialog;
    private int mPosition = 0;
    private LocationUtils locationUtils;
    private BDLocation mLocation = null;

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
        initListener();
        return view;
    }

    private void initListener() {
        cheYuanTrl.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                if (type == 0) {
                    getCarListData(true);
                } else {
                    getListData(true);
                }
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                if (type == 0) {
                    getCarListData(false);
                } else {
                    getListData(false);
                }
            }
        });

        locationUtils.setOnLocationListener(new LocationUtils.OnLocationListener() {
            @Override
            public void onReceiveLocation(final BDLocation location) {
                mLocation = location;
                getListData(true);
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
        cylMrrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mPosition = i;
                Intent intent = new Intent(mActivity, DriverDetailActivity.class);
                intent.putExtra(API.CARUSER, mList.get(i));
                startActivity(intent);
            }
        });
    }

    private void initData() {
        getCarListData(true);
    }

    /**
     * 获取熟车列表
     *
     * @param b
     */
    private void getCarListData(final boolean b) {

        //判断是否是熟车
        if (type != 0) {
            return;
        }
        //判断是否是已登录状态
        if (!SpUtils.isLogin(mActivity)) {
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("UserId", SpUtils.getUserData(mActivity).getId() + "");
        params.put("UserRole", API.GOODS + "");

        //根据经纬度获取附近的车辆
        SoapUtils.Post(mActivity, API.GetRelation, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("data", "获取附近的车辆时发生异常");
                if (b) {
                    cheYuanTrl.finishRefreshing();
                    mList.clear();
                } else {
                    cheYuanTrl.finishLoadmore();
                }
            }

            @Override
            public void onSuccess(String data) {
                try {
                    if (b) {
                        cheYuanTrl.finishRefreshing();
                        mList.clear();
                    } else {
                        cheYuanTrl.finishLoadmore();
                    }
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        CarUserBean carUserBean = GsonUtils.fromJson(array.optString(i), CarUserBean.class);
                        mList.add(carUserBean);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 获取界面数据
     *
     * @param b
     */
    private void getListData(final boolean b) {
        if (mLocation == null) {
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("x", mLocation.getLatitude() + "");
        params.put("y", mLocation.getLongitude() + "");

        //根据经纬度获取附近的车辆
        SoapUtils.Post(mActivity, API.GetNearByCar, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("data", "获取熟车列表时发生异常");
                if (b) {
                    cheYuanTrl.finishRefreshing();
                    mList.clear();
                } else {
                    cheYuanTrl.finishLoadmore();
                }
            }

            @Override
            public void onSuccess(String data) {
                try {
                    if (b) {
                        cheYuanTrl.finishRefreshing();
                        mList.clear();
                    } else {
                        cheYuanTrl.finishLoadmore();
                    }
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        CarUserBean carUserBean = GsonUtils.fromJson(array.optString(i), CarUserBean.class);
                        mList.add(carUserBean);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        });
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type", -1);
        }
        mAdapter = new UniversalAdapter<CarUserBean>(mActivity, mList, R.layout.che_yuan_item_layout) {

            @Override
            public void convert(UniversalViewHolder holder, int position, CarUserBean bean) {
                handleData(holder, bean, position);
            }
        };
        cylMrrlv.setAdapter(mAdapter);
        cheYuanTrl.setHeaderView(new ProgressLayout(mActivity));

        addDialog = new AlertDialog.Builder(mActivity).setMessage("是否将其添加为熟车").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                add();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create();

        removeDialog = new AlertDialog.Builder(mActivity).setMessage("是否将其从熟车中移除").setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                remove();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create();

        locationUtils = new LocationUtils(mActivity);
        if (type != 0) {
            locationUtils.startLocation();
            searchLinear.setVisibility(View.VISIBLE);
        } else {
            searchLinear.setVisibility(View.GONE);
        }
        //禁止上拉加载更多
        cheYuanTrl.setEnableLoadmore(false);
    }

    /**
     * 移除熟车
     */
    private void remove() {
        Map<String, String> params = new HashMap<>();
        //TODO 先注释掉，等接口
//        SoapUtils.Post(mActivity, API.BaseDict, params, new SoapCallback() {
//            @Override
//            public void onError(String error) {
//
//            }
//
//            @Override
//            public void onSuccess(String data) {
//
//            }
//        });
    }

    /**
     * 添加为熟车
     */
    private void add() {
        CarUserBean carUserBean = mList.get(mPosition);
        Map<String, String> params = new HashMap<>();
        params.put("ShipperId", SpUtils.getUserData(mActivity).getId());
        params.put("DriverId", carUserBean.getUserId());
        SoapUtils.Post(mActivity, API.InsertRelation, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                showToast("成功添加熟车");
                Log.e("data", data);
            }
        });
    }

    /**
     * 处理列表数据
     *
     * @param holder
     */
    private void handleData(UniversalViewHolder holder, final CarUserBean bean, int position) {
        mPosition = position;
        if (type == 0) {
            ImageView view = holder.getView(R.id.iv_item_icon);
            view.setImageResource(R.mipmap.icon_delete);
            holder.setText(R.id.tv_item_text, "移除熟车");
        }
        TextView ivStatus = holder.getView(R.id.tv_driver_status);
        if (bean.getSendId().equals("null") || TextUtils.isEmpty(bean.getSendId()) || bean.getSendId().equals("0")) {
            ivStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_empty_round_shpae));
            ivStatus.setTextColor(getResources().getColor(R.color.green_empty));
            ivStatus.setText("空车");
        } else {
            ivStatus.setBackgroundDrawable(getResources().getDrawable(R.drawable.orange_round_shape));
            ivStatus.setTextColor(getResources().getColor(R.color.orange));
            ivStatus.setText("运输中");
        }

        holder.setText(R.id.tv_driver_user, bean.getRealName());
        holder.setText(R.id.tv_car_code_item, bean.getCarNo());
        holder.setText(R.id.tv_car_type_item, bean.getCodeName() + "米/" + bean.getCodeName1());
        holder.setText(R.id.tv_car_location, bean.getCarAddress());
        CircleImageView image = holder.getView(R.id.iv_driver_icon);
        ImageLoader.loadUserIcon(mActivity, bean.getUserIcon(), image);

        //打电话
        holder.getView(R.id.linear_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemUtils.call(mActivity, bean.getUserName());
            }
        });
        //查看司机位置
        holder.getView(R.id.linear_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, LookDriverActivity.class);
                intent.putExtra("id", bean.getUserId());
                startActivity(intent);
            }
        });
        //添加熟车或从熟车中移除
        holder.getView(R.id.linear_car).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    removeDialog.show();
                } else {
                    addDialog.show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.iv_search)
    public void onViewClicked() {
        search();
    }

    /**
     * 搜索司机
     */
    private void search() {
        String data = etSearchData.getText().toString().trim();
        if (TextUtils.isEmpty(data)) {
            showToast("内容不能为空");
            return;
        }

        if (mLocation == null) {
            showToast("未获取定位信息，无法搜索");
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("nameorcarNumber", data);
        params.put("x", mLocation.getLatitude() + "");
        params.put("y", mLocation.getLongitude() + "");
        params.put("carLong", "");
        params.put("carType", "");

        SoapUtils.Post(mActivity, API.GetDriverList, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                Log.e("error", error);
                showToast("暂无此司机");
            }

            @Override
            public void onSuccess(String data) {
                JSONArray array = null;
                try {
                    mList.clear();
                    array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        CarUserBean carUserBean = GsonUtils.fromJson(array.optString(i), CarUserBean.class);
                        mList.add(carUserBean);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("暂无此司机");
                }

            }
        });
    }
}
