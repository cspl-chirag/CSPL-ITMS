package com.example.hp.superadminitms.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.BusLoginOPDatum;
import com.example.hp.superadminitms.MyApplication;
import com.example.hp.superadminitms.Network.AddBusLogoutDataRequest;
import com.example.hp.superadminitms.Network.AddBusLogoutDataResponse;
import com.example.hp.superadminitms.Network.BusRequest;
import com.example.hp.superadminitms.Network.BusResponse;
import com.example.hp.superadminitms.Network.GetBusLoginDataRequest;
import com.example.hp.superadminitms.Network.GetBusLoginDataResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;
import com.example.hp.superadminitms.utils.SearchableSpinner;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusLogoutOPActivity extends BaseActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener, AdapterView.OnItemSelectedListener {

    private EditText driverName;
    private EditText openingKm;
    private EditText remarks;
    private TextView submit;
    private int meterreading = 0;
    private SessionManager sessionManager;
    private int userID;
    private int companyID;
    private String userName;
    private ProgressDialog pDialog;
    private SearchableSpinner spnBus;
    private EditText logsheetNumber;
    private String route;
    private String driver;
    private Integer vehicleID = 0;
    private List<BusDatum> busList;
    private String remark;
    private List<BusLoginOPDatum> busLoginData;
    private EditText depotName;
    private EditText totalTrip;
    private EditText closingKm;
    private EditText runKm;
    private EditText actualTrip;
    private EditText logoutTime;
    private int runKM = 0;
    private String logouttime;
    private int actualtrip = -1;
    private Integer logID = 0;
    private EditText routeNumber;
    private EditText logDate;
    private VehicleSpinnerAdapter busDataAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_logout);
        initializeToolBar();
        initViews();

        checkConnection();

        getBusListData();
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
        getSupportActionBar().setTitle("Bus Logout");
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
        userID = sessionManager.getKeyUserId();
        companyID = sessionManager.getKeyCompanyId();
        userName = sessionManager.getKeyUserName();
        // Progress dialog
        pDialog = new ProgressDialog(this, R.style.DialogBox);
        pDialog.setCancelable(false);

        logDate = findViewById(R.id.logDate);
        spnBus = findViewById(R.id.spnBus);
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

        spnBus.setTitle("Select Bus");


        spnBus.setOnItemSelectedListener(this);
        spnBus.setSelection(0);

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

        logDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Calendar cal = Calendar.getInstance(Locale.getDefault());
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            BusLogoutOPActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int d, int m, int y) {
                            int month = m + 1;
                            logDate.setText(Common.getDate(d, month, y));

                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
                    );
                    datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                    datePickerDialog.show();
                    logDate.clearFocus();
                }
            }
        });

        if (logDate.getText().toString().isEmpty()) {
            logDate.setText(Common.getCurrentDate());
        }

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

                    TimePickerDialog timePickerDialog = new TimePickerDialog(BusLogoutOPActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_update_fuel, menu);
        return super.onCreateOptionsMenu(menu);
    }

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.verify_fuel:
                startActivity(new Intent(BusLogoutOPActivity.this,BusLogoutListOPActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check if the item ID matches android.R.id.home
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        // Check if the item ID matches R.id.verify_fuel
        else if (item.getItemId() == R.id.verify_fuel) {
            startActivity(new Intent(BusLogoutOPActivity.this, BusLogoutListOPActivity.class));
            return true;
        }
        // Default case to handle other menu items
        else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        vehicleID = busList.get(i).getVehicleID();
        getBusLoginDataForLogout();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                    BusDatum busDatum = (BusDatum) spnBus.getSelectedItem();
                    vehicleID = busDatum.getVehicleID();

                    AddBusLogout();
                } else {
                    if (! isValidLogoutTime(logouttime)) {
                        //loginTime.requestFocus();
                        Common.showSnack(findViewById(R.id.busLogoutLayout),"Please enter valid logout time");
                    }

                    if (! isValidTrip(actualtrip)) {
                        //loginTime.requestFocus();
                        Common.showSnack(findViewById(R.id.busLogoutLayout),"Please enter valid completed trips");
                    }

                    if (! isValidMeterReading(runKM)) {
                        closingKm.requestFocus();
                        Common.showSnack(findViewById(R.id.busLogoutLayout),"Please enter valid Closing Km\nClosing Km should be greater than Opening Km");
                    }

                    if (! isValidMeterReading(meterreading)) {
                        closingKm.requestFocus();
                        Common.showSnack(findViewById(R.id.busLogoutLayout),"Please enter valid Closing Km");
                    }
                }
                break;
        }
    }*/

    @Override
    public void onClick(View view) {
        hideKeyboard(view);
        int id = view.getId();

        if (id == R.id.submit) {
            logouttime = logoutTime.getText().toString();
            remark = remarks.getText().toString();

            if (closingKm.getText().toString() != null && !closingKm.getText().toString().equals("")) {
                meterreading = Integer.parseInt(closingKm.getText().toString());
            }

            if (runKm.getText().toString() != null && !runKm.getText().toString().equals("")) {
                runKM = Integer.parseInt(runKm.getText().toString());
            }

            if (actualTrip.getText().toString() != null && !actualTrip.getText().toString().equals("")) {
                actualtrip = Integer.parseInt(actualTrip.getText().toString());
            }

            if (isValidMeterReading(meterreading) && isValidMeterReading(runKM) && isValidTrip(actualtrip) && isValidLogoutTime(logouttime)) {
                BusDatum busDatum = (BusDatum) spnBus.getSelectedItem();
                vehicleID = busDatum.getVehicleID();

                AddBusLogout();
            } else {
                if (!isValidLogoutTime(logouttime)) {
                    Common.showSnack(findViewById(R.id.busLogoutLayout), "Please enter valid logout time");
                }

                if (!isValidTrip(actualtrip)) {
                    Common.showSnack(findViewById(R.id.busLogoutLayout), "Please enter valid completed trips");
                }

                if (!isValidMeterReading(runKM)) {
                    closingKm.requestFocus();
                    Common.showSnack(findViewById(R.id.busLogoutLayout), "Please enter valid Closing Km\nClosing Km should be greater than Opening Km");
                }

                if (!isValidMeterReading(meterreading)) {
                    closingKm.requestFocus();
                    Common.showSnack(findViewById(R.id.busLogoutLayout), "Please enter valid Closing Km");
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

    private void getBusListData() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(BusLogoutOPActivity.this, R.style.DialogBox);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getBusListData();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Getting Vehicle Data...");
            showDialog();

            Call<BusResponse> call = apiInterface.getBusListForLogout(new BusRequest(sessionManager.getKeyCompanyId()));

            call.enqueue(new Callback<BusResponse>() {
                @Override
                public void onResponse(Call<BusResponse> call, Response<BusResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);

                                busList = response.body().getBusData();
                                busDataAdapter = new VehicleSpinnerAdapter(BusLogoutOPActivity.this, android.R.layout.simple_spinner_dropdown_item, busList);
                                spnBus.setAdapter(busDataAdapter);
                                busDataAdapter.notifyDataSetChanged();

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                //clearUI();
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(BusLogoutOPActivity.this, R.style.DialogBox);
                                builder.setTitle(response.body().getMessage());
                                builder.setMessage(getResources().getString(R.string.BusListErrorMsg));
                                builder.setPositiveButton(getResources().getString(R.string.BusListErrorBtnTxt), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        onBackPressed();
                                    }
                                });
                                builder.setCancelable(false);
                                builder.show();
                                //Common.showSnack(findViewById(R.id.busLogoutLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.busLogoutLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<BusResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(BusLogoutOPActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getBusListData();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void getBusLoginDataForLogout() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(BusLogoutOPActivity.this, R.style.DialogBox);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getBusListData();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Getting Login Data...");
            showDialog();

            Call<GetBusLoginDataResponse> call = apiInterface.getBusLoginDataForLogout(new GetBusLoginDataRequest(sessionManager.getKeyCompanyId(), vehicleID));

            call.enqueue(new Callback<GetBusLoginDataResponse>() {
                @Override
                public void onResponse(Call<GetBusLoginDataResponse> call, Response<GetBusLoginDataResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);

                                busLoginData = response.body().getBusLoginData();
                                depotName.setText(busLoginData.get(0).getDeptName());
                                logsheetNumber.setText(busLoginData.get(0).getLogsheetCode());
                                routeNumber.setText(busLoginData.get(0).getRouteNo());
                                //totalTrip.setText(String.valueOf(busLoginData.get(0).getTotalTrip()));
                                driverName.setText(busLoginData.get(0).getDriverName());
                                openingKm.setText(String.valueOf(busLoginData.get(0).getLoginKm()));
                                logID = busLoginData.get(0).getLogID();

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                Common.showSnack(findViewById(R.id.busLogoutLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.busLogoutLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<GetBusLoginDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(BusLogoutOPActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getBusListData();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void AddBusLogout() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.busLogoutLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Please Wait...");
            showDialog();

            Call<AddBusLogoutDataResponse> call = null;
            try {
                call = apiInterface.addBusLogoutEntry(new AddBusLogoutDataRequest(logID, userID, Common.convertDateFormat(logDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"), logouttime, meterreading, runKM, actualtrip, remark, userName));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            call.enqueue(new Callback<AddBusLogoutDataResponse>() {
                @Override
                public void onResponse(Call<AddBusLogoutDataResponse> call, Response<AddBusLogoutDataResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);

                                Common.showSnack(findViewById(R.id.busLogoutLayout), response.body().getMessage());
                                clearUI();

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                Common.showSnack(findViewById(R.id.busLogoutLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.busLogoutLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<AddBusLogoutDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(BusLogoutOPActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddBusLogout();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void clearUI() {
        spnBus.setSelection(0);
        getBusListData();
        closingKm.setText("");
        runKm.setText("");
        actualTrip.setText("");
        logoutTime.setText(Common.getCurrentTime());
        remarks.setText("");
        meterreading = 0;
        actualtrip = -1;
        runKM = 0;
        findViewById(R.id.scrollFuelEntry).scrollTo(0, 0);
        findViewById(R.id.busLogoutLayout).requestFocus();
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
            Common.showSnack(this.findViewById(R.id.busLogoutLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showView(isConnected);
    }
}
