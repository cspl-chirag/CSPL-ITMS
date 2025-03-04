package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HP on 12/18/2018.
 */

public class DashboardResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("DashboardData")
    @Expose
    private List<DashboardDatum> dashboardData = null;

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

    public List<DashboardDatum> getDashboardData() {
        return dashboardData;
    }

    public void setDashboardData(List<DashboardDatum> dashboardData) {
        this.dashboardData = dashboardData;
    }


}
