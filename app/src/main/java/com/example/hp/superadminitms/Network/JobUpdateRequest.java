package com.example.hp.superadminitms.Network;

/**
 * Created by Android-2 on 14-Mar-19.
 */

public class JobUpdateRequest {
    private final int Job_Card_ID;
    private final int User_ID;
    private final String SolvedBy;
    private String SolvedDate;
    private final String SolvedTime;
    private final String SolvedDesc;

    public JobUpdateRequest(int job_Card_ID, int user_ID, String solvedBy, String solvedTime, String solvedDesc) {
        Job_Card_ID = job_Card_ID;
        User_ID = user_ID;
        SolvedBy = solvedBy;
        SolvedTime = solvedTime;
        SolvedDesc = solvedDesc;
    }
}
