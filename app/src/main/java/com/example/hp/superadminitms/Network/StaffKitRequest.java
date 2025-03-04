package com.example.hp.superadminitms.Network;

/**
 * Created by INFOTEK on 2/25/2020.
 */

public class StaffKitRequest {
    private int User_ID;
    private final int Company_ID;
    private String Assign_Date;
    private int Staff_ID;
    private String Assign_Kit_Detail;
    private String Assign_By;
    private String From_Date;
    private String To_Date;

    public StaffKitRequest(int user_ID, int company_ID, String assign_Date, int staff_ID, String assign_Kit_Detail, String assign_By) {
        User_ID = user_ID;
        Company_ID = company_ID;
        Assign_Date = assign_Date;
        Staff_ID = staff_ID;
        Assign_Kit_Detail = assign_Kit_Detail;
        Assign_By = assign_By;
    }

    public StaffKitRequest(int company_ID, int staff_ID) {
        Company_ID = company_ID;
        Staff_ID = staff_ID;
    }

    public StaffKitRequest(int company_ID, String from_Date, String to_Date) {
        Company_ID = company_ID;
        From_Date = from_Date;
        To_Date = to_Date;
    }
}
