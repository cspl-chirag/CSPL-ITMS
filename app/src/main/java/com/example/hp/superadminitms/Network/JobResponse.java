package com.example.hp.superadminitms.Network;


import com.example.hp.superadminitms.Model.JobDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android-2 on 09-Mar-19.
 */

public class JobResponse {

    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("JobCardData")
    @Expose
    private List<JobDatum> jobCardData = null;

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

    public List<JobDatum> getJobCardData() {
        return jobCardData;
    }

    public void setJobCardData(List<JobDatum> jobCardData) {
        this.jobCardData = jobCardData;
    }


}
