package com.example.hp.superadminitms.Activity.Dashboards;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Activity.AddAccidentEntryActivity;
import com.example.hp.superadminitms.Activity.AddDailySummaryActivity;
import com.example.hp.superadminitms.Activity.BusLoginOPActivity;
import com.example.hp.superadminitms.Activity.BusLogoutOPActivity;
import com.example.hp.superadminitms.Activity.FuelEntryOPActivity;
import com.example.hp.superadminitms.Activity.LoginActivity;
import com.example.hp.superadminitms.Activity.Menus.Admin_VehicleInfoActivity;
import com.example.hp.superadminitms.Activity.StoreAddBreakdownEntryActivity;
import com.example.hp.superadminitms.Activity.StoreAddJobEntryActivity;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.MyApplication;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Service.BaseActivity;


public class OperatorDashboard extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {

    private SessionManager sessionManager;
    private LinearLayout fuelEntry;
    private LinearLayout busLogin;
    private LinearLayout busLogout;
    private LinearLayout logOut, llReports, llAddDailySummary, llDailyJobCardEntry, llAddBreakdown, llAccidentEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_operator);

        initViews();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
            }
        }

        fuelEntry = findViewById(R.id.fuelEntry);
        busLogin = findViewById(R.id.busLogin);
        busLogout = findViewById(R.id.busLogout);
        logOut = findViewById(R.id.logout);
        llReports = findViewById(R.id.llReports);
        llAddDailySummary = findViewById(R.id.llAddDailySummary);
        llDailyJobCardEntry = findViewById(R.id.llDailyJobCardEntry);
        llAddBreakdown = findViewById(R.id.llAddBreakdown);
        llAccidentEntry = findViewById(R.id.llAccidentEntry);

        fuelEntry.setOnClickListener(this);
        busLogin.setOnClickListener(this);
        busLogout.setOnClickListener(this);
        logOut.setOnClickListener(this);
        llReports.setOnClickListener(this);
        llAddDailySummary.setOnClickListener(this);
        llDailyJobCardEntry.setOnClickListener(this);
        llAddBreakdown.setOnClickListener(this);
        llAccidentEntry.setOnClickListener(this);

        sessionManager = new SessionManager(this);
    }

    /*@Override
    public void onClick(View view) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.dashboardLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            switch (view.getId()) {
                case R.id.fuelEntry:
                    isConnected = ConnectivityReceiver.isConnected();
                    if (!isConnected) {
                        Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                    }
                    else {
                        startActivity(new Intent(OperatorDashboard.this, FuelEntryOPActivity.class));
                    }
                    break;

                case R.id.busLogin:
                    isConnected = ConnectivityReceiver.isConnected();
                    if (!isConnected) {
                        Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                    }
                    else {
                        startActivity(new Intent(OperatorDashboard.this, BusLoginOPActivity.class));
                    }
                    break;

                case R.id.busLogout:
                    isConnected = ConnectivityReceiver.isConnected();
                    if (!isConnected) {
                        Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                    }
                    else {
                        startActivity(new Intent(OperatorDashboard.this, BusLogoutOPActivity.class));
                    }
                    break;

                case R.id.llReports:
                    isConnected = ConnectivityReceiver.isConnected();
                    if (!isConnected) {
                        Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                    }
                    else {
                        startActivity(new Intent(OperatorDashboard.this, Admin_VehicleInfoActivity.class));
                    }
                    break;

                case R.id.llAddDailySummary:
                    isConnected = ConnectivityReceiver.isConnected();
                    if (!isConnected) {
                        Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                    }
                    else {
                        startActivity(new Intent(OperatorDashboard.this, AddDailySummaryActivity.class));
                    }
                    break;

                case R.id.llDailyJobCardEntry:
                    isConnected = ConnectivityReceiver.isConnected();
                    if (!isConnected) {
                        Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                    }
                    else {
                        startActivity(new Intent(OperatorDashboard.this, StoreAddJobEntryActivity.class));
                    }
                    break;

                case R.id.llAddBreakdown:
                    isConnected = ConnectivityReceiver.isConnected();
                    if (!isConnected) {
                        Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                    }
                    else {
                        startActivity(new Intent(OperatorDashboard.this, StoreAddBreakdownEntryActivity.class));
                    }
                    break;

                case R.id.llAccidentEntry:
                    isConnected = ConnectivityReceiver.isConnected();
                    if (!isConnected) {
                        Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                    }
                    else {
                        startActivity(new Intent(OperatorDashboard.this, AddAccidentEntryActivity.class));
                    }
                    break;

                case R.id.logout:
                    sessionManager.clearSession();
                    startActivity(new Intent(OperatorDashboard.this,LoginActivity.class));
                    finish();
            }
        }
    }*/

    @Override
    public void onClick(View view) {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.dashboardLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            int id = view.getId();

            if (id == R.id.fuelEntry) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(OperatorDashboard.this, FuelEntryOPActivity.class));
                }
            } else if (id == R.id.busLogin) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(OperatorDashboard.this, BusLoginOPActivity.class));
                }
            } else if (id == R.id.busLogout) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(OperatorDashboard.this, BusLogoutOPActivity.class));
                }
            } else if (id == R.id.llReports) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(OperatorDashboard.this, Admin_VehicleInfoActivity.class));
                }
            } else if (id == R.id.llAddDailySummary) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(OperatorDashboard.this, AddDailySummaryActivity.class));
                }
            } else if (id == R.id.llDailyJobCardEntry) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(OperatorDashboard.this, StoreAddJobEntryActivity.class));
                }
            } else if (id == R.id.llAddBreakdown) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(OperatorDashboard.this, StoreAddBreakdownEntryActivity.class));
                }
            } else if (id == R.id.llAccidentEntry) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(OperatorDashboard.this, AddAccidentEntryActivity.class));
                }
            } else if (id == R.id.logout) {
                sessionManager.clearSession();
                startActivity(new Intent(OperatorDashboard.this, LoginActivity.class));
                finish();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void showView(boolean isConnected) {
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showView(isConnected);
    }
}