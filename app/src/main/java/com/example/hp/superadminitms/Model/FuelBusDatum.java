package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ANDROID-PC on 04/01/2019.
 */

public class FuelBusDatum implements Serializable {

    @SerializedName("Fuel_ID")
    @Expose
    private Integer fuelID;
    @SerializedName("Fuel_Date")
    @Expose
    private String fuelDate;
    @SerializedName("Fuel_Type")
    @Expose
    private String fuelType;
    @SerializedName("Station")
    @Expose
    private String station;
    @SerializedName("BillNo")
    @Expose
    private Integer billNo;
    @SerializedName("Quantity")
    @Expose
    private Double quantity;
    @SerializedName("Rate")
    @Expose
    private Double rate;
    @SerializedName("MeterReading")
    @Expose
    private Integer meterReading;
    @SerializedName("Amount")
    @Expose
    private Double amount;
    @SerializedName("VehicleId")
    @Expose
    private String vehicleId;
    @SerializedName("DriverName")
    @Expose
    private String driverName;
    @SerializedName("VehicleName")
    @Expose
    private String vehicleName;

    public Integer getFuelID() {
        return fuelID;
    }

    public void setFuelID(Integer fuelID) {
        this.fuelID = fuelID;
    }

    public String getFuelDate() {
        return fuelDate;
    }

    public void setFuelDate(String fuelDate) {
        this.fuelDate = fuelDate;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public Integer getBillNo() {
        return billNo;
    }

    public void setBillNo(Integer billNo) {
        this.billNo = billNo;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Integer getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(Integer meterReading) {
        this.meterReading = meterReading;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }
}
