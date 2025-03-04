package com.example.hp.superadminitms.Network;

/**
 * Created by ANDROID-PC on 02/01/2019.
 */

public class GetLastLogOutKmDataRequest {
    private final int Vehicle_Company_ID;
    private final int VehicleID;

    public GetLastLogOutKmDataRequest(int vehicle_Company_ID, int vehicleID) {
        Vehicle_Company_ID = vehicle_Company_ID;
        VehicleID = vehicleID;
    }
}
