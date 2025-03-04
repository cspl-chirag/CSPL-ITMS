package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.BusDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/21/2020.
 */

public class DailySummaryBusListResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("BusData")
    @Expose
    private List<BusDatum> busData = null;

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

    public List<BusDatum> getBusData() {
        return busData;
    }

    public void setBusData(List<BusDatum> busData) {
        this.busData = busData;
    }
}
