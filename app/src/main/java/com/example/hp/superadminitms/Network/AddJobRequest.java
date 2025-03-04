package com.example.hp.superadminitms.Network;

/**
 * Created by Android-2 on 13-Mar-19.
 */

public class AddJobRequest {
    private final int User_ID;
    private final int Company_ID;
    private final int Vehicle_ID;
    private final String Driver_ID;
    private final int Route_ID;
    private final String JobDate;
    private final String JobTime;
    private final int MeterReading;
    private final String ProblemDesc;
    private final boolean Breaks;
    private final boolean Clutch;
    private final boolean Steering;
    private final boolean HeadLight;
    private final boolean SideLight;
    private final boolean Accelerator;
    private final boolean OtherProblem;

    public AddJobRequest(int user_ID, int company_ID, int vehicle_ID, String driver_ID, int route_ID, String jobDate, String jobTime, int meterReading, String problemDesc, boolean breaks, boolean clutch, boolean steering, boolean headLight, boolean sideLight, boolean accelerator, boolean otherProblem) {
        User_ID = user_ID;
        Company_ID = company_ID;
        Vehicle_ID = vehicle_ID;
        Driver_ID = driver_ID;
        Route_ID = route_ID;
        JobDate = jobDate;
        JobTime = jobTime;
        MeterReading = meterReading;
        ProblemDesc = problemDesc;
        Breaks = breaks;
        Clutch = clutch;
        Steering = steering;
        HeadLight = headLight;
        SideLight = sideLight;
        Accelerator = accelerator;
        OtherProblem = otherProblem;
    }
}
