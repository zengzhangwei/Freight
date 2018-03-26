package com.zl.freight.mode;

/**
 * Created by Administrator on 2018/3/26.
 */

public class LoginAfter {


    /**
     * Realname : 1000
     * Company : 1000
     * Car : 1000
     * Referral : 1000
     * InfoMoney : 1
     * DriverSend : 1
     */

    private String Realname; //实名认证
    private String Company;//公司认证
    private String Car;//车辆认证
    private String Referral;//简绍注册增加积分
    private String InfoMoney;//发布消息收费
    private String DriverSend;//司机临时发货收费

    public String getRealname() {
        return Realname;
    }

    public void setRealname(String Realname) {
        this.Realname = Realname;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String Company) {
        this.Company = Company;
    }

    public String getCar() {
        return Car;
    }

    public void setCar(String Car) {
        this.Car = Car;
    }

    public String getReferral() {
        return Referral;
    }

    public void setReferral(String Referral) {
        this.Referral = Referral;
    }

    public String getInfoMoney() {
        return InfoMoney;
    }

    public void setInfoMoney(String InfoMoney) {
        this.InfoMoney = InfoMoney;
    }

    public String getDriverSend() {
        return DriverSend;
    }

    public void setDriverSend(String DriverSend) {
        this.DriverSend = DriverSend;
    }
}
