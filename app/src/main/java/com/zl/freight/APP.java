package com.zl.freight;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.fuqianla.paysdk.FuQianLa;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017\12\8 0008.
 */

public class APP extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        //初始化JPush
        JPushInterface.init(this);
        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());
        //付钱拉初始化
        FuQianLa.getInstance().init(getApplicationContext());
    }
}
