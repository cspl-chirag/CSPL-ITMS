package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android-2 on 27-Feb-19.
 */

public class BusDatum {
    @SerializedName("Vehicle_ID")
    @Expose
    private Integer vehicleID;
    @SerializedName("Vehicle_Code")
    @Expose
    private String vehicleCode;
    @SerializedName("VehicleRegNo")
    @Expose
    private String vehicleRegNo;
    @SerializedName("FuelType")
    @Expose
    private String fuelType;

    public Integer getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(Integer vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
    }

    public String getVehicleRegNo() {
        return vehicleRegNo;
    }

    public void setVehicleRegNo(String vehicleRegNo) {
        this.vehicleRegNo = vehicleRegNo;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    @Override
    public String toString() {
        return this.vehicleCode + "-" + this.vehicleRegNo;

    }

}
