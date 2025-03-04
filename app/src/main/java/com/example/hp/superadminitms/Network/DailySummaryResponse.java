package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.DailySummaryDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android-2 on 24-Feb-20.
 */

public class DailySummaryResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("BusSummaryData")
    @Expose
    private List<DailySummaryDatum> busSummaryData = null;

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

    public List<DailySummaryDatum> getBusSummaryData() {
        return busSummaryData;
    }

    public void setBusSummaryData(List<DailySummaryDatum> busSummaryData) {
        this.busSummaryData = busSummaryData;
    }
}
