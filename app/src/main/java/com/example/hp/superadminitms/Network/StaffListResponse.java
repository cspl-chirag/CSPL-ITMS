package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.StaffDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/1/2020.
 */

public class StaffListResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StaffData")
    @Expose
    private List<StaffDatum> staffData;

    public StaffListResponse() {
        staffData = null;
    }

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

    public List<StaffDatum> getStaffData() {
        return staffData;
    }

    public void setStaffData(List<StaffDatum> staffData) {
        this.staffData = staffData;
    }

}
