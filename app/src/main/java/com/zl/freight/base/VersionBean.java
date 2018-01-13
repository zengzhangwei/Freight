package com.zl.freight.base;

/**
 * Created by zhanglei on 2017/4/22.
 */

public class VersionBean {

    /**
     * ID : cf3034d3-1a62-427d-9b73-e34d107f7ad8
     * VisionCode : 1
     * VisionName : v.1
     * url : http://172.16.18.88:8012/Download/app-release.apk
     * IsUpdate : false
     */

    private String ID;
    private int VisionCode;
    private String VisionName;
    private String url;
    //TODO 这个其实应该是boolean类型的，但是返回的是String类型，需要自己判断啦
    private String IsUpdate;
    private String Remark;


    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getVisionCode() {
        return VisionCode;
    }

    public void setVisionCode(int VisionCode) {
        this.VisionCode = VisionCode;
    }

    public String getVisionName() {
        return VisionName;
    }

    public void setVisionName(String VisionName) {
        this.VisionName = VisionName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIsUpdate() {
        return IsUpdate;
    }

    public void setIsUpdate(String IsUpdate) {
        this.IsUpdate = IsUpdate;
    }
}
