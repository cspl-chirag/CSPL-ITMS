package com.example.hp.superadminitms.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.JobCardAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.JobCardDatum;
import com.example.hp.superadminitms.Network.BreakdownRequest;
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

public class StoreJobListActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = "JobCardList ***** ";
    private RecyclerView rvJobList;
    private SessionManager sessionManager;
    private int CompanyId;
    private ProgressDialog pg;
    private boolean isConnected;
    private JobCardAdapter adapter;
    private LinearLayout llNotfound;
    private List<JobCardDatum> jobCardData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_st_job_list);
        initializeToolBar();
        initializeControls();
        setUpJobCardRv();
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
        getSupportActionBar().setTitle("Daily Buswise Maintanance");
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
        rvJobList = findViewById(R.id.rvJobcardList);
        sessionManager = new SessionManager(this);
        CompanyId = sessionManager.getKeyCompanyId();
        pg = new ProgressDialog(StoreJobListActivity.this, R.style.DialogBox);
        pg.setIndeterminate(false);
        pg.setCancelable(false);

        llNotfound = findViewById(R.id.llNotfound);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpJobCardRv();
    }

    private void setUpJobCardRv() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Getting JobCard List...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JobCardDatumResponse> call = apiInterface.getJobCardList(new BreakdownRequest(CompanyId));
            call.enqueue(new Callback<JobCardDatumResponse>() {
                @Override
                public void onResponse(Call<JobCardDatumResponse> call, Response<JobCardDatumResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.d(TAG, " Response : " + response.body().getMessage());
                                jobCardData = response.body().getDailyJobData();
                                rvJobList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                                adapter = new JobCardAdapter(StoreJobListActivity.this, jobCardData);
//                                rvJobList.setEnabled(false);
                                rvJobList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                llNotfound.setVisibility(View.VISIBLE);
                                pg.dismiss();
                                Log.d(TAG, " Error1 : " + response.body().getMessage());
                            }
                        } else {
                            llNotfound.setVisibility(View.VISIBLE);
                            pg.dismiss();
//                            Log.d(TAG," Error2 : "+response.body().getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        pg.dismiss();
                        Log.d(TAG, "Excpton : " + e.getStackTrace());
                    }
                }

                @Override
                public void onFailure(Call<JobCardDatumResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.d("BreakdownResponseFail:", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(StoreJobListActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setUpJobCardRv();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }


    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}
