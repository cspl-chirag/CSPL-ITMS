package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HP on 01/07/2019.
 */

public class FuelResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("FuelConsumptionData")
    @Expose
    private List<FuelDatum> fuelConsumptionData = null;

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

    public List<FuelDatum> getFuelConsumptionData() {
        return fuelConsumptionData;
    }

    public void setFuelConsumptionData(List<FuelDatum> fuelConsumptionData) {
        this.fuelConsumptionData = fuelConsumptionData;
    }

}
