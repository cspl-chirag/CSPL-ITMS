package com.example.hp.superadminitms.Network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android-2 on 05-Mar-19.
 */

public class AddBreakdownResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;

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
}
