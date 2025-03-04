package com.example.hp.superadminitms.Model;

/**
 * Created by ANDROID-PC on 25/12/2018.
 */

public class ShiftDatum {

    private int shiftNo;
    private String shiftName;

    public ShiftDatum(int shiftNo, String shiftName) {
        this.shiftNo = shiftNo;
        this.shiftName = shiftName;
    }

    public int getShiftNo() {
        return shiftNo;
    }

    public void setShiftNo(int shiftNo) {
        this.shiftNo = shiftNo;
    }

    public String getShiftName() {
        return shiftName;
    }

    public void setShiftName(String shiftName) {
        this.shiftName = shiftName;
    }
}
