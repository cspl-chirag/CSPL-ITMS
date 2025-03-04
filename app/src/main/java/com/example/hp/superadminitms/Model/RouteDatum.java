package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android-2 on 08-Mar-19.
 */

public class RouteDatum {
    @SerializedName("Route_ID")
    @Expose
    private Integer routeID;
    @SerializedName("Route_No")
    @Expose
    private String routeNo;

    public Integer getRouteID() {
        return routeID;
    }

    public void setRouteID(Integer routeID) {
        this.routeID = routeID;
    }

    public String getRouteNo() {
        return routeNo;
    }

    public void setRouteNo(String routeNo) {
        this.routeNo = routeNo;
    }

    public String toString() {
        return this.routeNo;
    }
}
