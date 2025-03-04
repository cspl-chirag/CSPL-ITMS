package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.PartStoreDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 1/24/2020.
 */

public class PartStoreListResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StoreData")
    @Expose
    private List<PartStoreDatum> storeData = null;

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

    public List<PartStoreDatum> getStoreData() {
        return storeData;
    }

    public void setStoreData(List<PartStoreDatum> storeData) {
        this.storeData = storeData;
    }

}
