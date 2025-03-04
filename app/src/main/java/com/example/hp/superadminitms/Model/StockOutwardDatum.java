package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by INFOTEK on 2/21/2020.
 */

public class StockOutwardDatum implements Serializable {
    @SerializedName("Outward_Batch_ID")
    @Expose
    private Integer outwardBatchID;
    @SerializedName("Part_ID")
    @Expose
    private Integer partID;
    @SerializedName("Vehicle_ID")
    @Expose
    private Integer vehicleID;
    @SerializedName("Outward_No")
    @Expose
    private Integer outwardNo;
    @SerializedName("Outward_DTM")
    @Expose
    private String outwardDTM;
    @SerializedName("Quantity")
    @Expose
    private Integer quantity;
    @SerializedName("Given_By")
    @Expose
    private String givenBy;
    @SerializedName("Operator_Name")
    @Expose
    private String operatorName;
    @SerializedName("Remark")
    @Expose
    private String remark;

    public Integer getOutwardBatchID() {
        return outwardBatchID;
    }

    public void setOutwardBatchID(Integer outwardBatchID) {
        this.outwardBatchID = outwardBatchID;
    }

    public Integer getPartID() {
        return partID;
    }

    public void setPartID(Integer partID) {
        this.partID = partID;
    }

    public Integer getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(Integer vehicleID) {
        this.vehicleID = vehicleID;
    }

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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getGivenBy() {
        return givenBy;
    }

    public void setGivenBy(String givenBy) {
        this.givenBy = givenBy;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
