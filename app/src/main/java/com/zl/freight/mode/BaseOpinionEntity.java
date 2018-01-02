package com.zl.freight.mode;

/**
 * Created by Administrator on 2018/1/2.
 */

public class BaseOpinionEntity {
    public int Id;
    public String Opinion = "";
    public String UserId = "";
    public String CreateTime = "";
    public int IsDelete;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getOpinion() {
        return Opinion;
    }

    public void setOpinion(String opinion) {
        Opinion = opinion;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public int getIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(int isDelete) {
        IsDelete = isDelete;
    }
}
