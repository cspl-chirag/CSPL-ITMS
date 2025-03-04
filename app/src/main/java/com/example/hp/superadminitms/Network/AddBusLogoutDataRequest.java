package com.example.hp.superadminitms.Network;

/**
 * Created by ANDROID-PC on 06/12/2018.
 */

public class AddBusLogoutDataRequest {
    private final int Log_ID;
    private final int User_ID;
    private final String LogOutDate;
    private final String LogOutTime;
    private final int LogOutKm;
    private final int RunKm;
    private final int ActualTrip;
    private final String Remarks;
    private final String OperatorName;

    public AddBusLogoutDataRequest(int log_ID, int user_ID, String logOutDate, String logOutTime, int logOutKm, int runKm, int actualTrip, String remarks, String operatorName) {
        Log_ID = log_ID;
        User_ID = user_ID;
        LogOutDate = logOutDate;
        LogOutTime = logOutTime;
        LogOutKm = logOutKm;
        RunKm = runKm;
        ActualTrip = actualTrip;
        Remarks = remarks;
        OperatorName = operatorName;
    }
}
