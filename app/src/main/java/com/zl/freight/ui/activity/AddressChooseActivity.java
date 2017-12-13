package com.zl.freight.ui.activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.MapView;
import com.zl.freight.R;
import com.zl.freight.base.BaseActivity;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author zhanglei
 *         地址选择器（从地图上选择）
 */
public class AddressChooseActivity extends BaseActivity implements OnGetGeoCoderResultListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ac_map)
    MapView mMapView;
    private BaiduMap mBaiduMap;
    private GeoCoder mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_address_choose);


        mBaiduMap = mMapView.getMap();
        String mStrName = getIntent().getExtras().getString("name");
        String mStrCity = getIntent().getExtras().getString("city");
        String mStrDistrict = getIntent().getExtras().getString("district");
        // 初始化搜索模块，注册事件监听
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(this);
        mSearch.geocode(new GeoCodeOption().city(mStrCity)
                .address(mStrDistrict + mStrName));

        mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

            @Override
            public void onMapStatusChangeStart(MapStatus arg0) {
// TODO Auto-generated method stub

            }

            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int i) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus arg0) {
// TODO Auto-generated method stub
                LatLng ptCenter = mBaiduMap.getMapStatus().target; //获取地图中心点坐标
// 反Geo搜索
                mSearch.reverseGeoCode(new ReverseGeoCodeOption()
                        .location(ptCenter));
            }

            @Override
            public void onMapStatusChange(MapStatus arg0) {
// TODO Auto-generated method stub

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
// 在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
// 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
// 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
// TODO Auto-generated method stub


//设置地图中心点坐标
        MapStatusUpdate status = MapStatusUpdateFactory.newLatLng(result.getLocation());
        mBaiduMap.animateMapStatus(status);
        Toast.makeText(mActivity, result.getAddress(), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
// TODO Auto-generated method stub

        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(mActivity, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));//改变地图状态？
        Toast.makeText(mActivity, result.getAddressDetail().city + "  " +
                        result.getAddressDetail().district + "  " + result.getAddressDetail().street +
                        result.getAddressDetail().streetNumber,
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
