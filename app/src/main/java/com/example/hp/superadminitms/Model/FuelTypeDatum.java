package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ANDROID-PC on 20/11/2018.
 */

public class FuelTypeDatum {

    @SerializedName("FuelType")
    @Expose
    private String fuelType;

    public FuelTypeDatum(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

}
