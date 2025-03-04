package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 2/8/2020.
 */

public class FuelReportDatum {
    @SerializedName("Vehicle_Code")
    @Expose
    private String vehicleCode;
    @SerializedName("VehicleRegNo")
    @Expose
    private String vehicleRegNo;
    @SerializedName("Driver_Name")
    @Expose
    private String driverName;
    @SerializedName("Kilometre")
    @Expose
    private Integer kilometre;
    @SerializedName("Quantity")
    @Expose
    private Float quantity;

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

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Integer getKilometre() {
        return kilometre;
    }

    public void setKilometre(Integer kilometre) {
        this.kilometre = kilometre;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

}
