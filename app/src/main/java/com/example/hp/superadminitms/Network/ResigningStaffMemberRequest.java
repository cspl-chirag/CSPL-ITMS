package com.example.hp.superadminitms.Network;

/**
 * Created by INFOTEK on 2/1/2020.
 */

public class ResigningStaffMemberRequest {
    private final int Company_ID;
    private final int User_ID;
    private final int Staff_ID;
    private final String ResignationDate;
    private final String ResignationReason;

    public ResigningStaffMemberRequest(int company_ID, int user_ID, int staff_ID, String resignationDate, String resignationReason) {
        Company_ID = company_ID;
        User_ID = user_ID;
        Staff_ID = staff_ID;
        ResignationDate = resignationDate;
        ResignationReason = resignationReason;
    }
}
