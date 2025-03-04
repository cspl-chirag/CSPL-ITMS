package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 2/12/2020.
 */

public class ReportBusWiseAverage {
    @SerializedName("Vehicle_Code")
    @Expose
    private String vehicleCode;
    @SerializedName("VehicleRegNo")
    @Expose
    private String vehicleRegNo;
    @SerializedName("TotalRunKM")
    @Expose
    private Integer totalRunKM;
    @SerializedName("TotalFuel")
    @Expose
    private Float totalFuel;
    @SerializedName("Average")
    @Expose
    private Float average;

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

    public Integer getTotalRunKM() {
        return totalRunKM;
    }

    public void setTotalRunKM(Integer totalRunKM) {
        this.totalRunKM = totalRunKM;
    }

    public Float getTotalFuel() {
        return totalFuel;
    }

    public void setTotalFuel(Float totalFuel) {
        this.totalFuel = totalFuel;
    }

    public Float getAverage() {
        return average;
    }

    public void setAverage(Float average) {
        this.average = average;
    }
}
