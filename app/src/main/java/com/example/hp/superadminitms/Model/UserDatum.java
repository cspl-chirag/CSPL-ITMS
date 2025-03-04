package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ANDROID-PC on 08/12/2018.
 */

public class UserDatum {
    @SerializedName("User_ID")
    @Expose
    private Integer userID;
    @SerializedName("User_Name")
    @Expose
    private String userName;
    @SerializedName("User_Company_ID")
    @Expose
    private Integer userCompanyID;
    @SerializedName("User_Type")
    @Expose
    private Integer userType;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserCompanyID() {
        return userCompanyID;
    }

    public void setUserCompanyID(Integer userCompanyID) {
        this.userCompanyID = userCompanyID;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}
