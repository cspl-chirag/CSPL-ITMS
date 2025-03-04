package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.LastKmDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ANDROID-PC on 02/01/2019.
 */

public class GetLastLogOutKmDataResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("LastKmData")
    @Expose
    private List<LastKmDatum> lastKmData = null;

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

    public List<LastKmDatum> getLastKmData() {
        return lastKmData;
    }

    public void setLastKmData(List<LastKmDatum> lastKmData) {
        this.lastKmData = lastKmData;
    }
}
