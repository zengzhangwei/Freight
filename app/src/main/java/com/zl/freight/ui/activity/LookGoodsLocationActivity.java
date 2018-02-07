package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.zhy.autolayout.AutoLinearLayout;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.mode.GoodsListBean;
import com.zl.freight.mode.NavigationMode;
import com.zl.freight.utils.LocationUtils;
import com.zl.freight.utils.NavigationUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 17/12/15
 * 查看货源位置及导航
 */
public class LookGoodsLocationActivity extends BaseActivity implements OnGetRoutePlanResultListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.lgla_map)
    MapView lglaMap;
    @BindView(R.id.tv_start_navigation)
    TextView tvStartNavigation;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.linear_start_nav)
    AutoLinearLayout linearStartNav;
    private BaiduMap mBaiduMap;
    private LocationUtils locationUtils;
    private double startLatitude, startLongitude;
    private RoutePlanSearch mSearch;
    private DrivingRouteOverlay mrouteOverlay;
    private GoodsListBean goodsListBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_goods_location);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {
        locationUtils.setOnLocationListener(new LocationUtils.OnLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                drawLocation(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
    }

    private void initView() {
        goodsListBean = (GoodsListBean) getIntent().getSerializableExtra("data");
        tvTitle.setText("货源位置");
        mBaiduMap = lglaMap.getMap();
        int mapLevel = lglaMap.getMapLevel();
        Log.e("mapLevel", mapLevel + "");
        locationUtils = new LocationUtils(mActivity);
        locationUtils.startLocation();

        // 初始化路线规划模块，注册事件监听;
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        startSearch();

        long round = Math.round(Double.valueOf(goodsListBean.getRange()) / 1000);
        tvDistance.setText("运输距离约为：" + round + "公里");
    }

    private void startSearch() {
        if (goodsListBean.getStartX().equals(goodsListBean.getEndX()) && goodsListBean.getStartY().equals(goodsListBean.getEndY())) {
            showToast("货物起始位置相同，无法导航");
            return;
        }
        PlanNode stNode = PlanNode.withLocation(new LatLng(Double.parseDouble(goodsListBean.getStartX()), Double.parseDouble(goodsListBean.getStartY())));
        PlanNode enNode = PlanNode.withLocation(new LatLng(Double.parseDouble(goodsListBean.getEndX()), Double.parseDouble(goodsListBean.getEndY())));
        mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
    }

    /**
     * 绘制当前位置标记
     *
     * @param latitude
     * @param longitude
     */
    private void drawLocation(double latitude, double longitude) {
        startLatitude = latitude;
        startLongitude = longitude;
    }

    @Override
    protected void onResume() {
        super.onResume();
        lglaMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        lglaMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        lglaMap.onDestroy();
    }

    @OnClick({R.id.iv_back, R.id.tv_start_navigation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //返回
            case R.id.iv_back:
                finish();
                break;
            //开始导航
            case R.id.tv_start_navigation:
                startNavigation();
                break;
        }
    }

    /**
     * 开始导航
     */
    private void startNavigation() {
        if (goodsListBean == null) {
            showToast("位置信息不全无法进行导航");
            return;
        }
        NavigationMode mode = new NavigationMode();
        mode.setStartLatitude(startLatitude);
        mode.setStartLongitude(startLongitude);
        mode.setEndLatitude(Double.parseDouble(goodsListBean.getEndX()));
        mode.setEndLongitude(Double.parseDouble(goodsListBean.getEndY()));

        NavigationUtils.start(mActivity, mode);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            showToast("起终点或途经点地址有岐义");
            return;
        }
        if (result.error == SearchResult.ERRORNO.PERMISSION_UNFINISHED) {
            //权限鉴定未完成则再次尝试
            showToast("权限鉴定未完成,请重试");
            return;
        }
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            showToast("抱歉，未找到结果");
            return;
        }

        try {
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {
                mBaiduMap.clear();
                // mroute = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaiduMap);
                mrouteOverlay = overlay;
                mBaiduMap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            }
        } catch (Exception e) {
            showToast("导航出错，可能起始点位置太近");
        }
    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

    //定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_location);
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            return BitmapDescriptorFactory.fromResource(R.mipmap.icon_map_location_empty);
        }
    }
}
