package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android-2 on 24-Feb-20.
 */

public class DailySummaryDatum {
    @SerializedName("BusOnRoad")
    @Expose
    private Integer busOnRoad;
    @SerializedName("Breakdown")
    @Expose
    private Integer breakdown;
    @SerializedName("Spare")
    @Expose
    private Integer spare;
    @SerializedName("Other")
    @Expose
    private Integer other;
    @SerializedName("TotalBus")
    @Expose
    private Integer totalBus;

    public Integer getBusOnRoad() {
        return busOnRoad;
    }

    public void setBusOnRoad(Integer busOnRoad) {
        this.busOnRoad = busOnRoad;
    }

    public Integer getBreakdown() {
        return breakdown;
    }

    public void setBreakdown(Integer breakdown) {
        this.breakdown = breakdown;
    }

    public Integer getSpare() {
        return spare;
    }

    public void setSpare(Integer spare) {
        this.spare = spare;
    }

    public Integer getOther() {
        return other;
    }

    public void setOther(Integer other) {
        this.other = other;
    }

    public Integer getTotalBus() {
        return totalBus;
    }

    public void setTotalBus(Integer totalBus) {
        this.totalBus = totalBus;
    }

}
