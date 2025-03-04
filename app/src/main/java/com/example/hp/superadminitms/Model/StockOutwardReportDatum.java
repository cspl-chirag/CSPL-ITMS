package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 1/25/2020.
 */

public class StockOutwardReportDatum {

    @SerializedName("Outward_No")
    @Expose
    private Integer outwardNo;
    @SerializedName("Outward_DTM")
    @Expose
    private String outwardDTM;
    @SerializedName("Part_Name")
    @Expose
    private String partName;
    @SerializedName("Part_Code")
    @Expose
    private String partCode;
    @SerializedName("Vehicle_Code")
    @Expose
    private String vehicleCode;
    @SerializedName("VehicleRegNo")
    @Expose
    private String vehicleRegNo;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("Person_Name")
    @Expose
    private String personName;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Given_By")
    @Expose
    private String givenBy;
    @SerializedName("Checked_By")
    @Expose
    private String checkedBy;
    @SerializedName("Remark")
    @Expose
    private String remark;

    public Integer getOutwardNo() {
        return outwardNo;
    }

    public void setOutwardNo(Integer outwardNo) {
        this.outwardNo = outwardNo;
    }

    public String getOutwardDTM() {
        return outwardDTM;
    }

    public void setOutwardDTM(String outwardDTM) {
        this.outwardDTM = outwardDTM;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getVehicleCode() {
        return vehicleCode;
    }

    public void setVehicleCode(String vehicleCode) {
        this.vehicleCode = vehicleCode;
    }

    public String getVehicleRegNo() {
        return vehicleRegNo;
    }

    public void setVehicleRegNo(String vehicleRegNo) {
        this.vehicleRegNo = vehicleRegNo;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGivenBy() {
        return givenBy;
    }

    public void setGivenBy(String givenBy) {
        this.givenBy = givenBy;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
