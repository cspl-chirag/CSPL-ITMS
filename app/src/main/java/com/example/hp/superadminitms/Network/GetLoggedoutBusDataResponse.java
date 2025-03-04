package com.example.hp.superadminitms.Network;


import com.example.hp.superadminitms.Model.LoggedOutBusDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ANDROID-PC on 21/01/2019.
 */

public class GetLoggedoutBusDataResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("LoggedOutBusData")
    @Expose
    private List<LoggedOutBusDatum> loggedOutBusData = null;

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

    public List<LoggedOutBusDatum> getLoggedOutBusData() {
        return loggedOutBusData;
    }

    public void setLoggedOutBusData(List<LoggedOutBusDatum> loggedOutBusData) {
        this.loggedOutBusData = loggedOutBusData;
    }
}
