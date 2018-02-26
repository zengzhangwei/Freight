package com.zl.freight.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/26.
 */

public class PathListBean implements Serializable {


    /**
     * Id : 1
     * LineFrom : 1
     * LineTo : 2
     * UserId : 44
     */

    private String Id;
    private String LineFrom;
    private String LineTo;
    private String UserId;

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getLineFrom() {
        return LineFrom;
    }

    public void setLineFrom(String LineFrom) {
        this.LineFrom = LineFrom;
    }

    public String getLineTo() {
        return LineTo;
    }

    public void setLineTo(String LineTo) {
        this.LineTo = LineTo;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }
}
