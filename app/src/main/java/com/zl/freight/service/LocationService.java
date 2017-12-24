package com.zl.freight.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.zl.freight.mode.BaseUserEntity;
import com.zl.freight.mode.CarTrackEntity;
import com.zl.freight.utils.API;
import com.zl.freight.utils.LocationUtils;
import com.zl.freight.utils.SoapCallback;
import com.zl.freight.utils.SoapUtils;
import com.zl.freight.utils.SpUtils;
import com.zl.zlibrary.utils.GsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017\12\23 0023.
 */

public class LocationService extends Service {

    private LocationClient mLocationClient;
    private BDLocationListener myListener = new MyLocationListener();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMap();
        initLocation();
    }

    private void initMap() {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
    }

    /**
     * 百度地图的定位回调
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            if (location.getLocType() != 61 && location.getLocType() != 161) {
                Log.e("MyLocation", "定位出错");
            } else {
                //定位成功上报司机位置
                BaseUserEntity userData = SpUtils.getUserData(getApplicationContext());
                String userRole = userData.getUserRole();
                //再次判断是否是司机
                if (!userRole.equals("1")) return;
                Map<String, String> params = new HashMap<>();
                CarTrackEntity trackEntity = new CarTrackEntity();
                trackEntity.setCarX(location.getLatitude() + "");
                trackEntity.setCarY(location.getLongitude() + "");
                trackEntity.setUserId(userData.getId());
                params.put("carTrackModelJson", GsonUtils.toJson(trackEntity));
                SoapUtils.Post(API.InsertCarTrack, params, new SoapCallback() {
                    @Override
                    public void onError(String error) {

                    }

                    @Override
                    public void onSuccess(String data) {

                    }
                });
            }
        }
    }

    /**
     * 初始化定位设置
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000 * 60 * 30;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        mLocationClient.setLocOption(option);
        //开始定位
        mLocationClient.start();
    }

}
