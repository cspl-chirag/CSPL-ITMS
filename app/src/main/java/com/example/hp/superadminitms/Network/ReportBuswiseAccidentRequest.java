package com.example.hp.superadminitms.Network;

/**
 * Created by INFOTEK on 2/26/2020.
 */

public class ReportBuswiseAccidentRequest {
    private final int Company_ID;
    private int Vehicle_ID;
    private String From_Date;
    private String To_Date;

    public ReportBuswiseAccidentRequest(int company_ID, int vehicle_ID) {
        Company_ID = company_ID;
        Vehicle_ID = vehicle_ID;
    }

    public ReportBuswiseAccidentRequest(int company_ID, String from_Date, String to_Date) {
        Company_ID = company_ID;
        From_Date = from_Date;
        To_Date = to_Date;
    }
}
