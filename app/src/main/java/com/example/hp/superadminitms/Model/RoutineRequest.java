package com.example.hp.superadminitms.Model;

/**
 * Created by HP on 01/08/2019.
 */

public class RoutineRequest {
    int Bus_Company_ID;
    int Flag_ID;

    public RoutineRequest(int Flag_ID, int Bus_Company_ID) {
        this.Flag_ID = Flag_ID;
        this.Bus_Company_ID = Bus_Company_ID;
    }

    public RoutineRequest(int Bus_Company_ID) {
        this.Bus_Company_ID = Bus_Company_ID;
    }
}
