package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.StockInwardReportDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 1/25/2020.
 */

public class ReportInwardResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StockInwardReport")
    @Expose
    private List<StockInwardReportDatum> stockInwardReport = null;

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

    public List<StockInwardReportDatum> getStockInwardReport() {
        return stockInwardReport;
    }

    public void setStockInwardReport(List<StockInwardReportDatum> stockInwardReport) {
        this.stockInwardReport = stockInwardReport;
    }
}
