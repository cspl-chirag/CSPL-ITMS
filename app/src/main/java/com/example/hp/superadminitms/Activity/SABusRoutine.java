package com.example.hp.superadminitms.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.hp.superadminitms.Adapter.ViewPagerAdapter;
import com.example.hp.superadminitms.Fragment.BusRoutineSAFragment;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.CompanyDatum;
import com.example.hp.superadminitms.Network.CompanyResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SABusRoutine extends BaseActivity {


    //private List<RoutineDataY> yesterdayRotine = new ArrayList<>();
    private TabLayout tlDashboard;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private boolean isConnected;
    private SessionManager sessionManager;
    private List<CompanyDatum> companyDatum = new ArrayList<>();
    private int userType, companyId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sa_busroutine);
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
        getSupportActionBar().setTitle("View Routine Information");
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
        sessionManager = new SessionManager(SABusRoutine.this);
        userType = sessionManager.getUserType();
        companyId = sessionManager.getKeyCompanyId();
        tlDashboard = findViewById(R.id.tlBusRoutine);
        viewPager = findViewById(R.id.vpBusRoutine);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<CompanyResponse> call = apiInterface.getCompanyList();
        call.enqueue(new Callback<CompanyResponse>() {
            @Override
            public void onResponse(Call<CompanyResponse> call, Response<CompanyResponse> response) {
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
                                        b.putInt("COMPANY_ID", companyDatum.get(i).getCompanyID());
                                        BusRoutineSAFragment fr1 = new BusRoutineSAFragment();
                                        fr1.setArguments(b);
                                        adapter.addFragment(fr1, companyDatum.get(i).getCompanyCity());
                                    }
                                }
                            } else if (userType == 1) {
                                for (int i = 0; i < companyDatum.size(); i++) {
                                    Bundle b = new Bundle();
                                    b.putInt("COMPANY_ID", companyDatum.get(i).getCompanyID());
                                    BusRoutineSAFragment fr1 = new BusRoutineSAFragment();
                                    fr1.setArguments(b);
                                    adapter.addFragment(fr1, companyDatum.get(i).getCompanyCity());
                                }
                            }
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
                        }
                    } else {
                        Log.d("CountError: ", response.body().getMessage());
                    }
                } catch (Exception e) {
                    Log.d("CountException:", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<CompanyResponse> call, Throwable t) {
                Log.d("CountFailure:", t.getMessage());
                final AlertDialog.Builder builder = new AlertDialog.Builder(SABusRoutine.this, R.style.DialogBox);
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
