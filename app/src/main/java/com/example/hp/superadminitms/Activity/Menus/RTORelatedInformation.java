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

import com.example.hp.superadminitms.Adapter.FitnessExpAdapter;
import com.example.hp.superadminitms.Adapter.InsuranceExpAdapter;
import com.example.hp.superadminitms.Adapter.LicenceExpAdapter;
import com.example.hp.superadminitms.Adapter.RtoExpAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.FitnessExpDatum;
import com.example.hp.superadminitms.Model.InsuranceExpDatum;
import com.example.hp.superadminitms.Model.LiceneceExpDatum;
import com.example.hp.superadminitms.Model.RTOExpDatum;
import com.example.hp.superadminitms.Network.ExpirationResponse;
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

public class RTORelatedInformation extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {
    private LinearLayout llLicenceExp, llInsuranceExp, llRtoExp, llFitnessExp;
    private LinearLayout llLicenceExpRecyclerview, llInsuranceExpRecyclerview, llRtoExpRecyclerview, llFitnessExpRecyclerview;
    private RecyclerView rvLicenceExp, rvRtoExp, rvInsuranceExp, rvFitnessExp;
    private TextView tvLicenceExp, tvInsuranceExp, tvRtoExp, tvFitnessExp;
    private boolean isConnected;
    private ProgressDialog pg;
    private SessionManager sessionManager;
    private int Company_Id, User_ID;
    private boolean bLicenseExpClicked = false, bInsuranceExpClicked = false, bRTOExpClicked = false, bFitnessExpClicked = false;
    private LinearLayout.LayoutParams params;
    private List<InsuranceExpDatum> insuranceExpData = new ArrayList<>();
    private List<RTOExpDatum> rtoExpData = new ArrayList<>();
    private List<FitnessExpDatum> fitnessExpData = new ArrayList<>();
    private List<LiceneceExpDatum> licenceExpData = new ArrayList<>();
    private LicenceExpAdapter licenceExpadapter;
    private InsuranceExpAdapter insuranceExpAdapter;
    private RtoExpAdapter rtoExpAdapter;
    private FitnessExpAdapter fitnessExpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rto_related_info);
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
                            licenceExpData = response.body().getExpirationAlertData().getTable();
                            insuranceExpData = response.body().getExpirationAlertData().getTable1();
                            rtoExpData = response.body().getExpirationAlertData().getTable2();
                            fitnessExpData = response.body().getExpirationAlertData().getTable3();
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
                        new AlertDialog.Builder(RTORelatedInformation.this, R.style.DialogBox);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gettingExpiryList();
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
        llLicenceExp = findViewById(R.id.llLicenceExp);
        llInsuranceExp = findViewById(R.id.llInsuranceExp);
        llRtoExp = findViewById(R.id.llRtoExp);
        llFitnessExp = findViewById(R.id.llFitnessExp);
        llLicenceExpRecyclerview = findViewById(R.id.llLicenceExpRecyclerview);
        llInsuranceExpRecyclerview = findViewById(R.id.llInsuranceExpRecyclerview);
        llRtoExpRecyclerview = findViewById(R.id.llRtoExpRecyclerview);
        llFitnessExpRecyclerview = findViewById(R.id.llFitnessExpRecyclerview);
        rvLicenceExp = findViewById(R.id.rvLicenceExp);
        rvInsuranceExp = findViewById(R.id.rvInsuranceExp);
        rvRtoExp = findViewById(R.id.rvRtoExp);
        rvFitnessExp = findViewById(R.id.rvFitnessExp);
        tvLicenceExp = findViewById(R.id.tvLicenceExp);
        tvInsuranceExp = findViewById(R.id.tvInsuranceExp);
        tvRtoExp = findViewById(R.id.tvRtoExp);
        tvFitnessExp = findViewById(R.id.tvFitnessExp);
        llLicenceExp.setOnClickListener(this);
        llInsuranceExp.setOnClickListener(this);
        llRtoExp.setOnClickListener(this);
        llFitnessExp.setOnClickListener(this);

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
        getSupportActionBar().setTitle("RTO Related Information");
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
            case R.id.llLicenceExp:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnackError(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    if (!bLicenseExpClicked) {
                        bLicenseExpClicked = true;
                        setLiecenceExpList();
                    }else
                    {
                        bLicenseExpClicked = false;
                        setControlsDisable();
                    }
                }
                break;
            case R.id.llInsuranceExp:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnackError(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    if (!bInsuranceExpClicked) {
                        bInsuranceExpClicked = true;
                        setInsuranceExpList();
                    }
                    else
                    {
                        bInsuranceExpClicked = false;
                        setControlsDisable();
                    }
                }
                break;
            case R.id.llRtoExp:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnackError(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    if (!bRTOExpClicked) {
                        bRTOExpClicked = true;
                        setRtoExpList();
                    }
                    else
                    {
                        bRTOExpClicked = false;
                        setControlsDisable();
                    }
                }
                break;
            case R.id.llFitnessExp:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected)
                {
                    Common.showSnackError(findViewById(R.id.rootlayout),getResources().getString(R.string.NetworkErrorMsg));
                }
                else
                {
                    if (!bFitnessExpClicked){
                        bFitnessExpClicked = true;
                        setFitnessExpList();
                    }
                    else
                    {
                        bFitnessExpClicked = false;
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

        if (id == R.id.llLicenceExp) {
            if (!bLicenseExpClicked) {
                bLicenseExpClicked = true;
                setLiecenceExpList();
            } else {
                bLicenseExpClicked = false;
                setControlsDisable();
            }
        } else if (id == R.id.llInsuranceExp) {
            if (!bInsuranceExpClicked) {
                bInsuranceExpClicked = true;
                setInsuranceExpList();
            } else {
                bInsuranceExpClicked = false;
                setControlsDisable();
            }
        } else if (id == R.id.llRtoExp) {
            if (!bRTOExpClicked) {
                bRTOExpClicked = true;
                setRtoExpList();
            } else {
                bRTOExpClicked = false;
                setControlsDisable();
            }
        } else if (id == R.id.llFitnessExp) {
            if (!bFitnessExpClicked) {
                bFitnessExpClicked = true;
                setFitnessExpList();
            } else {
                bFitnessExpClicked = false;
                setControlsDisable();
            }
        }
    }


    private void setControlsDisable() {
        llInsuranceExpRecyclerview.setVisibility(View.GONE);
        llRtoExpRecyclerview.setVisibility(View.GONE);
        llLicenceExpRecyclerview.setVisibility(View.GONE);
        llFitnessExpRecyclerview.setVisibility(View.GONE);
        llLicenceExp.setVisibility(View.VISIBLE);
        llInsuranceExp.setVisibility(View.VISIBLE);
        llRtoExp.setVisibility(View.VISIBLE);
        llFitnessExp.setVisibility(View.VISIBLE);
        tvLicenceExp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandmore, 0);
        tvInsuranceExp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandmore, 0);
        tvRtoExp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandmore, 0);
        tvFitnessExp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandmore, 0);
    }

    private void setLiecenceExpList() {
        llFitnessExp.setVisibility(View.GONE);
        llFitnessExpRecyclerview.setVisibility(View.GONE);
        llLicenceExp.setVisibility(View.VISIBLE);
        llLicenceExpRecyclerview.setVisibility(View.VISIBLE);
        llInsuranceExp.setVisibility(View.GONE);
        llInsuranceExpRecyclerview.setVisibility(View.GONE);
        llRtoExp.setVisibility(View.GONE);
        llRtoExpRecyclerview.setVisibility(View.GONE);

        /////////////////
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        params.weight = 1.0f;
        params.gravity = Gravity.TOP;
        llLicenceExp.setLayoutParams(params);
        //////////////////

        tvLicenceExp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandless, 0);
        pg.setMessage("Getting Licence Expiration Details...");
        pg.show();
        if (!licenceExpData.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pg.dismiss();
                }
            }, 1000);
            licenceExpadapter = new LicenceExpAdapter(RTORelatedInformation.this, licenceExpData);
            rvLicenceExp.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
            rvLicenceExp.setAdapter(licenceExpadapter);
            licenceExpadapter.notifyDataSetChanged();
        } else {
            pg.dismiss();
            Common.showSnackError(findViewById(R.id.rootlayout), " No Data Found...!");
            setControlsDisable();
        }
    }

    private void setInsuranceExpList() {
        llFitnessExp.setVisibility(View.GONE);
        llFitnessExpRecyclerview.setVisibility(View.GONE);
        llLicenceExp.setVisibility(View.GONE);
        llLicenceExpRecyclerview.setVisibility(View.GONE);
        llRtoExp.setVisibility(View.GONE);
        llRtoExpRecyclerview.setVisibility(View.GONE);
        llInsuranceExp.setVisibility(View.VISIBLE);
        llInsuranceExpRecyclerview.setVisibility(View.VISIBLE);

        /////////////////
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        params.weight = 1.0f;
        params.gravity = Gravity.FILL;
        params.gravity = Gravity.TOP;
        llInsuranceExp.setLayoutParams(params);
        //////////////////

        tvInsuranceExp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandless, 0);

        pg.setMessage("Getting Insurance Expiration Details...");
        pg.show();
        if (!insuranceExpData.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pg.dismiss();
                }
            }, 1000);
            insuranceExpAdapter = new InsuranceExpAdapter(RTORelatedInformation.this, insuranceExpData);
            rvInsuranceExp.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
            rvInsuranceExp.setAdapter(insuranceExpAdapter);
            insuranceExpAdapter.notifyDataSetChanged();
        } else {
            pg.dismiss();
            Common.showSnackError(findViewById(R.id.rootlayout), " No Data Found...!");
            setControlsDisable();
        }
    }

    private void setRtoExpList() {
        llFitnessExp.setVisibility(View.GONE);
        llFitnessExpRecyclerview.setVisibility(View.GONE);
        llLicenceExp.setVisibility(View.GONE);
        llLicenceExpRecyclerview.setVisibility(View.GONE);
        llInsuranceExp.setVisibility(View.GONE);
        llInsuranceExpRecyclerview.setVisibility(View.GONE);
        llRtoExp.setVisibility(View.VISIBLE);
        llRtoExpRecyclerview.setVisibility(View.VISIBLE);

        /////////////////
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        params.weight = 1.0f;
        params.gravity = Gravity.TOP;
        llRtoExp.setLayoutParams(params);
        //////////////////

        tvRtoExp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandless, 0);
        pg.setMessage("Getting RTO Permit Expiration Details...");
        pg.show();
        if (!rtoExpData.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pg.dismiss();
                }
            }, 1000);
            rtoExpAdapter = new RtoExpAdapter(RTORelatedInformation.this, rtoExpData);
            rvRtoExp.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
            rvRtoExp.setAdapter(rtoExpAdapter);
            rtoExpAdapter.notifyDataSetChanged();
        } else {
            pg.dismiss();
            Common.showSnackError(findViewById(R.id.rootlayout), " No Data Found...!");
            setControlsDisable();
        }


    }

    private void setFitnessExpList() {
        llFitnessExp.setVisibility(View.VISIBLE);
        llFitnessExpRecyclerview.setVisibility(View.VISIBLE);
        llLicenceExp.setVisibility(View.GONE);
        llLicenceExpRecyclerview.setVisibility(View.GONE);
        llInsuranceExp.setVisibility(View.GONE);
        llInsuranceExpRecyclerview.setVisibility(View.GONE);
        llRtoExp.setVisibility(View.GONE);
        llRtoExpRecyclerview.setVisibility(View.GONE);

        /////////////////
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
//        params.weight = 1.0f;
        params.gravity = Gravity.TOP;
        llFitnessExpRecyclerview.setLayoutParams(params);
        //////////////////

        tvFitnessExp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_expandless, 0);
        pg.setMessage("Getting Fiteness Expiration Details...");
        pg.show();
        if (!fitnessExpData.isEmpty()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pg.dismiss();
                }
            }, 1000);
            fitnessExpAdapter = new FitnessExpAdapter(RTORelatedInformation.this, fitnessExpData);
            rvFitnessExp.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
            rvFitnessExp.setAdapter(fitnessExpAdapter);
            fitnessExpAdapter.notifyDataSetChanged();
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
