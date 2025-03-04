package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 2/26/2020.
 */

public class AccidentReportBusWise {

    @SerializedName("Accident_Date")
    @Expose
    private String accidentDate;
    @SerializedName("Driver_Name")
    @Expose
    private String driverName;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Description")
    @Expose
    private String description;

    public String getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(String accidentDate) {
        this.accidentDate = accidentDate;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
