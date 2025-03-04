package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.StaffWiseKitAssignReport;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 3/14/2020.
 */

public class HistorytStaffAssignKitResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StaffWiseKitAssignHistory")
    @Expose
    private List<StaffWiseKitAssignReport> staffWiseKitAssignHistory = null;

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

    public List<StaffWiseKitAssignReport> getStaffWiseKitAssignHistory() {
        return staffWiseKitAssignHistory;
    }

    public void setStaffWiseKitAssignHistory(List<StaffWiseKitAssignReport> staffWiseKitAssignHistory) {
        this.staffWiseKitAssignHistory = staffWiseKitAssignHistory;
    }

}
