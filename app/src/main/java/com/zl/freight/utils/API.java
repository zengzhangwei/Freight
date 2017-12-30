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
    public static final String CARUSER = "carUser";
    public static final String USERID = "userId";
    public static final String USERICON = "userIcon";

    public static final int ADMIN = 0;
    public static final int DRIVER = 1;
    public static final int GOODS = 2;

    public static final String BaseUrl = "http://172.16.18.17";

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
    public static final String ShowUserInfo = "ShowUserInfo";

    /**
     * 查询车辆最新坐标
     */
    public static final String QueryCarTrack = "QueryCarTrack";

    /**
     * 获取货主的熟车列表  参数：UserId UserRole
     */
    public static final String GetRelation = "GetRelation";

    /**
     * 发布广告
     */
    public static final String AddInfo = "AddInfo";

    /**
     * 获取广告
     */
    public static final String GetInfo = "GetInfo";

    /**
     * 删除广告（只有管理员有这个权限）
     */
    public static final String DeleteInfo = "DeleteInfo";

    /**
     * 支付发布广告所需的费用
     */
    public static final String InfoPayResult = "InfoPayResult";

    /**
     * 货主发单
     */
    public static final String AddSend = "AddSend";

    /**
     * 获取附近的货物订单列表 CarX: CarY:
     */
    public static final String GetNearBySend = "GetNearBySend";

    /**
     * 司机接单
     */
    public static final String ReceiveSend = "ReceiveSend";

    /**
     * 删除订单 参数 SendId
     */
    public static final String DeleteSend = "DeleteSend";

    /**
     * 插入jpushid
     */
    public static final String InsertJpush = "InsertJpush";

    /**
     * 添加为熟车
     */
    public static final String InsertRelation = "InsertRelation";


}
