package com.example.hp.superadminitms.Network;

/**
 * Created by INFOTEK on 1/24/2020.
 */

public class AddPartRequest {
    private final int User_ID;
    private final int Company_ID;
    private String Part_Code;
    private String Part_Name;
    private String Store_Name;

    public AddPartRequest(int user_ID, int company_ID, String part_Code, String part_Name) {
        User_ID = user_ID;
        Company_ID = company_ID;
        Part_Code = part_Code;
        Part_Name = part_Name;
    }

    public AddPartRequest(int user_ID, int company_ID, String store_Name) {
        User_ID = user_ID;
        Company_ID = company_ID;
        Store_Name = store_Name;
    }
}
