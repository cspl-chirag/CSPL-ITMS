package com.example.hp.superadminitms.Network;

/**
 * Created by ANDROID-PC on 21/01/2019.
 */

public class UpdateBusLoginDataRequest {
    private final int Log_ID;
    private final int User_ID;
    private final String LoginTime;
    private final int Company_ID;
    private final int VehicleID;
    private final String LogsheetCode;
    private final int Shift;
    private final int Route_ID;
    private final String Driver_ID;
    private final int Depot_ID;
    private final int OpeningKm;
    private final String OperatorName;

    public UpdateBusLoginDataRequest(int log_ID, int user_ID, String loginTime, int company_ID, int vehicleID, String logsheetCode, int shift, int route_ID, String driver_ID, int depot_ID, int openingKm, String operatorName) {
        Log_ID = log_ID;
        User_ID = user_ID;
        LoginTime = loginTime;
        Company_ID = company_ID;
        VehicleID = vehicleID;
        LogsheetCode = logsheetCode;
        Shift = shift;
        Route_ID = route_ID;
        Driver_ID = driver_ID;
        Depot_ID = depot_ID;
        OpeningKm = openingKm;
        OperatorName = operatorName;
    }
}
