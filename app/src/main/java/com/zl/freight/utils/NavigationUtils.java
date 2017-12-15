package com.zl.freight.utils;

import android.content.Context;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.NaviParaOption;
import com.zl.freight.mode.NavigationMode;
import com.zl.zlibrary.utils.PackageUtils;

/**
 * Created by zhanglei on 2017/12/14.
 * 导航工具类
 */

public class NavigationUtils {

    public static void start(Context context, NavigationMode mode) {

        //判断百度地图是否被安装
        boolean isAppInstall = PackageUtils.getIsAppInstall(context, "com.baidu.BaiduMap");
        if (isAppInstall) {
            Toast.makeText(context, "请先安装百度地图", Toast.LENGTH_SHORT).show();
            return;
        }

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

}
