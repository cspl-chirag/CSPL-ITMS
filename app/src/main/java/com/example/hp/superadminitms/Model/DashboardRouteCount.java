package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 2/18/2020.
 */

public class DashboardRouteCount {
    @SerializedName("Route_No")
    @Expose
    private String routeNo;
    @SerializedName("Route_Name")
    @Expose
    private String routeName;
    @SerializedName("TodayOnRoad")
    @Expose
    private Integer todayOnRoad;
    @SerializedName("YesterdayOnRoad")
    @Expose
    private Integer yesterdayOnRoad;

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String getRouteName() {
        return routeName;
    }

    public void setRouteName(String routeName) {
        this.routeName = routeName;
    }

    public Integer getTodayOnRoad() {
        return todayOnRoad;
    }

    public void setTodayOnRoad(Integer todayOnRoad) {
        this.todayOnRoad = todayOnRoad;
    }

    public Integer getYesterdayOnRoad() {
        return yesterdayOnRoad;
    }

    public void setYesterdayOnRoad(Integer yesterdayOnRoad) {
        this.yesterdayOnRoad = yesterdayOnRoad;
    }
}
