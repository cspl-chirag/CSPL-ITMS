package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ANDROID-PC on 21/01/2019.
 */

public class LoggedOutBusDatum implements Serializable {
    @SerializedName("Log_ID")
    @Expose
    private Integer logID;
    @SerializedName("Vehicle_ID")
    @Expose
    private Integer vehicleID;
    @SerializedName("Vehicle_Code")
    @Expose
    private String vehicleCode;
    @SerializedName("VehicleRegNo")
    @Expose
    private String vehicleRegNo;
    @SerializedName("Dept_Name")
    @Expose
    private String deptName;
    @SerializedName("LogsheetCode")
    @Expose
    private String logsheetCode;
    @SerializedName("Route_No")
    @Expose
    private String routeNo;
    @SerializedName("Staff_Code")
    @Expose
    private String staffCode;
    @SerializedName("DriverName")
    @Expose
    private String driverName;
    @SerializedName("LoginKm")
    @Expose
    private Integer loginKm;
    @SerializedName("LogOutKm")
    @Expose
    private Integer logOutKm;
    @SerializedName("LogOutTime")
    @Expose
    private String logOutTime;
    @SerializedName("RunKm")
    @Expose
    private Integer runKm;
    @SerializedName("ActualTrip")
    @Expose
    private Integer actualTrip;
    @SerializedName("Remarks")
    @Expose
    private String remarks;

    public Integer getLogID() {
        return logID;
    }

    public void setLogID(Integer logID) {
        this.logID = logID;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getLogsheetCode() {
        return logsheetCode;
    }

    public void setLogsheetCode(String logsheetCode) {
        this.logsheetCode = logsheetCode;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Integer getLoginKm() {
        return loginKm;
    }

    public void setLoginKm(Integer loginKm) {
        this.loginKm = loginKm;
    }

    public Integer getLogOutKm() {
        return logOutKm;
    }

    public void setLogOutKm(Integer logOutKm) {
        this.logOutKm = logOutKm;
    }

    public String getLogOutTime() {
        return logOutTime;
    }

    public void setLogOutTime(String logOutTime) {
        this.logOutTime = logOutTime;
    }

    public Integer getRunKm() {
        return runKm;
    }

    public void setRunKm(Integer runKm) {
        this.runKm = runKm;
    }

    public Integer getActualTrip() {
        return actualTrip;
    }

    public void setActualTrip(Integer actualTrip) {
        this.actualTrip = actualTrip;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
