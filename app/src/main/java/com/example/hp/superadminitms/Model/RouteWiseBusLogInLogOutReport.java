package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 2/12/2020.
 */

public class RouteWiseBusLogInLogOutReport {
    @SerializedName("Route_No")
    @Expose
    private String routeNo;
    @SerializedName("Route_Name")
    @Expose
    private String routeName;
    @SerializedName("Vehicle_Code")
    @Expose
    private String vehicleCode;
    @SerializedName("VehicleRegNo")
    @Expose
    private String vehicleRegNo;
    @SerializedName("OpeningKm")
    @Expose
    private Integer openingKm;
    @SerializedName("ClosingKm")
    @Expose
    private Integer closingKm;
    @SerializedName("RunKm")
    @Expose
    private Integer runKm;
    @SerializedName("ActualTrip")
    @Expose
    private Float actualTrip;

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

    public Integer getOpeningKm() {
        return openingKm;
    }

    public void setOpeningKm(Integer openingKm) {
        this.openingKm = openingKm;
    }

    public Integer getClosingKm() {
        return closingKm;
    }

    public void setClosingKm(Integer closingKm) {
        this.closingKm = closingKm;
    }

    public Integer getRunKm() {
        return runKm;
    }

    public void setRunKm(Integer runKm) {
        this.runKm = runKm;
    }

    public Float getActualTrip() {
        return actualTrip;
    }

    public void setActualTrip(Float actualTrip) {
        this.actualTrip = actualTrip;
    }
}
