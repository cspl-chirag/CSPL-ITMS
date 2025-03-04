package com.example.hp.superadminitms.Network;

/**
 * Created by INFOTEK on 2/16/2020.
 */

public class SummaryRequest {

    private final int User_ID;
    private final int Company_ID;
    private final int Vehicle_ID;
    private final String Summary_DTM;
    private final String Reason;
    private final boolean IsSpare;

    public SummaryRequest(int user_ID, int company_ID, int vehicle_ID, String summary_DTM, String reason, boolean isSpare) {
        User_ID = user_ID;
        Company_ID = company_ID;
        Vehicle_ID = vehicle_ID;
        Summary_DTM = summary_DTM;
        Reason = reason;
        IsSpare = isSpare;
    }
}
