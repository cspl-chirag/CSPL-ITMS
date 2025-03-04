package com.example.hp.superadminitms.Network;

/**
 * Created by Android-2 on 08-Mar-19.
 */

public class RouteRequest {
    private final int Company_ID;
    private String LogDate;

    public RouteRequest(int company_ID) {
        Company_ID = company_ID;
    }

    public RouteRequest(int company_ID, String logDate) {
        Company_ID = company_ID;
        LogDate = logDate;
    }
}
