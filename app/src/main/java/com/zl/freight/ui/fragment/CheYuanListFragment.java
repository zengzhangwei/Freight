package com.zl.freight.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zl.freight.R;
import com.zl.freight.ui.activity.DriverDetailActivity;
import com.zl.freight.ui.activity.LookDriverActivity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.zlibrary.adapter.RecyclerAdapter;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.base.BaseFragment;
import com.zl.zlibrary.utils.SystemUtils;
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
 * @date 17/12/21
 * 车源列表
 */
public class CheYuanListFragment extends BaseFragment {


    @BindView(R.id.cyl_mrrlv)
    MRefreshRecyclerView cylMrrlv;
    Unbinder unbinder;
    @BindView(R.id.et_search_data)
    EditText etSearchData;
    private RecyclerAdapter<String> mAdapter;
    private List<String> mList = new ArrayList<>();
    private int type;
    private AlertDialog addDialog, removeDialog;
    private int mPosition = 0;

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
                startActivity(new Intent(mActivity, DriverDetailActivity.class));
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
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            mList.add("");
        }
        mAdapter.notifyDataSetChanged();
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type", -1);
        }
        mAdapter = new RecyclerAdapter<String>(mActivity, mList, R.layout.che_yuan_item_layout) {
            @Override
            protected void convert(ViewHolder holder, String s, int position) {
                handleData(holder, position);
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
    private void handleData(ViewHolder holder, int position) {
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

        holder.setText(R.id.tv_driver_user, "张磊");
        holder.setText(R.id.tv_car_code_item, "冀123456");
        holder.setText(R.id.tv_car_type_item, "9.6米/高栏");
        holder.setText(R.id.tv_car_location, "中兴路时代电子");
        //打电话
        holder.getView(R.id.linear_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemUtils.call(mActivity, "15075993917");
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
