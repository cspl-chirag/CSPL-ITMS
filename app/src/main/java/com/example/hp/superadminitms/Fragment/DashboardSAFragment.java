package com.example.hp.superadminitms.Fragment;


import static com.example.hp.superadminitms.Helper.Common.convertDateFormat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Adapter.AdpDataListRouteInfoDshbrd;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.AverageDatum;
import com.example.hp.superadminitms.Model.BusLoginDatum;
import com.example.hp.superadminitms.Model.BusLoginResponse;
import com.example.hp.superadminitms.Model.DashboardDatum;
import com.example.hp.superadminitms.Model.DashboardResponse;
import com.example.hp.superadminitms.Model.DashboardRouteCount;
import com.example.hp.superadminitms.Model.FuelDatum;
import com.example.hp.superadminitms.Model.FuelResponse;
import com.example.hp.superadminitms.Model.RoutineDataToday;
import com.example.hp.superadminitms.Model.RoutineDataYesterday;
import com.example.hp.superadminitms.Model.RoutineRequest;
import com.example.hp.superadminitms.Model.RoutineTodayResponse;
import com.example.hp.superadminitms.Model.RoutineYesterdayResponse;
import com.example.hp.superadminitms.Model.RunKmDatum;
import com.example.hp.superadminitms.Model.RunKmResponse;
import com.example.hp.superadminitms.Network.BreakdownRequest;
import com.example.hp.superadminitms.Network.DashboardRouteWiseCountResponse;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.github.anastr.speedviewlib.PointerSpeedometer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardSAFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {

    private static int Bus_Company_ID = 0;
    List<AverageDatum> averageData = new ArrayList<>();
    ArrayList<BarDataSet> yAxisDatasetRunKm = null;
    ArrayList<BarDataSet> yAxisDatasetFuel = null;
    ArrayList<BarDataSet> yAxisDatasetBusLogin = null;
    ArrayList<BarEntry> valueSet1 = new ArrayList<>();
    List<BarEntry> barEntryListRunKm = new ArrayList<>();
    List<BarEntry> barEntryListFuel = new ArrayList<>();
    List<BarEntry> barEntryListBusLogin = new ArrayList<>();
    ArrayList<String> xAxisStringRunKm = new ArrayList<>();
    ArrayList<String> xAxisStringFuel = new ArrayList<>();
    ArrayList<String> xAxisStringBusLogin = new ArrayList<>();
    private boolean isConnected;
    private SessionManager sessionManager;
    private TextView tvToadyBusOnRoadS1, tvYesterdayBusOnRoadS1, tvToadyBusOnRoadS2, tvYesterdayBusOnRoadS2;
    private TextView tvAvg, tvBreakdown, tvTotalRunKm, tvFuelRate, tvFuelAmnt;
    private PointerSpeedometer spFuel, spAvg;
    private BarChart barchartRunKm, barChartFuel, barChartBusLogin;
    private ProgressDialog pg;
    private CountDownTimer mTimer1;
    private Boolean IsFirstTime = true;
    private List<DashboardDatum> dashboardData = new ArrayList<>();
    private List<RunKmDatum> runKmData = new ArrayList<>();
    private List<FuelDatum> fuelData = new ArrayList<>();
    private List<BusLoginDatum> busLoginData = new ArrayList<>();
    private BarEntry barEntryRunKm, barEntryFuel, barEntryBusLogin;
    private BarData barDataRunKm;
    private BarData barDataFuel;
    private BarData barDataBusLogin;
    private View view;
    private TextView tvViewID;
    private String Date;
    private RecyclerView rvRouteInfo;
    private TextView tvTodayFirstBus, tvTodayFirstBusTime, tvTodayFirstBusRoute, tvTodayLastBus, tvTodayLastBusTime, tvTodayLastBusRoute;
    private TextView tvYesterdayFirstBus, tvYesterdayFirstBusTime, tvYesterdayFirstBusRoute, tvYesterdayLastBus, tvYesterdayLastBusTime, tvYesterdayLastBusRoute;
    private LinearLayout llRouteWiseBusOnRoad, llRouteWiseBusOnRoadExpanded;
    private String Selected_Date;
    private final List<String> routeNumber = new ArrayList<>();
    private AdpDataListRouteInfoDshbrd reportDataAdapter;
    private int Flag_ID;
    private List<RoutineDataToday> todayRoutineLogin = new ArrayList<>();
    private List<RoutineDataYesterday> yesterdaysRoutieLogin = new ArrayList<>();
    private boolean isExpanded = false;
    private TextView tvTodayBusOnRoadLable, tvTotalTodayBusCount, tvTotalYesterdayBusCount;
    private List<DashboardRouteCount> routeData = new ArrayList<>();

    public DashboardSAFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sa_dashboard, container, false);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        getSupportActionBar().setTitle(R.string.app_name);tvViewID = v.findViewById(R.id.tvViewID);
        tvViewID = view.findViewById(R.id.tvViewID);
        tvViewID.setText("Tab" + getArguments().getInt("ID", 0));
        Bus_Company_ID = getArguments().getInt("ID", 0);
        Log.e("ID", String.valueOf(Bus_Company_ID));
        initializeControls(view);
        initializeBarChartForRunKms();
        initializeLineChartForBusOnRoad();
        initializeBarChartForFuelConsumption();
        settingDashboard();
        setRouteInfo();
        settingDashboardTodayLogin();
        settingDashboardYesterdayLogin();
        mTimer1 = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {
                //settingDashboard();
            }

            @Override
            public void onFinish() {
                mTimer1.start();
            }
        }.start();
        return view;
    }

    private void initializeBarChartForRunKms() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            if (IsFirstTime) {
                pg.setMessage("Getting Dashboard Data...");
                pg.show();
                IsFirstTime = false;
            }
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<RunKmResponse> call = apiInterface.getWeeklyRunKm(new RoutineRequest(Bus_Company_ID));
            call.enqueue(new Callback<RunKmResponse>() {
                @Override
                public void onResponse(Call<RunKmResponse> call, Response<RunKmResponse> response) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.d("RUN KM RESPONSE : ", response.body().getMessage());
                                runKmData = response.body().getRunKmData();
                                //Collections.reverse(runKmData);
//                                ArrayList<BarDataSet> dataSets = null;
//                                ArrayList<BarEntry> valueSet1 = new ArrayList<>();
//                                BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
//                                valueSet1.add(v1e1);
                                for (int i = 0; i < runKmData.size(); i++) {
                                    barEntryRunKm = new BarEntry(runKmData.get(i).getTotalRunKm(), i);
                                    barEntryListRunKm.add(barEntryRunKm);
                                    Date = Common.convertDateFormat(runKmData.get(i).getDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd,MMM");
                                    Log.d("********", Date);
                                    xAxisStringRunKm.add(runKmData.get(i).getDays() + " " + Date);

                                    BarDataSet newBarDataset = new BarDataSet(barEntryListRunKm, "Runnnig kms");
                                    newBarDataset.setColor(getResources().getColor(R.color.colorAccentText));
                                    newBarDataset.setValueTextColor(getResources().getColor(R.color.white));
                                    newBarDataset.setValueTextSize(10f);
                                    newBarDataset.setBarShadowColor(getResources().getColor(R.color.colorPrimary));
//                                    newBarDataset.setBarSpacePercent(80);
                                    yAxisDatasetRunKm = new ArrayList<>();
                                    yAxisDatasetRunKm.add(newBarDataset);
                                    barDataRunKm = new BarData((IBarDataSet) xAxisStringRunKm, (IBarDataSet) yAxisDatasetRunKm);
                                    barchartRunKm.setData(barDataRunKm);
                                    barchartRunKm.animateXY(2000, 2000);
                                    barchartRunKm.notifyDataSetChanged();
                                    barchartRunKm.invalidate();
                                    barchartRunKm.setDrawBarShadow(true);
                                    barchartRunKm.setDrawValueAboveBar(true);
                                    barchartRunKm.getAxisRight().setEnabled(false);
                                    barchartRunKm.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                    barchartRunKm.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                    barchartRunKm.getAxisRight().setEnabled(false);
                                    barchartRunKm.getAxisLeft().setTextSize(10f);
                                    barchartRunKm.getAxisRight().setTextSize(10f);
                                    barchartRunKm.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                    barchartRunKm.getLegend().setTextColor(getResources().getColor(R.color.white));
                                    barchartRunKm.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//                                    barchartRunKm.setNoDataTextDescription("Cuurently Data Not Found");
//                                    barchartRunKm.setDescription("");
                                    // if more than 60 entries are displayed in the chart, no values will be
                                    // drawn
//                                    barchartRunKm.setMaxVisibleValueCount(60);
                                    // scaling can now only be done on x- and y-axis separately
                                    barchartRunKm.setPinchZoom(true);
                                    barchartRunKm.setDrawGridBackground(false);
                                }


                                // chart.setDrawYLabels(false);

                            } else {
                                Log.d("RUNKM Chart Error : ", response.body().getMessage());
                                if (pg.isShowing()) {
                                    pg.dismiss();
                                }
                            }
                        } else {
                            Log.d("RUNKM Chart Error : ", response.body().getMessage());
                            if (pg.isShowing()) {
                                pg.dismiss();
                            }
                        }
                    } catch (Exception e) {
                        Log.d("RUNKM Chart EXpt : ", e.getMessage());
                        if (pg.isShowing()) {
                            pg.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<RunKmResponse> call, Throwable t) {
                    Log.d("RUN KM CFailure : ", t.getMessage());
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    if (IsFirstTime) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogBox);
                        builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                        builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                        builder.setPositiveButton(R.string.NetworkErrorBtnTxt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                settingDashboard();
                                builder.show();
                            }
                        });

                        IsFirstTime = false;
                    }
                }
            });
        }

    }

    private void initializeLineChartForBusOnRoad() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            if (IsFirstTime) {
                pg.setMessage("Getting Dashboard Data...");
                pg.show();
                IsFirstTime = false;
            }
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<BusLoginResponse> call = apiInterface.getWeeklyBusData(new RoutineRequest(Bus_Company_ID));
            call.enqueue(new Callback<BusLoginResponse>() {
                @Override
                public void onResponse(Call<BusLoginResponse> call, Response<BusLoginResponse> response) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.d("BusLoginResponse :: ", response.body().getMessage());
                                busLoginData = response.body().getBusOnRoadData();
                                //Collections.reverse(averageData);
                                for (int i = 0; i < busLoginData.size(); i++) {
                                    barEntryBusLogin = new BarEntry(busLoginData.get(i).getTotalBus(), i);
                                    barEntryListBusLogin.add(barEntryBusLogin);
                                    Date = Common.convertDateFormat(busLoginData.get(i).getDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd,MMM");
                                    xAxisStringBusLogin.add(busLoginData.get(i).getDays() + " " + Date);

                                    BarDataSet newBarDataset = new BarDataSet(barEntryListBusLogin, "Bus On Road");
                                    newBarDataset.setColor(getResources().getColor(R.color.colorAccentText));
                                    newBarDataset.setValueTextColor(getResources().getColor(R.color.white));
                                    newBarDataset.setValueTextSize(10f);
                                    newBarDataset.setBarShadowColor(getResources().getColor(R.color.colorPrimary));
//                                    newBarDataset.setBarSpacePercent(80);
                                    yAxisDatasetBusLogin = new ArrayList<>();
                                    yAxisDatasetBusLogin.add(newBarDataset);
                                    //barDataBusLogin = new BarData(xAxisStringBusLogin, yAxisDatasetBusLogin);
                                    barChartBusLogin.setData(barDataBusLogin);
                                    barChartBusLogin.animateXY(2000, 2000);
                                    barChartBusLogin.notifyDataSetChanged();
                                    barChartBusLogin.invalidate();
                                    barChartBusLogin.setDrawBarShadow(true);
                                    barChartBusLogin.setDrawValueAboveBar(true);
                                    barChartBusLogin.getAxisRight().setEnabled(false);
                                    barChartBusLogin.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                    barChartBusLogin.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                    barChartBusLogin.getAxisRight().setEnabled(false);
                                    barChartBusLogin.getAxisRight().setTextSize(10f);
                                    barChartBusLogin.getAxisLeft().setTextSize(10f);
                                    barChartBusLogin.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                    barChartBusLogin.getLegend().setTextColor(getResources().getColor(R.color.white));
                                    barChartBusLogin.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//                                    barChartBusLogin.setNoDataTextDescription("Cuurently Data Not Found");
//                                    barChartBusLogin.setDescription("");
                                    // if more than 60 entries are displayed in the chart, no values will be
                                    // drawn
//                                    barchartRunKm.setMaxVisibleValueCount(60);
                                    // scaling can now only be done on x- and y-axis separately
                                    barChartBusLogin.setPinchZoom(true);
                                    barChartBusLogin.setDrawGridBackground(false);
                                }

                            } else {
                                Log.d("BusDatum ERROR :: ", response.body().getMessage());
                                if (pg.isShowing()) {
                                    pg.dismiss();
                                }
                            }
                        } else {
                            Log.d("BusDatum ERROR :: ", response.body().getMessage());
                            if (pg.isShowing()) {
                                pg.dismiss();
                            }
                        }
                    } catch (Exception e) {
                        Log.d("BusDatum EXCEPTION :: ", e.getMessage());
                        if (pg.isShowing()) {
                            pg.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BusLoginResponse> call, Throwable t) {
                    Log.d("BusDatum FAILURE : ", t.getMessage());
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    if (IsFirstTime) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogBox);
                        builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                        builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                        builder.setPositiveButton(R.string.NetworkErrorBtnTxt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                settingDashboard();
                                builder.show();
                            }
                        });

                        IsFirstTime = false;
                    }
                }
            });
        }

    }

    private void settingDashboard() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            if (IsFirstTime) {
                pg.setMessage("Getting Dashboard Data...");
                pg.show();
                IsFirstTime = false;
            }
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<DashboardResponse> call = apiInterface.getDashboardData(new RoutineRequest(Bus_Company_ID));
            call.enqueue(new Callback<DashboardResponse>() {
                @Override
                public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.d("DASHBOARD RESPONSE : ", response.body().getMessage());
                                dashboardData = response.body().getDashboardData();
                                DecimalFormat df = new DecimalFormat("#.##");
                                //tvAvg.setText(df.format(String.valueOf(dashboardData.get(0).getYesterdayAvg())));
                                tvAvg.setText(df.format(dashboardData.get(0).getYesterdayAvg()));
                                tvBreakdown.setText(String.valueOf(dashboardData.get(0).getYesterdayBreakDown()));
                                tvTotalRunKm.setText(String.valueOf(dashboardData.get(0).getYesterdayRunkm()));
                                tvToadyBusOnRoadS1.setText(dashboardData.get(0).getTodayOnRouteShift1() + " / " + dashboardData.get(0).getTotalBus());
                                tvToadyBusOnRoadS2.setText(dashboardData.get(0).getTodayOnRouteShift2() + " / " + dashboardData.get(0).getTotalBus());
                                tvYesterdayBusOnRoadS1.setText(dashboardData.get(0).getYesterdayOnRouteShift1() + " / " + dashboardData.get(0).getTotalBus());
                                tvYesterdayBusOnRoadS2.setText(dashboardData.get(0).getYesterdayOnRouteShift2() + " / " + dashboardData.get(0).getTotalBus());
                                tvFuelAmnt.setText(df.format(dashboardData.get(0).getYesterdayFuelConsumptionAmnt()) + " " + getResources().getString(R.string.rs));
                                tvFuelRate.setText(df.format(dashboardData.get(0).getYesterdayFuelConsumptionRate()) + " " + getResources().getString(R.string.rs));
                                spAvg.speedTo(Float.parseFloat(df.format(dashboardData.get(0).getYesterdayAvg())));
                                spFuel.speedTo(dashboardData.get(0).getYesterdayFuelConsumption().floatValue());
                            } else {
                                if (pg.isShowing()) {
                                    pg.dismiss();
                                }
                                Log.d("DASHBOARD ERROR :: ", response.body().getMessage());
                            }
                        } else {

                            if (pg.isShowing()) {
                                pg.dismiss();
                            }
                            Log.d("DASHBOARD ERROR :: ", response.body().getMessage());
                        }
                    } catch (Exception e) {

                        if (pg.isShowing()) {
                            pg.dismiss();
                        }
                        Log.d("DASHBOARD EXCEPTTION :", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<DashboardResponse> call, Throwable t) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    if (IsFirstTime) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogBox);
                        builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                        builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                        builder.setPositiveButton(R.string.NetworkErrorBtnTxt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                settingDashboard();
                                builder.show();
                            }
                        });

                        IsFirstTime = false;
                    }
                    Log.d("FAILURE ::: ", t.getMessage());
                }
            });

        }
    }

    private void initializeControls(View view) {
        sessionManager = new SessionManager(view.getContext());
        tvToadyBusOnRoadS1 = view.findViewById(R.id.tvTodayBusOnRoadS1);
        tvToadyBusOnRoadS2 = view.findViewById(R.id.tvTodayBusOnRoadS2);
        tvYesterdayBusOnRoadS1 = view.findViewById(R.id.tvYesterdayBusOnRoadS1);
        tvYesterdayBusOnRoadS2 = view.findViewById(R.id.tvYesterdayBusOnRoadS2);
        tvAvg = view.findViewById(R.id.tvAverage);
        tvBreakdown = view.findViewById(R.id.tvBreakdown);
        tvTotalRunKm = view.findViewById(R.id.tvQuantity);
        tvFuelAmnt = view.findViewById(R.id.tvFuelPrice);
        tvFuelRate = view.findViewById(R.id.tvFuelPricePerLiter);
        spAvg = view.findViewById(R.id.SMAVG);
        spFuel = view.findViewById(R.id.SMFuels);
        barchartRunKm = view.findViewById(R.id.barChartRunKm);
        barChartFuel = view.findViewById(R.id.barChartFuel);
        barChartBusLogin = view.findViewById(R.id.barChartBusOnRoad);
        pg = new ProgressDialog(getContext(), R.style.DialogBox);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        pg.setMessage("Getting Dashboard Data...");
        rvRouteInfo = view.findViewById(R.id.rvRouteInfo);
        tvTodayFirstBus = view.findViewById(R.id.tvTodayFirstBus);
        tvTodayFirstBusTime = view.findViewById(R.id.tvTodayFirstBusTime);
        tvTodayFirstBusRoute = view.findViewById(R.id.tvTodayFirstBusRoute);
        tvTodayLastBus = view.findViewById(R.id.tvTodayLastBus);
        tvTodayLastBusTime = view.findViewById(R.id.tvTodayLastBusTime);
        tvTodayLastBusRoute = view.findViewById(R.id.tvTodayLastBusRoute);
        tvYesterdayFirstBus = view.findViewById(R.id.tvYesterdayFirstBus);
        tvYesterdayFirstBusTime = view.findViewById(R.id.tvYesterdayFirstBusTime);
        tvYesterdayFirstBusRoute = view.findViewById(R.id.tvYesterdayFirstBusRoute);
        tvYesterdayLastBus = view.findViewById(R.id.tvYesterdayLastBus);
        tvYesterdayLastBusTime = view.findViewById(R.id.tvYesterdayLastBusTime);
        tvYesterdayLastBusRoute = view.findViewById(R.id.tvYesterdayLastBusRoute);
        llRouteWiseBusOnRoad = view.findViewById(R.id.llRouteWiseBusOnRoad);
        llRouteWiseBusOnRoadExpanded = view.findViewById(R.id.llRouteWiseBusOnRoadExpanded);
        tvTodayBusOnRoadLable = view.findViewById(R.id.tvTodayBusOnRoadLable);
        tvTotalTodayBusCount = view.findViewById(R.id.tvTotalTodayBusCount);
        tvTotalYesterdayBusCount = view.findViewById(R.id.tvTotalYesterdayBusCount);
        llRouteWiseBusOnRoad.setOnClickListener(this);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Snackbar.make(view.findViewById(R.id.rootlayout), R.string.NetworkErrorMsg, Snackbar.LENGTH_LONG).show();
        }
    }


    private void initializeBarChartForFuelConsumption() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            if (IsFirstTime) {
                pg.setMessage("Getting Dashboard Data...");
                pg.show();
                IsFirstTime = false;
            }
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<FuelResponse> call = apiInterface.getWeeklyFuel(new RoutineRequest(Bus_Company_ID));
            call.enqueue(new Callback<FuelResponse>() {
                @Override
                public void onResponse(Call<FuelResponse> call, Response<FuelResponse> response) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.d("FUEL RESPONSE : ", response.body().getMessage());
                                fuelData = response.body().getFuelConsumptionData();
                                //Collections.reverse(fuelData);
//                                ArrayList<BarDataSet> dataSets = null;
//                                ArrayList<BarEntry> valueSet1 = new ArrayList<>();
//                                BarEntry v1e1 = new BarEntry(110.000f, 0); // Jan
//                                valueSet1.add(v1e1);
                                for (int i = 0; i < fuelData.size(); i++) {
                                    barEntryFuel = new BarEntry(fuelData.get(i).getFuelConsumption().floatValue(), i);
                                    barEntryListFuel.add(barEntryFuel);
                                    Date = Common.convertDateFormat(fuelData.get(i).getDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd,MMM");

                                    xAxisStringFuel.add(fuelData.get(i).getDays() + " " + Date);

                                    BarDataSet newBarDataset = new BarDataSet(barEntryListFuel, "Fuel Consumption(Quantity)");
                                    newBarDataset.setColor(getResources().getColor(R.color.colorAccentText));
                                    newBarDataset.setValueTextColor(getResources().getColor(R.color.white));
                                    newBarDataset.setValueTextSize(10f);
                                    newBarDataset.setBarShadowColor(getResources().getColor(R.color.colorPrimary));
//                                    newBarDataset.setBarSpacePercent(80);
                                    yAxisDatasetFuel = new ArrayList<>();
                                    yAxisDatasetFuel.add(newBarDataset);
                                    //barDataFuel = new BarData(xAxisStringFuel, yAxisDatasetFuel);
                                    barChartFuel.setData(barDataFuel);
                                    barchartRunKm.animateXY(2000, 2000);
                                    barChartFuel.notifyDataSetChanged();
                                    barChartFuel.invalidate();
                                    barChartFuel.setDrawBarShadow(true);
                                    barChartFuel.setDrawValueAboveBar(true);
                                    barChartFuel.getAxisRight().setEnabled(false);
                                    barChartFuel.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                                    barChartFuel.getXAxis().setTextColor(getResources().getColor(R.color.white));
                                    barChartFuel.getAxisRight().setEnabled(false);
                                    barChartFuel.getAxisRight().setTextSize(10f);
                                    barChartFuel.getAxisLeft().setTextSize(10f);
                                    barChartFuel.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                                    barChartFuel.getLegend().setTextColor(getResources().getColor(R.color.white));
                                    barChartFuel.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//                                    barChartFuel.setNoDataTextDescription("Cuurently Data Not Found");
//                                    barChartFuel.setDescription();
                                    // if more than 60 entries are displayed in the chart, no values will be
                                    // drawn
//                                    barchartRunKm.setMaxVisibleValueCount(60);
                                    // scaling can now only be done on x- and y-axis separately
                                    barChartFuel.setPinchZoom(true);
                                    barChartFuel.setDrawGridBackground(false);
                                }


                                // chart.setDrawYLabels(false);

                            } else {
                                Log.d("FUEL Error : ", response.body().getMessage());
                                if (pg.isShowing()) {
                                    pg.dismiss();
                                }
                            }
                        } else {
                            Log.d("FUEL Error : ", response.body().getMessage());
                            if (pg.isShowing()) {
                                pg.dismiss();
                            }
                        }
                    } catch (Exception e) {
                        Log.d("FUEL EXCEPTION : ", e.getMessage());
                        if (pg.isShowing()) {
                            pg.dismiss();
                        }
                    }
                }

                @Override
                public void onFailure(Call<FuelResponse> call, Throwable t) {
                    Log.d("FUEL Failure : ", t.getMessage());
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    if (IsFirstTime) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogBox);
                        builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                        builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                        builder.setPositiveButton(R.string.NetworkErrorBtnTxt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                settingDashboard();
                                builder.show();
                            }
                        });

                        IsFirstTime = false;
                    }
                }
            });
        }

    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llRouteWiseBusOnRoad:

                if (!isConnected) {
                    Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    if (!isExpanded)
                    {
                        isExpanded = true;
                        tvTodayBusOnRoadLable.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_expandless,0);
                        llRouteWiseBusOnRoadExpanded.setVisibility(View.VISIBLE);
//                        setRouteInfo();
//                        settingDashboardTodayLogin();
                    }
                    else {
                        isExpanded = false;
                        llRouteWiseBusOnRoadExpanded.setVisibility(View.GONE);
                        tvTodayBusOnRoadLable.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_expandmore,0);
                    }
                }
                break;
        }
    }*/


    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llRouteWiseBusOnRoad:
                // Check network connectivity
                if (!isConnected) {
                    Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                    return; // Exit the method if there's no network connection
                }

                // Toggle expansion state
                if (isExpanded) {
                    // Collapse the layout
                    isExpanded = false;
                    llRouteWiseBusOnRoadExpanded.setVisibility(View.GONE);
                    tvTodayBusOnRoadLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandmore, 0);
                } else {
                    // Expand the layout
                    isExpanded = true;
                    llRouteWiseBusOnRoadExpanded.setVisibility(View.VISIBLE);
                    tvTodayBusOnRoadLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandless, 0);
                    // Uncomment the following lines if you need to perform additional actions when expanding
                    // setRouteInfo();
                    // settingDashboardTodayLogin();
                }
                break;
        }
    }*/

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llRouteWiseBusOnRoad) {
            // Check network connectivity
            if (!isConnected) {
                Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                return; // Exit the method if there's no network connection
            }

            // Toggle expansion state
            if (isExpanded) {
                // Collapse the layout
                isExpanded = false;
                llRouteWiseBusOnRoadExpanded.setVisibility(View.GONE);
                tvTodayBusOnRoadLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandmore, 0);
            } else {
                // Expand the layout
                isExpanded = true;
                llRouteWiseBusOnRoadExpanded.setVisibility(View.VISIBLE);
                tvTodayBusOnRoadLable.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandless, 0);
                // Uncomment the following lines if you need to perform additional actions when expanding
                // setRouteInfo();
                // settingDashboardTodayLogin();
            }
        }
    }


    private void setRouteInfo() {
        try {
            Selected_Date = Common.convertDateFormat(Common.getCurrentDate(), "dd-MM-yyyy", "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        pg.setMessage("Getting Route Inforamation...");
        pg.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Log.e("ID", String.valueOf(Bus_Company_ID));
        Call<DashboardRouteWiseCountResponse> call = apiInterface.getRouteAndBusCount(new BreakdownRequest(Bus_Company_ID));
        call.enqueue(new Callback<DashboardRouteWiseCountResponse>() {
            public Integer totalTodayBusCount = 0, totalYesterdayBusCount = 0;

            @Override
            public void onResponse(Call<DashboardRouteWiseCountResponse> call, Response<DashboardRouteWiseCountResponse> response) {
                pg.dismiss();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 1) {
                            Log.e("ROUTE INFO RESPONSE : ", response.body().getMessage());
                            routeData = response.body().getRouteWiseBusData();
                            reportDataAdapter = new AdpDataListRouteInfoDshbrd(getContext(), routeData);
                            rvRouteInfo.setLayoutManager(new GridLayoutManager(getContext(), 1));
                            rvRouteInfo.setAdapter(reportDataAdapter);
                            reportDataAdapter.notifyDataSetChanged();
                            for (int i = 0; i < routeData.size(); i++) {
                                totalTodayBusCount += routeData.get(i).getTodayOnRoad();
                                totalYesterdayBusCount += routeData.get(i).getYesterdayOnRoad();
                            }
                            tvTotalYesterdayBusCount.setText(String.valueOf(totalYesterdayBusCount));
                            tvTotalTodayBusCount.setText(String.valueOf(totalTodayBusCount));
                        } else {
                            pg.dismiss();
                            Log.e("ROUTE INFO ERROR 1: ", response.body().getMessage());
                        }
                    } else {
                        pg.dismiss();
                        Log.e("ROUTE INFO ERROR 2: ", response.errorBody().string());
                    }
                } catch (Exception e) {
                    pg.dismiss();
                    Log.e("ROUTE INFO EXCEPTION: ", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<DashboardRouteWiseCountResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("ROUTE INFO FAILURE: ", t.getMessage());
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogBox);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(R.string.NetworkErrorBtnTxt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setRouteInfo();
                        builder.show();
                    }
                });

            }
        });
    }

    private void settingDashboardTodayLogin() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            if (IsFirstTime) {
                pg.setMessage("Getting Dashboard Data...");
                pg.show();
                IsFirstTime = false;
            }
            Flag_ID = 1;
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<RoutineTodayResponse> call = apiInterface.getTodayBusRoutine(new RoutineRequest(Flag_ID, Bus_Company_ID));
            call.enqueue(new Callback<RoutineTodayResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<RoutineTodayResponse> call, Response<RoutineTodayResponse> response) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.d("TodayLoginResponse :; ", response.body().getMessage());
                                todayRoutineLogin = response.body().getTodayRoutineData();
                                tvTodayFirstBusTime.setText(convertDateFormat(todayRoutineLogin.get(0).getTodayFirstBusLogin(), "HH:mm:ss", "hh:mm a"));
                                tvTodayFirstBus.setText(String.valueOf(todayRoutineLogin.get(0).getTodayFirstBusLoginVehicle()));
                                tvTodayFirstBusRoute.setText(String.valueOf(todayRoutineLogin.get(0).getTodayFirstBusLoginRoute()));
                                tvTodayLastBusTime.setText(convertDateFormat(todayRoutineLogin.get(0).getTodayLastBusLogin(), "HH:mm:ss", "hh:mm a"));
                                tvTodayLastBus.setText(String.valueOf(todayRoutineLogin.get(0).getTodayLastBusLoginVehicle()));
                                tvTodayLastBusRoute.setText(String.valueOf(todayRoutineLogin.get(0).getTodayLastBusLoginRoute()));
                                //settingDashboardYesterdayLogin();
                            } else {
                                if (pg.isShowing()) {
                                    pg.dismiss();
                                }
                                tvTodayFirstBusTime.setText("N/A");
                                tvTodayFirstBus.setText("N/A");
                                tvTodayFirstBusRoute.setText("N/A");
                                tvTodayLastBusTime.setText("N/A");
                                tvTodayLastBusRoute.setText("N/A");
                                tvTodayLastBus.setText("N/A");
                                Log.d("TodayLoginResponseError1 : ", response.errorBody().string());

                            }
                        } else {
                            if (pg.isShowing()) {
                                pg.dismiss();
                            }
                            Log.d("TodayLoginResponseError2 : ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing()) {
                            pg.dismiss();
                        }
                        Log.d("TodayLoginResponseException : ", e.getMessage());
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<RoutineTodayResponse> call, Throwable t) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    if (IsFirstTime) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogBox);
                        builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                        builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                        builder.setPositiveButton(R.string.NetworkErrorBtnTxt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                settingDashboardTodayLogin();
                                builder.show();
                            }
                        });

                        IsFirstTime = false;
                    }
                    Log.d("TodayLoginResponseFailure : ", t.getMessage());
                }
            });
        }

    }

    private void settingDashboardYesterdayLogin() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            if (IsFirstTime) {
                pg.setMessage("Getting Dashboard Data...");
                pg.show();
                IsFirstTime = false;
            }
            Flag_ID = 1;
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<RoutineYesterdayResponse> call = apiInterface.getYesterdayBusRoutine(new RoutineRequest(Flag_ID, Bus_Company_ID));
            call.enqueue(new Callback<RoutineYesterdayResponse>() {
                @Override
                public void onResponse(Call<RoutineYesterdayResponse> call, Response<RoutineYesterdayResponse> response) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                yesterdaysRoutieLogin = response.body().getYesterdayRoutineData();
                                Log.d("YestrdayResponse : ", response.body().getMessage());
                                tvYesterdayFirstBusTime.setText(convertDateFormat(yesterdaysRoutieLogin.get(0).getYesterdayFirstBusLogin(), "HH:mm:ss", "hh:mm a"));
                                tvYesterdayFirstBus.setText(String.valueOf(yesterdaysRoutieLogin.get(0).getYesterdayFirstBusLoginVehicle()));
                                tvYesterdayFirstBusRoute.setText(String.valueOf(yesterdaysRoutieLogin.get(0).getYesterdayFirstBusLoginRoute()));
                                tvYesterdayLastBusTime.setText(convertDateFormat(yesterdaysRoutieLogin.get(0).getYesterdayLastBusLogin(), "HH:mm:ss", "hh:mm a"));
                                tvYesterdayLastBus.setText(String.valueOf(yesterdaysRoutieLogin.get(0).getYesterdayLastBusLoginVehicle()));
                                tvYesterdayLastBusRoute.setText(String.valueOf(yesterdaysRoutieLogin.get(0).getYesterdayLastBusLoginRoute()));
                            } else {
                                if (pg.isShowing()) {
                                    pg.dismiss();
                                }
                                Log.d("YestrdayNoFound : ", response.body().getMessage());
                                tvYesterdayFirstBusTime.setText("N/A");
                                tvYesterdayFirstBus.setText("N/A");
                                tvYesterdayFirstBusRoute.setText("N/A");
                                tvYesterdayLastBusTime.setText("N/A");
                                tvYesterdayLastBus.setText("N/A");
                                tvYesterdayLastBusRoute.setText("N/A");
                            }
                        } else {
                            if (pg.isShowing()) {
                                pg.dismiss();
                            }
                            Log.d("YestrdayError : ", response.body().getMessage());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing()) {
                            pg.dismiss();
                        }
                        Log.d("YesterdayExcptn : ", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<RoutineYesterdayResponse> call, Throwable t) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    if (IsFirstTime) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogBox);
                        builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                        builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                        builder.setPositiveButton(R.string.NetworkErrorBtnTxt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                settingDashboardYesterdayLogin();
                                builder.show();
                            }
                        });

                        IsFirstTime = false;
                    }
                    Log.d("Routine Failure : ", t.getMessage());
                }
            });
        }
    }
}
