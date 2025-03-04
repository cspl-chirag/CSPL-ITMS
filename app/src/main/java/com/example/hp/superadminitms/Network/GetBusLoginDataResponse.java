package com.example.hp.superadminitms.Network;


import com.example.hp.superadminitms.Model.BusLoginOPDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ANDROID-PC on 08/12/2018.
 */

public class GetBusLoginDataResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("BusLoginData")
    @Expose
    private List<BusLoginOPDatum> busLoginData = null;

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

    public List<BusLoginOPDatum> getBusLoginData() {
        return busLoginData;
    }

    public void setBusLoginData(List<BusLoginOPDatum> busLoginData) {
        this.busLoginData = busLoginData;
    }
}
