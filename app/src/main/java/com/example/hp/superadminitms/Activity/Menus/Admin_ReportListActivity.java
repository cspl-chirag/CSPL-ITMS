package com.example.hp.superadminitms.Activity.Menus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Activity.StoreBreakdownListActiviy;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Service.BaseActivity;

public class Admin_ReportListActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private LinearLayout llRouteWiseBusInfo, llBusInfo, llBreakdownList, llDailyBusMaintanance, llInventory, llStaff, llRtoRelatedInfo, llVehicleMaintananceHistory, llAccidentReports, llFineReport;
    private boolean isConnected;
    private SessionManager sessionManager;
    private int User_Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__report_list);
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
        getSupportActionBar().setTitle("Reports");
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
        User_Type = sessionManager.getUserType();

        llRouteWiseBusInfo = findViewById(R.id.llRouteWiseBusInfo);
        llBreakdownList = findViewById(R.id.llBreakdownList);
        llBusInfo = findViewById(R.id.llBusInfo);
        llDailyBusMaintanance = findViewById(R.id.llDailyBusMaintanance);
        llInventory = findViewById(R.id.llInventory);
        llRtoRelatedInfo = findViewById(R.id.llNotification);
        llStaff = findViewById(R.id.llStaff);
        llVehicleMaintananceHistory = findViewById(R.id.llVehicleMaintananceHistory);
        llAccidentReports = findViewById(R.id.llAccidentReports);
        llFineReport = findViewById(R.id.llFineReport);

        llRouteWiseBusInfo.setOnClickListener(this);
        llBreakdownList.setOnClickListener(this);
        llBusInfo.setOnClickListener(this);
        llDailyBusMaintanance.setOnClickListener(this);
        llInventory.setOnClickListener(this);
        llRtoRelatedInfo.setOnClickListener(this);
        llStaff.setOnClickListener(this);
        llVehicleMaintananceHistory.setOnClickListener(this);
        llAccidentReports.setOnClickListener(this);
        llFineReport.setOnClickListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

   /* @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.llRouteWiseBusInfo:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_ReportListActivity.this,RouteWiseReportsActivity.class));
                }
                break;
            case R.id.llBusInfo:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_ReportListActivity.this,Admin_VehicleInfoActivity.class));
                }
                break;
            case R.id.llBreakdownList:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_ReportListActivity.this,StoreBreakdownListActiviy.class));
                }
                break;
            case R.id.llDailyBusMaintanance:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_ReportListActivity.this,MaintananceListActivity.class));
                }
                break;
            case R.id.llInventory:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_ReportListActivity.this,Stock_ReportsMenuActivity.class));
                }
                break;
            case R.id.llStaff:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_ReportListActivity.this,Admin_StaffDetailsActivity.class));
                }
                break;
            case R.id.llNotification:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_ReportListActivity.this,RTORelatedInformation.class));
                }
                break;
            case R.id.llVehicleMaintananceHistory:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_ReportListActivity.this,VehicleMaintananceHistoryActivity.class));
                }
                break;

            case R.id.llAccidentReports:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_ReportListActivity.this,AccidentReportsActivity.class));
                }
                break;
            case R.id.llFineReport:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_ReportListActivity.this,FineReportsMenuActivity.class));
                }
                break;
        }
    }*/


    @Override
    public void onClick(View view) {
        int id = view.getId();
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            return;
        }

        if (id == R.id.llRouteWiseBusInfo) {
            startActivity(new Intent(Admin_ReportListActivity.this, RouteWiseReportsActivity.class));
        } else if (id == R.id.llBusInfo) {
            startActivity(new Intent(Admin_ReportListActivity.this, Admin_VehicleInfoActivity.class));
        } else if (id == R.id.llBreakdownList) {
            startActivity(new Intent(Admin_ReportListActivity.this, StoreBreakdownListActiviy.class));
        } else if (id == R.id.llDailyBusMaintanance) {
            startActivity(new Intent(Admin_ReportListActivity.this, MaintananceListActivity.class));
        } else if (id == R.id.llInventory) {
            startActivity(new Intent(Admin_ReportListActivity.this, Stock_ReportsMenuActivity.class));
        } else if (id == R.id.llStaff) {
            startActivity(new Intent(Admin_ReportListActivity.this, Admin_StaffDetailsActivity.class));
        } else if (id == R.id.llNotification) {
            startActivity(new Intent(Admin_ReportListActivity.this, RTORelatedInformation.class));
        } else if (id == R.id.llVehicleMaintananceHistory) {
            startActivity(new Intent(Admin_ReportListActivity.this, VehicleMaintananceHistoryActivity.class));
        } else if (id == R.id.llAccidentReports) {
            startActivity(new Intent(Admin_ReportListActivity.this, AccidentReportsActivity.class));
        } else if (id == R.id.llFineReport) {
            startActivity(new Intent(Admin_ReportListActivity.this, FineReportsMenuActivity.class));
        }
    }

}
