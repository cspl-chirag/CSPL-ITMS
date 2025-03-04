package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ANDROID-PC on 12/12/2018.
 */

public class DepotDatum {
    @SerializedName("Dept_ID")
    @Expose
    private Integer deptID;
    @SerializedName("Dept_Name")
    @Expose
    private String deptName;

    public Integer getDeptID() {
        return deptID;
    }

    public void setDeptID(Integer deptID) {
        this.deptID = deptID;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
}
