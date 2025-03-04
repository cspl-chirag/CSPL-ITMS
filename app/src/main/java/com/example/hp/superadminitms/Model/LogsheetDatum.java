package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ANDROID-PC on 03/01/2019.
 */

public class LogsheetDatum {

    @SerializedName("LogsheetCode")
    @Expose
    private String logsheetCode;
    @SerializedName("Route_ID")
    @Expose
    private Integer routeID;
    @SerializedName("Route_No")
    @Expose
    private String routeNo;
    @SerializedName("Dept_ID")
    @Expose
    private Integer deptID;
    @SerializedName("Dept_Name")
    @Expose
    private String deptName;
    @SerializedName("ScheduleStartTime")
    @Expose
    private String scheduleStartTime;

    public String getLogsheetCode() {
        return logsheetCode;
    }

    public void setLogsheetCode(String logsheetCode) {
        this.logsheetCode = logsheetCode;
    }

    public Integer getRouteID() {
        return routeID;
    }

    public void setRouteID(Integer routeID) {
        this.routeID = routeID;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public Integer getDeptID() {
        return deptID;
    }

    public void setDeptID(Integer deptID) {
        this.deptID = deptID;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getScheduleStartTime() {
        return scheduleStartTime;
    }

    public void setScheduleStartTime(String scheduleStartTime) {
        this.scheduleStartTime = scheduleStartTime;
    }

    @Override
    public String toString() {
        return this.logsheetCode;
    }
}
