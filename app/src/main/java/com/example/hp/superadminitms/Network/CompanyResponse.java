package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.CompanyDatum;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android-2 on 11-Feb-19.
 */

public class CompanyResponse {
    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("CompanyData")
    @Expose
    private List<CompanyDatum> companyData = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CompanyDatum> getCompanyData() {
        return companyData;
    }

    public void setCompanyData(List<CompanyDatum> companyData) {
        this.companyData = companyData;
    }
}
