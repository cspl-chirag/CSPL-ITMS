package com.example.hp.superadminitms.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.AdpDataListBreakdown;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BreakdownDatum;
import com.example.hp.superadminitms.Network.BreakdownRequest;
import com.example.hp.superadminitms.Network.BreakdownResponse;
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

public class StoreBreakdownListActiviy extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private boolean isConnected;
    private RecyclerView rvBrekdown;
    private ProgressDialog pg;
    private List<BreakdownDatum> breakdownData = new ArrayList<>();
    private AdpDataListBreakdown adapter;
    private SessionManager sessionManager;
    private LinearLayout llNotfound;
    private int companyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_st_breakdown_list);
        initializeToolBar();
        initializeControls();
        setupBreakdownRecyclerview();
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
        getSupportActionBar().setTitle("View Breakdown Buses");
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
    protected void onResume() {
        super.onResume();
        setupBreakdownRecyclerview();

    }

    private void setupBreakdownRecyclerview() {
        pg.setMessage("Getting Breakdown Bus Data...");
        pg.show();
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            Log.d("Company_ID", String.valueOf(companyId));
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<BreakdownResponse> call = apiInterface.getBreakdownData(new BreakdownRequest(companyId));
            call.enqueue(new Callback<BreakdownResponse>() {
                @Override
                public void onResponse(Call<BreakdownResponse> call, Response<BreakdownResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                breakdownData = response.body().getMaintenanceBreakDownData();
                                Log.d("BreakdownResponse:", response.body().getMessage());
                                rvBrekdown.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                                adapter = new AdpDataListBreakdown(StoreBreakdownListActiviy.this, breakdownData);
                                rvBrekdown.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            } else {
                                pg.dismiss();
                                Log.d("BreakdownResponseError:", response.body().getMessage());
                                llNotfound.setVisibility(View.VISIBLE);
                            }
                        } else {
                            pg.dismiss();
                            Log.d("BreakdownResponseError:", response.body().getMessage());
                            llNotfound.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        Log.d("BreakdownResponseExptn:", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<BreakdownResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.d("BreakdownResponseFail:", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(StoreBreakdownListActiviy.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setupBreakdownRecyclerview();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void initializeControls() {
        pg = new ProgressDialog(this, R.style.DialogBox);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        rvBrekdown = findViewById(R.id.rvBreakdownData);
        llNotfound = findViewById(R.id.llNotfound);
        sessionManager = new SessionManager(StoreBreakdownListActiviy.this);
        companyId = sessionManager.getKeyCompanyId();

    }

    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}
