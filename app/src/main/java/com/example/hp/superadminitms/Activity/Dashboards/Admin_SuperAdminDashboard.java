package com.example.hp.superadminitms.Activity.Dashboards;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Activity.LoginActivity;
import com.example.hp.superadminitms.Activity.Menus.Admin_ReportListActivity;
import com.example.hp.superadminitms.Adapter.ViewPagerAdapter;
import com.example.hp.superadminitms.Fragment.DashboardSAFragment;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.CompanyDatum;
import com.example.hp.superadminitms.Network.CompanyResponse;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
//import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_SuperAdminDashboard extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private TabLayout tlDashboard;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private boolean isConnected;
    private SessionManager sessionManager;
    private List<CompanyDatum> companyDatum = new ArrayList<>();
    private CountDownTimer mTimer1;
    private ProgressDialog pg;
    private final Boolean IsFirstTime = true;
    private String manufacturer;
    private int userType, companyId;

    private NavigationView navigationView;
    private boolean notifiaction_status = true;
    private MenuItem notificaationMenu;
    private final String topic = "DMS-ITMS";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_dashboard);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(R.string.app_name);
//        initializeFirebaseService();
        initializeControls();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void initializeControls() {
        sessionManager = new SessionManager(this);
        userType = sessionManager.getUserType();
        companyId = sessionManager.getKeyCompanyId();
        notifiaction_status = sessionManager.getKeyNotificationStatus();
        pg = new ProgressDialog(Admin_SuperAdminDashboard.this, R.style.DialogBox);
        pg.setMessage("Getting Dashboard Data...");
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        pg.show();

        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.drawer_layout), getResources().getString(R.string.NetworkErrorMsg));
        } else {

            sessionManager = new SessionManager(Admin_SuperAdminDashboard.this);
            tlDashboard = findViewById(R.id.tlDashboard);
            viewPager = findViewById(R.id.vpDashboard);
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<CompanyResponse> call = apiInterface.getCompanyList();
            call.enqueue(new Callback<CompanyResponse>() {
                @Override
                public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                companyDatum = response.body().getCompanyData();
                                Log.d("ComapnyCntResponse:", response.body().getMessage());

                                ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
                                if (userType == 2) {
                                    for (int i = 0; i < companyDatum.size(); i++) {
                                        if (companyId == companyDatum.get(i).getCompanyID()) {
                                            Bundle b = new Bundle();
                                            b.putInt("ID", companyDatum.get(i).getCompanyID());
                                            DashboardSAFragment fr1 = new DashboardSAFragment();
                                            fr1.setArguments(b);
                                            adapter.addFragment(fr1, companyDatum.get(i).getCompanyCity());
                                        }
                                    }
                                } else if (userType == 1) {
                                    for (int i = 0; i < companyDatum.size(); i++) {
                                        Bundle b = new Bundle();
                                        b.putInt("ID", companyDatum.get(i).getCompanyID());
                                        DashboardSAFragment fr1 = new DashboardSAFragment();
                                        fr1.setArguments(b);
                                        adapter.addFragment(fr1, companyDatum.get(i).getCompanyCity());
                                    }
                                }
//                                    for (int i = 0; i < companyDatum.size(); i++) {
//                                        Bundle b = new Bundle();
//                                        b.putInt("ID", companyDatum.get(i).getCompanyID());
//                                        DashboardSAFragment fr1 = new DashboardSAFragment();
//                                        fr1.setArguments(b);
//                                        adapter.addFragment(fr1, companyDatum.get(i).getCompanyCity());
//                                    }
                                viewPager.setAdapter(adapter);
                                tlDashboard.setupWithViewPager(viewPager);
                                tlDashboard.setClickable(true);
                                viewPager.setOffscreenPageLimit(companyDatum.size());
                                tlDashboard.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                    @Override
                                    public void onTabSelected(TabLayout.Tab tab) {
                                        viewPager.setCurrentItem(tab.getPosition());
                                    }

                                    @Override
                                    public void onTabUnselected(TabLayout.Tab tab) {

                                    }

                                    @Override
                                    public void onTabReselected(TabLayout.Tab tab) {

                                    }
                                });
                                viewPager.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        viewPager.setCurrentItem(0);
                                    }
                                });
                            } else {
                                Log.d("CountError: ", response.body().getMessage());

                                pg.dismiss();

                            }
                        } else {
                            Log.d("CountError: ", response.body().getMessage());

                            pg.dismiss();

                        }
                    } catch (Exception e) {
                        Log.d("CountException:", e.getMessage());

                        pg.dismiss();

                    }
                }

                @Override
                public void onFailure(Call<CompanyResponse> call, Throwable t) {
                    Log.d("CountFailure:", t.getMessage());

                    pg.dismiss();


                    final AlertDialog.Builder builder = new AlertDialog.Builder(Admin_SuperAdminDashboard.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(R.string.NetworkErrorBtnTxt, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            initializeControls();
                            builder.show();
                        }
                    });
                }
            });
        }
    }


    /*private void initializeFirebaseService(){
        if(notifiaction_status){
            FirebaseMessaging.getInstance().subscribeToTopic(topic)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            String msg = "Success";
                            if (!task.isSuccessful()) {
                                msg = "Failed";
                            }
                            Log.d("DebOnCreate", msg);
                        }
                    });
        }
    }*/

    /*public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_Reports:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(this.findViewById(R.id.drawer_layout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    startActivity(new Intent(Admin_SuperAdminDashboard.this, Admin_ReportListActivity.class));
                }
                break;
            case R.id.nav_logout:
                sessionManager.clearSession();
                startActivity(new Intent(Admin_SuperAdminDashboard.this, LoginActivity.class));
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }*/


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Reports) {
            isConnected = ConnectivityReceiver.isConnected();
            if (!isConnected) {
                Common.showSnack(this.findViewById(R.id.drawer_layout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                startActivity(new Intent(Admin_SuperAdminDashboard.this, Admin_ReportListActivity.class));
            }
        } else if (id == R.id.nav_logout) {
            sessionManager.clearSession();
            startActivity(new Intent(Admin_SuperAdminDashboard.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_notification_status, menu);
        notificaationMenu = menu.findItem(R.id.notification);
        notificaationMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (notifiaction_status) {
                    notifiaction_status = false;
                    sessionManager.setKeyNotificationStatus(notifiaction_status);
                    menu.findItem(R.id.notification).setIcon(R.drawable.ic_alarm_disabled);
                    //FirebaseMessaging.getInstance().unsubscribeFromTopic(topic);
                    Toast.makeText(getApplicationContext(), "NotifiactionStatus : Off", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    notifiaction_status = true;
//                    initializeFirebaseService();
                    sessionManager.setKeyNotificationStatus(notifiaction_status);
                    menu.findItem(R.id.notification).setIcon(R.drawable.ic_alarm_enabled);
                    Toast.makeText(getApplicationContext(), "NotifiactionStatus : On" + notifiaction_status, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.drawer_layout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}

