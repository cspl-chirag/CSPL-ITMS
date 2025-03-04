package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Android-2 on 06-Jan-20.
 */

public class WholeListPartDatum {

    @SerializedName("Part_ID")
    @Expose
    private Integer partID;
    @SerializedName("Part_Code")
    @Expose
    private String partCode;
    @SerializedName("Part_Name")
    @Expose
    private String partName;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("Rate")
    @Expose
    private Double rate;

    private Integer newQuantity;

    public WholeListPartDatum(Integer partID, String partCode, String partName) {
        this.partID = partID;
        this.partCode = partCode;
        this.partName = partName;
    }

    public WholeListPartDatum(Integer partID, String partCode, String partName, Integer quantity) {
        this.partID = partID;
        this.partCode = partCode;
        this.partName = partName;
        this.quantity = quantity;
    }

    public Integer getNewQuantity() {
        return newQuantity;
    }

    public void setNewQuantity(Integer newQuantityy) {
        newQuantity = newQuantityy;
    }

    public Integer getPartID() {
        return partID;
    }

    public void setPartID(Integer partID) {
        this.partID = partID;
    }

    public String getPartCode() {
        return partCode;
    }

    public void setPartCode(String partCode) {
        this.partCode = partCode;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    @Override
    public String toString() {

        if (this.partCode != null) {
            return this.partName + " - " + this.partCode;
        } else {
            return this.partName;
        }
    }
}
