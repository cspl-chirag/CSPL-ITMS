package com.example.hp.superadminitms.Activity.Menus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Activity.BusMaintananceHistoryActivity;
import com.example.hp.superadminitms.Activity.BuswisePartreplacedHistoryActivity;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Service.BaseActivity;

public class VehicleMaintananceHistoryActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private LinearLayout llBusMaintananceHistory, llBuswisePartreplacedHistory;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_maintanance_history);
        initializeToolBar();
        initializeControls();

    }

    private void initializeControls() {
        llBusMaintananceHistory = findViewById(R.id.llBusMaintananceHistory);
        llBuswisePartreplacedHistory = findViewById(R.id.llBuswisePartreplacedHistory);

        llBuswisePartreplacedHistory.setOnClickListener(this);
        llBusMaintananceHistory.setOnClickListener(this);
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
        getSupportActionBar().setTitle("Vehicle Maintanance History");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
            }
        }
    }

   /* @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.llBusMaintananceHistory:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnackError(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(VehicleMaintananceHistoryActivity.this,BusMaintananceHistoryActivity.class));
                }
                break;
            case R.id.llBuswisePartreplacedHistory:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnackError(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(VehicleMaintananceHistoryActivity.this,BuswisePartreplacedHistoryActivity.class));
                }
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

        if (id == R.id.llBusMaintananceHistory) {
            startActivity(new Intent(VehicleMaintananceHistoryActivity.this, BusMaintananceHistoryActivity.class));
        } else if (id == R.id.llBuswisePartreplacedHistory) {
            startActivity(new Intent(VehicleMaintananceHistoryActivity.this, BuswisePartreplacedHistoryActivity.class));
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
