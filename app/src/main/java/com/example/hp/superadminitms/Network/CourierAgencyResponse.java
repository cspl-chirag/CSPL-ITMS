package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.CourierAgencyDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 1/22/2020.
 */

public class CourierAgencyResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("CourierAgencyData")
    @Expose
    private List<CourierAgencyDatum> courierAgencyData = null;

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

    public List<CourierAgencyDatum> getCourierAgencyData() {
        return courierAgencyData;
    }

    public void setCourierAgencyData(List<CourierAgencyDatum> courierAgencyData) {
        this.courierAgencyData = courierAgencyData;
    }
}
