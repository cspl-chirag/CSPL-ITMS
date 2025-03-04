package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.ReportBusWiseAverage;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/12/2020.
 */

public class ReportBusWiseAvgResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("BusWiseAverageReport")
    @Expose
    private List<ReportBusWiseAverage> busWiseAverageReport = null;

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

    public List<ReportBusWiseAverage> getBusWiseAverageReport() {
        return busWiseAverageReport;
    }

    public void setBusWiseAverageReport(List<ReportBusWiseAverage> busWiseAverageReport) {
        this.busWiseAverageReport = busWiseAverageReport;
    }

}
