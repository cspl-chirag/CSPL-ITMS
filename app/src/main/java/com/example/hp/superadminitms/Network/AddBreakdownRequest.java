package com.example.hp.superadminitms.Network;

/**
 * Created by Android-2 on 05-Mar-19.
 */

public class AddBreakdownRequest {
    private final int User_ID;
    private final int Company_ID;
    private final int VehicleID;
    private final int Route_ID;
    private final int Driver_ID;
    private final String ProblemDate;
    private final String ProblemTime;
    private final String Problem;
    private final String Location;

    public AddBreakdownRequest(int user_ID, int company_ID, int vehicleID, int route_ID, int driver_ID, String problemDate, String problemTime, String problem, String location) {
        User_ID = user_ID;
        Company_ID = company_ID;
        VehicleID = vehicleID;
        Route_ID = route_ID;
        Driver_ID = driver_ID;
        ProblemDate = problemDate;
        ProblemTime = problemTime;
        Problem = problem;
        Location = location;
    }
}
