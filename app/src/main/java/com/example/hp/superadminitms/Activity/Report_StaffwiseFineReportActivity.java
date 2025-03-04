package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.ReportOffenceDataAdapter;
import com.example.hp.superadminitms.Adapter.StaffListSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.StaffDatum;
import com.example.hp.superadminitms.Model.StaffWiseOffenceReport;
import com.example.hp.superadminitms.Network.OffenceRequest;
import com.example.hp.superadminitms.Network.RouteRequest;
import com.example.hp.superadminitms.Network.StaffListResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Retrofit.StaffwiseOffenceReportResponse;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Report_StaffwiseFineReportActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner spStaffMembers;
    private LinearLayout llDataFound, llDataNotFound;
    private RecyclerView rvReport;
    private Button btnShow, btnClear;
    private SessionManager session;
    private int Company_Id, User_Id;
    private ProgressDialog pg;
    private boolean isConnected;
    private List<StaffDatum> staffData = new ArrayList<>();
    private final List<StaffDatum> staffMainList = new ArrayList<>();
    private StaffListSpinnerAdapter staffListSpinnerAdapter;
    private Integer Staff_Id;
    private List<StaffWiseOffenceReport> data = new ArrayList<>();
    private ReportOffenceDataAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_report);
        initializeToolBar();
        initializeControls();
        getStaffMemberList();
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
        getSupportActionBar().setTitle("Staffwise Offence Report");
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
        session = new SessionManager(this);
        Company_Id = session.getKeyCompanyId();
        User_Id = session.getKeyUserId();
        spStaffMembers = findViewById(R.id.spStaffMembers);
        llDataFound = findViewById(R.id.llDataFound);
        llDataNotFound = findViewById(R.id.llDataNotFound);
        rvReport = findViewById(R.id.rvReport);
        btnShow = findViewById(R.id.btnShow);
        btnClear = findViewById(R.id.btnClear);

        spStaffMembers.setOnItemSelectedListener(this);
        btnShow.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    private void getStaffMemberList() {
        if (!staffMainList.isEmpty()) {
            staffMainList.clear();
        }
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Getting Staff Members...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<StaffListResponse> call = apiInterface.getStaffList(new RouteRequest(Company_Id));
            call.enqueue(new Callback<StaffListResponse>() {
                @Override
                public void onResponse(Call<StaffListResponse> call, Response<StaffListResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("PART LIST RESPONSE : ", response.body().getMessage());
                                staffMainList.clear();
                                staffData = response.body().getStaffData();
                                staffMainList.add(new StaffDatum(0, "- -", "Select Staff Member", "- - -", ""));
                                for (int i = 0; i < staffData.size(); i++) {
                                    staffMainList.add(new StaffDatum(staffData.get(i).getStaffID(),
                                            staffData.get(i).getStaffCode(),
                                            staffData.get(i).getFirstName(),
                                            staffData.get(i).getMiddleName(),
                                            staffData.get(i).getLastName(),
                                            staffData.get(i).getCurrentAddress1(),
                                            staffData.get(i).getCurrentAddress2(),
                                            staffData.get(i).getCurrentLocality(),
                                            staffData.get(i).getPermanentAddress1(),
                                            staffData.get(i).getPermanentAddress2(),
                                            staffData.get(i).getDesignation(),
                                            staffData.get(i).getCurrentPincode()));
                                }
                                staffListSpinnerAdapter = new StaffListSpinnerAdapter(Report_StaffwiseFineReportActivity.this, android.R.layout.simple_spinner_dropdown_item, staffMainList);
                                spStaffMembers.setAdapter(staffListSpinnerAdapter);
                                staffListSpinnerAdapter.notifyDataSetChanged();
                            } else {
                                pg.dismiss();
                                Log.e("PART LIST ERROR 1 : ", response.body().getMessage());
                            }
                        } else {
                            pg.dismiss();
                            Log.e("PART LIST ERROR 2 : ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        Log.e("PART LIST EXCEPTION : ", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<StaffListResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("PART LIST FAILURE : ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Report_StaffwiseFineReportActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getStaffMemberList();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
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
                    Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    getStaffOffencenReportData();
                }
                break;
            case R.id.btnClear:
                clearHistoryData();
                break;
        }
    }*/


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnShow) {
            isConnected = ConnectivityReceiver.isConnected();
            if (!isConnected) {
                Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                getStaffOffencenReportData();
            }
        } else if (view.getId() == R.id.btnClear) {
            clearHistoryData();
        }
    }


    private void clearHistoryData() {
        data.clear();
        llDataFound.setVisibility(View.GONE);
        llDataNotFound.setVisibility(View.GONE);
    }

    private void getStaffOffencenReportData() {
        if (Staff_Id != 0) {
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<StaffwiseOffenceReportResponse> call = apiInterface.getStaffWiseOffenceReport(new OffenceRequest(Company_Id, Staff_Id));
            call.enqueue(new Callback<StaffwiseOffenceReportResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<StaffwiseOffenceReportResponse> call, Response<StaffwiseOffenceReportResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        pg.dismiss();
                                    }
                                }, 700);
                                Log.e("Staff_Assign_Kit_Report_Response : ", response.body().getMessage());
                                data = response.body().getStaffWiseOffenceReport();
                                adapter = new ReportOffenceDataAdapter(Report_StaffwiseFineReportActivity.this, data);
                                rvReport.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                                rvReport.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                llDataFound.setVisibility(View.VISIBLE);
                                llDataNotFound.setVisibility(View.GONE);
                            } else {
                                pg.dismiss();
                                Log.e("Staff_Assign_Kit_Report_ERROR 1 : ", response.body().getMessage());
                                llDataFound.setVisibility(View.GONE);
                                llDataNotFound.setVisibility(View.VISIBLE);
                            }
                        } else {
                            pg.dismiss();
                            Log.e("Staff_Assign_Kit_Report_ERROR 2 : ", response.errorBody().string());
                            llDataFound.setVisibility(View.GONE);
                            llDataNotFound.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        Log.e("Staff_Assign_Kit_Report_EXCEPTION : ", e.getMessage());
                        llDataFound.setVisibility(View.GONE);
                        llDataNotFound.setVisibility(View.VISIBLE);
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<StaffwiseOffenceReportResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("Staff_Assign_Kit_Report_FAILURE : ", t.getMessage());
                    llDataFound.setVisibility(View.GONE);
                    llDataNotFound.setVisibility(View.VISIBLE);
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Report_StaffwiseFineReportActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getStaffOffencenReportData();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });

        } else {
            Common.showSnackError(findViewById(R.id.rootlayout), "Select Staffmember...!");
        }
    }

   /* @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spStaffMembers:
                if (i == 0) {
                    Staff_Id = 0;
                } else {
                    Staff_Id = staffMainList.get(i).getStaffID();
                }
                break;
        }
    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spStaffMembers) {
            if (i == 0) {
                Staff_Id = 0;
            } else {
                Staff_Id = staffMainList.get(i).getStaffID();
            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
