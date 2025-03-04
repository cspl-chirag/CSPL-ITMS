package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.PartsDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/22/2020.
 */

public class PartListResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("PartsData")
    @Expose
    private List<PartsDatum> partsData = null;

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

    public List<PartsDatum> getPartsData() {
        return partsData;
    }

    public void setPartsData(List<PartsDatum> partsData) {
        this.partsData = partsData;
    }
}
