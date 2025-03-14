package com.example.hp.superadminitms.Network;

/**
 * Created by ANDROID-PC on 04/12/2018.
 */

public class AddFuelEntryDataRequest {
    private final int User_ID;
    private final int Company_ID;
    private final String Fuel_Date;
    private final String Fuel_Type;
    private final String Station;
    private final String BillNo;
    private final double Quantity;
    private final double Rate;
    private final int MeterReading;
    private final double Amount;
    private final int VehicleID;
    private final String DriverName;
    private final String OperatorName;

    public AddFuelEntryDataRequest(int user_ID, int company_ID, String fuel_Date, String fuel_Type, String station, String billNo, double quantity, double rate, int meterReading, double amount, int vehicleID, String driverName, String operatorName) {
        User_ID = user_ID;
        Company_ID = company_ID;
        Fuel_Date = fuel_Date;
        Fuel_Type = fuel_Type;
        Station = station;
        BillNo = billNo;
        Quantity = quantity;
        Rate = rate;
        MeterReading = meterReading;
        Amount = amount;
        VehicleID = vehicleID;
        DriverName = driverName;
        OperatorName = operatorName;
    }
}
