package com.example.hp.superadminitms.Network;


import com.example.hp.superadminitms.Model.FuelBusDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ANDROID-PC on 05/01/2019.
 */

public class GetFuelBusDataResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("FuelBusData")
    @Expose
    private List<FuelBusDatum> fuelBusData = null;

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

    public List<FuelBusDatum> getFuelBusData() {
        return fuelBusData;
    }

    public void setFuelBusData(List<FuelBusDatum> fuelBusData) {
        this.fuelBusData = fuelBusData;
    }
}
