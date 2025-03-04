package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.ExpirationAlertDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android-2 on 30-May-19.
 */

public class ExpirationResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("ExpirationAlertData")
    @Expose
    private ExpirationAlertDatum expirationAlertData;

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

    public ExpirationAlertDatum getExpirationAlertData() {
        return expirationAlertData;
    }

    public void setExpirationAlertData(ExpirationAlertDatum expirationAlertData) {
        this.expirationAlertData = expirationAlertData;
    }

}
