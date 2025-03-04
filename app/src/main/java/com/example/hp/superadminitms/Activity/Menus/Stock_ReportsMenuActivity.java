package com.example.hp.superadminitms.Activity.Menus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Activity.Admin_AvailabelStockActivity;
import com.example.hp.superadminitms.Activity.Admin_InwardReportActivity;
import com.example.hp.superadminitms.Activity.Admin_OutwardReportActivity;
import com.example.hp.superadminitms.Activity.Admin_PartReplacementReport;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Service.BaseActivity;

public class Stock_ReportsMenuActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private LinearLayout llStaffJoiningKitDetails, llPartReplacementReport, llOutwardEntryReport, llInwardEntryReport, llCurrentylAvailStock;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__inventory_menu);
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
        getSupportActionBar().setTitle("Stock Movement Reports");
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
        llInwardEntryReport = findViewById(R.id.llInwardEntryReport);
        llOutwardEntryReport = findViewById(R.id.llOutwardEntryReport);
        llPartReplacementReport = findViewById(R.id.llPartReplacementReport);
        llStaffJoiningKitDetails = findViewById(R.id.llStaffJoiningKitDetails);
        llCurrentylAvailStock = findViewById(R.id.llCurrentylAvailStock);
        llInwardEntryReport.setOnClickListener(this);
        llOutwardEntryReport.setOnClickListener(this);
        llStaffJoiningKitDetails.setOnClickListener(this);
        llPartReplacementReport.setOnClickListener(this);
        llCurrentylAvailStock.setOnClickListener(this);
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
            case R.id.llInwardEntryReport:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Stock_ReportsMenuActivity.this,Admin_InwardReportActivity.class));
                }
                break;
            case R.id.llOutwardEntryReport:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Stock_ReportsMenuActivity.this,Admin_OutwardReportActivity.class));
                }
                break;
            case R.id.llPartReplacementReport:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Stock_ReportsMenuActivity.this,Admin_PartReplacementReport.class));
                }
                break;
            case R.id.llCurrentylAvailStock:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Stock_ReportsMenuActivity.this,Admin_AvailabelStockActivity.class));
                }
                break;
            case R.id.llStaffJoiningKitDetails:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Stock_ReportsMenuActivity.this,Admin_StaffAsigningKitReports.class));
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

        if (id == R.id.llInwardEntryReport) {
            startActivity(new Intent(Stock_ReportsMenuActivity.this, Admin_InwardReportActivity.class));
        } else if (id == R.id.llOutwardEntryReport) {
            startActivity(new Intent(Stock_ReportsMenuActivity.this, Admin_OutwardReportActivity.class));
        } else if (id == R.id.llPartReplacementReport) {
            startActivity(new Intent(Stock_ReportsMenuActivity.this, Admin_PartReplacementReport.class));
        } else if (id == R.id.llCurrentylAvailStock) {
            startActivity(new Intent(Stock_ReportsMenuActivity.this, Admin_AvailabelStockActivity.class));
        } else if (id == R.id.llStaffJoiningKitDetails) {
            startActivity(new Intent(Stock_ReportsMenuActivity.this, Admin_StaffAsigningKitReports.class));
        }
    }

}
