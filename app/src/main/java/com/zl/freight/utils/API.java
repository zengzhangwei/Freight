package com.zl.freight.utils;

import android.os.Environment;

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

    public static final int ratio = 100;
    public static int money = 0;
    //图片存储父路径
    public static final String image_file_path = Environment.getExternalStorageDirectory().getPath() + "/HuoCheDuo/";

    public static final String BaseUrl = "http://172.16.18.17";
//    public static final String BaseUrl = "http://139.129.131.31:8088/";

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
     * 货主修改订单
     */
    public static final String UpdateSend = "UpdateSend";

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

    /**
     * 提交反馈意见
     */
    public static final String InsertOpinion = "InsertOpinion";

    /**
     * 获取反馈意见
     */
    public static final String GetOpinion = "GetOpinion";

    /**
     * 删除反馈意见
     */
    public static final String DeleteOpinion = "DeleteOpinion";

    /**
     * 获取管理员发布的信息
     */
    public static final String GetAdminInfo = "GetAdminInfo";

    /**
     * 获取自己发布的货物列表
     */
    public static final String GetCarSend = "GetCarSend";

    /**
     * 获取运单
     */
    public static final String GetSend = "GetSend";

    /**
     * 获取用户自己发布的文章
     */
    public static final String GetCarInfo = "GetCarInfo";

    /**
     * 查询用户资料是否通过
     */
    public static final String SaveCheck = "SaveCheck";

    /**
     * 审核用户资料
     */
    public static final String CheckSave = "CheckSave";

    /**
     * 查询充值和提现记录
     */
    public static final String GetPayLog = "GetPayLog";

    /**
     * 获取时间段内的支出情况
     */
    public static final String GetOutcome = "GetOutcome";

    /**
     * 时间段内收入
     */
    public static final String GetIncome = "GetIncome";

    /**
     * 用户更新资料
     */
    public static final String UpdateUserInfo = "UpdateUserInfo";

    /**
     * 用户更新资料
     * 更新头像和备用手机号
     */
    public static final String UpdateBaseUser = "UpdateBaseUser";

    /**
     * 积分变动
     */
    public static final String IntegralChange = "IntegralChange";

    /**
     * 审核用户信息列表
     */
    public static final String CheckList = "CheckList ";

    /**
     * 插入充值记录
     */
    public static final String InsertPayLog = "InsertPayLog";

    /**
     * 发送验证码
     */
    public static final String MessageCheck = "MessageCheck";

    /**
     * 修改密码
     */
    public static final String ForgetPassword = "ForgetPassword";

    /**
     * 提现
     */
    public static final String DoCasch = "DoCasch";

    /**
     * 查询可提现的金额
     */
    public static final String AbleCasch = "AbleCasch";

    /**
     * 司机装货
     */
    public static final String ReceiveGoods = "ReceiveGoods";

    /**
     * 完成订单
     */
    public static final String OverSend = "OverSend";

    /**
     * 司机请求货主完成订单
     */
    public static final String RequsetOverSend = "RequsetOverSend";

    /**
     * 绑定支付宝账号
     */
    public static final String AddBankCard = "AddBankCard";

    /**
     * 根据车牌号搜索司机
     */
    public static final String GetDriverList = "GetDriverList";

    /**
     * 添加订阅路线
     */
    public static final String AddLine = "AddLine";

    /**
     * 删除订阅路线
     */
    public static final String DelLine = "DelLine";

    /**
     * 编辑专线
     */
    public static final String EditLine = "EditLine";

    /**
     * 显示我的专线
     */
    public static final String ShowLine = "ShowLine";

    /**
     * 获取省列表
     */
    public static final String GetSheng = "GetSheng";

    /**
     * 获取省辖内市区,市辖区内县列表
     */
    public static final String GetCity = "GetCity";


}
