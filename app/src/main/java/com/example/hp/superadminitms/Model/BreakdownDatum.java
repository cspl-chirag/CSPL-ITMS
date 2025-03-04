package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Android-2 on 07-Feb-19.
 */

public class BreakdownDatum implements Serializable {
    @SerializedName("Problem_ID")
    @Expose
    private Integer problemID;
    @SerializedName("ProblemDate")
    @Expose
    private String problemDate;
    @SerializedName("Problem")
    @Expose
    private String problem;
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
    @SerializedName("Company_City")
    @Expose
    private String companyCity;

    public Integer getProblemID() {
        return problemID;
    }

    public void setProblemID(Integer problemID) {
        this.problemID = problemID;
    }

    public String getProblemDate() {
        return problemDate;
    }

    public void setProblemDate(String problemDate) {
        this.problemDate = problemDate;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
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

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }
}