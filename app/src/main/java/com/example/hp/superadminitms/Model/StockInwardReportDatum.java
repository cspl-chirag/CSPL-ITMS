package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 1/25/2020.
 */

public class StockInwardReportDatum {
    @SerializedName("Inward_No")
    @Expose
    private Integer inwardNo;
    @SerializedName("Inward_DTM")
    @Expose
    private String inwardDTM;
    @SerializedName("Bill_No")
    @Expose
    private String billNo;
    @SerializedName("Store_Name")
    @Expose
    private String storeName;
    @SerializedName("Part_Name")
    @Expose
    private String partName;
    @SerializedName("Part_Code")
    @Expose
    private String partCode;
    @SerializedName("Manufacturer_Name")
    @Expose
    private String manufacturerName;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("Rate")
    @Expose
    private Float rate;
    @SerializedName("Purchased_By")
    @Expose
    private String purchasedBy;
    @SerializedName("Checked_By")
    @Expose
    private String checkedBy;
    @SerializedName("Courier_Agency_Name")
    @Expose
    private String courierAgencyName;
    @SerializedName("Remark")
    @Expose
    private String remark;

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

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
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

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
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

    public String getPurchasedBy() {
        return purchasedBy;
    }

    public void setPurchasedBy(String purchasedBy) {
        this.purchasedBy = purchasedBy;
    }

    public String getCheckedBy() {
        return checkedBy;
    }

    public void setCheckedBy(String checkedBy) {
        this.checkedBy = checkedBy;
    }

    public String getCourierAgencyName() {
        return courierAgencyName;
    }

    public void setCourierAgencyName(String courierAgencyName) {
        this.courierAgencyName = courierAgencyName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
