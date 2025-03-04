package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.ReportBusWiseMaintenanceHistory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/15/2020.
 */

public class ReportBusMaintananceHistoryResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("DailyBusWiseMaintenanceReport")
    @Expose
    private List<ReportBusWiseMaintenanceHistory> dailyBusWiseMaintenanceReport = null;

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

    public List<ReportBusWiseMaintenanceHistory> getDailyBusWiseMaintenanceReport() {
        return dailyBusWiseMaintenanceReport;
    }

    public void setDailyBusWiseMaintenanceReport(List<ReportBusWiseMaintenanceHistory> dailyBusWiseMaintenanceReport) {
        this.dailyBusWiseMaintenanceReport = dailyBusWiseMaintenanceReport;
    }
}
