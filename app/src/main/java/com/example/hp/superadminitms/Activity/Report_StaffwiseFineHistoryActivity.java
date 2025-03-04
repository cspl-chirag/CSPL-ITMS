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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.HistoryOffenceDataAdapter;
import com.example.hp.superadminitms.Adapter.PartReplacementHistoryAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.StaffWiseOffenceReport;
import com.example.hp.superadminitms.Model.StockPartReplacementReportDatum;
import com.example.hp.superadminitms.Network.OffenceRequest;
import com.example.hp.superadminitms.Network.StaffwiseOffenceHistoryResponse;
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

public class Report_StaffwiseFineHistoryActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
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
    private final List<StockPartReplacementReportDatum> reportData = new ArrayList<>();
    private PartReplacementHistoryAdapter reportAdapter;
    private final List<BusDatum> busDatum = new ArrayList<>();
    private final String TAG = "************";
    private Integer Vehicle_Id;
    private List<StaffWiseOffenceReport> data = new ArrayList<>();
    private HistoryOffenceDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_fine_history);
        initializeToolBar();
        initializeControls();
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
        getSupportActionBar().setTitle("Staffwise Offence History");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
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

    /*@Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnShow:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
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
                    DatePickerDialog startDatePickerDialog = new DatePickerDialog(Report_StaffwiseFineHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    DatePickerDialog endDatepickerDialog = new DatePickerDialog(Report_StaffwiseFineHistoryActivity.this, new DatePickerDialog.OnDateSetListener() {
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
        if (view.getId() == R.id.btnShow) {
            isConnected = ConnectivityReceiver.isConnected();
            if (!isConnected) {
                Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                getReportData();
            }
        } else if (view.getId() == R.id.btnClear) {
            clearHistoryData();
        } else if (view.getId() == R.id.ivStartDate) {
            if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            } else {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog startDatePickerDialog = new DatePickerDialog(
                        Report_StaffwiseFineHistoryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                try {
                                    Selected_Start_Date = Common.convertDateFormat(
                                            Common.getDate(year, (monthOfYear + 1), dayOfMonth),
                                            "dd-MM-yyyy",
                                            "yyyy-MM-dd"
                                    );
                                    tvStartDate.setText(Selected_Start_Date);
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
                        Report_StaffwiseFineHistoryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                try {
                                    Selected_End_Date = Common.convertDateFormat(
                                            Common.getDate(year, (monthOfYear + 1), dayOfMonth),
                                            "dd-MM-yyyy",
                                            "yyyy-MM-dd"
                                    );
                                    tvEndDate.setText(Selected_End_Date);
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
//        Log.e("Selected_Start_Date",Selected_Start_Date);
//        Log.e("Selected_End_Date",Selected_End_Date);
        pg.setMessage("Please Wait...");
        pg.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<StaffwiseOffenceHistoryResponse> call = apiInterface.getStaffWiseOffenceHistory(new OffenceRequest(Company_Id, Selected_Start_Date, Selected_End_Date));
        call.enqueue(new Callback<StaffwiseOffenceHistoryResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<StaffwiseOffenceHistoryResponse> call, Response<StaffwiseOffenceHistoryResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 1) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pg.dismiss();
                                }
                            }, 700);
                            Log.e("Staff_Assignkit_History_Response : ", response.body().getMessage());
                            data = response.body().getStaffWiseOffenceHistory();
                            adapter = new HistoryOffenceDataAdapter(Report_StaffwiseFineHistoryActivity.this, data);
                            rvReport.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                            rvReport.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            llDataFound.setVisibility(View.VISIBLE);
                            llDataNotFound.setVisibility(View.GONE);
                        } else {
                            pg.dismiss();
                            Log.e("Staff_Assignkit_History_Error 1 : ", response.body().getMessage());
                            llDataFound.setVisibility(View.GONE);
                            llDataNotFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        pg.dismiss();
                        Log.e("Staff_Assignkit_History_Error 2 : ", response.errorBody().string());
                        llDataFound.setVisibility(View.GONE);
                        llDataNotFound.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    pg.dismiss();
                    Log.e("Staff_Assignkit_History_Exception : ", e.getMessage());
                    llDataFound.setVisibility(View.GONE);
                    llDataNotFound.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<StaffwiseOffenceHistoryResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("Staff_Assignkit_History_Failure : ", t.getMessage());
                llDataFound.setVisibility(View.GONE);
                llDataNotFound.setVisibility(View.VISIBLE);
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Report_StaffwiseFineHistoryActivity.this);
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

    private void clearHistoryData() {
        reportData.clear();
        llDataFound.setVisibility(View.GONE);
        llDataNotFound.setVisibility(View.GONE);
    }
}
