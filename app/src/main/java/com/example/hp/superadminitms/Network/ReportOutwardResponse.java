package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.StockOutwardReportDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 1/25/2020.
 */

public class ReportOutwardResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StockOutwardReport")
    @Expose
    private List<StockOutwardReportDatum> stockOutwardReport = null;

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

    public List<StockOutwardReportDatum> getStockOutwardReport() {
        return stockOutwardReport;
    }

    public void setStockOutwardReport(List<StockOutwardReportDatum> stockOutwardReport) {
        this.stockOutwardReport = stockOutwardReport;
    }


}
