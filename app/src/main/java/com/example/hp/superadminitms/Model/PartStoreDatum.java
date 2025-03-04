package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by INFOTEK on 1/24/2020.
 */

public class PartStoreDatum {
    @SerializedName("Store_ID")
    @Expose
    private Integer storeID;
    @SerializedName("Store_Name")
    @Expose
    private String storeName;

    public PartStoreDatum(Integer storeID, String storeName) {
        this.storeID = storeID;
        this.storeName = storeName;
    }

    public Integer getStoreID() {
        return storeID;
    }

    public void setStoreID(Integer storeID) {
        this.storeID = storeID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    @Override
    public String toString() {
        return this.getStoreName();
    }
}
