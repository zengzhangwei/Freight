package com.zl.freight.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.zl.freight.mode.NavigationMode;
import com.zl.zlibrary.utils.PackageUtils;

import java.net.URISyntaxException;

/**
 * Created by zhanglei on 2017/12/14.
 * 导航工具类
 */

public class NavigationUtils {

    public static void start(Context context, NavigationMode mode) {

        //判断百度地图是否被安装
        boolean isAppInstall = PackageUtils.getIsAppInstall(context, "com.baidu.BaiduMap");
        boolean isGaoDeInstall = PackageUtils.getIsAppInstall(context, "com.autonavi.minimap");
        if (isAppInstall && isGaoDeInstall) {
            Toast.makeText(context, "请先安装百度地图或高德地图", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isAppInstall) {//首选百度
            baiduNavigation(context, mode);
        } else {//没按百度用高德
            startNaviGao(context, mode);
        }
    }

    /**
     * 百度导航
     *
     * @param context
     * @param mode
     */
    private static void baiduNavigation(Context context, NavigationMode mode) {
        LatLng start = new LatLng(mode.getStartLatitude(), mode.getStartLongitude());
        LatLng end = new LatLng(mode.getEndLatitude(), mode.getEndLongitude());
        // 构建 导航参数
        NaviParaOption para = new NaviParaOption()
                .startPoint(start)
                .endPoint(end)
                .startName(mode.getStartAddress())
                .endName(mode.getEndAddress());

        try {
            // 调起百度地图骑行导航
            BaiduMapNavigation.openBaiduMapBikeNavi(para, context);
        } catch (BaiduMapAppNotSupportNaviException e) {
            e.printStackTrace();
        }
    }

    /**
     * 高德导航
     *
     * @param context
     * @param mode
     */
    private static void startNaviGao(Context context, NavigationMode mode) {
        //调用高德地图app的导航方法，http://lbs.amap.com/api/amap-mobile/guide/android/navigation，详情请查看此网址介绍
        Uri uri = Uri.parse("androidamap://navi?sourceApplication=广宗汽车运输公司&poiname=我的目的地&lat="
                + mode.getEndLatitude() + "&lon=" + mode.getEndLongitude() + "&dev=0");
        Intent intent = new Intent("android.intent.category.DEFAULT", uri);
        intent.setPackage("com.autonavi.minimap");
        context.startActivity(intent);
    }

}
