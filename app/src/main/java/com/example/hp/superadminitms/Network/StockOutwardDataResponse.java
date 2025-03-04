package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.StockOutwardDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/21/2020.
 */

public class StockOutwardDataResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("StockOutwardData")
    @Expose
    private List<StockOutwardDatum> stockOutwardData = null;

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

    public List<StockOutwardDatum> getStockOutwardData() {
        return stockOutwardData;
    }

    public void setStockOutwardData(List<StockOutwardDatum> stockOutwardData) {
        this.stockOutwardData = stockOutwardData;
    }

}
