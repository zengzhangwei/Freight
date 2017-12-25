package com.zl.freight.mode;

/**
 * Created by Administrator on 2017/12/22.
 */

public class CarTrackEntity {
    public String Id;
    public String SendId = "0";
    public String UserId = "";
    public String CarX = "";
    public String CarY = "";
    public String CreateTime;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getSendId() {
        return SendId;
    }

    public void setSendId(String sendId) {
        SendId = sendId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCarX() {
        return CarX;
    }

    public void setCarX(String carX) {
        CarX = carX;
    }

    public String getCarY() {
        return CarY;
    }

    public void setCarY(String carY) {
        CarY = carY;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }
}
