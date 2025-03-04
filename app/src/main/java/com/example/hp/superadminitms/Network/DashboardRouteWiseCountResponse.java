package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.DashboardRouteCount;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/18/2020.
 */

public class DashboardRouteWiseCountResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("RouteWiseBusData")
    @Expose
    private List<DashboardRouteCount> routeWiseBusData = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DashboardRouteCount> getRouteWiseBusData() {
        return routeWiseBusData;
    }

    public void setRouteWiseBusData(List<DashboardRouteCount> routeWiseBusData) {
        this.routeWiseBusData = routeWiseBusData;
    }
}
