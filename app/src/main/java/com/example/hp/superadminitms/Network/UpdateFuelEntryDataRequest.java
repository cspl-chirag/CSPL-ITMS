package com.example.hp.superadminitms.Network;

/**
 * Created by ANDROID-PC on 07/01/2019.
 */

public class UpdateFuelEntryDataRequest {
    private final int Fuel_ID;
    private final int User_ID;
    private final int Company_ID;
    private final String Fuel_Type;
    private final String Station;
    private final String BillNo;
    private final double Quantity;
    private final double Rate;
    private final int MeterReading;
    private final double Amount;
    private final String DriverName;
    private final String OperatorName;

    public UpdateFuelEntryDataRequest(int fuel_ID, int user_ID, int company_ID, String fuel_Type, String station, String billNo, double quantity, double rate, int meterReading, double amount, String driverName, String operatorName) {
        Fuel_ID = fuel_ID;
        User_ID = user_ID;
        Company_ID = company_ID;
        Fuel_Type = fuel_Type;
        Station = station;
        BillNo = billNo;
        Quantity = quantity;
        Rate = rate;
        MeterReading = meterReading;
        Amount = amount;
        DriverName = driverName;
        OperatorName = operatorName;
    }
}
