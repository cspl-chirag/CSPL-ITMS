package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by HP on 01/07/2019.
 */

public class FuelDatum {

    @SerializedName("Days")
    @Expose
    private String days;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("FuelConsumption")
    @Expose
    private Integer fuelConsumption;

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

    public Integer getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(Integer fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }


}
