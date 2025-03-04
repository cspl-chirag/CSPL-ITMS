package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.JobCardDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android-2 on 13-Mar-19.
 */

public class JobCardDatumResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("DailyJobData")
    @Expose
    private List<JobCardDatum> dailyJobData = null;

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

    public List<JobCardDatum> getDailyJobData() {
        return dailyJobData;
    }

    public void setDailyJobData(List<JobCardDatum> dailyJobData) {
        this.dailyJobData = dailyJobData;
    }
}
