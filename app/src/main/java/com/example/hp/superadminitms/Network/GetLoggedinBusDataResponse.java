package com.example.hp.superadminitms.Network;


import com.example.hp.superadminitms.Model.LoggedInBusDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ANDROID-PC on 10/01/2019.
 */

public class GetLoggedinBusDataResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("LoggedInBusData")
    @Expose
    private List<LoggedInBusDatum> loggedInBusData = null;

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

    public List<LoggedInBusDatum> getLoggedInBusData() {
        return loggedInBusData;
    }

    public void setLoggedInBusData(List<LoggedInBusDatum> loggedInBusData) {
        this.loggedInBusData = loggedInBusData;
    }
}
