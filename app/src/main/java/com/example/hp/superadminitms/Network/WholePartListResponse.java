package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.WholeListPartDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android-2 on 06-Jan-20.
 */

public class WholePartListResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("PartsData")
    @Expose
    private List<WholeListPartDatum> partsData = null;

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

    public List<WholeListPartDatum> getPartsData() {
        return partsData;
    }

    public void setPartsData(List<WholeListPartDatum> partsData) {
        this.partsData = partsData;
    }
}
