package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.RouteWiseBusLogInLogOutReport;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/12/2020.
 */

public class RoutewiseBusLoginLogoutResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("RouteWiseBusLogInLogOutReport")
    @Expose
    private List<RouteWiseBusLogInLogOutReport> routeWiseBusLogInLogOutReport = null;

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

    public List<RouteWiseBusLogInLogOutReport> getRouteWiseBusLogInLogOutReport() {
        return routeWiseBusLogInLogOutReport;
    }

    public void setRouteWiseBusLogInLogOutReport(List<RouteWiseBusLogInLogOutReport> routeWiseBusLogInLogOutReport) {
        this.routeWiseBusLogInLogOutReport = routeWiseBusLogInLogOutReport;
    }

}
