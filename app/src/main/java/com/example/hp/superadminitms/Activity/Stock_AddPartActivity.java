package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Network.AddPartRequest;
import com.example.hp.superadminitms.Network.AddPartResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import retrofit2.Callback;
import retrofit2.Response;

public class Stock_AddPartActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {

    private SessionManager sessionManager;
    private ProgressDialog pg;
    private int Company_ID, User_ID;
    private TextView btnSubmit;
    private EditText etPartName, etPartCode;
    private boolean isConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock__add_part);
        initializeToolBar();
        initializeControl();
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
        getSupportActionBar().setTitle("Add New Part");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
            }
        }
    }

    private void initializeControl() {
        sessionManager = new SessionManager(this);
        pg = Common.showProgressDialog(this);
        pg.setMessage("Please Wait...");
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        User_ID = sessionManager.getKeyUserId();
        Company_ID = sessionManager.getKeyCompanyId();
        etPartName = findViewById(R.id.etPartName);
        etPartCode = findViewById(R.id.etPartCode);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    AddPart();
                }
                break;
        }
    }*/


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnSubmit) {
            isConnected = ConnectivityReceiver.isConnected();
            if (!isConnected) {
                Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                AddPart();
            }
        }
    }


    private void AddPart() {
        if (etPartName.getText().toString().isEmpty() || etPartName.getText().toString().equals("")) {
            etPartName.requestFocus();
            etPartName.setError("Enter Part Name...!");
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            pg.show();
            retrofit2.Call<AddPartResponse> call = apiInterface.addPart(new AddPartRequest(User_ID, Company_ID, etPartCode.getText().toString(), etPartName.getText().toString()));
            call.enqueue(new Callback<AddPartResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(retrofit2.Call<AddPartResponse> call, Response<AddPartResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("ADD PART RESPONSE : ", response.body().getMessage());
                                Toast.makeText(getApplicationContext(), "New Part Added.", Toast.LENGTH_LONG).show();
                                onBackPressed();
                            } else {
                                pg.dismiss();
                                Log.e("ADD PART RESPONSE ERROR 1 : ", response.body().getMessage());
                            }
                        } else {
                            pg.dismiss();
                            Log.e("ADD PART RESPONSE ERROR 2 : ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        Log.e("ADD PART RESPONSE EXCEPTION : ", e.getMessage());
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(retrofit2.Call<AddPartResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("ADD PART RESPONSE EXCEPTION : ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Stock_AddPartActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddPart();
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
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}
