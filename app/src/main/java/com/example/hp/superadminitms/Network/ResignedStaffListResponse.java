package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.ResignedStaffDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/1/2020.
 */

public class ResignedStaffListResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("ResignedStaffData")
    @Expose
    private List<ResignedStaffDatum> resignedStaffData = null;

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

    public List<ResignedStaffDatum> getResignedStaffData() {
        return resignedStaffData;
    }

    public void setResignedStaffData(List<ResignedStaffDatum> resignedStaffData) {
        this.resignedStaffData = resignedStaffData;
    }

}
