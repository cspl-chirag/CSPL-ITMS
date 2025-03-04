package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 12/24/2018.
 */

public class RunKmDatum {
    @SerializedName("Days")
    @Expose
    private String days;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("TotalRunKm")
    @Expose
    private Integer totalRunKm;

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTotalRunKm() {
        return totalRunKm;
    }

    public void setTotalRunKm(Integer totalRunKm) {
        this.totalRunKm = totalRunKm;
    }

}
