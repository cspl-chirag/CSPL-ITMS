package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.StockInwardDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/18/2020.
 */

public class StockInwardDataResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StockInwardData")
    @Expose
    private List<StockInwardDatum> stockInwardData = null;

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

    public List<StockInwardDatum> getStockInwardData() {
        return stockInwardData;
    }

    public void setStockInwardData(List<StockInwardDatum> stockInwardData) {
        this.stockInwardData = stockInwardData;
    }

}
