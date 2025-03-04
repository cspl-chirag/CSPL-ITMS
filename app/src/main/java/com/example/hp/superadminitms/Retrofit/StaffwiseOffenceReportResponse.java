package com.example.hp.superadminitms.Retrofit;

import com.example.hp.superadminitms.Model.StaffWiseOffenceReport;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 3/14/2020.
 */

public class StaffwiseOffenceReportResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StaffWiseOffenceReport")
    @Expose
    private List<StaffWiseOffenceReport> staffWiseOffenceReport = null;

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

    public List<StaffWiseOffenceReport> getStaffWiseOffenceReport() {
        return staffWiseOffenceReport;
    }

    public void setStaffWiseOffenceReport(List<StaffWiseOffenceReport> staffWiseOffenceReport) {
        this.staffWiseOffenceReport = staffWiseOffenceReport;
    }

}
