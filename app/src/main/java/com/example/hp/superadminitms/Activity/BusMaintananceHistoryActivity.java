package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.MaintananceHistoryAdapter;
import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.ReportBusWiseMaintenanceHistory;
import com.example.hp.superadminitms.Network.BusRequest;
import com.example.hp.superadminitms.Network.BusResponse;
import com.example.hp.superadminitms.Network.ReportBusMaintananceHistoryResponse;
import com.example.hp.superadminitms.Network.ReportRequest;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusMaintananceHistoryActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Button btnShow, btnClear;
    private LinearLayout llDataNotFound, llDataFound;
    private RecyclerView rvReport;
    private ImageView ivStartDate, ivEndDate;
    private String Selected_Start_Date, Selected_End_Date;
    private TextView tvStartDate, tvEndDate;
    private SessionManager sessionManager;
    private int Company_Id;
    private ProgressDialog pg;
    private boolean isConnected;
    private Calendar c;
    private final long mLastClickTime = 0;
    private int mYear, mMonth, mDay;
    private List<ReportBusWiseMaintenanceHistory> reportData = new ArrayList<>();
    private MaintananceHistoryAdapter reportAdapter;
    private SpinnerAdapter spVehicleAdapter;
    private Spinner spVehicleRegNo;
    private List<BusDatum> busDatum = new ArrayList<>();
    private final String TAG = "************";
    private Integer Vehicle_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_maintanance_history);
        initializeToolBar();
        initializeControls();
        getVehicleInfoSpinner();
    }

    private void initializeToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getSupportActionBar().setTitle("Bus Maintanance Report");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
            }
        }
    }

    private void initializeControls() {
        sessionManager = new SessionManager(this);
        Company_Id = sessionManager.getKeyCompanyId();
        pg = Common.showProgressDialog(this);
        pg.setCancelable(false);
        pg.setIndeterminate(false);
        btnShow = findViewById(R.id.btnShow);
        btnClear = findViewById(R.id.btnClear);
        llDataFound = findViewById(R.id.llDataFound);
        llDataNotFound = findViewById(R.id.llDataNotFound);
        rvReport = findViewById(R.id.rvReport);
        ivStartDate = findViewById(R.id.ivStartDate);
        ivEndDate = findViewById(R.id.ivEndDate);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        spVehicleRegNo = findViewById(R.id.spVehileRegNo);
        try {
            Selected_End_Date = Common.convertDateFormat(Common.getCurrentDate(), "dd-MM-yyyy", "yyyy-MM-dd");
            Selected_Start_Date = Common.convertDateFormat(Common.getCurrentDate(), "dd-MM-yyyy", "yyyy-MM-dd");
        } catch (ParseException e) {
            Common.showSnackError(findViewById(R.id.rootlayout), e.getMessage());
        }
        tvStartDate.setText(Selected_Start_Date);
        tvEndDate.setText(Selected_End_Date);
        ivStartDate.setOnClickListener(this);
        ivEndDate.setOnClickListener(this);
        btnShow.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        spVehicleRegNo.setOnItemSelectedListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShow:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    getReportData();
                }
                break;
            case R.id.btnClear:
                clearHistoryData();
                break;

            case R.id.ivStartDate:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                    return;
                else {
                    c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog startDatePickerDialog = new DatePickerDialog(BusMaintananceHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                Selected_Start_Date = (Common.convertDateFormat(Common.getDate(year, (monthOfYear + 1), dayOfMonth), "dd-MM-yyyy", "yyyy-MM-dd"));
                                tvStartDate.setText(Selected_Start_Date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, mYear, mMonth, mDay);
                    startDatePickerDialog.show();
                    startDatePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    startDatePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                break;

            case R.id.ivEndDate:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                    return;
                else {
                    c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog endDatepickerDialog = new DatePickerDialog(BusMaintananceHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                Selected_End_Date = Common.convertDateFormat(Common.getDate(year, (monthOfYear + 1), dayOfMonth), "dd-MM-yyyy", "yyyy-MM-dd");
                                tvEndDate.setText(Selected_End_Date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, mYear, mMonth, mDay);
                    endDatepickerDialog.show();
                    endDatepickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    endDatepickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
                break;

        }
    }*/

    @Override
    public void onClick(View view) {
        int id = view.getId();  // Get the ID of the clicked view

        if (id == R.id.btnShow) {
            // Handle btnShow click
            isConnected = ConnectivityReceiver.isConnected();  // Check network connection
            if (!isConnected) {
                // If not connected, show an error message
                Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                // If connected, get report data
                getReportData();
            }
        } else if (id == R.id.btnClear) {
            // Handle btnClear click
            clearHistoryData();
        } else if (id == R.id.ivStartDate) {
            // Handle ivStartDate click with a debouncing check
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                // Ignore if clicked too soon
            } else {
                // Show start date picker dialog
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog startDatePickerDialog = new DatePickerDialog(BusMaintananceHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            Selected_Start_Date = Common.convertDateFormat(Common.getDate(year, (monthOfYear + 1), dayOfMonth), "dd-MM-yyyy", "yyyy-MM-dd");
                            tvStartDate.setText(Selected_Start_Date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                startDatePickerDialog.show();
                startDatePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startDatePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        } else if (id == R.id.ivEndDate) {
            // Handle ivEndDate click with a debouncing check
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
                // Ignore if clicked too soon
            } else {
                // Show end date picker dialog
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog endDatePickerDialog = new DatePickerDialog(BusMaintananceHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        try {
                            Selected_End_Date = Common.convertDateFormat(Common.getDate(year, (monthOfYear + 1), dayOfMonth), "dd-MM-yyyy", "yyyy-MM-dd");
                            tvEndDate.setText(Selected_End_Date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                endDatePickerDialog.show();
                endDatePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                endDatePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }


    private void clearHistoryData() {
        reportData.clear();
        llDataFound.setVisibility(View.GONE);
        llDataNotFound.setVisibility(View.GONE);
    }

    private void getVehicleInfoSpinner() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(BusMaintananceHistoryActivity.this, R.style.DialogBox);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getVehicleInfoSpinner();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            pg.setMessage("Getting Bus List...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<BusResponse> call = apiInterface.getVehicleInfo(new BusRequest(Company_Id));
            call.enqueue(new Callback<BusResponse>() {
                @Override
                public void onResponse(Call<BusResponse> call, Response<BusResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        pg.dismiss();
                                    }
                                }, 1000);
                                busDatum = response.body().getBusData();
                                spVehicleAdapter = new VehicleSpinnerAdapter(BusMaintananceHistoryActivity.this, android.R.layout.simple_spinner_dropdown_item, busDatum);
                                spVehicleRegNo.setAdapter(spVehicleAdapter);
                            } else {
                                if (pg.isShowing())
                                    pg.dismiss();
                                Log.d(TAG, "BusListError1 : " + response.body().getMessage());
                            }
                        } else {
                            if (pg.isShowing())
                                pg.dismiss();
                            Log.d(TAG, "BusListEerror2 : " + response.body().getMessage());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing())
                            pg.dismiss();
                        e.printStackTrace();
                        Log.d(TAG, "BusListExcption : " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<BusResponse> call, Throwable t) {
                    if (pg.isShowing())
                        pg.dismiss();
                    Log.d(TAG, "BusListFailure : " + t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(BusMaintananceHistoryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getVehicleInfoSpinner();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });

        }
    }
    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spVehileRegNo:
                Vehicle_Id = busDatum.get(i).getVehicleID();
                break;
        }
    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();  // Get the ID of the selected AdapterView

        if (id == R.id.spVehileRegNo) {
            // Handle item selection for spVehileRegNo
            Vehicle_Id = busDatum.get(i).getVehicleID();
        }
        // Add more conditions here if there are other Spinner IDs to handle
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void getReportData() {
//        if (!reportData.isEmpty())
//        {
//            reportData.clear();
//        }
//        Selected_Start_Date = tvStartDate.getText().toString()+" 00:00:00";
//        Selected_End_Date = tvEndDate.getText().toString()+ " 23:59:59";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        pg.setMessage("Getting Inward Report Data...");
        pg.show();
        Call<ReportBusMaintananceHistoryResponse> call = apiInterface.getBuswiseMaintananceHistory(new ReportRequest(Company_Id, Vehicle_Id, Selected_Start_Date, Selected_End_Date));
        call.enqueue(new Callback<ReportBusMaintananceHistoryResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ReportBusMaintananceHistoryResponse> call, Response<ReportBusMaintananceHistoryResponse> response) {
                pg.dismiss();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 1) {
                            llDataFound.setVisibility(View.VISIBLE);
                            llDataNotFound.setVisibility(View.GONE);
                            Log.e("MAINTANANCE REPORT RESPONSE : ", response.body().getMessage());
                            reportData = response.body().getDailyBusWiseMaintenanceReport();
                            reportAdapter = new MaintananceHistoryAdapter(BusMaintananceHistoryActivity.this, reportData);
                            rvReport.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                            rvReport.setAdapter(reportAdapter);
                            reportAdapter.notifyDataSetChanged();
                        } else {
                            pg.dismiss();
                            Log.e("REPORT RESPONSE ERROR 1 : ", response.body().getMessage());
                            llDataFound.setVisibility(View.GONE);
                            llDataNotFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        pg.dismiss();
                        Log.e(" REPORT RESPONSE ERROR 2 : ", response.errorBody().string());
                        llDataFound.setVisibility(View.GONE);
                        llDataNotFound.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    pg.dismiss();
                    Log.e(" REPORT RESPONSE EXCEPTION : ", e.getMessage());
                    llDataFound.setVisibility(View.GONE);
                    llDataNotFound.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ReportBusMaintananceHistoryResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("REPORT RESPONSE FAILURE : ", t.getMessage());
                llDataFound.setVisibility(View.GONE);
                llDataNotFound.setVisibility(View.VISIBLE);
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(BusMaintananceHistoryActivity.this);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getReportData();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });
    }


}
