package com.zl.freight.mode;

/**
 * Created by Administrator on 2017/12/22.
 */

public class BasePayLogEntity {
    public int Id;
    public int UserId;
    public String UserName = "";
    public int PayMoney;
    public int CashValue;
    public String CreateTime = "";

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getPayMoney() {
        return PayMoney;
    }

    public void setPayMoney(int payMoney) {
        PayMoney = payMoney;
    }

    public int getCashValue() {
        return CashValue;
    }

    public void setCashValue(int cashValue) {
        CashValue = cashValue;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}
