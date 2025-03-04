package com.example.hp.superadminitms.Network;

/**
 * Created by Android-2 on 27-Feb-19.
 */

public class BusRequest {
    int Vehicle_Company_ID;
    private String LogDate;

    public BusRequest(int Vehicle_Company_ID) {
        this.Vehicle_Company_ID = Vehicle_Company_ID;
    }

    public BusRequest(int vehicle_Company_ID, String logDate) {
        Vehicle_Company_ID = vehicle_Company_ID;
        LogDate = logDate;
    }
}

