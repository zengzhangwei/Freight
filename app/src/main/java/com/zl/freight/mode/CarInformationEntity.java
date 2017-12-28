package com.zl.freight.mode;

/**
 * Created by Administrator on 2017/12/22.
 */

public class CarInformationEntity {
    public int Id;
    public int InfoType;
    public String InfoKey = "";
    public String InfoContent = "";
    public String InfoLink = "";
    public String InfoPic = "";
    public int IsPay;
    public int PayLimit;
    public String CreateAt = "";
    public String UpdateAt = "";

    public String getInfoPic() {
        return InfoPic;
    }

    public void setInfoPic(String infoPic) {
        InfoPic = infoPic;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getInfoType() {
        return InfoType;
    }

    public void setInfoType(int infoType) {
        InfoType = infoType;
    }

    public String getInfoKey() {
        return InfoKey;
    }

    public void setInfoKey(String infoKey) {
        InfoKey = infoKey;
    }

    public String getInfoContent() {
        return InfoContent;
    }

    public void setInfoContent(String infoContent) {
        InfoContent = infoContent;
    }

    public String getInfoLink() {
        return InfoLink;
    }

    public void setInfoLink(String infoLink) {
        InfoLink = infoLink;
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
}
