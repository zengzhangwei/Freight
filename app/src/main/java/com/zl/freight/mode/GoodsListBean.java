package com.zl.freight.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/2.
 */

public class GoodsListBean implements Serializable {


    /**
     * Id : 7
     * UserId : 42
     * StartPlace : 邢台市桥西区
     * StartX : 37.068761
     * StartY : 114.491676
     * EndPlace : 邢台市桥东区
     * EndX : 37.067315960234815
     * EndY : 114.50059841878371
     * UseCarType : 35
     * UseCarLong : 27
     * UseCarClass : 3
     * GoodName : fog
     * GoodsType : 47
     * GoodsWeight : 10.00
     * WeightUnit : 吨
     * Freight : 1400.00
     * GoDate : 01月03日(今天)
     * GoTime : 全天
     * IsAnyTime : 0
     * HandlingType : 51
     * PayType : 0
     * Remark :
     * GoodsPic : /images/2018/1/4 星期四/131595040359181188.png
     * IsRetry : 0
     * IsOften : 1
     * Isvisible : 1
     * IsInfoPay : 0
     * InfoMoney : 0.00
     * ReceiveId : 0
     * ReceiveDatetime :
     * CreateAt : 2018/1/4 星期四 上午 9:47:15
     * UpdateAt : 2018/1/4 星期四 上午 9:47:15
     * Times :
     * IsPay :
     * PayLimit :
     * Range : 807.63
     * Isdelete : 0
     * Id1 : 42
     * UserName : 15075993917
     * PassWord : 123456
     * Referral : leilei
     * ReferralTel : 15075993917
     * RealName : zhanglei
     * UserIcon :
     * IdCardNumber : 130526199311146498
     * IdCard1 : /images/2017/12/27 星期三/131588376051676063.png
     * IdCard2 : /images/2017/12/27 星期三/131588376055136261.png
     * UserRole : 2
     * OtherTel :
     * OtherTel1 :
     * Integral : 0
     * IsSave : 1
     * IsCheck : 0
     * CreateAt1 : 2017/12/27 星期三 下午 4:40:05
     * UpdateAt1 :
     * CheckAt :
     * CheckBy :
     * CheckByName :
     * IsDelete1 : 0
     * CodeName : 高栏
     * CodeName1 : 16
     * CodeName2 : 零担
     * CodeName3 : 重货
     * CodeName4 : 一装一卸
     * CodeName5 :
     */

    private String Id;
    private String UserId;
    private String StartPlace;
    private String StartX;
    private String StartY;
    private String EndPlace;
    private String EndX;
    private String EndY;
    private String UseCarType;
    private String UseCarLong;
    private String UseCarClass;
    private String GoodName;
    private String GoodsType;
    private String GoodsWeight;
    private String WeightUnit;
    private String Freight;
    private String GoDate;
    private String GoTime;
    private String IsAnyTime;
    private String HandlingType;
    private String PayType;
    private String Remark;
    private String GoodsPic;
    private String IsRetry;
    private String IsOften;
    private String Isvisible;
    private String IsInfoPay;
    private String InfoMoney;
    private String ReceiveId;
    private String ReceiveDatetime;
    private String CreateAt;
    private String UpdateAt;
    private String Times;
    private String IsPay;
    private String PayLimit;
    private String Range;
    private String Isdelete;
    private String Id1;
    private String UserName;
    private String PassWord;
    private String Referral;
    private String ReferralTel;
    private String RealName;
    private String UserIcon;
    private String IdCardNumber;
    private String IdCard1;
    private String IdCard2;
    private String IsOver;
    private String UserRole;
    private String OtherTel;
    private String OtherTel1;
    private String Integral;
    private String IsSave;
    private String IsCheck;
    private String CreateAt1;
    private String UpdateAt1;
    private String CheckAt;
    private String CheckBy;
    private String CheckByName;
    private String IsDelete1;
    private String CodeName;
    private String CodeName1;
    private String CodeName2;
    private String CodeName3;
    private String CodeName4;
    private String CodeName5;
    private String UserName1;
    public String StateFrom = "";//货物名称
    public String StateTo = "";//货物名称

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

    public String getIsOver() {
        return IsOver;
    }

    public void setIsOver(String isOver) {
        IsOver = isOver;
    }

    public String getUserName1() {
        return UserName1;
    }

    public void setUserName1(String userName1) {
        UserName1 = userName1;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getStartPlace() {
        return StartPlace;
    }

    public void setStartPlace(String StartPlace) {
        this.StartPlace = StartPlace;
    }

    public String getStartX() {
        return StartX;
    }

    public void setStartX(String StartX) {
        this.StartX = StartX;
    }

    public String getStartY() {
        return StartY;
    }

    public void setStartY(String StartY) {
        this.StartY = StartY;
    }

    public String getEndPlace() {
        return EndPlace;
    }

    public void setEndPlace(String EndPlace) {
        this.EndPlace = EndPlace;
    }

    public String getEndX() {
        return EndX;
    }

    public void setEndX(String EndX) {
        this.EndX = EndX;
    }

    public String getEndY() {
        return EndY;
    }

    public void setEndY(String EndY) {
        this.EndY = EndY;
    }

    public String getUseCarType() {
        return UseCarType;
    }

    public void setUseCarType(String UseCarType) {
        this.UseCarType = UseCarType;
    }

    public String getUseCarLong() {
        return UseCarLong;
    }

    public void setUseCarLong(String UseCarLong) {
        this.UseCarLong = UseCarLong;
    }

    public String getUseCarClass() {
        return UseCarClass;
    }

    public void setUseCarClass(String UseCarClass) {
        this.UseCarClass = UseCarClass;
    }

    public String getGoodName() {
        return GoodName;
    }

    public void setGoodName(String GoodName) {
        this.GoodName = GoodName;
    }

    public String getGoodsType() {
        return GoodsType;
    }

    public void setGoodsType(String GoodsType) {
        this.GoodsType = GoodsType;
    }

    public String getGoodsWeight() {
        return GoodsWeight;
    }

    public void setGoodsWeight(String GoodsWeight) {
        this.GoodsWeight = GoodsWeight;
    }

    public String getWeightUnit() {
        return WeightUnit;
    }

    public void setWeightUnit(String WeightUnit) {
        this.WeightUnit = WeightUnit;
    }

    public String getFreight() {
        return Freight;
    }

    public void setFreight(String Freight) {
        this.Freight = Freight;
    }

    public String getGoDate() {
        return GoDate;
    }

    public void setGoDate(String GoDate) {
        this.GoDate = GoDate;
    }

    public String getGoTime() {
        return GoTime;
    }

    public void setGoTime(String GoTime) {
        this.GoTime = GoTime;
    }

    public String getIsAnyTime() {
        return IsAnyTime;
    }

    public void setIsAnyTime(String IsAnyTime) {
        this.IsAnyTime = IsAnyTime;
    }

    public String getHandlingType() {
        return HandlingType;
    }

    public void setHandlingType(String HandlingType) {
        this.HandlingType = HandlingType;
    }

    public String getPayType() {
        return PayType;
    }

    public void setPayType(String PayType) {
        this.PayType = PayType;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getGoodsPic() {
        return GoodsPic;
    }

    public void setGoodsPic(String GoodsPic) {
        this.GoodsPic = GoodsPic;
    }

    public String getIsRetry() {
        return IsRetry;
    }

    public void setIsRetry(String IsRetry) {
        this.IsRetry = IsRetry;
    }

    public String getIsOften() {
        return IsOften;
    }

    public void setIsOften(String IsOften) {
        this.IsOften = IsOften;
    }

    public String getIsvisible() {
        return Isvisible;
    }

    public void setIsvisible(String Isvisible) {
        this.Isvisible = Isvisible;
    }

    public String getIsInfoPay() {
        return IsInfoPay;
    }

    public void setIsInfoPay(String IsInfoPay) {
        this.IsInfoPay = IsInfoPay;
    }

    public String getInfoMoney() {
        return InfoMoney;
    }

    public void setInfoMoney(String InfoMoney) {
        this.InfoMoney = InfoMoney;
    }

    public String getReceiveId() {
        return ReceiveId;
    }

    public void setReceiveId(String ReceiveId) {
        this.ReceiveId = ReceiveId;
    }

    public String getReceiveDatetime() {
        return ReceiveDatetime;
    }

    public void setReceiveDatetime(String ReceiveDatetime) {
        this.ReceiveDatetime = ReceiveDatetime;
    }

    public String getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(String CreateAt) {
        this.CreateAt = CreateAt;
    }

    public String getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(String UpdateAt) {
        this.UpdateAt = UpdateAt;
    }

    public String getTimes() {
        return Times;
    }

    public void setTimes(String Times) {
        this.Times = Times;
    }

    public String getIsPay() {
        return IsPay;
    }

    public void setIsPay(String IsPay) {
        this.IsPay = IsPay;
    }

    public String getPayLimit() {
        return PayLimit;
    }

    public void setPayLimit(String PayLimit) {
        this.PayLimit = PayLimit;
    }

    public String getRange() {
        return Range;
    }

    public void setRange(String Range) {
        this.Range = Range;
    }

    public String getIsdelete() {
        return Isdelete;
    }

    public void setIsdelete(String Isdelete) {
        this.Isdelete = Isdelete;
    }

    public String getId1() {
        return Id1;
    }

    public void setId1(String Id1) {
        this.Id1 = Id1;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String PassWord) {
        this.PassWord = PassWord;
    }

    public String getReferral() {
        return Referral;
    }

    public void setReferral(String Referral) {
        this.Referral = Referral;
    }

    public String getReferralTel() {
        return ReferralTel;
    }

    public void setReferralTel(String ReferralTel) {
        this.ReferralTel = ReferralTel;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String RealName) {
        this.RealName = RealName;
    }

    public String getUserIcon() {
        return UserIcon;
    }

    public void setUserIcon(String UserIcon) {
        this.UserIcon = UserIcon;
    }

    public String getIdCardNumber() {
        return IdCardNumber;
    }

    public void setIdCardNumber(String IdCardNumber) {
        this.IdCardNumber = IdCardNumber;
    }

    public String getIdCard1() {
        return IdCard1;
    }

    public void setIdCard1(String IdCard1) {
        this.IdCard1 = IdCard1;
    }

    public String getIdCard2() {
        return IdCard2;
    }

    public void setIdCard2(String IdCard2) {
        this.IdCard2 = IdCard2;
    }

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String UserRole) {
        this.UserRole = UserRole;
    }

    public String getOtherTel() {
        return OtherTel;
    }

    public void setOtherTel(String OtherTel) {
        this.OtherTel = OtherTel;
    }

    public String getOtherTel1() {
        return OtherTel1;
    }

    public void setOtherTel1(String OtherTel1) {
        this.OtherTel1 = OtherTel1;
    }

    public String getIntegral() {
        return Integral;
    }

    public void setIntegral(String Integral) {
        this.Integral = Integral;
    }

    public String getIsSave() {
        return IsSave;
    }

    public void setIsSave(String IsSave) {
        this.IsSave = IsSave;
    }

    public String getIsCheck() {
        return IsCheck;
    }

    public void setIsCheck(String IsCheck) {
        this.IsCheck = IsCheck;
    }

    public String getCreateAt1() {
        return CreateAt1;
    }

    public void setCreateAt1(String CreateAt1) {
        this.CreateAt1 = CreateAt1;
    }

    public String getUpdateAt1() {
        return UpdateAt1;
    }

    public void setUpdateAt1(String UpdateAt1) {
        this.UpdateAt1 = UpdateAt1;
    }

    public String getCheckAt() {
        return CheckAt;
    }

    public void setCheckAt(String CheckAt) {
        this.CheckAt = CheckAt;
    }

    public String getCheckBy() {
        return CheckBy;
    }

    public void setCheckBy(String CheckBy) {
        this.CheckBy = CheckBy;
    }

    public String getCheckByName() {
        return CheckByName;
    }

    public void setCheckByName(String CheckByName) {
        this.CheckByName = CheckByName;
    }

    public String getIsDelete1() {
        return IsDelete1;
    }

    public void setIsDelete1(String IsDelete1) {
        this.IsDelete1 = IsDelete1;
    }

    public String getCodeName() {
        return CodeName;
    }

    public void setCodeName(String CodeName) {
        this.CodeName = CodeName;
    }

    public String getCodeName1() {
        return CodeName1;
    }

    public void setCodeName1(String CodeName1) {
        this.CodeName1 = CodeName1;
    }

    public String getCodeName2() {
        return CodeName2;
    }

    public void setCodeName2(String CodeName2) {
        this.CodeName2 = CodeName2;
    }

    public String getCodeName3() {
        return CodeName3;
    }

    public void setCodeName3(String CodeName3) {
        this.CodeName3 = CodeName3;
    }

    public String getCodeName4() {
        return CodeName4;
    }

    public void setCodeName4(String CodeName4) {
        this.CodeName4 = CodeName4;
    }

    public String getCodeName5() {
        return CodeName5;
    }

    public void setCodeName5(String CodeName5) {
        this.CodeName5 = CodeName5;
    }
}
