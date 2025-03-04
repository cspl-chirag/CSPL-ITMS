package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by HP on 12/24/2018.
 */

public class RunKmResponse {

    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("RunKmData")
    @Expose
    private List<RunKmDatum> runKmData = null;

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

    public List<RunKmDatum> getRunKmData() {
        return runKmData;
    }

    public void setRunKmData(List<RunKmDatum> runKmData) {
        this.runKmData = runKmData;
    }


}
