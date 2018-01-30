package com.zl.freight.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2018\1\15 0015.
 */

public class UserBean implements Serializable {
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
    private String Bankaccount = "";
    private int IsDelete;

    public String getBankaccount() {
        return Bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        Bankaccount = bankaccount;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getReferral() {
        return Referral;
    }

    public void setReferral(String referral) {
        Referral = referral;
    }

    public String getReferralTel() {
        return ReferralTel;
    }

    public void setReferralTel(String referralTel) {
        ReferralTel = referralTel;
    }

    public String getRealName() {
        return RealName;
    }

    public void setRealName(String realName) {
        RealName = realName;
    }

    public String getUserIcon() {
        return UserIcon;
    }

    public void setUserIcon(String userIcon) {
        UserIcon = userIcon;
    }

    public String getIdCardNumber() {
        return IdCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        IdCardNumber = idCardNumber;
    }

    public String getIdCard1() {
        return IdCard1;
    }

    public void setIdCard1(String idCard1) {
        IdCard1 = idCard1;
    }

    public String getIdCard2() {
        return IdCard2;
    }

    public void setIdCard2(String idCard2) {
        IdCard2 = idCard2;
    }

    public String getUserRole() {
        return UserRole;
    }

    public void setUserRole(String userRole) {
        UserRole = userRole;
    }

    public String getOtherTel() {
        return OtherTel;
    }

    public void setOtherTel(String otherTel) {
        OtherTel = otherTel;
    }

    public String getOtherTel1() {
        return OtherTel1;
    }

    public void setOtherTel1(String otherTel1) {
        OtherTel1 = otherTel1;
    }

    public String getIntegral() {
        return Integral;
    }

    public void setIntegral(String integral) {
        Integral = integral;
    }

    public String getIsSave() {
        return IsSave;
    }

    public void setIsSave(String isSave) {
        IsSave = isSave;
    }

    public String getIsCheck() {
        return IsCheck;
    }

    public void setIsCheck(String isCheck) {
        IsCheck = isCheck;
    }

    public String getCreateAt() {
        return CreateAt;
    }

    public void setCreateAt(String createAt) {
        CreateAt = createAt;
    }

    public String getCreateBy() {
        return CreateBy;
    }

    public void setCreateBy(String createBy) {
        CreateBy = createBy;
    }

    public String getCreateByName() {
        return CreateByName;
    }

    public void setCreateByName(String createByName) {
        CreateByName = createByName;
    }

    public String getUpdateAt() {
        return UpdateAt;
    }

    public void setUpdateAt(String updateAt) {
        UpdateAt = updateAt;
    }

    public String getUpdateBy() {
        return UpdateBy;
    }

    public void setUpdateBy(String updateBy) {
        UpdateBy = updateBy;
    }

    public String getUpdateByName() {
        return UpdateByName;
    }

    public void setUpdateByName(String updateByName) {
        UpdateByName = updateByName;
    }

    public String getCheckAt() {
        return CheckAt;
    }

    public void setCheckAt(String checkAt) {
        CheckAt = checkAt;
    }

    public String getCheckBy() {
        return CheckBy;
    }

    public void setCheckBy(String checkBy) {
        CheckBy = checkBy;
    }

    public String getCheckByName() {
        return CheckByName;
    }

    public void setCheckByName(String checkByName) {
        CheckByName = checkByName;
    }

    public int getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(int isDelete) {
        IsDelete = isDelete;
    }
}
