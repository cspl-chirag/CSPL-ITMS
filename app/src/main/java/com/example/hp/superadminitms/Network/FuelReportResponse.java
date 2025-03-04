package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.FuelReportDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/8/2020.
 */

public class FuelReportResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("FuelReportData")
    @Expose
    private List<FuelReportDatum> fuelReportData = null;

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

    public List<FuelReportDatum> getFuelReportData() {
        return fuelReportData;
    }

    public void setFuelReportData(List<FuelReportDatum> fuelReportData) {
        this.fuelReportData = fuelReportData;
    }

}
