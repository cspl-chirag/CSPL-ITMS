package com.example.hp.superadminitms.Network;

/**
 * Created by INFOTEK on 1/25/2020.
 */

public class ReportPartReplacementRequest {
    private final int Company_ID;
    private int Vehicle_ID;
    private String From_DTM;
    private String To_DTM;
    private String Date;

    public ReportPartReplacementRequest(int company_ID, int vehicle_ID, String from_DTM, String to_DTM) {
        Company_ID = company_ID;
        Vehicle_ID = vehicle_ID;
        From_DTM = from_DTM;
        To_DTM = to_DTM;
    }

    public ReportPartReplacementRequest(int company_ID, String from_DTM, String to_DTM) {
        Company_ID = company_ID;
        From_DTM = from_DTM;
        To_DTM = to_DTM;
    }

    public ReportPartReplacementRequest(int company_ID, String date) {
        Company_ID = company_ID;
        Date = date;
    }
}
