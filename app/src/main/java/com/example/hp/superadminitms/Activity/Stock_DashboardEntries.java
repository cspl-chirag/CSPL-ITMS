package com.example.hp.superadminitms.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Service.BaseActivity;


public class Stock_DashboardEntries extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    LinearLayout llInwardEntry, llOutwardEntry, llInwardEntries, llOutwardEntries;
    private boolean isConnected;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock__dashboard_entries);
        initializeToolBar();
        initializeControls();
    }

    private void initializeControls() {
        sessionManager = new SessionManager(Stock_DashboardEntries.this);
        llInwardEntry = findViewById(R.id.llInwardEntry);
        llOutwardEntry = findViewById(R.id.llOutwardEntry);
        llInwardEntries = findViewById(R.id.llInwardEntries);
        llOutwardEntries = findViewById(R.id.llOutwardEntries);
        llInwardEntry.setOnClickListener(this);
        llOutwardEntry.setOnClickListener(this);
        llOutwardEntries.setOnClickListener(this);
        llInwardEntries.setOnClickListener(this);
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
        getSupportActionBar().setTitle("inventory Management");
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.llInwardEntry:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Stock_DashboardEntries.this,Stock_InwardEntryActivity.class));
                }
                break;
            case R.id.llOutwardEntry:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Stock_DashboardEntries.this,Stock_OutwardEntryActivity.class));
                }
                break;
            case R.id.llInwardEntries:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Stock_DashboardEntries.this,StockInawardListActivity.class));
                }
                break;
            case R.id.llOutwardEntries:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnack(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(Stock_DashboardEntries.this,StockOutwardListActivity.class));
                }
                break;
        }
    }*/

    @Override
    public void onClick(View view) {
        int id = view.getId();
        isConnected = ConnectivityReceiver.isConnected();

        if (id == R.id.llInwardEntry) {
            if (!isConnected) {
                Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                startActivity(new Intent(Stock_DashboardEntries.this, Stock_InwardEntryActivity.class));
            }
        } else if (id == R.id.llOutwardEntry) {
            if (!isConnected) {
                Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                startActivity(new Intent(Stock_DashboardEntries.this, Stock_OutwardEntryActivity.class));
            }
        } else if (id == R.id.llInwardEntries) {
            if (!isConnected) {
                Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                startActivity(new Intent(Stock_DashboardEntries.this, StockInawardListActivity.class));
            }
        } else if (id == R.id.llOutwardEntries) {
            if (!isConnected) {
                Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                startActivity(new Intent(Stock_DashboardEntries.this, StockOutwardListActivity.class));
            }
        }
    }

}
