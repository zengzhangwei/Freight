package com.zl.freight.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/22.
 */

public class BaseUserEntity implements Serializable {

    /**
     * Id : 1
     * UserName : admin
     * PassWord : 1
     * Referral :
     * ReferralTel :
     * RealName :
     * UserIcon :
     * IdCardNumber :
     * IdCard1 :
     * IdCard2 :
     * UserRole :
     * OtherTel :
     * OtherTel1 :
     * Integral :
     * IsSave :
     * IsCheck :
     * CreateAt :
     * CreateBy :
     * CreateByName :
     * UpdateAt :
     * UpdateBy :
     * UpdateByName :
     * CheckAt :
     * CheckBy :
     * CheckByName :
     * IsDelete :
     */

    private String Id;
    private String UserName = "";
    private String PassWord = "";
    private String Referral = "";
    private String ReferralTel = "";
    private String RealName = "";
    private String UserIcon = "";
    private String IdCardNumber = "";
    private String IdCard1 = "";
    private String IdCard2 = "";
    private String UserRole = "";
    private String OtherTel = "";
    private String OtherTel1 = "";
    private String Integral = "";
    private String IsSave;
    private String IsCheck;
    private String CreateAt = "";
    private String CreateBy = "";
    private String CreateByName = "";
    private String UpdateAt = "";
    private String UpdateBy = "";
    private String UpdateByName = "";
    private String CheckAt = "";
    private String CheckBy = "";
    private String CheckByName = "";
    private String IsDelete;
    private String CarLong = "31";
    private String CarType = "33";

    public String getCarLong() {
        return CarLong;
    }

    public void setCarLong(String carLong) {
        CarLong = carLong;
    }

    public String getCarType() {
        return CarType;
    }

    public void setCarType(String carType) {
        CarType = carType;
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
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

    public String getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(String CreateAt) {
        this.CreateAt = CreateAt;
    }

    public String getCreateBy() {
        return CreateBy;
    }

    public void setCreateBy(String CreateBy) {
        this.CreateBy = CreateBy;
    }

    public String getCreateByName() {
        return CreateByName;
    }

    public void setCreateByName(String CreateByName) {
        this.CreateByName = CreateByName;
    }

    public String getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(String UpdateAt) {
        this.UpdateAt = UpdateAt;
    }

    public String getUpdateBy() {
        return UpdateBy;
    }

    public void setUpdateBy(String UpdateBy) {
        this.UpdateBy = UpdateBy;
    }

    public String getUpdateByName() {
        return UpdateByName;
    }

    public void setUpdateByName(String UpdateByName) {
        this.UpdateByName = UpdateByName;
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

    public String getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(String IsDelete) {
        this.IsDelete = IsDelete;
    }
}
