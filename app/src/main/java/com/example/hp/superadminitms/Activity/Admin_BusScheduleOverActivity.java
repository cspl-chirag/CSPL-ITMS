package com.example.hp.superadminitms.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.LoggedOutBusDataOPAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.LoggedOutBusDatum;
import com.example.hp.superadminitms.MyApplication;
import com.example.hp.superadminitms.Network.GetLoggedoutBusDataRequest;
import com.example.hp.superadminitms.Network.GetLoggedoutBusDataResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Admin_BusScheduleOverActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private SessionManager sessionManager;
    private int companyID;
    private ProgressDialog pDialog;
    private RecyclerView rvLoggedOutBusList;
    private LinearLayout llEmpty, llDataFound;
    private boolean FirstLoad = true;
    private List<LoggedOutBusDatum> loggedOutBusData;
    private LoggedOutBusDataOPAdapter loggedOutBusDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__bus_schedule_over);
        initializeToolBar();
        initViews();

        getLoggedOutBusList();
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
        getSupportActionBar().setTitle("Bus Schedule Over");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
            }
        }
    }

    private void initViews() {

        sessionManager = new SessionManager(this);
        companyID = sessionManager.getKeyCompanyId();

        // Progress dialog
        pDialog = new ProgressDialog(this, R.style.DialogBox);
        pDialog.setCancelable(false);

        rvLoggedOutBusList = findViewById(R.id.rvLoggedOutBusList);
        llEmpty = findViewById(R.id.llEmpty);
        rvLoggedOutBusList.setLayoutManager(new LinearLayoutManager(Admin_BusScheduleOverActivity.this));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void getLoggedOutBusList() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(Admin_BusScheduleOverActivity.this);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getLoggedOutBusList();
                }
            });
            builder.setCancelable(false);
            builder.show();
            //Common.showSnack(findViewById(R.id.loggedInBusListLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Getting LoggedOut Bus Data...");
            showDialog();

            Call<GetLoggedoutBusDataResponse> call = apiInterface.getLoggedOutBusList(new GetLoggedoutBusDataRequest(companyID));

            call.enqueue(new Callback<GetLoggedoutBusDataResponse>() {
                @Override
                public void onResponse(Call<GetLoggedoutBusDataResponse> call, Response<GetLoggedoutBusDataResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
//                                Log.e("RESPONSE : ",response.body().getMessage());
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);
                                FirstLoad = false;
                                llEmpty.setVisibility(View.GONE);
                                rvLoggedOutBusList.setVisibility(View.VISIBLE);

                                loggedOutBusData = response.body().getLoggedOutBusData();
                                //Collections.reverse(ignitionReportData);
                                loggedOutBusDataAdapter = new LoggedOutBusDataOPAdapter(loggedOutBusData, Admin_BusScheduleOverActivity.this);

                                rvLoggedOutBusList.setAdapter(loggedOutBusDataAdapter);
                                loggedOutBusDataAdapter.notifyDataSetChanged();

                            } else {
//                                Log.e("Error 1: ",response.body().getMessage());
                                if (pDialog.isShowing())
                                    hideDialog();
                                rvLoggedOutBusList.setVisibility(View.GONE);
                                llEmpty.setVisibility(View.VISIBLE);
                            }
                        } else {
//                            Log.e("Error 2: ",response.errorBody().string());

                            if (pDialog.isShowing())
                                hideDialog();
                            rvLoggedOutBusList.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception ex) {
//                        Log.e("EXCEPTION: ", ex.getMessage());

                        ex.printStackTrace();
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<GetLoggedoutBusDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();
//                    Log.e("FAILURE: ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Admin_BusScheduleOverActivity.this);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getLoggedOutBusList();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!FirstLoad) {
            getLoggedOutBusList();
        }
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showView(isConnected);
    }

    private void showView(boolean isConnected) {
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.loggedOutBusListLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showView(isConnected);
    }
}
