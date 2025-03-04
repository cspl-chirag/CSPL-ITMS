package com.example.hp.superadminitms.Activity.Menus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Activity.RouteWiseBusReportActivity;
import com.example.hp.superadminitms.Activity.RouteWiseLoginLogoutReportActivity;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Service.BaseActivity;

public class RouteWiseReportsActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private LinearLayout llRouteWiseLoginLogoutReport, llRoutewiseBusInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_wise_reports);
        initializeToolBar();
        initialzeControls();
    }

    private void initialzeControls() {
        llRouteWiseLoginLogoutReport = findViewById(R.id.llRouteWiseLoginLogoutReport);
        llRoutewiseBusInformation = findViewById(R.id.llRoutewiseBusInformation);
        llRoutewiseBusInformation.setOnClickListener(this);
        llRouteWiseLoginLogoutReport.setOnClickListener(this);
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
        getSupportActionBar().setTitle("View Routewise Bus Information");
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
        switch (view.getId()) {
            case R.id.llRoutewiseBusInformation:
                startActivity(new Intent(RouteWiseReportsActivity.this,RouteWiseBusReportActivity.class));
                break;
            case R.id.llRouteWiseLoginLogoutReport:
                startActivity(new Intent(RouteWiseReportsActivity.this,RouteWiseLoginLogoutReportActivity.class));
                break;
        }
    }*/

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.llRoutewiseBusInformation) {
            startActivity(new Intent(RouteWiseReportsActivity.this, RouteWiseBusReportActivity.class));
        } else if (id == R.id.llRouteWiseLoginLogoutReport) {
            startActivity(new Intent(RouteWiseReportsActivity.this, RouteWiseLoginLogoutReportActivity.class));
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
