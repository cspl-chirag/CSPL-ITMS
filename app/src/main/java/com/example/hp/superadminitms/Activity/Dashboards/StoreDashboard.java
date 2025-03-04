package com.example.hp.superadminitms.Activity.Dashboards;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.hp.superadminitms.Activity.LoginActivity;
import com.example.hp.superadminitms.Activity.StoreAddBreakdownEntryActivity;
import com.example.hp.superadminitms.Activity.StoreAddJobEntryActivity;
import com.example.hp.superadminitms.Activity.StoreBreakdownListActiviy;
import com.example.hp.superadminitms.Activity.StoreJobListActivity;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Service.BaseActivity;
import com.google.android.material.navigation.NavigationView;

public class StoreDashboard extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {
    LinearLayout llAddBreakdown, llBreakdownList, llJobCardEntry, llJobCardList;
    private boolean isConnected;
    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialeControls();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initialeControls() {
        sessionManager = new SessionManager(StoreDashboard.this);
        llJobCardEntry = findViewById(R.id.llDailyJobCardEntry);
        llJobCardList = findViewById(R.id.llDailyJobCardList);
        llAddBreakdown = findViewById(R.id.llAddBreakdown);
        llBreakdownList = findViewById(R.id.llBrekdownList);
        llJobCardEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(StoreDashboard.this, StoreAddJobEntryActivity.class));
                }
            }
        });
        llJobCardList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(StoreDashboard.this, StoreJobListActivity.class));
                }
            }
        });
        llAddBreakdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(StoreDashboard.this, StoreAddBreakdownEntryActivity.class));
                }
            }
        });
        llBreakdownList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(StoreDashboard.this, StoreBreakdownListActiviy.class));
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            sessionManager.clearSession();
            startActivity(new Intent(StoreDashboard.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}
