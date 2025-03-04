package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.RouteDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android-2 on 08-Mar-19.
 */

public class RouteResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("RouteData")
    @Expose
    private List<RouteDatum> routeData = null;

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

    public List<RouteDatum> getRouteData() {
        return routeData;
    }

    public void setRouteData(List<RouteDatum> routeData) {
        this.routeData = routeData;
    }

}
