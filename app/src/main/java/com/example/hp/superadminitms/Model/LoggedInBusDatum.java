package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ANDROID-PC on 10/01/2019.
 */

public class LoggedInBusDatum implements Serializable {
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
    @SerializedName("Route_No")
    @Expose
    private String routeNo;
    @SerializedName("LogsheetCode")
    @Expose
    private String logsheetCode;
    @SerializedName("Staff_Code")
    @Expose
    private String staffCode;
    @SerializedName("DriverName")
    @Expose
    private String driverName;
    @SerializedName("Dept_Name")
    @Expose
    private String deptName;
    @SerializedName("Shift")
    @Expose
    private Integer shift;
    @SerializedName("LoginKm")
    @Expose
    private Integer loginKm;
    @SerializedName("LoginTime")
    @Expose
    private String loginTime;

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

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getLogsheetCode() {
        return logsheetCode;
    }

    public void setLogsheetCode(String logsheetCode) {
        this.logsheetCode = logsheetCode;
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

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getShift() {
        return shift;
    }

    public void setShift(Integer shift) {
        this.shift = shift;
    }

    public Integer getLoginKm() {
        return loginKm;
    }

    public void setLoginKm(Integer loginKm) {
        this.loginKm = loginKm;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }
}
