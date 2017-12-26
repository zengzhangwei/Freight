package com.zl.freight.utils;

/**
 * Created by Administrator on 2017/12/16.
 */

public class API {

    //纬度
    public static final String LATITUDE = "latitude";
    //经度
    public static final String LONGITUDE = "longitude";
    public static final int ISLOGIN = 0X111;
    public static final String ISFINISH = "isFinish";
    public static final String USERNAME = "UserName";

    public static final int ADMIN = 0;
    public static final int DRIVER = 1;
    public static final int GOODS = 2;

    public static final String BaseUrl = "http://172.16.18.17:31571";

    /**
     * 登录
     */
    public static final String Login = "Login";

    /**
     * 获取各种字典项
     * 参数：CodeName （用车类型、车长、车型、货物类型、装卸方式、付款方式、发货备注）
     */
    public static final String BaseDict = "BaseDict";

    /**
     * 用户注册
     */
    public static final String Register = "Register";

    /**
     * 上报司机位置
     */
    public static final String InsertCarTrack = "InsertCarTrack";

    /**
     * 获取附近的车辆
     */
    public static final String GetNearByCar = "GetNearByCar";

    /**
     * 获取用户信息
     */
    public static final String ShowUserInfo = "ShowUserInfo ";


}
