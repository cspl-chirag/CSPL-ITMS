package com.example.hp.superadminitms.Network;

/**
 * Created by INFOTEK on 2/25/2020.
 */

public class AccidentEntryRequest {
    private final int User_ID;
    private final int Company_ID;
    private final int Vehicle_ID;
    private final String Accident_Date;
    private final int Driver_ID;
    private final int Route_ID;
    private final String Location;
    private final String Description;

    public AccidentEntryRequest(int user_ID, int company_ID, int vehicle_ID, String accident_Date, int driver_ID, int route_ID, String location, String description) {
        User_ID = user_ID;
        Company_ID = company_ID;
        Vehicle_ID = vehicle_ID;
        Accident_Date = accident_Date;
        Driver_ID = driver_ID;
        Route_ID = route_ID;
        Location = location;
        Description = description;
    }
}
