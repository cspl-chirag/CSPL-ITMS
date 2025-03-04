package com.example.hp.superadminitms.Retrofit;


import com.example.hp.superadminitms.Model.AverageResponse;
import com.example.hp.superadminitms.Model.BusLoginResponse;
import com.example.hp.superadminitms.Model.DashboardResponse;
import com.example.hp.superadminitms.Model.FuelResponse;
import com.example.hp.superadminitms.Model.RoutineRequest;
import com.example.hp.superadminitms.Model.RoutineTodayResponse;
import com.example.hp.superadminitms.Model.RoutineYesterdayResponse;
import com.example.hp.superadminitms.Model.RunKmResponse;
import com.example.hp.superadminitms.Network.*;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by ANDROID-PC on 06/13/2018.
 */

public interface ApiInterface {

    @POST("api/DMS/checkLogin")
    Call<LoginResponse> checkLogin(@Body LoginRequest loginRequest);

    @POST("api/DMS/getDashboardData")
    Call<DashboardResponse> getDashboardData(@Body RoutineRequest routineRequest);

    @POST("api/DMS/getYesterdayRoutineData")
    Call<RoutineYesterdayResponse> getYesterdayBusRoutine(@Body RoutineRequest routineRequest);

    @POST("api/DMS/getTodayRoutineData")
    Call<RoutineTodayResponse> getTodayBusRoutine(@Body RoutineRequest routineRequest);

    @POST("api/DMS/getLast7DaysRunKmData")
    Call<RunKmResponse> getWeeklyRunKm(@Body RoutineRequest routineRequest);

    @POST("api/DMS/getLast7DaysAverageData")
    Call<AverageResponse> getWeeklyAverage();

    @POST("api/DMS/getLast7DaysFuelConsumptionData")
    Call<FuelResponse> getWeeklyFuel(@Body RoutineRequest routineRequest);

    @POST("api/DMS/getLast7DaysBusOnRoadData")
    Call<BusLoginResponse> getWeeklyBusData(@Body RoutineRequest routineRequest);

    @GET("api/DMS/getCompanyList")
    Call<CompanyResponse> getCompanyList();

    @POST("api/DMS/getMaintenanceBreakDownData")
    Call<BreakdownResponse> getBreakdownData(@Body BreakdownRequest breakdownRequest);

    @POST("api/DMS/updateMaintenanceBreakDownSolved")
    Call<UpdateResponse> updateStatus(@Body BreakdownUpdateRequest breakdownUpdateRequest);

    @POST("api/DMS/getBusList")
    Call<BusResponse> getVehicleInfo(@Body BusRequest busRequest);

    @POST("api/DMS/getBusListForJobCard")
    Call<BusResponse> getVehicleForJobCard(@Body BusRequest busRequest);


    @POST("api/DMS/getDriverList")
    Call<DriverResponse> getDriverInfo(@Body DriverRequest driverRequest);

    @POST("api/DMS/getAllRouteListForBusLoginUpdate")
    Call<RouteResponse> getRouteInfo(@Body RouteRequest routeRequest);

    @POST("api/DMS/AddBusBreakDownEntry")
    Call<AddBreakdownResponse> addBreakdownEntry(@Body AddBreakdownRequest addBreakdownRequest);

    @GET("api/DMS/getJobsData")
    Call<JobResponse> getJobs();

    @POST("api/DMS/AddJobCardEntry")
    Call<AddJobResponse> addJob(@Body AddJobRequest addJobRequest);

    @POST("api/DMS/getDailyJobCardList")
    Call<JobCardDatumResponse> getJobCardList(@Body BreakdownRequest breakdownRequest);

    @POST("api/DMS/UpdateJobCardSolved")
    Call<UpdateResponse> updateJob(@Body JobUpdateRequest jobUpdateRequest);

    @POST("api/DMS/getPartStockList")
    Call<PartListResponse> getPartList(@Body WholePartListRequest partListRRequest);

    @POST("api/DMS/getPartStockListForStockManager")
    Call<WholePartListResponse> getWholePartList(@Body WholePartListRequest partListRRequest);

    @POST("api/DMS/getCourierAgencyList")
    Call<CourierAgencyResponse> getCourierAgencyList(@Body CourierAgencyRequest courierAgencyRequest);

    @POST("api/DMS/InsertPartReplace")
    Call<InsertPartReplaceResponse> insertPartReplace(@Body List<InsertPartReplaceRequest> insertPartReplaceRequestList);

    @POST("api/DMS/AddInwardEntry")
    Call<AddInwardEntryResponse> addInwardEntry(@Body List<AddInwardEntryRequest> addInwardEntryRequest);

    @POST("api/DMS/AddOutwardEntry")
    Call<OutwardEntryResponse> addOutwadEntry(@Body List<OutwardEntryRequest> outwardEntryRequests);

    @POST("api/DMS/AddPartEntry")
    Call<AddPartResponse> addPart(@Body AddPartRequest addPartRequest);

    @POST("api/DMS/AddStoreEntry")
    Call<AddStoreResponse> addStore(@Body AddPartRequest request);

    @POST("api/DMS/getStoreList")
    Call<PartStoreListResponse> getStoreList(@Body WholePartListRequest addPartRequest);

    @POST("api/DMS/getPartReplacementReport")
    Call<ReportPartReplacementResponse> getPartReplacementReport(@Body ReportPartReplacementRequest reportPartReplacementReeuest);

    @POST("api/DMS/getStockInwardReport")
    Call<ReportInwardResponse> getStockInwardReport(@Body ReportPartReplacementRequest stockInwardReporRequest);

    @POST("api/DMS/getStockOutwardReport")
    Call<ReportOutwardResponse> getStockOutwardReport(@Body ReportPartReplacementRequest reportPartReplacementReeuest);

    @POST("api/DMS/getStaffList")
    Call<StaffListResponse> getStaffList(@Body RouteRequest routeRequest);

    @POST("api/DMS/getResignedStaffList")
    Call<ResignedStaffListResponse> getResignedStaffList(@Body RouteRequest routeRequest);

    @POST("api/DMS/StaffResignEntry")
    Call<ResigningStaffMemberResponse> resigningStaffMember(@Body ResigningStaffMemberRequest resigningStaffMemberRequest);

    @POST("api/DMS/getFuelReport")
    Call<FuelReportResponse> getFuelReport(@Body ReportPartReplacementRequest request);

    @POST("api/DMS/getBusLogInLogOutReport")
    Call<BusLoginLogoutReportResponse> getBusLoginLogoutReport(@Body ReportPartReplacementRequest request);

    @POST("api/DMS/getRouteWiseBusInfoReport")
    Call<RouteWiseBusReportResponse> getRouteWiseBusReport(@Body ReportPartReplacementRequest request);

    @POST("api/DMS/getRouteWiseBusLogInLogOutReport")
    Call<RoutewiseBusLoginLogoutResponse> getRoutewiseBusLoginLogoutReport(@Body ReportPartReplacementRequest request);

    @POST("api/DMS/getBusWiseAverageReport")
    Call<ReportBusWiseAvgResponse> getBuswiseAvgData(@Body ReportRequest reportRequest);

    @POST("api/DMS/getDailyBusWiseMaintenanceReport")
    Call<ReportBusMaintananceHistoryResponse> getBuswiseMaintananceHistory(@Body ReportRequest reportRequest);

    @POST("api/DMS/AddDailyBusSummaryEntry")
    Call<AddSummaryResponse> addDailyBusSummaryEntry(@Body SummaryRequest summaryRequest);

    @POST("api/DMS/getStockInwardList")
    Call<StockInwardDataResponse> getStockForInwardUpdate(@Body BreakdownRequest breakdownRequest);

    @POST("api/DMS/getRouteWiseBusCountForDashboard")
    Call<DashboardRouteWiseCountResponse> getRouteAndBusCount(@Body BreakdownRequest request);

    @POST("api/DMS/UpdateInwardEntry")
    Call<UpdateInwardEntryResponse> updateInwardEntry(@Body List<UpdateInwardEntryRequest> updateInwardEntryRequests);

    @POST("api/DMS/getStockOutwardList")
    Call<StockOutwardDataResponse> getStockForOutwardUpdate(@Body BreakdownRequest request);

    @POST("api/DMS/UpdateOutwardEntry")
    Call<UpdateOutwardEntryResponse> updateOutwardEntry(@Body List<UpdateOutwardEntryRequest> updateOutwardEntryRequests);

    @POST("api/DMS/getBusListForDailySummary")
    Call<DailySummaryBusListResponse> getDailySummaryBusList(@Body BreakdownRequest request);

    @POST("api/DMS/getDailyBusSummary")
    Call<DailySummaryResponse> getDailySummary(@Body BreakdownRequest request);

    @POST("api/DMS/AddAccidentEntry")
    Call<AccidentEntryResponse> addAccidentEntry(@Body AccidentEntryRequest request);

    @POST("api/DMS/AddStaffAssignKitEntry")
    Call<StaffKitEntryResponse> addStaffKit(@Body StaffKitRequest request);

    @POST("api/DMS/getBusWiseAccidentReport")
    Call<ReportBuswiseAccidentResponse> getBusWiseAccidentReport(@Body ReportBuswiseAccidentRequest request);

    @POST("api/DMS/getBusWiseAccidentHistory")
    Call<ReportDatewiseAccidentResponse> getDatewiseAccidentReport(@Body ReportBuswiseAccidentRequest request);

    @POST("api/DMS/getStaffWiseKitAssignReport")
    Call<ReportStaffAssignKitResponse> getStaffWiseKitAssignReport(@Body StaffKitRequest request);

    @POST("api/DMS/getStaffWiseKitAssignHistory")
    Call<HistorytStaffAssignKitResponse> getStaffWiseKitAssignHistory(@Body StaffKitRequest request);

    @POST("api/DMS/AddStaffOffenceEntry")
    Call<StaffOffenceEntryResponse> addStaffOffenceEntry(@Body OffenceRequest request);

    @POST("api/DMS/getStaffWiseOffenceReport")
    Call<StaffwiseOffenceReportResponse> getStaffWiseOffenceReport(@Body OffenceRequest request);

    @POST("api/DMS/getStaffWiseOffenceHistory")
    Call<StaffwiseOffenceHistoryResponse> getStaffWiseOffenceHistory(@Body OffenceRequest request);

//////////////////////////////////////////////////////////////////////

    @POST("api/DMS/getBusList")
    Call<BusResponse> getBusList(@Body BusRequest busListDataRequest);

    @POST("api/DMS/getDepotList")
    Call<DepotListDataResponse> getDepotList(@Body DepotListDataRequest depotListDataRequest);

    @POST("api/DMS/getDriverList")
    Call<DriverResponse> getDriverList(@Body DriverRequest driverListDataRequest);

    @POST("api/DMS/getAllRouteList")
    Call<RouteResponse> getRouteList(@Body RouteRequest routeListDataRequest);

    @POST("api/DMS/getAllRouteListForBusLoginUpdate")
    Call<RouteResponse> getAllRouteListForBusLoginUpdate(@Body RouteRequest routeListDataRequest);

    @POST("api/DMS/getRouteWiseLogsheetList")
    Call<LogsheetListDataResponse> getRouteWiseLogsheetList(@Body LogsheetListDataRequest logsheetListDataRequest);

    @POST("api/DMS/getAllLogsheetListForBusLoginUpdate")
    Call<LogsheetListDataResponse> getAllLogsheetListForBusLoginUpdate(@Body LogsheetListDataRequest logsheetListDataRequest);

    @POST("api/DMS/getBusListForLogin")
    Call<BusResponse> getBusListForLogin(@Body BusRequest busListDataRequest);

    @POST("api/DMS/getBusListForLogout")
    Call<BusResponse> getBusListForLogout(@Body BusRequest busListDataRequest);

    @POST("api/DMS/getBusLoginDataForLogout")
    Call<GetBusLoginDataResponse> getBusLoginDataForLogout(@Body GetBusLoginDataRequest getBusLoginDataRequest);

    @POST("api/DMS/getLastLogOutKm")
    Call<GetLastLogOutKmDataResponse> getLastLogOutKm(@Body GetLastLogOutKmDataRequest getLastLogOutKmDataRequest);

    @POST("api/DMS/AddFuelEntry")
    Call<AddFuelEntryDataResponse> addFuelEntry(@Body AddFuelEntryDataRequest addFuelEntryDataRequest);

    @POST("api/DMS/AddBusLoginEntry")
    Call<AddBusLoginDataResponse> addBusLoginEntry(@Body AddBusLoginDataRequest addBusLoginDataRequest);

    @POST("api/DMS/AddBusLogoutEntry")
    Call<AddBusLogoutDataResponse> addBusLogoutEntry(@Body AddBusLogoutDataRequest addBusLogoutDataRequest);

    @POST("api/DMS/getDateWiseFuelledBusList")
    Call<GetFuelBusDataResponse> getFuelBusData(@Body GetFuelBusDataRequest getFuelBusDataRequest);

    @POST("api/DMS/UpdateFuelEntry")
    Call<UpdateFuelEntryDataResponse> updateFuelEntry(@Body UpdateFuelEntryDataRequest updateFuelEntryDataRequest);

    @POST("api/DMS/UpdateBusLoginEntry")
    Call<UpdateBusLoginDataResponse> updateBusLogin(@Body UpdateBusLoginDataRequest updateBusLoginDataRequest);

    @POST("api/DMS/getLoggedInBusListForUpdate")
    Call<GetLoggedinBusDataResponse> getLoggedInBusList(@Body GetLoggedinBusDataRequest getLoggedinBusDataRequest);

    @POST("api/DMS/getLoggedOutBusListForUpdate")
    Call<GetLoggedoutBusDataResponse> getLoggedOutBusList(@Body GetLoggedoutBusDataRequest getLoggedoutBusDataRequest);

    @POST("api/DMS/UpdateBusLogoutEntry")
    Call<UpdateBusLogoutDataResponse> updateBusLogout(@Body UpdateBusLogoutDataRequest updateBusLogoutDataRequest);

    @GET("api/DMS/getExpirationAlertData")
    Call<ExpirationResponse> getExpirationDetails();


}


///
/*



getPartStockList
   Company_ID
UpdateJobCardSolved_NEW
    cmd.Parameters.AddWithValue("@Job_Card_ID", jcd.Job_Card_ID);
                    cmd.Parameters.AddWithValue("@User_ID", jcd.User_ID);
                    cmd.Parameters.AddWithValue("@SolvedBy", jcd.SolvedBy);
                    cmd.Parameters.AddWithValue("@SolvedTime", jcd.SolvedTime);
                    cmd.Parameters.AddWithValue("@SolvedDesc", jcd.SolvedDesc);
                    cmd.Parameters.AddWithValue("@IsPartReplaced", jcd.IsPartReplaced);

                    if (jcd.IsPartReplaced.Equals(true))
                    {
                        cmd.Parameters.AddWithValue("@Company_ID", jcd.Company_ID);
                        cmd.Parameters.AddWithValue("@Part_ID", jcd.Part_ID);
                        cmd.Parameters.AddWithValue("@Vehicle_ID", jcd.Vehicle_ID);
                        cmd.Parameters.AddWithValue("@Outward_DTM", jcd.Outward_DTM);
                        cmd.Parameters.AddWithValue("@Quantity", jcd.Quantity);
                        cmd.Parameters.AddWithValue("@Person_Name", jcd.Person_Name);
                        cmd.Parameters.AddWithValue("@Description", jcd.Description);
                        cmd.Parameters.AddWithValue("@Checked_By", jcd.Checked_By);
                        cmd.Parameters.AddWithValue("@Operator_Name", jcd.Operator_Name);
                        cmd.Parameters.AddWithValue("@Remark", jcd.Remark);
                    }






 */