package com.zl.freight.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/22.
 */

public class CarSendEntity implements Serializable {
    public int Id;//Id
    public String UserId = "";//货主Id
    public String StartPlace = "";//出发地点
    public String StartX = "";//出发地点坐标X
    public String StartY = "";//出发地点坐标Y
    public String EndPlace = "";//终点位置
    public String EndX = "";//终点X
    public String EndY = "";//终点Y
    public int UseCarType;//车型
    public int UseCarLong;//车长
    public int UseCarClass;//车种类
    public String GoodName = "";//货物名称
    public String StateFrom = "";//货物名称
    public String StateTo = "";//货物名称
    public int GoodsType;//货物类别
    public double GoodsWeight;//货物重量
    public String WeightUnit = "";//重量单位
    public double Freight;//运费
    public String GoDate = "";//装货时间：天
    public String GoTime = "";//装货时间
    public int IsAnyTime;//是否全天
    public int HandlingType;//装卸方式
    public int PayType;//支付方式
    public String Remark = "";//备注
    public String GoodsPic = "";//货物图片
    public int IsRetry;//是否智能重发
    public int IsOften;//存为长发货源
    public int Isvisible;//同城不可见
    public int IsInfoPay;//是否需要信息费
    public double InfoMoney;//信息费金额
    public int ReceiveId;//接单人用户Id
    public int Times;//重发次数
    public int IsPay;//是否支付信息费
    public int PayLimit;//支付限期
    public String CreateAt = "";//创建时间
    public String UpdateAt = "";//更新时间
    public int Isdelete;//是否删除
    public double Range;//预计距离

    public String getStateFrom() {
        return StateFrom;
    }

    public void setStateFrom(String stateFrom) {
        StateFrom = stateFrom;
    }

    public String getStateTo() {
        return StateTo;
    }

    public void setStateTo(String stateTo) {
        StateTo = stateTo;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getStartPlace() {
        return StartPlace;
    }

    public void setStartPlace(String startPlace) {
        StartPlace = startPlace;
    }

    public String getStartX() {
        return StartX;
    }

    public void setStartX(String startX) {
        StartX = startX;
    }

    public String getStartY() {
        return StartY;
    }

    public void setStartY(String startY) {
        StartY = startY;
    }

    public String getEndPlace() {
        return EndPlace;
    }

    public void setEndPlace(String endPlace) {
        EndPlace = endPlace;
    }

    public String getEndX() {
        return EndX;
    }

    public void setEndX(String endX) {
        EndX = endX;
    }

    public String getEndY() {
        return EndY;
    }

    public void setEndY(String endY) {
        EndY = endY;
    }

    public int getUseCarType() {
        return UseCarType;
    }

    public void setUseCarType(int useCarType) {
        UseCarType = useCarType;
    }

    public int getUseCarLong() {
        return UseCarLong;
    }

    public void setUseCarLong(int useCarLong) {
        UseCarLong = useCarLong;
    }

    public int getUseCarClass() {
        return UseCarClass;
    }

    public void setUseCarClass(int useCarClass) {
        UseCarClass = useCarClass;
    }

    public String getGoodName() {
        return GoodName;
    }

    public void setGoodName(String goodName) {
        GoodName = goodName;
    }

    public int getGoodsType() {
        return GoodsType;
    }

    public void setGoodsType(int goodsType) {
        GoodsType = goodsType;
    }

    public double getGoodsWeight() {
        return GoodsWeight;
    }

    public void setGoodsWeight(double goodsWeight) {
        GoodsWeight = goodsWeight;
    }

    public String getWeightUnit() {
        return WeightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        WeightUnit = weightUnit;
    }

    public double getFreight() {
        return Freight;
    }

    public void setFreight(double freight) {
        Freight = freight;
    }

    public String getGoDate() {
        return GoDate;
    }

    public void setGoDate(String goDate) {
        GoDate = goDate;
    }

    public String getGoTime() {
        return GoTime;
    }

    public void setGoTime(String goTime) {
        GoTime = goTime;
    }

    public int getIsAnyTime() {
        return IsAnyTime;
    }

    public void setIsAnyTime(int isAnyTime) {
        IsAnyTime = isAnyTime;
    }

    public int getHandlingType() {
        return HandlingType;
    }

    public void setHandlingType(int handlingType) {
        HandlingType = handlingType;
    }

    public int getPayType() {
        return PayType;
    }

    public void setPayType(int payType) {
        PayType = payType;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getGoodsPic() {
        return GoodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        GoodsPic = goodsPic;
    }

    public int getIsRetry() {
        return IsRetry;
    }

    public void setIsRetry(int isRetry) {
        IsRetry = isRetry;
    }

    public int getIsOften() {
        return IsOften;
    }

    public void setIsOften(int isOften) {
        IsOften = isOften;
    }

    public int getIsvisible() {
        return Isvisible;
    }

    public void setIsvisible(int isvisible) {
        Isvisible = isvisible;
    }

    public int getIsInfoPay() {
        return IsInfoPay;
    }

    public void setIsInfoPay(int isInfoPay) {
        IsInfoPay = isInfoPay;
    }

    public double getInfoMoney() {
        return InfoMoney;
    }

    public void setInfoMoney(double infoMoney) {
        InfoMoney = infoMoney;
    }

    public int getReceiveId() {
        return ReceiveId;
    }

    public void setReceiveId(int receiveId) {
        ReceiveId = receiveId;
    }

    public int getTimes() {
        return Times;
    }

    public void setTimes(int times) {
        Times = times;
    }

    public int getIsPay() {
        return IsPay;
    }

    public void setIsPay(int isPay) {
        IsPay = isPay;
    }

    public int getPayLimit() {
        return PayLimit;
    }

    public void setPayLimit(int payLimit) {
        PayLimit = payLimit;
    }

    public String getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(String createAt) {
        CreateAt = createAt;
    }

    public String getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(String updateAt) {
        UpdateAt = updateAt;
    }

    public int getIsdelete() {
        return Isdelete;
    }

    public void setIsdelete(int isdelete) {
        Isdelete = isdelete;
    }

    public double getRange() {
        return Range;
    }

    public void setRange(double range) {
        Range = range;
    }
}
