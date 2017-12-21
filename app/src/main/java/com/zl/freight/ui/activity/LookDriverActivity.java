package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.zl.freight.utils.API;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 * @date 12/12/15
 * 查看司机位置
 */
public class LookDriverActivity extends BaseActivity implements OnGetGeoCoderResultListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.lda_map)
    MapView ldaMap;
    private BaiduMap mBaiduMap;
    private double latitude, longitude;
    private GeoCoder mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_driver);
        ButterKnife.bind(this);
        initView();
        initListener();
    }

    private void initListener() {
        //设置搜索的监听事件
        mSearch.setOnGetGeoCodeResultListener(this);
    }

    private void initView() {
        tvTitle.setText(R.string.driver_location);
        latitude = getIntent().getDoubleExtra(API.LATITUDE, 0);
        longitude = getIntent().getDoubleExtra(API.LONGITUDE, 0);
        mBaiduMap = ldaMap.getMap();
        //获取搜索模块
        mSearch = GeoCoder.newInstance();
        //绘制司机当前位置的坐标点
        drawGoodsEnd();
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
        //设置地图中心点
        setLoactionCenter(latitude, longitude);
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

    @Override
    protected void onResume() {
        super.onResume();
        ldaMap.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ldaMap.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ldaMap.onDestroy();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有检索到结果
            showToast("对不起，没有找到搜索的位置");
            return;
        }

        //创建InfoWindow展示的view
        TextView button = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.text_layout, null);
        button.setText("司机位置：" + result.getAddress());
        //定义用于显示该InfoWindow的坐标点
        LatLng pt = new LatLng(latitude, longitude);

        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, pt, -90);

        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            //没有检索到结果
            showToast("对不起，没有找到搜索的位置");
            return;
        }
        //创建InfoWindow展示的view
        TextView button = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.text_layout, null);
        button.setText("司机位置：" + result.getAddress());
        //定义用于显示该InfoWindow的坐标点
        LatLng pt = new LatLng(latitude, longitude);

        //创建InfoWindow , 传入 view， 地理坐标， y 轴偏移量
        InfoWindow mInfoWindow = new InfoWindow(button, pt, -90);

        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);
    }
}
