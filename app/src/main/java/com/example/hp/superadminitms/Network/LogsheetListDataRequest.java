package com.example.hp.superadminitms.Network;

/**
 * Created by ANDROID-PC on 03/01/2019.
 */

public class LogsheetListDataRequest {

    private int Route_ID;
    private int Company_ID;
    private String LogDate;

    public LogsheetListDataRequest(int route_ID, int company_ID) {
        Route_ID = route_ID;
        Company_ID = company_ID;
    }

    public LogsheetListDataRequest(int route_ID, int company_ID, String logDate) {
        Route_ID = route_ID;
        Company_ID = company_ID;
        LogDate = logDate;
    }
}
