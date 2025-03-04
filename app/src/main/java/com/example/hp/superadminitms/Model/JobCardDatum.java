package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Android-2 on 13-Mar-19.
 */

public class JobCardDatum implements Serializable {
    @SerializedName("Job_Card_ID")
    @Expose
    private Integer jobCardID;
    @SerializedName("JobDate")
    @Expose
    private String jobDate;
    @SerializedName("JobTime")
    @Expose
    private String jobTime;
    @SerializedName("Vehicle_ID")
    @Expose
    private Integer vehicleID;
    @SerializedName("Vehicle_Code")
    @Expose
    private String vehicleCode;
    @SerializedName("VehicleRegNo")
    @Expose
    private String vehicleRegNo;
    @SerializedName("Route_No")
    @Expose
    private String routeNo;
    @SerializedName("DriverName")
    @Expose
    private String driverName;
    @SerializedName("ProblemDesc")
    @Expose
    private String problemDesc;
    @SerializedName("Breaks")
    @Expose
    private Boolean breaks;
    @SerializedName("Clutch")
    @Expose
    private Boolean clutch;
    @SerializedName("Steering")
    @Expose
    private Boolean steering;
    @SerializedName("HeadLight")
    @Expose
    private Boolean headLight;
    @SerializedName("SideLight")
    @Expose
    private Boolean sideLight;
    @SerializedName("Accelerator")
    @Expose
    private Boolean accelerator;
    @SerializedName("OtherProblem")
    @Expose
    private Boolean otherProblem;
    @SerializedName("Company_City")
    @Expose
    private String companyCity;

    public Integer getJobCardID() {
        return jobCardID;
    }

    public void setJobCardID(Integer jobCardID) {
        this.jobCardID = jobCardID;
    }

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

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
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

    public Boolean getBreaks() {
        return breaks;
    }

    public void setBreaks(Boolean breaks) {
        this.breaks = breaks;
    }

    public Boolean getClutch() {
        return clutch;
    }

    public void setClutch(Boolean clutch) {
        this.clutch = clutch;
    }

    public Boolean getSteering() {
        return steering;
    }

    public void setSteering(Boolean steering) {
        this.steering = steering;
    }

    public Boolean getHeadLight() {
        return headLight;
    }

    public void setHeadLight(Boolean headLight) {
        this.headLight = headLight;
    }

    public Boolean getSideLight() {
        return sideLight;
    }

    public void setSideLight(Boolean sideLight) {
        this.sideLight = sideLight;
    }

    public Boolean getAccelerator() {
        return accelerator;
    }

    public void setAccelerator(Boolean accelerator) {
        this.accelerator = accelerator;
    }

    public Boolean getOtherProblem() {
        return otherProblem;
    }

    public void setOtherProblem(Boolean otherProblem) {
        this.otherProblem = otherProblem;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }
}