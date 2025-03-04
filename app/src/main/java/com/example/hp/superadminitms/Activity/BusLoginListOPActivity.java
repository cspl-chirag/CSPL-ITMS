package com.example.hp.superadminitms.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.hp.superadminitms.Adapter.LoggedInBusDataOPAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.LoggedInBusDatum;
import com.example.hp.superadminitms.MyApplication;
import com.example.hp.superadminitms.Network.GetLoggedinBusDataRequest;
import com.example.hp.superadminitms.Network.GetLoggedinBusDataResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusLoginListOPActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private SessionManager sessionManager;
    private int companyID;
    private ProgressDialog pDialog;
    private RecyclerView rvLoggedInBusList;
    private LinearLayout llEmpty, llDataFound;
    private List<LoggedInBusDatum> loggedInBusData;
    private LoggedInBusDataOPAdapter loggedInBusDataAdapter;
    private boolean FirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_login_list);
        initializeToolBar();
        initViews();

        getLoggedInBusList();

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
        getSupportActionBar().setTitle("Logged in Bus List");
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

        rvLoggedInBusList = findViewById(R.id.rvLoggedInBusList);
        llEmpty = findViewById(R.id.llEmpty);
        llDataFound = findViewById(R.id.llDataFound);
        rvLoggedInBusList.setLayoutManager(new LinearLayoutManager(BusLoginListOPActivity.this));

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

    private void getLoggedInBusList() {
        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(BusLoginListOPActivity.this);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getLoggedInBusList();
                }
            });
            builder.setCancelable(false);
            builder.show();
            //Common.showSnack(findViewById(R.id.loggedInBusListLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Getting LoggedIn Bus Data...");
            showDialog();

            Call<GetLoggedinBusDataResponse> call = apiInterface.getLoggedInBusList(new GetLoggedinBusDataRequest(companyID));

            call.enqueue(new Callback<GetLoggedinBusDataResponse>() {
                @Override
                public void onResponse(Call<GetLoggedinBusDataResponse> call, Response<GetLoggedinBusDataResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);
                                FirstLoad = false;
                                llDataFound.setVisibility(View.VISIBLE);
                                llEmpty.setVisibility(View.GONE);

                                loggedInBusData = response.body().getLoggedInBusData();
                                //Collections.reverse(ignitionReportData);
                                loggedInBusDataAdapter = new LoggedInBusDataOPAdapter(loggedInBusData, BusLoginListOPActivity.this);

                                rvLoggedInBusList.setAdapter(loggedInBusDataAdapter);
                                loggedInBusDataAdapter.notifyDataSetChanged();

                                loggedInBusDataAdapter.SetOnItemClickListener(new LoggedInBusDataOPAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        LoggedInBusDatum loggedInBusDatum = loggedInBusData.get(position);
                                        Intent intent = new Intent(BusLoginListOPActivity.this, UpdateBusLoginOPActivity.class);
                                        intent.putExtra("LoggedInBusData", loggedInBusDatum);
                                        startActivity(intent);
                                    }
                                });

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                llDataFound.setVisibility(View.GONE);
                                llEmpty.setVisibility(View.VISIBLE);
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            llDataFound.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<GetLoggedinBusDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(BusLoginListOPActivity.this);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getLoggedInBusList();
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
            getLoggedInBusList();
        }
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showView(isConnected);
    }

    private void showView(boolean isConnected) {
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.loggedInBusListLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showView(isConnected);
    }
}
