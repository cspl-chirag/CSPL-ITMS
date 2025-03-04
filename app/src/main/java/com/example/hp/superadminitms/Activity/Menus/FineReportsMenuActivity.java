package com.example.hp.superadminitms.Activity.Menus;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Activity.Report_StaffwiseFineHistoryActivity;
import com.example.hp.superadminitms.Activity.Report_StaffwiseFineReportActivity;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Service.BaseActivity;

public class FineReportsMenuActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private LinearLayout llStaffWiseOffenceReportk, llStaffWiseOffenceHistory;
    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fine_reports_menu);
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
        getSupportActionBar().setTitle("Fine Reports");
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
        llStaffWiseOffenceReportk = findViewById(R.id.llStaffWiseOffenceReportk);
        llStaffWiseOffenceHistory = findViewById(R.id.llStaffWiseOffenceHistory);

        llStaffWiseOffenceReportk.setOnClickListener(this);
        llStaffWiseOffenceHistory.setOnClickListener(this);

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnackError(findViewById(R.id.rootlayout), getString(R.string.NetworkErrorMsg));
        }
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.llStaffWiseOffenceReportk:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnackError(findViewById(R.id.rootlayout),getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(FineReportsMenuActivity.this,Report_StaffwiseFineReportActivity.class));
                }
                break;
            case R.id.llStaffWiseOffenceHistory:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnackError(findViewById(R.id.rootlayout),getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    startActivity(new Intent(FineReportsMenuActivity.this,Report_StaffwiseFineHistoryActivity.class));
                }
                break;
        }
    }*/

    @Override
    public void onClick(View view) {
        int id = view.getId();
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected) {
            Common.showSnackError(findViewById(R.id.rootlayout), getString(R.string.NetworkErrorMsg));
            return;
        }

        if (id == R.id.llStaffWiseOffenceReportk) {
            startActivity(new Intent(FineReportsMenuActivity.this, Report_StaffwiseFineReportActivity.class));
        } else if (id == R.id.llStaffWiseOffenceHistory) {
            startActivity(new Intent(FineReportsMenuActivity.this, Report_StaffwiseFineHistoryActivity.class));
        }
    }

}
