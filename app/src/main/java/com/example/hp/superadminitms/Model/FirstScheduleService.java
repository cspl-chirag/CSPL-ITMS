package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 2/5/2020.
 */

public class FirstScheduleService {
    @SerializedName("Vehicle_ID")
    @Expose
    private Integer vehicleID;
    @SerializedName("Vehicle_Name")
    @Expose
    private String vehicleName;
    @SerializedName("LogDate")
    @Expose
    private String logDate;
    @SerializedName("FirstServiceKm")
    @Expose
    private Integer firstServiceKm;
    @SerializedName("NoOfOccurence")
    @Expose
    private Integer noOfOccurence;

    public Integer getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(Integer vehicleID) {
        this.vehicleID = vehicleID;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public Integer getFirstServiceKm() {
        return firstServiceKm;
    }

    public void setFirstServiceKm(Integer firstServiceKm) {
        this.firstServiceKm = firstServiceKm;
    }

    public Integer getNoOfOccurence() {
        return noOfOccurence;
    }

    public void setNoOfOccurence(Integer noOfOccurence) {
        this.noOfOccurence = noOfOccurence;
    }

}
