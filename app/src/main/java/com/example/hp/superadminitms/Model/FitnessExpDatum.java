package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android-2 on 30-May-19.
 */

public class FitnessExpDatum {

    @SerializedName("Vehicle_ID")
    @Expose
    private Integer vehicleID;
    @SerializedName("Vehicle")
    @Expose
    private String vehicle;
    @SerializedName("FitnessRenDat")
    @Expose
    private String fitnessRenDat;

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

    public String getFitnessRenDat() {
        return fitnessRenDat;
    }

    public void setFitnessRenDat(String fitnessRenDat) {
        this.fitnessRenDat = fitnessRenDat;
    }

}
