package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.CompoundButtonCompat;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Network.AddSummaryResponse;
import com.example.hp.superadminitms.Network.BreakdownRequest;
import com.example.hp.superadminitms.Network.DailySummaryBusListResponse;
import com.example.hp.superadminitms.Network.SummaryRequest;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;
import com.example.hp.superadminitms.utils.SearchableSpinner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDailySummaryActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Button btnSubmit;
    private EditText etReason;
    private CheckBox cbIsSpare, cbOther;
    private SearchableSpinner spVehileRegNo;
    private SessionManager session;
    private ProgressDialog pg;
    private int Company_Id, User_Id;
    private int Vehicle_Id;
    private List<BusDatum> busDatum = new ArrayList<>();
    private VehicleSpinnerAdapter spVehicleAdapter;
    private final String TAG = "******";
    private String Spare;
    private boolean IsSpare = false, IsOther = false;
    private boolean isConnected;
    private String DateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_summary);
        initializeToolBar();
        initializeControls();
        getVehicleInfoSpinner();
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
        getSupportActionBar().setTitle("Add Daily Bus Summary");
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
        User_Id = session.getKeyUserId();
        Company_Id = session.getKeyCompanyId();
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        etReason = findViewById(R.id.etReason);
        cbIsSpare = findViewById(R.id.cbIsSpare);
        cbOther = findViewById(R.id.cbOther);
        int[][] states = {{android.R.attr.state_checked}, {}};
        final int[] colors = {getResources().getColor(R.color.colorAccentText), getResources().getColor(R.color.colorAccent)};
        CompoundButtonCompat.setButtonTintList(cbOther, new ColorStateList(states, colors));
        CompoundButtonCompat.setButtonTintList(cbIsSpare, new ColorStateList(states, colors));
        spVehileRegNo = findViewById(R.id.spVehileRegNo);
        spVehileRegNo.setOnItemSelectedListener(this);
        cbOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    int[][] states = {{android.R.attr.state_checked}, {}};
                    final int[] colors = {getResources().getColor(R.color.colorAccentText), getResources().getColor(R.color.colorAccent)};
                    CompoundButtonCompat.setButtonTintList(cbOther, new ColorStateList(states, colors));
                    IsOther = true;
                    if (cbIsSpare.isChecked())
                        etReason.setText(Spare + ",");
                } else {
                    if (cbIsSpare.isChecked())
                        etReason.setText(Spare + ",");
                    else
                        etReason.getText().clear();
                }

            }
        });
        cbIsSpare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    int[][] states = {{android.R.attr.state_checked}, {}};
                    final int[] colors = {getResources().getColor(R.color.colorAccentText), getResources().getColor(R.color.colorAccent)};
                    CompoundButtonCompat.setButtonTintList(cbIsSpare, new ColorStateList(states, colors));
                    IsSpare = true;
                    Spare = "Spare";
                    etReason.setText(Spare);
                } else {
                    if (!cbOther.isChecked())
                        etReason.getText().clear();
                }
            }
        });
    }

    private void getVehicleInfoSpinner() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(AddDailySummaryActivity.this, R.style.DialogBox);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getVehicleInfoSpinner();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            pg.setMessage("Getting Bus List...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<DailySummaryBusListResponse> call = apiInterface.getDailySummaryBusList(new BreakdownRequest(Company_Id));
            call.enqueue(new Callback<DailySummaryBusListResponse>() {
                @Override
                public void onResponse(Call<DailySummaryBusListResponse> call, Response<DailySummaryBusListResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        pg.dismiss();
                                    }
                                }, 1000);
                                busDatum = response.body().getBusData();
                                spVehicleAdapter = new VehicleSpinnerAdapter(AddDailySummaryActivity.this, android.R.layout.simple_spinner_dropdown_item, busDatum);
                                spVehileRegNo.setAdapter(spVehicleAdapter);
                            } else {
                                if (pg.isShowing())
                                    pg.dismiss();
                                Log.d(TAG, "BusListError1 : " + response.body().getMessage());
                            }
                        } else {
                            if (pg.isShowing())
                                pg.dismiss();
                            Log.d(TAG, "BusListEerror2 : " + response.errorBody());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing())
                            pg.dismiss();
                        e.printStackTrace();
                        Log.d(TAG, "BusListExcption : " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<DailySummaryBusListResponse> call, Throwable t) {
                    if (pg.isShowing())
                        pg.dismiss();
                    Log.d(TAG, "BusListFailure : " + t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(AddDailySummaryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getVehicleInfoSpinner();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });

        }
    }

    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spVehileRegNo:
                Vehicle_Id = busDatum.get(i).getVehicleID();
                break;
        }
    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();  // Get the ID of the selected AdapterView

        if (id == R.id.spVehileRegNo) {  // Check if the ID matches spVehileRegNo
            Vehicle_Id = busDatum.get(i).getVehicleID();  // Set Vehicle_Id based on the selected item
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

   /* @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    addSummary();
                }
                break;
        }
    }*/

    @Override
    public void onClick(View view) {
        int id = view.getId();  // Get the ID of the clicked view

        if (id == R.id.btnSubmit) {  // Check if the ID matches btnSubmit
            isConnected = ConnectivityReceiver.isConnected();  // Check network connection
            if (!isConnected) {
                // If not connected, show an error message
                Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                // If connected, perform the action
                addSummary();
            }
        }
    }


    private void addSummary() {
        try {
            DateTime = Common.convertDateFormat(Common.getCurrentDate_Time(), "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!etReason.getText().toString().isEmpty() && (cbIsSpare.isChecked() || cbOther.isChecked())) {
            pg.setMessage("Please Wait...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<AddSummaryResponse> call = apiInterface.addDailyBusSummaryEntry(new SummaryRequest(User_Id, Company_Id, Vehicle_Id, DateTime, etReason.getText().toString(), IsSpare));
            call.enqueue(new Callback<AddSummaryResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<AddSummaryResponse> call, Response<AddSummaryResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("Add Summary Response : ", response.body().getMessage());
                                Toast.makeText(getApplicationContext(), "Summary Added", Toast.LENGTH_SHORT).show();
                                clearControls();

                            } else {
                                pg.dismiss();
                                Log.e("Add Summary Error 1: ", response.body().getMessage());
                            }
                        } else {
                            pg.dismiss();
                            Log.e("Add Summary Error 2: ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        Log.e("Add Summary Exception : ", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<AddSummaryResponse> call, Throwable t) {
                    Log.d(TAG, "Add Summary Failure : " + t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(AddDailySummaryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            addSummary();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        } else {
            if (!(cbIsSpare.isChecked()) || cbOther.isChecked()) {
                Common.showSnackError(findViewById(R.id.rootlayout), "Select or enter atlest one reason...!");
            }
            if (!etReason.getText().toString().isEmpty()) {
                Common.showSnackError(findViewById(R.id.rootlayout), "Select or enter atlest one reason...!");
            }
        }

    }

    private void clearControls() {
        cbOther.setChecked(false);
        cbIsSpare.setChecked(false);
        etReason.getText().clear();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}
