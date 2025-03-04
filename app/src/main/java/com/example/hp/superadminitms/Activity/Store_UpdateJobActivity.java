package com.example.hp.superadminitms.Activity;

import static com.example.hp.superadminitms.Helper.Common.convertDateFormat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.example.hp.superadminitms.Adapter.ViewPagerAdapter;
import com.example.hp.superadminitms.Fragment.StorePartReplacementFragment;
import com.example.hp.superadminitms.Fragment.StoreUpdateJobFragment;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.CompanyDatum;
import com.example.hp.superadminitms.Model.JobCardDatum;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Service.BaseActivity;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class Store_UpdateJobActivity extends BaseActivity {
    private static final String BUNDLE_VEHICLE_REG_NO = "vehicle_reg_no";
    private static final String BUNDLE_PROBLEM = "problem";
    private static final String BUNDLE_JOB_ID = "job_id";
    private static final String BUNDLE_VEHICLE_ID = "vehicle_id";
    private static final String BUNDLE_ROUTE_NO = "route_no";
    private static final String BUNDLE_DRIVER_NAME = "driver_name";
    private static final String BUNDLE_BRAKES = "brakes";
    private static final String BUNDLE_CLUTCH = "clutch";
    private static final String BUNDLE_ACCELERATOR = "accelerator";
    private static final String BUNDLE_HEADLIGHT = "headlight";
    private static final String BUNDLE_SIDELIGHT = "sidelight";
    private static final String BUNDLE_STEERING = "steering";
    private static final String BUNDLE_OTHER_PROBLEM = "other_problem";
    private static final String BUNDLE_JOB_DATE = "problem_date";
    private static String vehicle_reg_No;
    private static String Problem;
    private static Integer Job_ID;
    private static Integer Vehicle_ID;
    private static String RouteNo;
    private static String driverName;
    private static Boolean Breaks;
    private static Boolean Clutch;
    private static Boolean Accelerator;
    private static Boolean HeadLight;
    private static Boolean SideLight;
    private static Boolean Steering;
    private static Boolean OtherProblem;
    private static String ProblemDate;
    private static final String EXTRA_MENU = "1";
    private TabLayout tlDashboard;
    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private boolean isConnected;
    private SessionManager sessionManager;
    private final List<CompanyDatum> companyDatum = new ArrayList<>();
    private int userType, companyId;

    public static Intent newIntent(Context c, JobCardDatum jobCardDatum) {
        vehicle_reg_No = jobCardDatum.getVehicleCode() + " - " + jobCardDatum.getVehicleRegNo();
        Log.d("IDIDIDI::", vehicle_reg_No);
        Problem = jobCardDatum.getProblemDesc();
        Job_ID = jobCardDatum.getJobCardID();
        Vehicle_ID = jobCardDatum.getVehicleID();
        RouteNo = jobCardDatum.getRouteNo();
        driverName = String.valueOf(jobCardDatum.getDriverName());
        Breaks = jobCardDatum.getBreaks();
        Clutch = jobCardDatum.getClutch();
        Accelerator = jobCardDatum.getAccelerator();
        HeadLight = jobCardDatum.getHeadLight();
        SideLight = jobCardDatum.getSideLight();
        Steering = jobCardDatum.getSteering();
        OtherProblem = jobCardDatum.getOtherProblem();

        try {
            ProblemDate = convertDateFormat(jobCardDatum.getJobDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(c, Store_UpdateJobActivity.class);
        intent.putExtra(EXTRA_MENU, jobCardDatum);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_store_job);
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
        getSupportActionBar().setTitle("Completing Job");
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
        sessionManager = new SessionManager(Store_UpdateJobActivity.this);
        userType = sessionManager.getUserType();
        companyId = sessionManager.getKeyCompanyId();
        tlDashboard = findViewById(R.id.tlUpdateJob);
        viewPager = findViewById(R.id.vpUpdateJob);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        StoreUpdateJobFragment fr1 = new StoreUpdateJobFragment();
        Bundle jobInfo = new Bundle();
        jobInfo.putString(BUNDLE_VEHICLE_REG_NO, vehicle_reg_No);
        jobInfo.putString(BUNDLE_PROBLEM, Problem);
        jobInfo.putInt(BUNDLE_JOB_ID, Job_ID);
        jobInfo.putInt(BUNDLE_VEHICLE_ID, Vehicle_ID);
        jobInfo.putString(BUNDLE_ROUTE_NO, RouteNo);
        jobInfo.putString(BUNDLE_DRIVER_NAME, driverName);
        jobInfo.putBoolean(BUNDLE_BRAKES, Breaks);
        jobInfo.putBoolean(BUNDLE_CLUTCH, Clutch);
        jobInfo.putBoolean(BUNDLE_ACCELERATOR, Accelerator);
        jobInfo.putBoolean(BUNDLE_HEADLIGHT, HeadLight);
        jobInfo.putBoolean(BUNDLE_SIDELIGHT, SideLight);
        jobInfo.putBoolean(BUNDLE_STEERING, Steering);
        jobInfo.putBoolean(BUNDLE_OTHER_PROBLEM, OtherProblem);
        jobInfo.putString(BUNDLE_JOB_DATE, ProblemDate);
        fr1.setArguments(jobInfo);

        StorePartReplacementFragment fr2 = new StorePartReplacementFragment();
        Bundle partsInfo = new Bundle();
        partsInfo.putString(BUNDLE_VEHICLE_REG_NO, vehicle_reg_No);
        partsInfo.putInt(BUNDLE_JOB_ID, Job_ID);
        partsInfo.putInt(BUNDLE_VEHICLE_ID, Vehicle_ID);
        fr2.setArguments(partsInfo);

        adapter.addFragment(fr1, "Update Job");
        adapter.addFragment(fr2, "If Part Replaced");
        viewPager.setAdapter(adapter);
        tlDashboard.setupWithViewPager(viewPager);
    }

}
