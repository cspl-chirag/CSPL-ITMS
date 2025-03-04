package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.ReportBuswiseAVGAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.ReportBusWiseAverage;
import com.example.hp.superadminitms.Network.ReportBusWiseAvgResponse;
import com.example.hp.superadminitms.Network.ReportRequest;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportBusWiseAverageActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private Button btnShow, btnClear;
    private LinearLayout llDataNotFound, llDataFound;
    private RecyclerView rvAvgReport;
    private ImageView ivStartDate, ivEndDate;
    private String Selected_Start_Date, Selected_End_Date;
    private TextView tvStartDate, tvEndDate, tvTotalRunKm, tvTotalFuelQty, tvTotalAvg;
    private SessionManager sessionManager;
    private int Company_Id;
    private ProgressDialog pg;
    private boolean isConnected;
    private Calendar c;
    private final long mLastClickTime = 0;
    private int mYear, mMonth, mDay;
    private List<ReportBusWiseAverage> inwardReportData = new ArrayList<>();
    private ReportBuswiseAVGAdapter inwardReportAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_wise_average_report);
        initializeToolBar();
        initializeControls();
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
        getSupportActionBar().setTitle("Buswise Average Report");
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
        rvAvgReport = findViewById(R.id.rvReport);
        ivStartDate = findViewById(R.id.ivStartDate);
        ivEndDate = findViewById(R.id.ivEndDate);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvTotalRunKm = findViewById(R.id.tvTotalRunKm);
        tvTotalFuelQty = findViewById(R.id.tvTotalFuelQty);
        tvTotalAvg = findViewById(R.id.tvTotalAvg);
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
                llDataFound.setVisibility(View.GONE);
                llDataNotFound.setVisibility(View.GONE);
                break;

            case R.id.ivStartDate:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                    return;
                else {
                    c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog startDatePickerDialog = new DatePickerDialog(ReportBusWiseAverageActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year, int monthOfYear, int dayOfMonth) {
                            Selected_Start_Date = Common.getDate(year, (monthOfYear + 1), dayOfMonth);
                            try {
                                tvStartDate.setText(Common.convertDateFormat(Selected_Start_Date, "dd-MM-yyyy", "yyyy-MM-dd"));
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
                    DatePickerDialog endDatepickerDialog = new DatePickerDialog(ReportBusWiseAverageActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year, int monthOfYear, int dayOfMonth) {
                            Selected_End_Date = Common.getDate(year, (monthOfYear + 1), dayOfMonth);
                            try {
                                tvEndDate.setText(Common.convertDateFormat(Selected_End_Date, "dd-MM-yyyy", "yyyy-MM-dd"));
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
        if (view.getId() == R.id.btnShow) {
            isConnected = ConnectivityReceiver.isConnected();
            if (!isConnected) {
                Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                getReportData();
            }
        } else if (view.getId() == R.id.btnClear) {
            llDataFound.setVisibility(View.GONE);
            llDataNotFound.setVisibility(View.GONE);
        } else if (view.getId() == R.id.ivStartDate) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            } else {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog startDatePickerDialog = new DatePickerDialog(
                        ReportBusWiseAverageActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                Selected_Start_Date = Common.getDate(year, (monthOfYear + 1), dayOfMonth);
                                try {
                                    tvStartDate.setText(Common.convertDateFormat(Selected_Start_Date, "dd-MM-yyyy", "yyyy-MM-dd"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        mYear,
                        mMonth,
                        mDay
                );
                startDatePickerDialog.show();
                startDatePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                startDatePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        } else if (view.getId() == R.id.ivEndDate) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            } else {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog endDatePickerDialog = new DatePickerDialog(
                        ReportBusWiseAverageActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                Selected_End_Date = Common.getDate(year, (monthOfYear + 1), dayOfMonth);
                                try {
                                    tvEndDate.setText(Common.convertDateFormat(Selected_End_Date, "dd-MM-yyyy", "yyyy-MM-dd"));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        mYear,
                        mMonth,
                        mDay
                );
                endDatePickerDialog.show();
                endDatePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                endDatePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }
    }


    private void getReportData() {
        llDataFound.setVisibility(View.VISIBLE);
        llDataNotFound.setVisibility(View.GONE);
        Selected_Start_Date = tvStartDate.getText().toString() + " 00:00:00";
        Selected_End_Date = tvEndDate.getText().toString() + " 23:59:59";
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        pg.setMessage("Getting Inward Report Data...");
        pg.show();
        Call<ReportBusWiseAvgResponse> call = apiInterface.getBuswiseAvgData(new ReportRequest(Company_Id, Selected_Start_Date, Selected_End_Date));
        call.enqueue(new Callback<ReportBusWiseAvgResponse>() {
            public Integer totalRunKm = 0;
            public float totalAvg = 0, totalFuelQty = 0;

            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ReportBusWiseAvgResponse> call, Response<ReportBusWiseAvgResponse> response) {
                pg.dismiss();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 1) {
                            Log.e("INWARD REPORT RESPONSE : ", response.body().getMessage());
                            inwardReportData = response.body().getBusWiseAverageReport();
                            rvAvgReport.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                            inwardReportAdapter = new ReportBuswiseAVGAdapter(ReportBusWiseAverageActivity.this, inwardReportData);
                            rvAvgReport.setAdapter(inwardReportAdapter);
                            inwardReportAdapter.notifyDataSetChanged();
                            for (int i = 0; i < inwardReportData.size(); i++) {
                                totalFuelQty += inwardReportData.get(i).getTotalFuel();
                                totalRunKm += inwardReportData.get(i).getTotalRunKM();
                            }
                            DecimalFormat df = new DecimalFormat("0.00");
                            totalAvg = totalRunKm / totalFuelQty;
                            if (Double.isInfinite(totalAvg) || Double.isNaN(totalAvg)) {
                                tvTotalAvg.setText("0.0");
                            } else {
                                tvTotalAvg.setText(df.format(totalAvg));
                            }
                            tvTotalFuelQty.setText(String.valueOf(totalFuelQty));
                            tvTotalRunKm.setText(String.valueOf(totalRunKm));
                        } else {
                            pg.dismiss();
                            Log.e("INWARD REPORT RESPONSE ERROR 1 : ", response.body().getMessage());
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
                    Log.e("INWARD REPORT RESPONSE EXCEPTION : ", e.getMessage());
                    llDataFound.setVisibility(View.GONE);
                    llDataNotFound.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ReportBusWiseAvgResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("INWARD REPORT RESPONSE FAILURE : ", t.getMessage());
                llDataFound.setVisibility(View.GONE);
                llDataNotFound.setVisibility(View.VISIBLE);
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ReportBusWiseAverageActivity.this);
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
