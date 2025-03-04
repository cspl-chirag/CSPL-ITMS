package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 3/13/2020.
 */

public class StaffWiseKitAssignReport {

    @SerializedName("Staff_Member_Name")
    @Expose
    private String staffMemberName;
    @SerializedName("Assign_Date")
    @Expose
    private String assignDate;
    @SerializedName("Designation_Name")
    @Expose
    private String designationName;
    @SerializedName("Assign_By")
    @Expose
    private String assignBy;
    @SerializedName("Assign_Kit_Detail")
    @Expose
    private String assignKitDetail;

    public String getStaffMemberName() {
        return staffMemberName;
    }

    public void setStaffMemberName(String staffMemberName) {
        this.staffMemberName = staffMemberName;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public String getDesignationName() {
        return designationName;
    }

    public void setDesignationName(String designationName) {
        this.designationName = designationName;
    }

    public String getAssignBy() {
        return assignBy;
    }

    public void setAssignBy(String assignBy) {
        this.assignBy = assignBy;
    }

    public String getAssignKitDetail() {
        return assignKitDetail;
    }

    public void setAssignKitDetail(String assignKitDetail) {
        this.assignKitDetail = assignKitDetail;
    }
}
