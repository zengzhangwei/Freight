package com.zl.freight.mode;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/22.
 */

public class BaseCarEntity implements Serializable {
    public int Id;
    public int UserId;
    public int CarLong;
    public int CarType;
    public String CarNo;
    public String DrivingLlicence;
    public String VehicleLicense;
    public String CarPic1;
    public String CarPic2;
    public String CarPic3;
    public int CreateBy;
    public String CreateByName;
    public int UpdateBy;
    public String UpdateByName;
    public int CheckBy;
    public String CheckByName;

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

    public int getCarLong() {
        return CarLong;
    }

    public void setCarLong(int carLong) {
        CarLong = carLong;
    }

    public int getCarType() {
        return CarType;
    }

    public void setCarType(int carType) {
        CarType = carType;
    }

    public String getCarNo() {
        return CarNo;
    }

    public void setCarNo(String carNo) {
        CarNo = carNo;
    }

    public String getDrivingLlicence() {
        return DrivingLlicence;
    }

    public void setDrivingLlicence(String drivingLlicence) {
        DrivingLlicence = drivingLlicence;
    }

    public String getVehicleLicense() {
        return VehicleLicense;
    }

    public void setVehicleLicense(String vehicleLicense) {
        VehicleLicense = vehicleLicense;
    }

    public String getCarPic1() {
        return CarPic1;
    }

    public void setCarPic1(String carPic1) {
        CarPic1 = carPic1;
    }

    public String getCarPic2() {
        return CarPic2;
    }

    public void setCarPic2(String carPic2) {
        CarPic2 = carPic2;
    }

    public String getCarPic3() {
        return CarPic3;
    }

    public void setCarPic3(String carPic3) {
        CarPic3 = carPic3;
    }

    public int getCreateBy() {
        return CreateBy;
    }

    public void setCreateBy(int createBy) {
        CreateBy = createBy;
    }

    public String getCreateByName() {
        return CreateByName;
    }

    public void setCreateByName(String createByName) {
        CreateByName = createByName;
    }

    public int getUpdateBy() {
        return UpdateBy;
    }

    public void setUpdateBy(int updateBy) {
        UpdateBy = updateBy;
    }

    public String getUpdateByName() {
        return UpdateByName;
    }

    public void setUpdateByName(String updateByName) {
        UpdateByName = updateByName;
    }

    public int getCheckBy() {
        return CheckBy;
    }

    public void setCheckBy(int checkBy) {
        CheckBy = checkBy;
    }

    public String getCheckByName() {
        return CheckByName;
    }

    public void setCheckByName(String checkByName) {
        CheckByName = checkByName;
    }
}
