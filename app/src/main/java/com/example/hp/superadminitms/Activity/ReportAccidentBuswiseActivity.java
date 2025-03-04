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

import com.example.hp.superadminitms.Adapter.ReportAccidentDataAdapter;
import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.AccidentReportBusWise;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Network.BusRequest;
import com.example.hp.superadminitms.Network.BusResponse;
import com.example.hp.superadminitms.Network.ReportBuswiseAccidentRequest;
import com.example.hp.superadminitms.Network.ReportBuswiseAccidentResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportAccidentBuswiseActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private Spinner spVehileRegNo;
    private Button btnShow, btnClear;
    private List<BusDatum> busDatum = new ArrayList<>();
    private VehicleSpinnerAdapter spVehicleAdapter;
    private final String TAG = "********";
    private SessionManager session;
    private int Company_ID;
    private ProgressDialog pg;
    private Integer Vehicle_Id;
    private boolean isConnected;
    private LinearLayout llDataFound, llDataNotFound;
    private RecyclerView rvReport;
    private List<AccidentReportBusWise> data = new ArrayList<>();
    private ReportAccidentDataAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_report_buswise);
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
        getSupportActionBar().setTitle("Accident History");
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
        session = new SessionManager(this);
        Company_ID = session.getKeyCompanyId();
        pg = Common.showProgressDialog(this);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        pg.setMessage("Please Wait...");
        spVehileRegNo = findViewById(R.id.spVehileRegNo);
        btnShow = findViewById(R.id.btnShow);
        btnClear = findViewById(R.id.btnClear);
        llDataFound = findViewById(R.id.llDataFound);
        llDataNotFound = findViewById(R.id.llDataNotFound);
        rvReport = findViewById(R.id.rvReport);


        spVehileRegNo.setOnItemSelectedListener(this);
        btnShow.setOnClickListener(this);
        btnClear.setOnClickListener(this);
    }

    private void getVehicleInfoSpinner() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(ReportAccidentBuswiseActivity.this, R.style.DialogBox);
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
            Call<BusResponse> call = apiInterface.getVehicleInfo(new BusRequest(Company_ID));
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
                                Log.d(TAG, "BusListResponse : " + response.body().getMessage());

                                busDatum = response.body().getBusData();
                                spVehicleAdapter = new VehicleSpinnerAdapter(ReportAccidentBuswiseActivity.this, android.R.layout.simple_spinner_dropdown_item, busDatum);
                                spVehileRegNo.setAdapter(spVehicleAdapter);
                                spVehicleAdapter.notifyDataSetChanged();
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
                            new AlertDialog.Builder(ReportAccidentBuswiseActivity.this, R.style.DialogBox);
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
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnShow:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    getAccidentData();
                }
                break;
            case R.id.btnClear:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    if (!data.isEmpty())
                    {
                        data.clear();
                    }
                    llDataFound.setVisibility(View.GONE);
                    llDataNotFound.setVisibility(View.VISIBLE);
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
                getAccidentData();
            }
        } else if (view.getId() == R.id.btnClear) {
            isConnected = ConnectivityReceiver.isConnected();
            if (!isConnected) {
                Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                if (!data.isEmpty()) {
                    data.clear();
                }
                llDataFound.setVisibility(View.GONE);
                llDataNotFound.setVisibility(View.VISIBLE);
            }
        }
    }


    private void getAccidentData() {
        if (!data.isEmpty()) {
            data.clear();
        }
        pg.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ReportBuswiseAccidentResponse> call = apiInterface.getBusWiseAccidentReport(new ReportBuswiseAccidentRequest(Company_ID, Vehicle_Id));
        call.enqueue(new Callback<ReportBuswiseAccidentResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ReportBuswiseAccidentResponse> call, Response<ReportBuswiseAccidentResponse> response) {
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 1) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    pg.dismiss();
                                }
                            }, 700);
                            Log.e("ACCIDENT DATA RESPONSE  : ", response.body().getMessage());
                            llDataFound.setVisibility(View.VISIBLE);
                            llDataNotFound.setVisibility(View.GONE);
                            data = response.body().getBusWiseAccidentReport();
                            adapter = new ReportAccidentDataAdapter(ReportAccidentBuswiseActivity.this, data);
                            rvReport.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                            rvReport.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            pg.dismiss();
                            Log.e("ACCIDENT DATA ERROR 1 : ", response.body().getMessage());
                            llDataFound.setVisibility(View.GONE);
                            llDataNotFound.setVisibility(View.VISIBLE);
                        }
                    } else {
                        pg.dismiss();
                        Log.e("ACCIDENT DATA ERROR 2 : ", response.errorBody().string());
                        llDataFound.setVisibility(View.GONE);
                        llDataNotFound.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    pg.dismiss();
                    Log.e("ACCIDENT DATA EXCEPTION : ", e.getMessage());
                    llDataFound.setVisibility(View.GONE);
                    llDataNotFound.setVisibility(View.VISIBLE);
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ReportBuswiseAccidentResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("ACCIDENT DATA FAILURE : ", t.getMessage());
                llDataFound.setVisibility(View.GONE);
                llDataNotFound.setVisibility(View.VISIBLE);
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ReportAccidentBuswiseActivity.this, R.style.DialogBox);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getAccidentData();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId())
        {
            case R.id.spVehileRegNo:
                Vehicle_Id = busDatum.get(i).getVehicleID();
                break;

        }
    }*/


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spVehileRegNo) {
            Vehicle_Id = busDatum.get(i).getVehicleID();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}
