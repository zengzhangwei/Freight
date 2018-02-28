package com.zl.freight.ui.activity;

import android.graphics.Path;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.GoodsListBean;
import com.zl.freight.mode.PathListBean;
import com.zl.freight.ui.dialog.CarLengthDialog;
import com.zl.freight.utils.API;
import com.zl.freight.utils.ImageLoader;
import com.zl.freight.utils.LocationUtils;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.adapter.RecyclerAdapter;
import com.zl.zlibrary.adapter.ViewHolder;
import com.zl.zlibrary.utils.GsonUtils;
import com.zl.zlibrary.utils.SystemUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author zhanglei
 * @date 18/2/7
 * 专线货源
 */
public class PathListActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_origin)
    TextView tvOrigin;
    @BindView(R.id.iv_arrows)
    ImageView ivArrows;
    @BindView(R.id.tv_end_point)
    TextView tvEndPoint;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_car_type)
    TextView tvCarType;
    @BindView(R.id.path_listView)
    RecyclerView pathListView;
    @BindView(R.id.path_trl)
    TwinklingRefreshLayout pathTrl;
    private CarLengthDialog carLengthDialog;
    private List<GoodsListBean> mList = new ArrayList<>();
    private RecyclerAdapter<GoodsListBean> mAdapter;
    private PathListBean data;
    private int page = 1;
    private LocationUtils locationUtils;
    private BDLocation bdLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_list);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initData() {

    }

    private void initListener() {
        pathTrl.setOnRefreshListener(new RefreshListenerAdapter() {
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
                showToast("定位失败");
            }
        });
    }

    private void initView() {

        data = (PathListBean) getIntent().getSerializableExtra("data");
        tvOrigin.setText(data.getLineFrom());
        tvEndPoint.setText(data.getLineTo());

        carLengthDialog = new CarLengthDialog(mActivity, 0);
        pathTrl.setHeaderView(new ProgressLayout(mActivity));
        pathTrl.setEnableLoadmore(false);
        mAdapter = new RecyclerAdapter<GoodsListBean>(mActivity, mList, R.layout.goods_item) {
            @Override
            protected void convert(ViewHolder holder, GoodsListBean s, int position) {
                handleData(holder, s, position);
            }
        };
        pathListView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        pathListView.setAdapter(mAdapter);

        locationUtils = new LocationUtils(mActivity);
        locationUtils.startLocation();
    }

    /**
     * 获取列表数据
     */
    private void getDataList(final boolean b) {
        if (bdLocation == null) return;
        BaseUserEntity userData = SpUtils.getUserData(mActivity);
        Map<String, String> params = new HashMap<>();
        if (b) {
            page = 1;
        } else {
            page++;
        }
        params.put("UserRole", userData.getUserRole());
        params.put("UserId", SpUtils.getUserData(mActivity).getId());
        params.put("IsLine", "0");
        params.put("LineId", data.getId());
        params.put("Linefrom", "");
        params.put("Lineto", "");
        params.put("CarX", bdLocation.getLatitude() + "");
        params.put("CarY", bdLocation.getLongitude() + "");
        params.put("PageIndex", page + "");
        params.put("PageSize", "10");

        JSONObject object = new JSONObject(params);
        Log.e("object", object.toString());
        SoapUtils.Post(mActivity, API.GetNearBySend, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                isFnish(b);
                showToast(error);
            }

            @Override
            public void onSuccess(String data) {
                isFnish(b);
                try {
                    if (b) {
                        mList.clear();
                    }
                    JSONArray array = new JSONArray(data);
                    for (int i = 0; i < array.length(); i++) {
                        GoodsListBean sendEntity = GsonUtils.fromJson(array.optString(i), GoodsListBean.class);
                        mList.add(sendEntity);
                    }
                    mAdapter.notifyDataSetChanged();

                    if (mList.size() <= 0){
                        showToast("暂无货源");
                    }
                } catch (Exception e) {

                }
            }
        });
    }

    /**
     * 处理列表数据
     *
     * @param holder
     */
    private void handleData(ViewHolder holder, final GoodsListBean s, int position) {
        CircleImageView imageView = holder.getView(R.id.iv_user_icon);
        ImageLoader.loadUserIcon(mActivity, s.getUserIcon(), imageView);
        holder.getView(R.id.iv_phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SystemUtils.call(mActivity, s.getUserName());
            }
        });
        //出发地
        holder.setText(R.id.tv_origin, s.getStartPlace());
        //目的地
        holder.setText(R.id.tv_end_point, s.getEndPlace());
        //发布人
        holder.setText(R.id.tv_user_name, s.getRealName());
        //货物发布时间
        holder.setText(R.id.tv_goods_issue_time, s.getCreateAt());
        //货物描述
//        holder.setText(R.id.tv_car_data, getResources().getString(R.string.data));
        String data = s.getCodeName1() + "米  " + s.getCodeName() + "/" + s.getGoodsWeight() + s.getWeightUnit() + " " + s.getCodeName5() + "\n装车时间"
                + s.getGoDate() + s.getGoTime() + "  " + s.getCodeName3();
        holder.setText(R.id.tv_car_data, data);
    }

    private void isFnish(boolean b) {
        if (pathTrl == null) {
            return;
        }
        if (b) {
            pathTrl.finishRefreshing();
        } else {
            pathTrl.finishLoadmore();
        }
    }

    @OnClick({R.id.iv_back, R.id.tv_title_right, R.id.tv_car_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_car_type:
                carLengthDialog.show(view);
                break;
            case R.id.tv_title_right:
                delete();
                break;
        }
    }

    /**
     * 删除路线
     */
    private void delete() {
        Map<String, String> params = new HashMap<>();
        params.put("Id", data.getId());
        SoapUtils.Post(mActivity, API.DelLine, params, new SoapCallback() {
            @Override
            public void onError(String error) {
                showToast("删除失败");
            }

            @Override
            public void onSuccess(String data) {
                showToast("删除成功");
                finish();
            }
        });
    }
}
