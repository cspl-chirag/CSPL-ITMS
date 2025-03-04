package com.example.hp.superadminitms.Activity.Menus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Activity.Admin_BusOnRoadActivity;
import com.example.hp.superadminitms.Activity.DailySummaryInfoActivity;
import com.example.hp.superadminitms.Activity.ReportBusLoginLogoutActivity;
import com.example.hp.superadminitms.Activity.ReportBusWiseAverageActivity;
import com.example.hp.superadminitms.Activity.ReportFuelActivity;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Service.BaseActivity;

public class Admin_VehicleInfoActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private LinearLayout llBusOnRoad, llLoginLogoutReport, llFuelReport, llBuswiseAverageReport, llDailyBusSummary;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__route_wise_vehicle_info);
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
        getSupportActionBar().setTitle("View Buswise Information");
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
        llBusOnRoad = findViewById(R.id.llBusOnRoad);
        llLoginLogoutReport = findViewById(R.id.llLoginLogoutReport);
        llFuelReport = findViewById(R.id.llFuelReport);
        llBuswiseAverageReport = findViewById(R.id.llBuswiseAverageReport);
        llDailyBusSummary = findViewById(R.id.llDailyBusSummary);

        llBusOnRoad.setOnClickListener(this);
        llLoginLogoutReport.setOnClickListener(this);
        llFuelReport.setOnClickListener(this);
        llBuswiseAverageReport.setOnClickListener(this);
        llDailyBusSummary.setOnClickListener(this);
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
            case R.id.llBusOnRoad:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_VehicleInfoActivity.this,Admin_BusOnRoadActivity.class));
                }
                break;
            case R.id.llLoginLogoutReport:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_VehicleInfoActivity.this,ReportBusLoginLogoutActivity.class));
                }
                break;
            case R.id.llFuelReport:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_VehicleInfoActivity.this,ReportFuelActivity.class));
                }
                break;
            case R.id.llBuswiseAverageReport:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_VehicleInfoActivity.this,ReportBusWiseAverageActivity.class));
                }
                break;
            case R.id.llDailyBusSummary:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Admin_VehicleInfoActivity.this,DailySummaryInfoActivity.class));
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

        if (id == R.id.llBusOnRoad) {
            startActivity(new Intent(Admin_VehicleInfoActivity.this, Admin_BusOnRoadActivity.class));
        } else if (id == R.id.llLoginLogoutReport) {
            startActivity(new Intent(Admin_VehicleInfoActivity.this, ReportBusLoginLogoutActivity.class));
        } else if (id == R.id.llFuelReport) {
            startActivity(new Intent(Admin_VehicleInfoActivity.this, ReportFuelActivity.class));
        } else if (id == R.id.llBuswiseAverageReport) {
            startActivity(new Intent(Admin_VehicleInfoActivity.this, ReportBusWiseAverageActivity.class));
        } else if (id == R.id.llDailyBusSummary) {
            startActivity(new Intent(Admin_VehicleInfoActivity.this, DailySummaryInfoActivity.class));
        }
    }

}
