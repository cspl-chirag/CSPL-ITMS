package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.StaffWiseKitAssignReport;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 3/13/2020.
 */

public class ReportStaffAssignKitResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StaffWiseKitAssignReport")
    @Expose
    private List<StaffWiseKitAssignReport> staffWiseKitAssignReport = null;

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

    public List<StaffWiseKitAssignReport> getStaffWiseKitAssignReport() {
        return staffWiseKitAssignReport;
    }

    public void setStaffWiseKitAssignReport(List<StaffWiseKitAssignReport> staffWiseKitAssignReport) {
        this.staffWiseKitAssignReport = staffWiseKitAssignReport;
    }
}
