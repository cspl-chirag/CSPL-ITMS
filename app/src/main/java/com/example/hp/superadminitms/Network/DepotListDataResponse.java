package com.example.hp.superadminitms.Network;


import com.example.hp.superadminitms.Model.DepotDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ANDROID-PC on 12/12/2018.
 */

public class DepotListDataResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("DepotData")
    @Expose
    private List<DepotDatum> depotData = null;

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

    public List<DepotDatum> getDepotData() {
        return depotData;
    }

    public void setDepotData(List<DepotDatum> depotData) {
        this.depotData = depotData;
    }
}
