package com.example.hp.superadminitms.Network;

/**
 * Created by INFOTEK on 2/22/2020.
 */

public class UpdateOutwardEntryRequest {
    private final int User_ID;
    private final int Company_ID;
    private final int Outward_Batch_ID;
    private final int Part_ID;
    private final int Vehicle_ID;
    private final String Outward_DTM;
    private final int Quantity;
    private final String Given_By;
    private final String Operator_Name;
    private final String Remark;

    public UpdateOutwardEntryRequest(int user_ID, int company_ID, int outward_Batch_ID, int part_ID, int vehicle_ID, String outward_DTM, int quantity, String given_By, String operator_Name, String remark) {
        User_ID = user_ID;
        Company_ID = company_ID;
        Outward_Batch_ID = outward_Batch_ID;
        Part_ID = part_ID;
        Vehicle_ID = vehicle_ID;
        Outward_DTM = outward_DTM;
        Quantity = quantity;
        Given_By = given_By;
        Operator_Name = operator_Name;
        Remark = remark;
    }
}
