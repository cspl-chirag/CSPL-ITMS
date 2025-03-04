package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android-2 on 30-May-19.
 */

public class RTOExpDatum {
    @SerializedName("Vehicle_ID")
    @Expose
    private Integer vehicleID;
    @SerializedName("Vehicle")
    @Expose
    private String vehicle;
    @SerializedName("RegiValidUpto")
    @Expose
    private String regiValidUpto;

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

    public String getRegiValidUpto() {
        return regiValidUpto;
    }

    public void setRegiValidUpto(String regiValidUpto) {
        this.regiValidUpto = regiValidUpto;
    }
}
