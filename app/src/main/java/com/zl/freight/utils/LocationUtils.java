package com.zl.freight.utils;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * Created by 张磊 on 2017/4/10 0010.
 * 定位工具类(这个是百度地图的定位工具类，如果有需要就自行解开注释)
 */

public class LocationUtils {
    private LocationClient mLocationClient;
    private BDLocationListener myListener = new MyLocationListener();
    private Context mContext;

    public LocationUtils(Context context) {
        mContext = context;
        initMap(mContext);
        initLocation();
    }

    private void initMap(Context context) {
        mLocationClient = new LocationClient(context);
        mLocationClient.registerLocationListener(myListener);
    }

    /**
     * 百度地图的定位回调
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            if (location.getLocType() != 61 && location.getLocType() != 161) {
                if (onLocationListener != null) {
                    onLocationListener.onConnectHotSpotMessage("定位失败", location.getLocType());
                }
            } else {
                if (onLocationListener != null) {
                    onLocationListener.onReceiveLocation(location);
                }
            }

            stopLocation();

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

        int span = 0;
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

    }

    public void startLocation() {
        mLocationClient.start();
    }

    public void stopLocation() {
        mLocationClient.stop();
    }

    /**
     * 定位结果的回调监听
     */
    public interface OnLocationListener {
        void onReceiveLocation(final BDLocation location);

        void onConnectHotSpotMessage(String s, int i);
    }

    private OnLocationListener onLocationListener;

    public void setOnLocationListener(OnLocationListener onLocationListener) {
        this.onLocationListener = onLocationListener;
    }
}
