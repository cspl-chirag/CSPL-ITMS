package com.example.hp.superadminitms.Network;

/**
 * Created by INFOTEK on 2/12/2020.
 */

public class ReportRequest {
    private final int Company_ID;
    private int Vehicle_ID;
    private final String From_Date;
    private final String To_Date;

    public ReportRequest(int company_ID, String from_Date, String to_Date) {
        Company_ID = company_ID;
        From_Date = from_Date;
        To_Date = to_Date;
    }

    public ReportRequest(int company_ID, int vehicle_ID, String from_Date, String to_Date) {
        Company_ID = company_ID;
        Vehicle_ID = vehicle_ID;
        From_Date = from_Date;
        To_Date = to_Date;
    }
}
