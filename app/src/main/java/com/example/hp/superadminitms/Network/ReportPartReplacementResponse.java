package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.StockPartReplacementReportDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 1/25/2020.
 */

public class ReportPartReplacementResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("PartReplacementReport")
    @Expose
    private List<StockPartReplacementReportDatum> partReplacementReport = null;

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

    public List<StockPartReplacementReportDatum> getPartReplacementReport() {
        return partReplacementReport;
    }

    public void setPartReplacementReport(List<StockPartReplacementReportDatum> partReplacementReport) {
        this.partReplacementReport = partReplacementReport;
    }
}
