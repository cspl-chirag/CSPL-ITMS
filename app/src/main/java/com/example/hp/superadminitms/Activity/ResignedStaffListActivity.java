package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.ResignedStaffMemberListAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.ResignedStaffDatum;
import com.example.hp.superadminitms.Network.ResignedStaffListResponse;
import com.example.hp.superadminitms.Network.RouteRequest;
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

public class ResignedStaffListActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private RecyclerView rvStaffMemberList;
    private LinearLayout llDataFound, llDataNotFound;
    private ProgressDialog pg;
    private SessionManager sessionManager;
    private int Company_Id;
    private boolean isConnected;
    private List<ResignedStaffDatum> staffData = new ArrayList<>();
    private ResignedStaffMemberListAdapter adapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resigned_staff_list);
        initializeToolBar();
        intializeControls();
        gettingStaffMemberList();
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
        getSupportActionBar().setTitle("Resigned Staff Members");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
            }
        }
    }

    private void intializeControls() {
        pg = Common.showProgressDialog(this);
        pg.setCancelable(false);
        pg.setIndeterminate(false);
        sessionManager = new SessionManager(this);
        Company_Id = sessionManager.getKeyCompanyId();
        llDataFound = findViewById(R.id.llDataFound);
        llDataNotFound = findViewById(R.id.llDataNotFound);
        llDataNotFound.setVisibility(View.GONE);
        llDataFound.setVisibility(View.GONE);
        rvStaffMemberList = findViewById(R.id.rvStaffMemberList);
    }

    private void gettingStaffMemberList() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            pg.setMessage("Getting Staff Members...");
            pg.show();
            Call<ResignedStaffListResponse> call = apiInterface.getResignedStaffList(new RouteRequest(Company_Id));
            call.enqueue(new Callback<ResignedStaffListResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<ResignedStaffListResponse> call, Response<ResignedStaffListResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                llDataFound.setVisibility(View.VISIBLE);
                                llDataNotFound.setVisibility(View.GONE);
                                Log.e("STAFF DATA RESPONSE : ", response.body().getMessage());
                                staffData = response.body().getResignedStaffData();
                                rvStaffMemberList.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                                adapter = new ResignedStaffMemberListAdapter(ResignedStaffListActivity.this, staffData);
                                rvStaffMemberList.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            } else {
                                pg.dismiss();
                                Log.e("STAFF DATA RESPONSE ERROR 1: ", response.body().getMessage());
                                llDataFound.setVisibility(View.GONE);
                                llDataNotFound.setVisibility(View.VISIBLE);
                            }
                        } else {
                            pg.dismiss();
                            Log.e("STAFF DATA RESPONSE ERROR 2: ", response.errorBody().string());
                            llDataFound.setVisibility(View.GONE);
                            llDataNotFound.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        Log.e("STAFF DATA RESPONSE EXCEPTION : ", e.getMessage());
                        llDataFound.setVisibility(View.GONE);
                        llDataNotFound.setVisibility(View.VISIBLE);
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<ResignedStaffListResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("STAFF DATA RESPONSE FAILURE : ", t.getMessage());
                    llDataFound.setVisibility(View.GONE);
                    llDataNotFound.setVisibility(View.VISIBLE);
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(ResignedStaffListActivity.this);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            gettingStaffMemberList();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("Enter Name or Code to be search");
        searchView.setPadding(0, 5, 0, 5);
        searchView.setBackground(getResources().getDrawable(R.drawable.bg_green_rounded_corner_8dp));
        searchView.setFocusableInTouchMode(true);
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (staffData.size() > 0) {
                    adapter.getFilter().filter(newText);
                    return true;
                }
                return true;
            }
        });

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}
