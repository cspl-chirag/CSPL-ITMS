package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 3/14/2020.
 */

public class StaffWiseOffenceReport {
    @SerializedName("Staff_Member_Name")
    @Expose
    private String staffMemberName;
    @SerializedName("Offence_Date")
    @Expose
    private String offenceDate;
    @SerializedName("Designation_Name")
    @Expose
    private String designationName;
    @SerializedName("Offence")
    @Expose
    private String offence;
    @SerializedName("Offence_Amount")
    @Expose
    private Float offenceAmount;
    @SerializedName("Penalty_By")
    @Expose
    private String penaltyBy;

    public String getStaffMemberName() {
        return staffMemberName;
    }

    public void setStaffMemberName(String staffMemberName) {
        this.staffMemberName = staffMemberName;
    }

    public String getOffenceDate() {
        return offenceDate;
    }

    public void setOffenceDate(String offenceDate) {
        this.offenceDate = offenceDate;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getOffence() {
        return offence;
    }

    public void setOffence(String offence) {
        this.offence = offence;
    }

    public Float getOffenceAmount() {
        return offenceAmount;
    }

    public void setOffenceAmount(Float offenceAmount) {
        this.offenceAmount = offenceAmount;
    }

    public String getPenaltyBy() {
        return penaltyBy;
    }

    public void setPenaltyBy(String penaltyBy) {
        this.penaltyBy = penaltyBy;
    }

}
