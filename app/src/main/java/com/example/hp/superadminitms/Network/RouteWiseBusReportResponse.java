package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.RouteWiseBusReport;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/12/2020.
 */

public class RouteWiseBusReportResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("RouteWiseBusInfoReport")
    @Expose
    private List<RouteWiseBusReport> routeWiseBusInfoReport = null;

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

    public List<RouteWiseBusReport> getRouteWiseBusInfoReport() {
        return routeWiseBusInfoReport;
    }

    public void setRouteWiseBusInfoReport(List<RouteWiseBusReport> routeWiseBusInfoReport) {
        this.routeWiseBusInfoReport = routeWiseBusInfoReport;
    }

}
