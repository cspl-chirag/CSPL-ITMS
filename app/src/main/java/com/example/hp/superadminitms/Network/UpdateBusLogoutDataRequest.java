package com.example.hp.superadminitms.Network;

/**
 * Created by ANDROID-PC on 21/01/2019.
 */

public class UpdateBusLogoutDataRequest {
    private final int Log_ID;
    private final int User_ID;
    private final String LogOutTime;
    private final int LogOutKm;
    private final int RunKm;
    private final int ActualTrip;
    private final String Remarks;
    private final String OperatorName;

    public UpdateBusLogoutDataRequest(int log_ID, int user_ID, String logOutTime, int logOutKm, int runKm, int actualTrip, String remarks, String operatorName) {
        Log_ID = log_ID;
        User_ID = user_ID;
        LogOutTime = logOutTime;
        LogOutKm = logOutKm;
        RunKm = runKm;
        ActualTrip = actualTrip;
        Remarks = remarks;
        OperatorName = operatorName;
    }
}
