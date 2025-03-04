package com.example.hp.superadminitms.Network;


import com.example.hp.superadminitms.Model.DriverDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android-2 on 08-Mar-19.
 */

public class DriverResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("DriverData")
    @Expose
    private List<DriverDatum> driverData = null;

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

    public List<DriverDatum> getDriverData() {
        return driverData;
    }

    public void setDriverData(List<DriverDatum> driverData) {
        this.driverData = driverData;
    }
}
