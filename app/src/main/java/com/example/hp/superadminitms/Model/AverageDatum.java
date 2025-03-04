package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 12/26/2018.
 */

public class AverageDatum {
    @SerializedName("Days")
    @Expose
    private String days;
    @SerializedName("Average")
    @Expose
    private Double average;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }


}
