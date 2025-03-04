package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 1/25/2020.
 */

public class StockPartReplacementReportDatum {
    @SerializedName("Vehicle_Code")
    @Expose
    private String vehicleCode;
    @SerializedName("VehicleRegNo")
    @Expose
    private String vehicleRegNo;
    @SerializedName("Driver_Name")
    @Expose
    private String driverName;
    @SerializedName("Route_No")
    @Expose
    private String routeNo;
    @SerializedName("Route_Name")
    @Expose
    private String routeName;
    @SerializedName("JobDate")
    @Expose
    private String jobDate;
    @SerializedName("JobTime")
    @Expose
    private String jobTime;
    @SerializedName("MeterReading")
    @Expose
    private int meterReading;
    @SerializedName("ProblemDesc")
    @Expose
    private String problemDesc;
    @SerializedName("VehicleRegNo1")
    @Expose
    private String vehicleRegNo1;
    @SerializedName("Part_Replace_DTM")
    @Expose
    private String partReplaceDTM;
    @SerializedName("Part_Name")
    @Expose
    private String partName;
    @SerializedName("Part_Code")
    @Expose
    private String partCode;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("Mechanic_Name")
    @Expose
    private String mechanicName;
    @SerializedName("Breaks")
    @Expose
    private Boolean breaks;
    @SerializedName("Clutch")
    @Expose
    private Boolean clutch;
    @SerializedName("Steering")
    @Expose
    private Boolean steering;
    @SerializedName("Headlight")
    @Expose
    private Boolean headlight;
    @SerializedName("SideLight")
    @Expose
    private Boolean sideLight;
    @SerializedName("Accelerator")
    @Expose
    private Boolean accelerator;
    @SerializedName("OtherProblem")
    @Expose
    private Boolean otherProblem;
    @SerializedName("SolvedBy")
    @Expose
    private String solvedBy;
    @SerializedName("SolvedDate")
    @Expose
    private String solvedDate;
    @SerializedName("SolvedTime")
    @Expose
    private String solvedTime;

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

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
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

    public Integer getMeterReading() {
        return meterReading;
    }

    public void setMeterReading(Integer meterReading) {
        this.meterReading = meterReading;
    }

    public String getProblemDesc() {
        return problemDesc;
    }

    public void setProblemDesc(String problemDesc) {
        this.problemDesc = problemDesc;
    }

    public String getVehicleRegNo1() {
        return vehicleRegNo1;
    }

    public void setVehicleRegNo1(String vehicleRegNo1) {
        this.vehicleRegNo1 = vehicleRegNo1;
    }

    public String getPartReplaceDTM() {
        return partReplaceDTM;
    }

    public void setPartReplaceDTM(String partReplaceDTM) {
        this.partReplaceDTM = partReplaceDTM;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getMechanicName() {
        return mechanicName;
    }

    public void setMechanicName(String mechanicName) {
        this.mechanicName = mechanicName;
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

    public Boolean getHeadlight() {
        return headlight;
    }

    public void setHeadlight(Boolean headlight) {
        this.headlight = headlight;
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

    public String getSolvedBy() {
        return solvedBy;
    }

    public void setSolvedBy(String solvedBy) {
        this.solvedBy = solvedBy;
    }

    public String getSolvedDate() {
        return solvedDate;
    }

    public void setSolvedDate(String solvedDate) {
        this.solvedDate = solvedDate;
    }

    public String getSolvedTime() {
        return solvedTime;
    }

    public void setSolvedTime(String solvedTime) {
        this.solvedTime = solvedTime;
    }
}
