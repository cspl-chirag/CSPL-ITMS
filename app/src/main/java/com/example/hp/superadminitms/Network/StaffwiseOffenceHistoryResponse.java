package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.StaffWiseOffenceReport;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 3/14/2020.
 */

public class StaffwiseOffenceHistoryResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StaffWiseOffenceHistory")
    @Expose
    private List<StaffWiseOffenceReport> staffWiseOffenceHistory = null;

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

    public List<StaffWiseOffenceReport> getStaffWiseOffenceHistory() {
        return staffWiseOffenceHistory;
    }

    public void setStaffWiseOffenceHistory(List<StaffWiseOffenceReport> staffWiseOffenceHistory) {
        this.staffWiseOffenceHistory = staffWiseOffenceHistory;
    }
}
