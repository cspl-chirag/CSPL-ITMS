package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by INFOTEK on 2/18/2020.
 */

public class StockInwardDatum implements Serializable {
    @SerializedName("Bill_No")
    @Expose
    private String billNo;
    @SerializedName("Part_ID")
    @Expose
    private Integer partID;
    @SerializedName("Part_Name")
    @Expose
    private String partName;
    @SerializedName("Store_ID")
    @Expose
    private Integer storeID;
    @SerializedName("Manufacturer_Name")
    @Expose
    private String manufacturerName;
    @SerializedName("Inward_No")
    @Expose
    private Integer inwardNo;
    @SerializedName("Inward_DTM")
    @Expose
    private String inwardDTM;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("Rate")
    @Expose
    private Float rate;
    @SerializedName("Bill_Amount")
    @Expose
    private Float billAmount;
    @SerializedName("Checked_By")
    @Expose
    private String checkedBy;
    @SerializedName("Courier_Agency_ID")
    @Expose
    private Integer courierAgencyID;
    @SerializedName("Courier_LR_Number")
    @Expose
    private String courierLRNumber;
    @SerializedName("Remark")
    @Expose
    private String remark;

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Integer getPartID() {
        return partID;
    }

    public void setPartID(Integer partID) {
        this.partID = partID;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public Integer getInwardNo() {
        return inwardNo;
    }

    public void setInwardNo(Integer inwardNo) {
        this.inwardNo = inwardNo;
    }

    public String getInwardDTM() {
        return inwardDTM;
    }

    public void setInwardDTM(String inwardDTM) {
        this.inwardDTM = inwardDTM;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getRate() {
        return rate;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public Float getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(Float billAmount) {
        this.billAmount = billAmount;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public Integer getCourierAgencyID() {
        return courierAgencyID;
    }

    public void setCourierAgencyID(Integer courierAgencyID) {
        this.courierAgencyID = courierAgencyID;
    }

    public String getCourierLRNumber() {
        return courierLRNumber;
    }

    public void setCourierLRNumber(String courierLRNumber) {
        this.courierLRNumber = courierLRNumber;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

}
