package com.zl.freight;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2017\12\8 0008.
 */

public class APP extends Application {

    {
        PlatformConfig.setWeixin("wx0b20db818d6a3b52", "2de0e53d09138bf5a0c1de3d1f9a6529");
        PlatformConfig.setQQZone("1106700742", "hm7mqiwacyT1Ywzb");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        //初始化JPush
        JPushInterface.init(this);
        //初始化百度地图
        SDKInitializer.initialize(getApplicationContext());

        //初始化友盟分享
        UMShareAPI.get(this);

    }
}
