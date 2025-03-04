package com.example.hp.superadminitms.Network;

/**
 * Created by ANDROID-PC on 08/12/2018.
 */

public class GetBusLoginDataRequest {
    private final int Vehicle_Company_ID;
    private final int VehicleID;

    public GetBusLoginDataRequest(int vehicle_Company_ID, int vehicleID) {
        Vehicle_Company_ID = vehicle_Company_ID;
        VehicleID = vehicleID;
    }
}
