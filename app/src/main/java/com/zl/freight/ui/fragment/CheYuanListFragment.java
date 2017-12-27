package com.zl.freight.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.zl.freight.R;
import com.zl.freight.mode.CarUserBean;
import com.zl.freight.ui.activity.DriverDetailActivity;
import com.zl.freight.ui.activity.LookDriverActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.LocationUtils;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.adapter.RecyclerAdapter;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.SystemUtils;
import com.zl.zlibrary.view.MRefreshRecyclerView;

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
 * @date 17/12/21
 * 车源列表
 */
public class CheYuanListFragment extends BaseFragment {

    @BindView(R.id.cyl_mrrlv)
    MRefreshRecyclerView cylMrrlv;
    Unbinder unbinder;
    @BindView(R.id.et_search_data)
    EditText etSearchData;
    private RecyclerAdapter<CarUserBean> mAdapter;
    private List<CarUserBean> mList = new ArrayList<>();
    private int type; // 0 为熟车列表  1 为找车列表
    private AlertDialog addDialog, removeDialog;
    private int mPosition = 0;
    private LocationUtils locationUtils;
    private BDLocation mLocation;

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
        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mPosition = position;
                Intent intent = new Intent(mActivity, DriverDetailActivity.class);
                intent.putExtra(API.CARUSER, mList.get(position));
                startActivity(intent);
            }
        });
        cylMrrlv.setOnMRefreshListener(new MRefreshRecyclerView.OnMRefreshListener() {
            @Override
            public void onRefresh() {
                cylMrrlv.setRefreshing(false);
            }

            @Override
            public void onLoadMore() {
                cylMrrlv.setRefreshing(false);
            }
        });
        locationUtils.setOnLocationListener(new LocationUtils.OnLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                mLocation = location;
                getListData(true);
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
    }

    private void initData() {

        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取界面数据
     *
     * @param b
     */
    private void getListData(boolean b) {
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
                Log.e("data", error);
            }

            @Override
            public void onSuccess(String data) {
                try {
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
        mAdapter = new RecyclerAdapter<CarUserBean>(mActivity, mList, R.layout.che_yuan_item_layout) {
            @Override
            protected void convert(ViewHolder holder, CarUserBean s, int position) {
                handleData(holder, s, position);
            }
        };
        cylMrrlv.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        cylMrrlv.setAdapter(mAdapter);

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
        }
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
     * 处理列表数据
     *
     * @param holder
     */
    private void handleData(ViewHolder holder, final CarUserBean bean, int position) {
        mPosition = position;
        if (type == 0) {
            ImageView view = holder.getView(R.id.iv_item_icon);
            view.setImageResource(R.mipmap.icon_delete);
            holder.setText(R.id.tv_item_text, "移除熟车");
        }
        TextView ivStatus = holder.getView(R.id.tv_driver_status);
        if (position < 4) {
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
        holder.setText(R.id.tv_car_type_item, bean.getCarLong() + "米/" + bean.getCarType());
        holder.setText(R.id.tv_car_location, bean.getCarAddress());
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
                startActivity(new Intent(mActivity, LookDriverActivity.class));
            }
        });
        //添加熟车或从熟车中移除
        holder.getView(R.id.linear_car).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 0) {
                    remove();
                } else {
                    add();
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
        if (data == null) {
            data = "";
        }
        Map<String, String> params = new HashMap<>();
        //TODO 待补充接口
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
}
