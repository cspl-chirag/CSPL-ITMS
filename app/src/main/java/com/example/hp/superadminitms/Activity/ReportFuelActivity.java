package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
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

import com.example.hp.superadminitms.Adapter.FuelReportAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.FuelReportDatum;
import com.example.hp.superadminitms.Network.FuelReportResponse;
import com.example.hp.superadminitms.Network.ReportPartReplacementRequest;
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

public class ReportFuelActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private TextView tvStartDate, tvTotal;
    private Button btnShow, btnClear;
    private LinearLayout llDataNotFound, llDataFound;
    private RecyclerView rvReport;
    private Toolbar toolbar;
    private ProgressDialog pg;
    private SessionManager sessionManager;
    private int Company_Id;
    private boolean isConnected;
    private List<FuelReportDatum> reportData = new ArrayList<>();
    private FuelReportAdapter adapter;
    private String Selected_Date;
    private final long mLastClickTime = 0;
    private Calendar c;
    private int mYear;
    private int mMonth;
    private int mDay;
    private ImageView ivStartDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_fuel);
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
        getSupportActionBar().setTitle("Fuel Report");
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
        pg = Common.showProgressDialog(this);
        pg.setCancelable(false);
        pg.setIndeterminate(false);
        sessionManager = new SessionManager(this);
        Company_Id = sessionManager.getKeyCompanyId();
        tvStartDate = findViewById(R.id.tvStartDate);
        tvTotal = findViewById(R.id.tvTotal);
        btnShow = findViewById(R.id.btnShow);
        btnClear = findViewById(R.id.btnClear);
        llDataFound = findViewById(R.id.llDataFound);
        llDataNotFound = findViewById(R.id.llDataNotFound);
        rvReport = findViewById(R.id.rvReport);
        ivStartDate = findViewById(R.id.ivStartDate);

        try {
            Selected_Date = Common.convertDateFormat(Common.getCurrentDate(), "dd-MM-yyyy", "yyyy-MM-dd");
            tvStartDate.setText(Selected_Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btnShow.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        ivStartDate.setOnClickListener(this);
    }

    private void getReportData() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Getting Fuel Report Data...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Log.e("Selected_Date : ", Selected_Date);

            Call<FuelReportResponse> call = apiInterface.getFuelReport(new ReportPartReplacementRequest(Company_Id, Selected_Date));
            call.enqueue(new Callback<FuelReportResponse>() {
                public Float total = Float.valueOf(0);

                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<FuelReportResponse> call, Response<FuelReportResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("FUEL REPORT RESPONSE : ", response.body().getMessage());
                                llDataFound.setVisibility(View.VISIBLE);
                                reportData = response.body().getFuelReportData();
                                for (int i = 0; i < reportData.size(); i++) {
                                    total += reportData.get(i).getQuantity();
                                }
                                tvTotal.setText(String.valueOf(total));
                                adapter = new FuelReportAdapter(ReportFuelActivity.this, reportData);
                                rvReport.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                                rvReport.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                pg.dismiss();
                                llDataFound.setVisibility(View.GONE);
                                llDataNotFound.setVisibility(View.VISIBLE);
                                Log.e("FUEL REPORT ERROR 1 : ", response.body().getMessage());
                            }
                        } else {
                            pg.dismiss();
                            llDataFound.setVisibility(View.GONE);
                            llDataNotFound.setVisibility(View.VISIBLE);
                            Log.e("FUEL REPORT ERROR 2 : ", response.body().getMessage());
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        llDataFound.setVisibility(View.GONE);
                        llDataNotFound.setVisibility(View.VISIBLE);
                        Log.e("FUEL REPORT EXCEPTION : ", e.getMessage());
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<FuelReportResponse> call, Throwable t) {
                    pg.dismiss();
                    llDataFound.setVisibility(View.GONE);
                    llDataNotFound.setVisibility(View.VISIBLE);
                    Log.e("FUEL REPORT FAILURE : ", t.getMessage());
                }
            });
        }
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnShow:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                    return;
                else {
                    getReportData();
                }
                break;
            case R.id.btnClear:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                    return;
                else {
                    reportData.clear();
                    llDataNotFound.setVisibility(View.GONE);
                    llDataFound.setVisibility(View.GONE);
                }
                break;
            case R.id.ivStartDate:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000)
                    return;
                else {
                    c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                    DatePickerDialog startDatePickerDialog = new DatePickerDialog(ReportFuelActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year, int monthOfYear, int dayOfMonth) {
                            try {
                                Selected_Date = Common.convertDateFormat(Common.getDate(year, (monthOfYear + 1), dayOfMonth), "dd-MM-yyyy", "yyyy-MM-dd");
                                tvStartDate.setText(Selected_Date);
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
        }
    }*/

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnShow) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            } else {
                getReportData();
            }
        } else if (view.getId() == R.id.btnClear) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            } else {
                reportData.clear();
                llDataNotFound.setVisibility(View.GONE);
                llDataFound.setVisibility(View.GONE);
            }
        } else if (view.getId() == R.id.ivStartDate) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            } else {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog startDatePickerDialog = new DatePickerDialog(
                        ReportFuelActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                try {
                                    Selected_Date = Common.convertDateFormat(
                                            Common.getDate(year, (monthOfYear + 1), dayOfMonth),
                                            "dd-MM-yyyy",
                                            "yyyy-MM-dd"
                                    );
                                    tvStartDate.setText(Selected_Date);
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
        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}
