package com.example.hp.superadminitms.Network;

/**
 * Created by INFOTEK on 3/14/2020.
 */

public class OffenceRequest {
    private int User_ID;
    private final int Company_ID;
    private int Staff_ID;
    private String Offence;
    private String Offence_Date;
    private double Offence_Amount;
    private String Penalty_By;
    private String From_Date;
    private String To_Date;

    public OffenceRequest(int user_ID, int company_ID, int staff_ID, String offence, String offence_Date, double offence_Amount, String penalty_By) {
        User_ID = user_ID;
        Company_ID = company_ID;
        Staff_ID = staff_ID;
        Offence = offence;
        Offence_Date = offence_Date;
        Offence_Amount = offence_Amount;
        Penalty_By = penalty_By;
    }

    public OffenceRequest(int company_ID, int staff_ID) {
        Company_ID = company_ID;
        Staff_ID = staff_ID;
    }

    public OffenceRequest(int company_ID, String from_Date, String to_Date) {
        Company_ID = company_ID;
        From_Date = from_Date;
        To_Date = to_Date;
    }
}
