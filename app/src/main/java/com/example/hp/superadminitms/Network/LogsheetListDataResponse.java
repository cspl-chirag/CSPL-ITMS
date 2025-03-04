package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.LogsheetDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ANDROID-PC on 03/01/2019.
 */

public class LogsheetListDataResponse {

    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("LogsheetData")
    @Expose
    private List<LogsheetDatum> logsheetData = null;

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

    public List<LogsheetDatum> getLogsheetData() {
        return logsheetData;
    }

    public void setLogsheetData(List<LogsheetDatum> logsheetData) {
        this.logsheetData = logsheetData;
    }

}
