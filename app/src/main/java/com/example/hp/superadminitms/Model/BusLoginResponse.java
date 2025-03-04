package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HP on 01/08/2019.
 */

public class BusLoginResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("BusOnRoadData")
    @Expose
    private List<BusLoginDatum> busOnRoadData = null;

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

    public List<BusLoginDatum> getBusOnRoadData() {
        return busOnRoadData;
    }

    public void setBusOnRoadData(List<BusLoginDatum> busOnRoadData) {
        this.busOnRoadData = busOnRoadData;
    }

}
