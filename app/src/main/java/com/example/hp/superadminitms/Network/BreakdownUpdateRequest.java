package com.example.hp.superadminitms.Network;

/**
 * Created by Android-2 on 18-Feb-19.
 */

public class BreakdownUpdateRequest {
    private final int Problem_ID;
    private final int User_ID;
    private final String WorkDone;
    private final String Remark;
    private final String SolvedBy;
    private final String SolvedDate;
    private final String SolvedTime;

    public BreakdownUpdateRequest(int problem_ID, int user_ID, String workDone, String remark, String solvedBy, String solvedDate, String solvedTime) {
        Problem_ID = problem_ID;
        User_ID = user_ID;
        WorkDone = workDone;
        Remark = remark;
        SolvedBy = solvedBy;
        SolvedDate = solvedDate;
        SolvedTime = solvedTime;
    }
}
