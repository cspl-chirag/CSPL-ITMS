package com.example.hp.superadminitms.Activity.Menus;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.JobCardAdapter;
import com.example.hp.superadminitms.Adapter.ScheduleFirstServiceAdapter;
import com.example.hp.superadminitms.Adapter.ScheduleSecondServiceAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.FirstScheduleService;
import com.example.hp.superadminitms.Model.JobCardDatum;
import com.example.hp.superadminitms.Model.SecondScheduleService;
import com.example.hp.superadminitms.Network.BreakdownRequest;
import com.example.hp.superadminitms.Network.ExpirationResponse;
import com.example.hp.superadminitms.Network.JobCardDatumResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaintananceListActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private LinearLayout llDailyJobCardList, ll1stScheduleService, ll2ndScheduleService;
    private LinearLayout llDailyJobCardListRecyclerview, ll1stScheduleServiceRecyclerview, ll2ndScheduleServiceRecyclerview;
    private RecyclerView rvJobcardList, rv1stScheduService, rv2ndScheduService;
    private TextView tvJobcardDetails, tv1stService, tv2ndService;
    private boolean isConnected;
    private ProgressDialog pg;
    private SessionManager sessionManager;
    private int Company_Id, User_ID;
    private boolean jobCardClicked = false, b1stServiceClicked = false, b2ndServiceClicked = false;
    private List<JobCardDatum> jobcaedData = new ArrayList<>();
    private JobCardAdapter jobCardAdapter;
    private List<FirstScheduleService> datum1stScheduleService = new ArrayList<>();
    private List<SecondScheduleService> datum2ndScheduleService = new ArrayList<>();
    private ScheduleFirstServiceAdapter adapter1stScheduleService;
    private ScheduleSecondServiceAdapter adapter2ndScheduleService;
    private LinearLayout.LayoutParams params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintanance_list);
        initializeToolBar();
        initializeControls();
        gettingExpiryList();
    }

    private void gettingExpiryList() {
        pg.setMessage("Please Wait...");
        pg.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ExpirationResponse> call = apiInterface.getExpirationDetails();
        call.enqueue(new Callback<ExpirationResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ExpirationResponse> call, Response<ExpirationResponse> response) {
                pg.dismiss();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 1) {
                            Log.e("EXPIRATION LIST RESPONSE : ", response.body().getMessage());
                            datum1stScheduleService = response.body().getExpirationAlertData().getTable4();
                            datum2ndScheduleService = response.body().getExpirationAlertData().getTable5();
                        } else {
                            pg.dismiss();
                            Log.d("Expiration Response ERROR 1:", response.body().getMessage());
                            setControlsDisable();
                        }
                    } else {
                        pg.dismiss();
                        Log.d("Expiration Response ERROR 2:", response.errorBody().string());
                        setControlsDisable();
                    }
                } catch (Exception e) {
                    pg.dismiss();
                    Log.d("Expiration Response EXCEPTION:", e.getMessage());
                    setControlsDisable();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ExpirationResponse> call, Throwable t) {
                pg.dismiss();
                Log.d("Expiration Response Fail:", t.getMessage());
                setControlsDisable();
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MaintananceListActivity.this, R.style.DialogBox);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setJobListRecyclerView();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    private void initializeControls() {
        pg = Common.showProgressDialog(this);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        sessionManager = new SessionManager(this);
        User_ID = sessionManager.getKeyUserId();
        Company_Id = sessionManager.getKeyCompanyId();
        llDailyJobCardList = findViewById(R.id.llDailyJobCardList);
        ll1stScheduleService = findViewById(R.id.ll1stScheduleService);
        ll2ndScheduleService = findViewById(R.id.ll2ndScheduleService);
        llDailyJobCardListRecyclerview = findViewById(R.id.llDailyJobCardListRecyclerview);
        ll1stScheduleServiceRecyclerview = findViewById(R.id.ll1stScheduleServiceRecyclerview);
        ll2ndScheduleServiceRecyclerview = findViewById(R.id.ll2ndScheduleServiceRecyclerview);
        rvJobcardList = findViewById(R.id.rvJobcardList);
        rv1stScheduService = findViewById(R.id.rv1stScheduService);
        rv2ndScheduService = findViewById(R.id.rv2ndScheduService);
        tvJobcardDetails = findViewById(R.id.tvJobcardDetails);
        tv1stService = findViewById(R.id.tv1stService);
        tv2ndService = findViewById(R.id.tv2ndService);
        llDailyJobCardList.setOnClickListener(this);
        ll1stScheduleService.setOnClickListener(this);
        ll2ndScheduleService.setOnClickListener(this);

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
        getSupportActionBar().setTitle("Daily Buswise Mainatanance");
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
        switch (view.getId())
        {
            case R.id.llDailyJobCardList:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnackError(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    if (!jobCardClicked) {
                        jobCardClicked = true;
                        setJobListRecyclerView();
                    }else
                    {
                        jobCardClicked = false;
                        setControlsDisable();
                    }
                }
                break;
            case R.id.ll1stScheduleService:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnackError(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    if (!b1stServiceClicked) {
                        b1stServiceClicked = true;
                        set1stScheduleService();
                    }
                    else
                    {
                        b1stServiceClicked = false;
                        setControlsDisable();
                    }
                }
                break;
            case R.id.ll2ndScheduleService:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnackError(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    if (!b2ndServiceClicked) {
                        b2ndServiceClicked = true;
                        set2ndScheduleService();
                    }
                    else
                    {
                        b2ndServiceClicked = false;
                        setControlsDisable();
                    }
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

        if (id == R.id.llDailyJobCardList) {
            if (!jobCardClicked) {
                jobCardClicked = true;
                setJobListRecyclerView();
            } else {
                jobCardClicked = false;
                setControlsDisable();
            }
        } else if (id == R.id.ll1stScheduleService) {
            if (!b1stServiceClicked) {
                b1stServiceClicked = true;
                set1stScheduleService();
            } else {
                b1stServiceClicked = false;
                setControlsDisable();
            }
        } else if (id == R.id.ll2ndScheduleService) {
            if (!b2ndServiceClicked) {
                b2ndServiceClicked = true;
                set2ndScheduleService();
            } else {
                b2ndServiceClicked = false;
                setControlsDisable();
            }
        }
    }


    private void setControlsDisable() {
        ll1stScheduleServiceRecyclerview.setVisibility(View.GONE);
        ll2ndScheduleServiceRecyclerview.setVisibility(View.GONE);
        llDailyJobCardListRecyclerview.setVisibility(View.GONE);
        llDailyJobCardList.setVisibility(View.VISIBLE);
        ll1stScheduleService.setVisibility(View.VISIBLE);
        ll2ndScheduleService.setVisibility(View.VISIBLE);
        tvJobcardDetails.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandmore, 0);
        tv1stService.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandmore, 0);
        tv2ndService.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandmore, 0);
    }

    private void setJobListRecyclerView() {

        ll1stScheduleService.setVisibility(View.GONE);
        ll1stScheduleServiceRecyclerview.setVisibility(View.GONE);
        ll2ndScheduleService.setVisibility(View.GONE);
        ll2ndScheduleServiceRecyclerview.setVisibility(View.GONE);
        llDailyJobCardListRecyclerview.setVisibility(View.VISIBLE);
        llDailyJobCardList.setVisibility(View.VISIBLE);

        /////////////////
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        params.weight = 1.0f;
        params.gravity = Gravity.TOP;
        llDailyJobCardList.setLayoutParams(params);
        //////////////////

        tvJobcardDetails.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandless, 0);
        pg.setMessage("Getting Daily Buswise Jobcard Details...");
        pg.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<JobCardDatumResponse> call = apiInterface.getJobCardList(new BreakdownRequest(1));
        call.enqueue(new Callback<JobCardDatumResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JobCardDatumResponse> call, Response<JobCardDatumResponse> response) {
                pg.dismiss();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 1) {
                            Log.e("JOB CARD DATA RESPONSE: ", response.body().getMessage());
                            jobcaedData = response.body().getDailyJobData();
                            jobCardAdapter = new JobCardAdapter(MaintananceListActivity.this, jobcaedData);
                            rvJobcardList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                            rvJobcardList.setAdapter(jobCardAdapter);
                            jobCardAdapter.notifyDataSetChanged();
                        } else {
                            pg.dismiss();
                            setControlsDisable();
                            Common.showSnackError(findViewById(R.id.rootlayout), response.body().getMessage() + " No Data Found...!");
                            Log.e("JOB CARD DATA RESPONSE ERROR 1: ", response.body().getMessage());
                        }
                    } else {
                        pg.dismiss();
                        setControlsDisable();
                        Common.showSnackError(findViewById(R.id.rootlayout), " Something went wrong...!");
                        Log.e("JOB CARD DATA RESPONSE ERROR 2: ", response.body().getMessage());
                    }
                } catch (Exception e) {
                    pg.dismiss();
                    setControlsDisable();
                    Common.showSnackError(findViewById(R.id.rootlayout), e.getMessage() + " Something went wrong...!");
                    Log.e("JOB CARD DATA RESPONSE EXCEPTION 1: ", response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<JobCardDatumResponse> call, Throwable t) {
                pg.dismiss();
                Log.d("JOB CARD DATA FAILURE:", t.getMessage());
                setControlsDisable();
                Common.showSnackError(findViewById(R.id.rootlayout), t.getMessage() + " Something went wrong...!");
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(MaintananceListActivity.this, R.style.DialogBox);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setJobListRecyclerView();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    private void set1stScheduleService() {
        llDailyJobCardList.setVisibility(View.GONE);
        llDailyJobCardListRecyclerview.setVisibility(View.GONE);
        ll2ndScheduleService.setVisibility(View.GONE);
        ll2ndScheduleServiceRecyclerview.setVisibility(View.GONE);
        ll1stScheduleService.setVisibility(View.VISIBLE);
        ll1stScheduleServiceRecyclerview.setVisibility(View.VISIBLE);

        /////////////////
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        params.weight = 1.0f;
        params.gravity = Gravity.FILL;
        params.gravity = Gravity.TOP;
        ll1stScheduleService.setLayoutParams(params);
        //////////////////

        tv1stService.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandless, 0);

        pg.setMessage("Getting 1st Schedule Service Details...");
        pg.show();
        if (!datum1stScheduleService.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pg.dismiss();
                }
            }, 1000);
            adapter1stScheduleService = new ScheduleFirstServiceAdapter(MaintananceListActivity.this, datum1stScheduleService);
            rv1stScheduService.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
            rv1stScheduService.setAdapter(adapter1stScheduleService);
            adapter1stScheduleService.notifyDataSetChanged();
        } else {
            pg.dismiss();
            Common.showSnackError(findViewById(R.id.rootlayout), " No Data Found...!");
            setControlsDisable();
        }
    }

    private void set2ndScheduleService() {
        llDailyJobCardList.setVisibility(View.GONE);
        llDailyJobCardListRecyclerview.setVisibility(View.GONE);
        ll1stScheduleService.setVisibility(View.GONE);
        ll1stScheduleServiceRecyclerview.setVisibility(View.GONE);
        ll2ndScheduleService.setVisibility(View.VISIBLE);
        ll2ndScheduleServiceRecyclerview.setVisibility(View.VISIBLE);

        /////////////////
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        params.weight = 1.0f;
        params.gravity = Gravity.TOP;
        ll1stScheduleService.setLayoutParams(params);
        //////////////////

        tv2ndService.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandless, 0);
        pg.setMessage("Getting 2nd Schedule Service Details...");
        pg.show();
        if (!datum2ndScheduleService.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pg.dismiss();
                }
            }, 1000);
            adapter2ndScheduleService = new ScheduleSecondServiceAdapter(MaintananceListActivity.this, datum2ndScheduleService);
            rv2ndScheduService.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
            rv2ndScheduService.setAdapter(adapter2ndScheduleService);
            adapter2ndScheduleService.notifyDataSetChanged();
        } else {
            pg.dismiss();
            Common.showSnackError(findViewById(R.id.rootlayout), " No Data Found...!");
            setControlsDisable();
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
