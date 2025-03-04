package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android-2 on 30-May-19.
 */

public class InsuranceExpDatum {
    @SerializedName("Vehicle_ID")
    @Expose
    private Integer vehicleID;
    @SerializedName("Vehicle")
    @Expose
    private String vehicle;
    @SerializedName("InsuranceRenDate")
    @Expose
    private String insuranceRenDate;

    public Integer getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(Integer vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getInsuranceRenDate() {
        return insuranceRenDate;
    }

    public void setInsuranceRenDate(String insuranceRenDate) {
        this.insuranceRenDate = insuranceRenDate;
    }

}
