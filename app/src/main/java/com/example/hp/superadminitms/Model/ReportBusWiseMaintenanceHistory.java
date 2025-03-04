package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 2/15/2020.
 */

public class ReportBusWiseMaintenanceHistory {
    @SerializedName("JobDate")
    @Expose
    private String jobDate;
    @SerializedName("JobTime")
    @Expose
    private String jobTime;
    @SerializedName("Vehicle_Code")
    @Expose
    private String vehicleCode;
    @SerializedName("VehicleRegNo")
    @Expose
    private String vehicleRegNo;
    @SerializedName("MeterReading")
    @Expose
    private Integer meterReading;
    @SerializedName("Driver_Name")
    @Expose
    private String driverName;
    @SerializedName("ProblemDesc")
    @Expose
    private String problemDesc;
    @SerializedName("SolvedDesc")
    @Expose
    private String solvedDesc;
    @SerializedName("Company_City")
    @Expose
    private String companyCity;

    public String getJobDate() {
        return jobDate;
    }

    public void setJobDate(String jobDate) {
        this.jobDate = jobDate;
    }

    public String getJobTime() {
        return jobTime;
    }

    public void setJobTime(String jobTime) {
        this.jobTime = jobTime;
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

    public Integer getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(Integer meterReading) {
        this.meterReading = meterReading;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getProblemDesc() {
        return problemDesc;
    }

    public void setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
    }

    public String getSolvedDesc() {
        return solvedDesc;
    }

    public void setSolvedDesc(String solvedDesc) {
        this.solvedDesc = solvedDesc;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }
}
