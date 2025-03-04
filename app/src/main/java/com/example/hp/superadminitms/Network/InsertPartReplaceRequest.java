package com.example.hp.superadminitms.Network;

/**
 * Created by INFOTEK on 1/10/2020.
 */

public class InsertPartReplaceRequest {
    private final int Job_ID;
    private final int User_ID;
    private final int Company_ID;
    private final String Part_Replace_DTM;
    private final int Part_ID;
    private final int Vehicle_ID;
    private final int Quantity;
    private final String Mechanic_Name;
    private final String Operator_Name;
    private final String Remark;

    public InsertPartReplaceRequest(int job_ID, int user_ID, int company_ID, String part_Replace_DTM, int part_ID, int vehicle_ID, int quantity, String mechanic_Name, String operator_Name, String remark) {
        Job_ID = job_ID;
        User_ID = user_ID;
        Company_ID = company_ID;
        Part_Replace_DTM = part_Replace_DTM;
        Part_ID = part_ID;
        Vehicle_ID = vehicle_ID;
        Quantity = quantity;
        Mechanic_Name = mechanic_Name;
        Operator_Name = operator_Name;
        Remark = remark;
    }
}
