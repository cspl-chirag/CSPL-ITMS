package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.UserDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ANDROID-PC on 01/08/2018.
 */

public class LoginResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("UserData")
    @Expose
    private List<UserDatum> userData = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<UserDatum> getUserData() {
        return userData;
    }

    public void setUserData(List<UserDatum> userData) {
        this.userData = userData;
    }
}
