package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.BusLogInLogOutReportDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/8/2020.
 */

public class BusLoginLogoutReportResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("BusLogInLogOutReportData")
    @Expose
    private List<BusLogInLogOutReportDatum> busLogInLogOutReportData = null;

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

    public List<BusLogInLogOutReportDatum> getBusLogInLogOutReportData() {
        return busLogInLogOutReportData;
    }

    public void setBusLogInLogOutReportData(List<BusLogInLogOutReportDatum> busLogInLogOutReportData) {
        this.busLogInLogOutReportData = busLogInLogOutReportData;
    }

}
