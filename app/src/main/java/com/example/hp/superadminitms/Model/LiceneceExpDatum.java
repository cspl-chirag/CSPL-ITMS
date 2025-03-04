package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android-2 on 30-May-19.
 */

public class LiceneceExpDatum {
    @SerializedName("Staff_ID")
    @Expose
    private Integer staffID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("LicenceExpiryDate")
    @Expose
    private String licenceExpiryDate;

    public Integer getStaffID() {
        return staffID;
    }

    public void setStaffID(Integer staffID) {
        this.staffID = staffID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLicenceExpiryDate() {
        return licenceExpiryDate;
    }

    public void setLicenceExpiryDate(String licenceExpiryDate) {
        this.licenceExpiryDate = licenceExpiryDate;
    }
}
