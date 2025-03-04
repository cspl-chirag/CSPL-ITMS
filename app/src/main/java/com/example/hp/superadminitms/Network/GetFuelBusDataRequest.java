package com.example.hp.superadminitms.Network;

/**
 * Created by ANDROID-PC on 05/01/2019.
 */

public class GetFuelBusDataRequest {
    private final int Company_ID;
    private final String Fuel_Date;

    public GetFuelBusDataRequest(int company_ID, String fuel_Date) {
        Company_ID = company_ID;
        Fuel_Date = fuel_Date;
    }
}
