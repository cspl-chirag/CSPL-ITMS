package com.example.hp.superadminitms.Network;

/**
 * Created by ANDROID-PC on 06/12/2018.
 */

public class AddBusLoginDataRequest {
    private final int User_ID;
    private final String LogDate;
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

    public AddBusLoginDataRequest(int user_ID, String logDate, String loginTime, int company_ID, int vehicleID, String logsheetCode, int shift, int routeID, String driverID, int depotID, int openingKm, String operatorName) {
        User_ID = user_ID;
        LogDate = logDate;
        LoginTime = loginTime;
        Company_ID = company_ID;
        VehicleID = vehicleID;
        LogsheetCode = logsheetCode;
        Shift = shift;
        Route_ID = routeID;
        Driver_ID = driverID;
        Depot_ID = depotID;
        OpeningKm = openingKm;
        OperatorName = operatorName;
    }
}
