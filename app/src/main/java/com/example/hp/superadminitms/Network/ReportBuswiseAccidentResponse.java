package com.example.hp.superadminitms.Network;

import com.example.hp.superadminitms.Model.AccidentReportBusWise;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by INFOTEK on 2/26/2020.
 */

public class ReportBuswiseAccidentResponse {

    @SerializedName("Status_Code")
    @Expose
    private Integer statusCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("BusWiseAccidentReport")
    @Expose
    private List<AccidentReportBusWise> busWiseAccidentReport = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AccidentReportBusWise> getBusWiseAccidentReport() {
        return busWiseAccidentReport;
    }

    public void setBusWiseAccidentReport(List<AccidentReportBusWise> busWiseAccidentReport) {
        this.busWiseAccidentReport = busWiseAccidentReport;
    }


}
