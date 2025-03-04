package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android-2 on 11-Feb-19.
 */

public class CompanyDatum {

    @SerializedName("Company_ID")
    @Expose
    private Integer companyID;
    @SerializedName("Company_Name")
    @Expose
    private String companyName;
    @SerializedName("Company_City")
    @Expose
    private String companyCity;

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

}
