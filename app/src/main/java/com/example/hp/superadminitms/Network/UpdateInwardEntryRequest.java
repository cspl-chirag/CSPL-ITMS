package com.example.hp.superadminitms.Network;

/**
 * Created by Android-2 on 19-Feb-20.
 */

public class UpdateInwardEntryRequest {
    private final int User_ID;
    private final int Company_ID;
    private final String Bill_No;
    private final int Part_ID;
    private final int Store_ID;
    private final String Manufacturer_Name;
    private final String Inward_DTM;
    private final int Quantity;
    private final double Rate;
    private final double Bill_Amount;
    private final String Checked_By;
    private final String Operator_Name;
    private final int Courier_Agency_ID;
    private final String Courier_LR_Number;
    private final String Remark;

    public UpdateInwardEntryRequest(int user_ID, int company_ID, String bill_No, int part_ID, int store_ID, String manufacturer_Name, String inward_DTM, int quantity, double rate, double bill_Amount, String checked_By, String operator_Name, int courier_Agency_ID, String courier_LR_Number, String remark) {
        User_ID = user_ID;
        Company_ID = company_ID;
        Bill_No = bill_No;
        Part_ID = part_ID;
        Store_ID = store_ID;
        Manufacturer_Name = manufacturer_Name;
        Inward_DTM = inward_DTM;
        Quantity = quantity;
        Rate = rate;
        Bill_Amount = bill_Amount;
        Checked_By = checked_By;
        Operator_Name = operator_Name;
        Courier_Agency_ID = courier_Agency_ID;
        Courier_LR_Number = courier_LR_Number;
        Remark = remark;
    }
}
