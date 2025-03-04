package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.StockInwardDataAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.StockInwardDatum;
import com.example.hp.superadminitms.Network.BreakdownRequest;
import com.example.hp.superadminitms.Network.StockInwardDataResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockInawardListActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private RecyclerView rvInwardData;
    private LinearLayout llDataFound, llDataNotFound;
    private SessionManager session;
    private int Company_Id;
    private ProgressDialog pg;
    private boolean isConnected;
    private List<StockInwardDatum> inwardData = new ArrayList<>();
    private StockInwardDataAdapter adapter;
    private final List<String> billNo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_inaward_list);
        initializeToolBar();
        initializeControls();
        getInwardData();
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
        getSupportActionBar().setTitle("Stock Inward Data");
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
        pg = Common.showProgressDialog(this);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        session = new SessionManager(this);
        Company_Id = session.getKeyCompanyId();
        rvInwardData = findViewById(R.id.rvInwardData);
        llDataFound = findViewById(R.id.llDataFound);
        llDataNotFound = findViewById(R.id.llDataNotFound);
    }

    private void getInwardData() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Please Wait...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<StockInwardDataResponse> call = apiInterface.getStockForInwardUpdate(new BreakdownRequest(Company_Id));
            call.enqueue(new Callback<StockInwardDataResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<StockInwardDataResponse> call, Response<StockInwardDataResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                llDataFound.setVisibility(View.VISIBLE);
                                llDataNotFound.setVisibility(View.GONE);
                                Log.e("STOCK INWARD DATA RESPONSE: ", response.body().getMessage());
                                inwardData = response.body().getStockInwardData();
                                setInwardDataRecyclerView(inwardData);
                            } else {
                                llDataFound.setVisibility(View.GONE);
                                llDataNotFound.setVisibility(View.VISIBLE);
                                pg.dismiss();
                                Log.e("STOCK INWARD DATA ERROR 1: ", response.body().getMessage());
                            }
                        } else {
                            llDataFound.setVisibility(View.GONE);
                            llDataNotFound.setVisibility(View.VISIBLE);
                            pg.dismiss();
                            Log.e("STOCK INWARD DATA ERROR 2: ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        llDataFound.setVisibility(View.GONE);
                        llDataNotFound.setVisibility(View.VISIBLE);
                        pg.dismiss();
                        Log.e("STOCK INWARD DATA EXCEPTION: ", e.getMessage());
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<StockInwardDataResponse> call, Throwable t) {
                    llDataFound.setVisibility(View.GONE);
                    llDataNotFound.setVisibility(View.VISIBLE);
                    pg.dismiss();
                    Log.e("STOCK INWARD DATA FAILURE: ", t.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getInwardData();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void setInwardDataRecyclerView(List<StockInwardDatum> inwardData) {
        if (inwardData.size() > 0) {

            //////////////////////////////////////////////////////////////////////////////////////////
            for (StockInwardDatum StockInwardDatum : inwardData) {
                billNo.add(StockInwardDatum.getBillNo());
            }

            List<String> removedDuplicate = new ArrayList<>();

            for (String o : billNo) {
                if (!removedDuplicate.contains(o))
                    removedDuplicate.add(o);
            }

            billNo.clear();
            billNo.addAll(removedDuplicate);
            ////////////////////////////////////////////////////////////////////////////////////////
            Log.e("HCF LIST COUNT : ", String.valueOf(billNo.size()));
            llDataFound.setVisibility(View.VISIBLE);
            llDataNotFound.setVisibility(View.GONE);
            adapter = new StockInwardDataAdapter(this, billNo, inwardData);
            rvInwardData.setLayoutManager(new GridLayoutManager(this, 1));
            rvInwardData.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            adapter.SetOnItemClickListener(new StockInwardDataAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int pos) {
                    List<StockInwardDatum> filteredInwardData = new ArrayList<>();
                    filteredInwardData.clear();

                    for (StockInwardDatum sd : inwardData) {
                        if (inwardData.get(pos).getBillNo().equals(sd.getBillNo())) {
                            filteredInwardData.add(sd);
                        }
                    }

                    startActivity(new Intent(StockInawardListActivity.this, StockInwardUpdateActivity.class).putExtra("InwardList", (Serializable) filteredInwardData));
                }
            });
        } else {
            llDataFound.setVisibility(View.GONE);
            llDataNotFound.setVisibility(View.VISIBLE);
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
