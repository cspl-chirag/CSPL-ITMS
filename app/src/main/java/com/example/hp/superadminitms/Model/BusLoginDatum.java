package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 01/08/2019.
 */

public class BusLoginDatum {
    @SerializedName("Days")
    @Expose
    private String days;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("TotalBus")
    @Expose
    private Integer totalBus;

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

    public Integer getTotalBus() {
        return totalBus;
    }

    public void setTotalBus(Integer totalBus) {
        this.totalBus = totalBus;
    }
}
