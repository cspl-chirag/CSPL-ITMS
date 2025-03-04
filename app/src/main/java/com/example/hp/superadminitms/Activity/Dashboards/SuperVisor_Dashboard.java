package com.example.hp.superadminitms.Activity.Dashboards;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Activity.BusLoginOPActivity;
import com.example.hp.superadminitms.Activity.BusLogoutListOPActivity;
import com.example.hp.superadminitms.Activity.FuelEntryOPActivity;
import com.example.hp.superadminitms.Activity.LoginActivity;
import com.example.hp.superadminitms.Activity.Menus.Admin_ReportListActivity;
import com.example.hp.superadminitms.Activity.ResignStaffMemberActivity;
import com.example.hp.superadminitms.Activity.StaffAssignKitEntryActivity;
import com.example.hp.superadminitms.Activity.Staff_Offence_Entry_Activity;
import com.example.hp.superadminitms.Activity.Stock_InwardEntryActivity;
import com.example.hp.superadminitms.Activity.Stock_OutwardEntryActivity;
import com.example.hp.superadminitms.Activity.StoreAddBreakdownEntryActivity;
import com.example.hp.superadminitms.Activity.StoreAddJobEntryActivity;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Service.BaseActivity;

public class SuperVisor_Dashboard extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private LinearLayout llBusLogin, llBusLogout, llFuelEntry, llAddBreakdown;
    private LinearLayout llDailyJobCardEntry, llInwardEntry, llStaffAssignKit, llStaffFine;
    private LinearLayout llOutwardEntry, llResignStaffMember, llReports, llLogout;
    private SessionManager sessionManager;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_visor__dashboard);
        intitializeToolBar();
        initializeControls();
    }

    private void intitializeToolBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("CSPL-DMS");
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
        llBusLogin = findViewById(R.id.llBusLogin);
        llBusLogout = findViewById(R.id.llBusLogout);
        llFuelEntry = findViewById(R.id.llFuelEntry);
        llAddBreakdown = findViewById(R.id.llAddBreakdown);
        llDailyJobCardEntry = findViewById(R.id.llDailyJobCardEntry);
        llInwardEntry = findViewById(R.id.llInwardEntry);
        llOutwardEntry = findViewById(R.id.llOutwardEntry);
        llResignStaffMember = findViewById(R.id.llResignStaffMember);
        llReports = findViewById(R.id.llReports);
        llLogout = findViewById(R.id.llLogout);
        llStaffAssignKit = findViewById(R.id.llStaffAssignKit);
        llStaffFine = findViewById(R.id.llStaffFine);

        llBusLogin.setOnClickListener(this);
        llBusLogout.setOnClickListener(this);
        llFuelEntry.setOnClickListener(this);
        llAddBreakdown.setOnClickListener(this);
        llDailyJobCardEntry.setOnClickListener(this);
        llInwardEntry.setOnClickListener(this);
        llOutwardEntry.setOnClickListener(this);
        llResignStaffMember.setOnClickListener(this);
        llReports.setOnClickListener(this);
        llLogout.setOnClickListener(this);
        llStaffAssignKit.setOnClickListener(this);
        llStaffFine.setOnClickListener(this);
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llBusLogin:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(SuperVisor_Dashboard.this, BusLoginOPActivity.class));
//                    finish();
                }
                break;
            case R.id.llBusLogout:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(SuperVisor_Dashboard.this, BusLogoutListOPActivity.class));
//                    finish();
                }
                break;
            case R.id.llFuelEntry:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(SuperVisor_Dashboard.this, FuelEntryOPActivity.class));
//                    finish();
                }
                break;
            case R.id.llAddBreakdown:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(SuperVisor_Dashboard.this, StoreAddBreakdownEntryActivity.class));
//                    finish();
                }
                break;
            case R.id.llDailyJobCardEntry:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(SuperVisor_Dashboard.this, StoreAddJobEntryActivity.class));
//                    finish();
                }
                break;
            case R.id.llInwardEntry:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(SuperVisor_Dashboard.this, Stock_InwardEntryActivity.class));
//                    finish();
                }
                break;
            case R.id.llOutwardEntry:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(SuperVisor_Dashboard.this, Stock_OutwardEntryActivity.class));
//                    finish();
                }
                break;
            case R.id.llResignStaffMember:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(SuperVisor_Dashboard.this, ResignStaffMemberActivity.class));
//                    finish();
                }
                break;
            case R.id.llReports:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(SuperVisor_Dashboard.this, Admin_ReportListActivity.class));
//                    finish();
                }
                break;
            case R.id.llStaffAssignKit:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(SuperVisor_Dashboard.this, StaffAssignKitEntryActivity.class));
//                    finish();
                }
                break;
            case R.id.llStaffFine:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(SuperVisor_Dashboard.this, Staff_Offence_Entry_Activity.class));
//                    finish();
                }
                break;
            case R.id.llLogout:
                sessionManager.clearSession();
                startActivity(new Intent(SuperVisor_Dashboard.this, LoginActivity.class));
                finish();
                break;
        }
    }*/


    @Override
    public void onClick(View view) {
        int id = view.getId();
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected) {
            Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            return;
        }

        if (id == R.id.llBusLogin) {
            startActivity(new Intent(SuperVisor_Dashboard.this, BusLoginOPActivity.class));
        } else if (id == R.id.llBusLogout) {
            startActivity(new Intent(SuperVisor_Dashboard.this, BusLogoutListOPActivity.class));
        } else if (id == R.id.llFuelEntry) {
            startActivity(new Intent(SuperVisor_Dashboard.this, FuelEntryOPActivity.class));
        } else if (id == R.id.llAddBreakdown) {
            startActivity(new Intent(SuperVisor_Dashboard.this, StoreAddBreakdownEntryActivity.class));
        } else if (id == R.id.llDailyJobCardEntry) {
            startActivity(new Intent(SuperVisor_Dashboard.this, StoreAddJobEntryActivity.class));
        } else if (id == R.id.llInwardEntry) {
            startActivity(new Intent(SuperVisor_Dashboard.this, Stock_InwardEntryActivity.class));
        } else if (id == R.id.llOutwardEntry) {
            startActivity(new Intent(SuperVisor_Dashboard.this, Stock_OutwardEntryActivity.class));
        } else if (id == R.id.llResignStaffMember) {
            startActivity(new Intent(SuperVisor_Dashboard.this, ResignStaffMemberActivity.class));
        } else if (id == R.id.llReports) {
            startActivity(new Intent(SuperVisor_Dashboard.this, Admin_ReportListActivity.class));
        } else if (id == R.id.llStaffAssignKit) {
            startActivity(new Intent(SuperVisor_Dashboard.this, StaffAssignKitEntryActivity.class));
        } else if (id == R.id.llStaffFine) {
            startActivity(new Intent(SuperVisor_Dashboard.this, Staff_Offence_Entry_Activity.class));
        } else if (id == R.id.llLogout) {
            sessionManager.clearSession();
            startActivity(new Intent(SuperVisor_Dashboard.this, LoginActivity.class));
            finish();
        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}
