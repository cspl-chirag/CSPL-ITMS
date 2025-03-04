package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.DailySummaryDatum;
import com.example.hp.superadminitms.Network.BreakdownRequest;
import com.example.hp.superadminitms.Network.DailySummaryResponse;
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

public class DailySummaryInfoActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private TextView tvOnRoadBuses, tvBusesInSpare, tvBusesInOther, tvBreakdownBuses, tvTotal;
    private boolean isConnected;
    private SessionManager session;
    private int Company_Id;
    private ProgressDialog pg;
    private List<DailySummaryDatum> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_summary_info);
        initializeToolBar();
        initializeControls();
        getDailySummaryData();
    }

    private void initializeControls() {
        session = new SessionManager(this);
        Company_Id = session.getKeyCompanyId();
        pg = Common.showProgressDialog(this);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        tvBreakdownBuses = findViewById(R.id.tvBreakdownBuses);
        tvBusesInOther = findViewById(R.id.tvBusesInOther);
        tvBusesInSpare = findViewById(R.id.tvBusesInSpare);
        tvOnRoadBuses = findViewById(R.id.tvOnRoadBuses);
        tvTotal = findViewById(R.id.tvTotal);

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
        getSupportActionBar().setTitle("Daily Bus Summary");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
            }
        }
    }

    private void getDailySummaryData() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Getting Daily Summary Data...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<DailySummaryResponse> call = apiInterface.getDailySummary(new BreakdownRequest(Company_Id));
            call.enqueue(new Callback<DailySummaryResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<DailySummaryResponse> call, Response<DailySummaryResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("DAILY BUS SUMMARY RESPONSE : ", response.body().getMessage());
                                data = response.body().getBusSummaryData();
                                tvOnRoadBuses.setText(String.valueOf(data.get(0).getBusOnRoad()));
                                tvBreakdownBuses.setText(String.valueOf(data.get(0).getBreakdown()));
                                tvBusesInOther.setText(String.valueOf(data.get(0).getOther()));
                                tvBusesInSpare.setText(String.valueOf(data.get(0).getSpare()));
                                tvTotal.setText(String.valueOf(data.get(0).getTotalBus()));
                            } else {
                                pg.dismiss();
                                initializeControlsToValueZero();
                                Log.e("DAILY BUS SUMMARY ERROR 1 : ", response.body().getMessage());
                            }
                        } else {
                            pg.dismiss();
                            initializeControlsToValueZero();
                            Log.e("DAILY BUS SUMMARY ERROR 2 : ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        initializeControlsToValueZero();
                        Log.e("DAILY BUS SUMMARY EXCEPTION : ", e.getMessage());
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<DailySummaryResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("DAILY BUS SUMMARY FAIlURE : ", t.getMessage());
                    initializeControlsToValueZero();
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(DailySummaryInfoActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getDailySummaryData();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void initializeControlsToValueZero() {
        tvOnRoadBuses.setText("0");
        tvBreakdownBuses.setText("0");
        tvBusesInOther.setText("0");
        tvBusesInSpare.setText("0");
        tvTotal.setText("0");
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}
