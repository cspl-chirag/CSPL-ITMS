package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Android-2 on 30-May-19.
 */

public class ExpirationAlertDatum {
    @SerializedName("Table")
    @Expose
    private List<LiceneceExpDatum> liceneceExpDatumList = null;
    @SerializedName("Table1")
    @Expose
    private List<InsuranceExpDatum> insuranceExpDatumList = null;
    @SerializedName("Table2")
    @Expose
    private List<RTOExpDatum> rtoExpDatumList = null;
    @SerializedName("Table3")
    @Expose
    private List<FitnessExpDatum> fitnessExpDatumList = null;
    @SerializedName("Table4")
    @Expose
    private List<FirstScheduleService> firstScheduleServices = null;
    @SerializedName("Table5")
    @Expose
    private List<SecondScheduleService> secondScheduleServices = null;

    public List<LiceneceExpDatum> getTable() {
        return liceneceExpDatumList;
    }

    public void setTable(List<LiceneceExpDatum> table) {
        this.liceneceExpDatumList = table;
    }

    public List<InsuranceExpDatum> getTable1() {
        return insuranceExpDatumList;
    }

    public void setTable1(List<InsuranceExpDatum> table1) {
        this.insuranceExpDatumList = table1;
    }

    public List<RTOExpDatum> getTable2() {
        return rtoExpDatumList;
    }

    public void setTable2(List<RTOExpDatum> table2) {
        this.rtoExpDatumList = table2;
    }

    public List<FitnessExpDatum> getTable3() {
        return fitnessExpDatumList;
    }

    public void setTable3(List<FitnessExpDatum> table3) {
        this.fitnessExpDatumList = table3;
    }

    public List<FirstScheduleService> getTable4() {
        return firstScheduleServices;
    }

    public void setFirstScheduleServices(List<FirstScheduleService> firstScheduleServices) {
        this.firstScheduleServices = firstScheduleServices;
    }

    public List<SecondScheduleService> getTable5() {
        return secondScheduleServices;
    }

    public void setSecondScheduleServices(List<SecondScheduleService> secondScheduleServices) {
        this.secondScheduleServices = secondScheduleServices;
    }
}
