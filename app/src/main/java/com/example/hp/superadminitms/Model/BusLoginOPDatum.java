package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ANDROID-PC on 08/12/2018.
 */

public class BusLoginOPDatum {
    @SerializedName("Log_ID")
    @Expose
    private Integer logID;
    @SerializedName("LoginKm")
    @Expose
    private Integer loginKm;
    @SerializedName("LogsheetCode")
    @Expose
    private String logsheetCode;
    @SerializedName("Route_No")
    @Expose
    private String routeNo;
    @SerializedName("DriverName")
    @Expose
    private String driverName;
    @SerializedName("Dept_Name")
    @Expose
    private String deptName;
    @SerializedName("TotalTrip")
    @Expose
    private Double totalTrip;

    public Integer getLogID() {
        return logID;
    }

    public void setLogID(Integer logID) {
        this.logID = logID;
    }

    public Integer getLoginKm() {
        return loginKm;
    }

    public void setLoginKm(Integer loginKm) {
        this.loginKm = loginKm;
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

    public Double getTotalTrip() {
        return totalTrip;
    }

    public void setTotalTrip(Double totalTrip) {
        this.totalTrip = totalTrip;
    }


}
