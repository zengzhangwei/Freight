package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
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
public class LookGoodsLocationActivity extends BaseActivity {

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
    private BaiduMap mBaiduMap;
    private LocationUtils locationUtils;
    private double latitude, longitude, startLatitude, startLongitude;

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
                drawGoodsEnd();
                setLoactionCenter(latitude, longitude);
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
    }

    private void initView() {
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        tvTitle.setText("货源位置");
        mBaiduMap = lglaMap.getMap();
        locationUtils = new LocationUtils(mActivity);
        locationUtils.startLocation();
    }

    /**
     * 在地图上绘制货源标记
     */
    private void drawGoodsEnd() {
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_red_location);

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);

        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

//        //创建InfoWindow展示的view
//        TextView button = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.text_layout, null);
//        button.setText("货源地");
//        //定义用于显示该InfoWindow的坐标点
//        LatLng pt = new LatLng(latitude, longitude);
//
//        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
//        InfoWindow mInfoWindow = new InfoWindow(button, pt, -90);
//
//        //显示InfoWindow
//        mBaiduMap.showInfoWindow(mInfoWindow);
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
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);

        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.mipmap.icon_blue_location);

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);

        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        //创建InfoWindow展示的view
        TextView button = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.text_layout, null);
        button.setText("当前位置");
        //定义用于显示该InfoWindow的坐标点
        LatLng pt = new LatLng(latitude, longitude);

        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, pt, -90);

        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
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

    /**
     * 设定地图中心位置
     */
    private void setLoactionCenter(double latitude, double longitude) {
        //设定中心点坐标
        LatLng cenpt = new LatLng(latitude, longitude);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                .target(cenpt)
                .zoom(18)
                .build();
        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
        //改变地图状态
        mBaiduMap.setMapStatus(mMapStatusUpdate);
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
        if (startLongitude == 0 || longitude == 0 || startLatitude == 0 || latitude == 0) {
            showDialog("位置信息不全无法进行导航");
            return;
        }
        NavigationMode mode = new NavigationMode();
        mode.setStartLatitude(startLatitude);
        mode.setStartLongitude(startLongitude);
        mode.setEndLatitude(latitude);
        mode.setEndLongitude(longitude);

        NavigationUtils.start(mActivity, mode);
    }
}
