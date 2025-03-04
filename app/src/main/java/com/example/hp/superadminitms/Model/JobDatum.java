package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android-2 on 09-Mar-19.
 */

public class JobDatum {
    @SerializedName("Job_ID")
    @Expose
    private Integer jobID;
    @SerializedName("JobName")
    @Expose
    private String jobName;

    public Integer getJobID() {
        return jobID;
    }

    public void setJobID(Integer jobID) {
        this.jobID = jobID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }
}
