package com.example.hp.superadminitms.Activity.Menus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Activity.ReportStaffwiseKitAssignActivity;
import com.example.hp.superadminitms.Activity.ReportStaffwiseKitAssignHistoryActivity;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Service.BaseActivity;

public class Admin_StaffAsigningKitReports extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private LinearLayout llStaffwiseKitAssignReport;
    private LinearLayout llStaffwiseKitAssignHistory;
    private boolean isConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__staff_joining_kit_details);
        initializeToolBar();
        initializeControls();
    }

    private void initializeControls() {
        llStaffwiseKitAssignReport = findViewById(R.id.llStaffwiseKitAssignReport);
        llStaffwiseKitAssignHistory = findViewById(R.id.llStaffwiseKitAssignHistory);

        llStaffwiseKitAssignReport.setOnClickListener(this);
        llStaffwiseKitAssignHistory.setOnClickListener(this);
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
        getSupportActionBar().setTitle("Staff Joining Kit Details");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
            }
        }
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.llStaffwiseKitAssignReport:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_StaffAsigningKitReports.this,ReportStaffwiseKitAssignActivity.class));
                }
                break;
            case R.id.llStaffwiseKitAssignHistory:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_StaffAsigningKitReports.this,ReportStaffwiseKitAssignHistoryActivity.class));
                }
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

        if (id == R.id.llStaffwiseKitAssignReport) {
            startActivity(new Intent(Admin_StaffAsigningKitReports.this, ReportStaffwiseKitAssignActivity.class));
        } else if (id == R.id.llStaffwiseKitAssignHistory) {
            startActivity(new Intent(Admin_StaffAsigningKitReports.this, ReportStaffwiseKitAssignHistoryActivity.class));
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
