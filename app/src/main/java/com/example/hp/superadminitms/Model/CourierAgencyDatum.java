package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 1/22/2020.
 */

public class CourierAgencyDatum {
    @SerializedName("Courier_Agency_ID")
    @Expose
    private Integer courierAgencyID;
    @SerializedName("Courier_Agency_Name")
    @Expose
    private String courierAgencyName;

    public CourierAgencyDatum(Integer courierAgencyID, String courierAgencyName) {
        this.courierAgencyID = courierAgencyID;
        this.courierAgencyName = courierAgencyName;
    }

    public Integer getCourierAgencyID() {
        return courierAgencyID;
    }

    public void setCourierAgencyID(Integer courierAgencyID) {
        this.courierAgencyID = courierAgencyID;
    }

    public String getCourierAgencyName() {
        return courierAgencyName;
    }

    public void setCourierAgencyName(String courierAgencyName) {
        this.courierAgencyName = courierAgencyName;
    }

    @Override
    public String toString() {
        return this.courierAgencyName;
    }
}
