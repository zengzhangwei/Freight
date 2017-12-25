package com.zl.freight.mode;

/**
 * Created by Administrator on 2017/12/22.
 */

public class BaseCompanyEntity {
    public int Id;
    public int UserId;
    public String CompanyName = "";
    public String CompanyCode = "";
    public String CompanyAddress = "";
    public String CompanyDescription = "";
    public String CompanyPic = "";
    public String StorePic = "";
    public String StorePic1 = "";
    public String StorePic2 = "";
    public int CreateBy;
    public String CreateByName = "";
    public int UpdateBy;
    public String UpdateByName = "";
    public int CheckBy;
    public String CheckByName = "";

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

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getCompanyCode() {
        return CompanyCode;
    }

    public void setCompanyCode(String companyCode) {
        CompanyCode = companyCode;
    }

    public String getCompanyAddress() {
        return CompanyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        CompanyAddress = companyAddress;
    }

    public String getCompanyDescription() {
        return CompanyDescription;
    }

    public void setCompanyDescription(String companyDescription) {
        CompanyDescription = companyDescription;
    }

    public String getCompanyPic() {
        return CompanyPic;
    }

    public void setCompanyPic(String companyPic) {
        CompanyPic = companyPic;
    }

    public String getStorePic() {
        return StorePic;
    }

    public void setStorePic(String storePic) {
        StorePic = storePic;
    }

    public String getStorePic1() {
        return StorePic1;
    }

    public void setStorePic1(String storePic1) {
        StorePic1 = storePic1;
    }

    public String getStorePic2() {
        return StorePic2;
    }

    public void setStorePic2(String storePic2) {
        StorePic2 = storePic2;
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
