package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.BreakdownDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android-2 on 07-Feb-19.
 */

public class BreakdownResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("MaintenanceBreakDownData")
    @Expose
    private List<BreakdownDatum> maintenanceBreakDownData = null;

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

    public List<BreakdownDatum> getMaintenanceBreakDownData() {
        return maintenanceBreakDownData;
    }

    public void setMaintenanceBreakDownData(List<BreakdownDatum> maintenanceBreakDownData) {
        this.maintenanceBreakDownData = maintenanceBreakDownData;
    }
}
