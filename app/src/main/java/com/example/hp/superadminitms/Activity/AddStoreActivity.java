package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
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
import com.example.hp.superadminitms.Network.AddStoreResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStoreActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private EditText etStoreName;
    private TextView tvSubmit;
    private boolean isConnected;
    private SessionManager sessionManager;
    private int Company_Id;
    private ProgressDialog pg;
    private int User_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_store);
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
        getSupportActionBar().setTitle("Add New Store");
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
        sessionManager = new SessionManager(this);
        User_Id = sessionManager.getKeyUserId();
        Company_Id = sessionManager.getKeyCompanyId();
        pg = Common.showProgressDialog(this);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        pg.setMessage("Please Wait...");
        etStoreName = findViewById(R.id.etStoreName);
        tvSubmit = findViewById(R.id.btnSubmit);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    if (!etStoreName.getText().toString().isEmpty()) {
                        addNewStore();
                    } else {
                        etStoreName.requestFocus();
                        etStoreName.setError("Enter Store Name...!");
                    }
                }
            }
        });
    }

    private void addNewStore() {
        pg.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<AddStoreResponse> call = apiInterface.addStore(new AddPartRequest(User_Id, Company_Id, etStoreName.getText().toString()));
        call.enqueue(new Callback<AddStoreResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<AddStoreResponse> call, Response<AddStoreResponse> response) {
                pg.dismiss();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 1) {
                            Log.e("ADD STORE RESPONSE : ", response.body().getMessage());
                            Toast.makeText(getApplicationContext(), "New Store Added.", Toast.LENGTH_LONG).show();
                            onBackPressed();
                        } else {
                            pg.dismiss();
                            Log.e("ADD STORE RESPONSE ERROR 1 : ", response.body().getMessage());
                        }
                    } else {
                        pg.dismiss();
                        Log.e("ADD STORE RESPONSE ERROR 2 : ", response.errorBody().string());
                    }
                } catch (Exception e) {
                    pg.dismiss();
                    Log.e("ADD STORE RESPONSE EXCEPTION : ", e.getMessage());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<AddStoreResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("ADD STORE RESPONSE FAILURE: ", t.getMessage());
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
