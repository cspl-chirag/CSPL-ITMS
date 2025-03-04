package com.example.hp.superadminitms.Activity;

import static com.example.hp.superadminitms.Helper.Common.convertDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.LoggedOutBusDatum;
import com.example.hp.superadminitms.MyApplication;
import com.example.hp.superadminitms.Network.UpdateBusLogoutDataRequest;
import com.example.hp.superadminitms.Network.UpdateBusLogoutDataResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.text.ParseException;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateBusLogoutOPActivity extends BaseActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private SessionManager sessionManager;
    private int userID;
    private int companyID;
    private String userName;
    private ProgressDialog pDialog;
    private EditText busName;
    private EditText depotName;
    private EditText logsheetNumber;
    private EditText routeNumber;
    private EditText totalTrip;
    private EditText driverName;
    private EditText openingKm;
    private EditText closingKm;
    private EditText runKm;
    private EditText actualTrip;
    private EditText logoutTime;
    private EditText remarks;
    private TextView submit;
    private int runKM;
    private int meterreading = 0;
    private String logouttime;
    private String remark;
    private int actualtrip = -1;
    private LoggedOutBusDatum loggedOutBusDatum;
    private Integer logID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bus_log_out);
        initializeToolBar();
        initViews();

        checkConnection();
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
        getSupportActionBar().setTitle("Update Bus Logout");
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
        loggedOutBusDatum = (LoggedOutBusDatum) getIntent().getSerializableExtra("LoggedOutBusData");
        logID = loggedOutBusDatum.getLogID();

        sessionManager = new SessionManager(this);
        userID = sessionManager.getKeyUserId();
        companyID = sessionManager.getKeyCompanyId();
        userName = sessionManager.getKeyUserName();

        // Progress dialog
        pDialog = new ProgressDialog(this, R.style.DialogBox);
        pDialog.setCancelable(false);

        busName = findViewById(R.id.busName);
        depotName = findViewById(R.id.depotName);
        logsheetNumber = findViewById(R.id.logsheetNumber);
        routeNumber = findViewById(R.id.routeNumber);
        totalTrip = findViewById(R.id.totalTrip);
        driverName = findViewById(R.id.driverName);
        openingKm = findViewById(R.id.openingKm);
        closingKm = findViewById(R.id.closingKm);
        runKm = findViewById(R.id.runKm);
        actualTrip = findViewById(R.id.actualTrip);
        logoutTime = findViewById(R.id.logoutTime);
        remarks = findViewById(R.id.remarks);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(this);

        closingKm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (closingKm.getText().toString() != null && !closingKm.getText().toString().equals("")) {
                    if (Integer.parseInt(closingKm.getText().toString()) > Integer.parseInt(openingKm.getText().toString())) {
                        int runkm = Integer.parseInt(closingKm.getText().toString()) - Integer.parseInt(openingKm.getText().toString());
                        runKm.setText(String.valueOf(runkm));
                    } else {
                        runKM = 0;
                        runKm.setText("");
                    }
                }
            }
        });

        if (logoutTime.getText().toString().isEmpty()) {
            logoutTime.setText(Common.getCurrentTime());
        }

        logoutTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    hideKeyboard(view);
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateBusLogoutOPActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            logoutTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, false);
                    timePickerDialog.show();
                    logoutTime.clearFocus();
                }
            }
        });

        busName.setText(loggedOutBusDatum.getVehicleCode() + "-" + loggedOutBusDatum.getVehicleRegNo());
        depotName.setText(loggedOutBusDatum.getDeptName());
        logsheetNumber.setText(loggedOutBusDatum.getLogsheetCode());
        routeNumber.setText(loggedOutBusDatum.getRouteNo());
        driverName.setText(loggedOutBusDatum.getDriverName());
        openingKm.setText(String.valueOf(loggedOutBusDatum.getLoginKm()));
        closingKm.setText(String.valueOf(loggedOutBusDatum.getLogOutKm()));
        actualTrip.setText(String.valueOf(loggedOutBusDatum.getActualTrip()));
        try {
            logoutTime.setText(convertDateFormat(loggedOutBusDatum.getLogOutTime(), "HH:mm:ss", "HH:mm"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        remarks.setText(loggedOutBusDatum.getRemarks());
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

   /* @Override
    public void onClick(View view) {
        hideKeyboard(view);
        switch (view.getId()) {
            case R.id.submit:

                logouttime = logoutTime.getText().toString();
                remark = remarks.getText().toString();

                if (closingKm.getText().toString() != null && !closingKm.getText().toString().equals("")){
                    meterreading = Integer.parseInt(closingKm.getText().toString());
                }

                if (runKm.getText().toString() != null && !runKm.getText().toString().equals("")){
                    runKM = Integer.parseInt(runKm.getText().toString());
                }

                if (actualTrip.getText().toString() != null && !actualTrip.getText().toString().equals("")) {
                    actualtrip = Integer.parseInt(actualTrip.getText().toString());
                }

                if (isValidMeterReading(meterreading) && isValidMeterReading(runKM) && isValidTrip(actualtrip) && isValidLogoutTime(logouttime)) {

                    UpdateBusLogout();
                } else {
                    if (! isValidLogoutTime(logouttime)) {
                        //loginTime.requestFocus();
                        Common.showSnack(findViewById(R.id.updateBusLogoutLayout),"Please enter valid logout time");
                    }

                    if (! isValidTrip(actualtrip)) {
                        //loginTime.requestFocus();
                        Common.showSnack(findViewById(R.id.updateBusLogoutLayout),"Please enter valid completed trips");
                    }

                    if (! isValidMeterReading(runKM)) {
                        closingKm.requestFocus();
                        Common.showSnack(findViewById(R.id.updateBusLogoutLayout),"Please enter valid Closing Km\nClosing Km should be greater than Opening Km");
                    }

                    if (! isValidMeterReading(meterreading)) {
                        closingKm.requestFocus();
                        Common.showSnack(findViewById(R.id.updateBusLogoutLayout),"Please enter valid Closing Km");
                    }
                }
                break;
        }
    }*/

    @Override
    public void onClick(View view) {
        hideKeyboard(view);

        // Check if the view clicked is the submit button
        if (view.getId() == R.id.submit) {
            logouttime = logoutTime.getText().toString();
            remark = remarks.getText().toString();

            // Parse and validate fields
            if (closingKm.getText().toString() != null && !closingKm.getText().toString().equals("")) {
                meterreading = Integer.parseInt(closingKm.getText().toString());
            } else {
                meterreading = -1; // Default or invalid value
            }

            if (runKm.getText().toString() != null && !runKm.getText().toString().equals("")) {
                runKM = Integer.parseInt(runKm.getText().toString());
            } else {
                runKM = -1; // Default or invalid value
            }

            if (actualTrip.getText().toString() != null && !actualTrip.getText().toString().equals("")) {
                actualtrip = Integer.parseInt(actualTrip.getText().toString());
            } else {
                actualtrip = -1; // Default or invalid value
            }

            // Validate fields and perform actions
            if (isValidMeterReading(meterreading) && isValidMeterReading(runKM) && isValidTrip(actualtrip) && isValidLogoutTime(logouttime)) {
                UpdateBusLogout();
            } else {
                if (!isValidLogoutTime(logouttime)) {
                    Common.showSnack(findViewById(R.id.updateBusLogoutLayout), "Please enter valid logout time");
                }

                if (!isValidTrip(actualtrip)) {
                    Common.showSnack(findViewById(R.id.updateBusLogoutLayout), "Please enter valid completed trips");
                }

                if (!isValidMeterReading(runKM)) {
                    closingKm.requestFocus();
                    Common.showSnack(findViewById(R.id.updateBusLogoutLayout), "Please enter valid Closing Km\nClosing Km should be greater than Opening Km");
                }

                if (!isValidMeterReading(meterreading)) {
                    closingKm.requestFocus();
                    Common.showSnack(findViewById(R.id.updateBusLogoutLayout), "Please enter valid Closing Km");
                }
            }
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

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void UpdateBusLogout() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.updateBusLogoutLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Please Wait...");
            showDialog();

            Call<UpdateBusLogoutDataResponse> call = apiInterface.updateBusLogout(new UpdateBusLogoutDataRequest(logID, userID, logouttime, meterreading, runKM, actualtrip, remark, userName));

            call.enqueue(new Callback<UpdateBusLogoutDataResponse>() {
                @Override
                public void onResponse(Call<UpdateBusLogoutDataResponse> call, Response<UpdateBusLogoutDataResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                        finish();
                                    }
                                }, 1000);

                                Common.showSnack(findViewById(R.id.updateBusLogoutLayout), response.body().getMessage());

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                Common.showSnack(findViewById(R.id.updateBusLogoutLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.updateBusLogoutLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<UpdateBusLogoutDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(UpdateBusLogoutOPActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UpdateBusLogout();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private boolean isValidMeterReading(int meterreading) {
        return meterreading != 0;
    }

    private boolean isValidTrip(int trip) {
        return trip >= 0;
    }

    private boolean isValidLogoutTime(String logouttime) {
        return logouttime != null && logouttime.length() > 3;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showView(isConnected);
    }

    private void showView(boolean isConnected) {
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.updateBusLogoutLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showView(isConnected);
    }
}
