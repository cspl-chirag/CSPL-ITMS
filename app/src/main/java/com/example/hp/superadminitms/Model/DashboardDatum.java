package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 12/18/2018.
 */

public class DashboardDatum {
    @SerializedName("TodayOnRouteShift1")
    @Expose
    private Integer todayOnRouteShift1;
    @SerializedName("YesterdayOnRouteShift1")
    @Expose
    private Integer yesterdayOnRouteShift1;
    @SerializedName("TodayOnRouteShift2")
    @Expose
    private Integer todayOnRouteShift2;
    @SerializedName("YesterdayOnRouteShift2")
    @Expose
    private Integer yesterdayOnRouteShift2;
    @SerializedName("YesterdayRunkm")
    @Expose
    private Integer yesterdayRunkm;
    @SerializedName("YesterdayBreakDown")
    @Expose
    private Integer yesterdayBreakDown;
    @SerializedName("YesterdayAvg")
    @Expose
    private Double yesterdayAvg;
    @SerializedName("YesterdayFuelConsumption")
    @Expose
    private Double yesterdayFuelConsumption;
    @SerializedName("YesterdayFuelConsumptionAmnt")
    @Expose
    private Double yesterdayFuelConsumptionAmnt;
    @SerializedName("YesterdayFuelConsumptionRate")
    @Expose
    private Double yesterdayFuelConsumptionRate;
    @SerializedName("YesterdayAvg1")
    @Expose
    private Integer yesterdayAvg1;
    @SerializedName("TotalBus")
    @Expose
    private Integer totalBus;

    public Integer getTodayOnRouteShift1() {
        return todayOnRouteShift1;
    }

    public void setTodayOnRouteShift1(Integer todayOnRouteShift1) {
        this.todayOnRouteShift1 = todayOnRouteShift1;
    }

    public Integer getYesterdayOnRouteShift1() {
        return yesterdayOnRouteShift1;
    }

    public void setYesterdayOnRouteShift1(Integer yesterdayOnRouteShift1) {
        this.yesterdayOnRouteShift1 = yesterdayOnRouteShift1;
    }

    public Integer getTodayOnRouteShift2() {
        return todayOnRouteShift2;
    }

    public void setTodayOnRouteShift2(Integer todayOnRouteShift2) {
        this.todayOnRouteShift2 = todayOnRouteShift2;
    }

    public Integer getYesterdayOnRouteShift2() {
        return yesterdayOnRouteShift2;
    }

    public void setYesterdayOnRouteShift2(Integer yesterdayOnRouteShift2) {
        this.yesterdayOnRouteShift2 = yesterdayOnRouteShift2;
    }

    public Integer getYesterdayRunkm() {
        return yesterdayRunkm;
    }

    public void setYesterdayRunkm(Integer yesterdayRunkm) {
        this.yesterdayRunkm = yesterdayRunkm;
    }

    public Integer getYesterdayBreakDown() {

        return yesterdayBreakDown;
    }

    public void setYesterdayBreakDown(Integer yesterdayBreakDown) {
        this.yesterdayBreakDown = yesterdayBreakDown;
    }

    public Double getYesterdayAvg() {
        return yesterdayAvg;
    }

    public void setYesterdayAvg(Double yesterdayAvg) {
        this.yesterdayAvg = yesterdayAvg;
    }

    public Double getYesterdayFuelConsumption() {
        return yesterdayFuelConsumption;
    }

    public void setYesterdayFuelConsumption(Double yesterdayFuelConsumption) {
        this.yesterdayFuelConsumption = yesterdayFuelConsumption;
    }

    public Double getYesterdayFuelConsumptionAmnt() {
        return yesterdayFuelConsumptionAmnt;
    }

    public void setYesterdayFuelConsumptionAmnt(Double yesterdayFuelConsumptionAmnt) {
        this.yesterdayFuelConsumptionAmnt = yesterdayFuelConsumptionAmnt;
    }

    public Double getYesterdayFuelConsumptionRate() {
        return yesterdayFuelConsumptionRate;
    }

    public void setYesterdayFuelConsumptionRate(Double yesterdayFuelConsumptionRate) {
        this.yesterdayFuelConsumptionRate = yesterdayFuelConsumptionRate;
    }

    public Integer getYesterdayAvg1() {
        return yesterdayAvg1;
    }

    public void setYesterdayAvg1(Integer yesterdayAvg1) {
        this.yesterdayAvg1 = yesterdayAvg1;
    }

    public Integer getTotalBus() {
        return totalBus;
    }

    public void setTotalBus(Integer totalBus) {
        this.totalBus = totalBus;
    }
}
