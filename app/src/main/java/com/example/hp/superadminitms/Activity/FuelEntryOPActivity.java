package com.example.hp.superadminitms.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Adapter.AdpSpinnerDriver;
import com.example.hp.superadminitms.Adapter.FuelDataOPAdapter;
import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.DriverDatum;
import com.example.hp.superadminitms.Model.FuelTypeDatum;
import com.example.hp.superadminitms.MyApplication;
import com.example.hp.superadminitms.Network.AddFuelEntryDataRequest;
import com.example.hp.superadminitms.Network.AddFuelEntryDataResponse;
import com.example.hp.superadminitms.Network.BusRequest;
import com.example.hp.superadminitms.Network.BusResponse;
import com.example.hp.superadminitms.Network.DriverRequest;
import com.example.hp.superadminitms.Network.DriverResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;
import com.example.hp.superadminitms.utils.SearchableSpinner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FuelEntryOPActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {

    private SearchableSpinner spnBus;
    private List<BusDatum> busList;
    private VehicleSpinnerAdapter busDataAdapter;
    private ArrayList<FuelTypeDatum> fuelList;
    private FuelDataOPAdapter fuelAdapter;
    private Spinner spnFuelType;
    private SessionManager sessionManager;
    private ProgressDialog pDialog;
    private EditText routeName;
    private SearchableSpinner spnDriver;
    private EditText stationName;
    private EditText billNo;
    private EditText couponCode;
    private EditText fuelRate;
    private EditText fuelQuantity;
    private EditText fuelAmount;
    private EditText meterReading;
    private TextView submit;
    private int userID;
    private int companyID;
    private String userName;
    private String route;
    private String driver;
    private String station;
    private String billno;
    private String coupon;
    //private double billno=0;
    //private double coupon=0;
    private double rate = 0;
    private double quantity = 0;
    private double amount = 0;
    private int meterreading = 0;
    private Integer vehicleID = 0;
    private String fuelType;
    private List<DriverDatum> driverList;
    private AdpSpinnerDriver driverAdapter;
    private EditText fuelDate;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_entry);

        initializeToolBar();
        initViews();

        checkConnection();

        getBusListData();

        getDriverListData();
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
        getSupportActionBar().setTitle("Fuel Entry");
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

        spnBus = findViewById(R.id.spnBus);
        routeName = findViewById(R.id.logsheetNumber);
        spnDriver = findViewById(R.id.spnDriver);
        spnFuelType = findViewById(R.id.spnFuelType);
        stationName = findViewById(R.id.stationName);
        fuelDate = findViewById(R.id.fuelDate);
        billNo = findViewById(R.id.billNo);
        couponCode = findViewById(R.id.couponCode);
        fuelRate = findViewById(R.id.fuelRate);
        fuelQuantity = findViewById(R.id.fuelQuantity);
        fuelAmount = findViewById(R.id.fuelAmount);
        meterReading = findViewById(R.id.openingKm);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(this);

        spnBus.setTitle("Select Bus");
        spnDriver.setTitle("Select Driver");

        fuelList = new ArrayList<>();
        fuelList.add(new FuelTypeDatum("DIESEL"));
        fuelList.add(new FuelTypeDatum("PETROL"));
        //fuelList.add(new FuelTypeDatum("CNG"));
        fuelAdapter = new FuelDataOPAdapter(FuelEntryOPActivity.this, fuelList);

        spnFuelType.setAdapter(fuelAdapter);

        fuelAdapter.notifyDataSetChanged();

        fuelDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Calendar cal = Calendar.getInstance(Locale.getDefault());
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            FuelEntryOPActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int d, int m, int y) {
                            int month = m + 1;
                            fuelDate.setText(Common.getDate(d, month, y));

                        }
                    }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
                    );
                    datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                    datePickerDialog.show();
                    fuelDate.clearFocus();
                }
            }
        });

        if (fuelDate.getText().toString().isEmpty()) {
            fuelDate.setText(Common.getCurrentDate());
        }

        fuelRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if ((fuelRate.getText().toString() != null && !fuelRate.getText().toString().equals("")) && (fuelQuantity.getText().toString() != null && !fuelQuantity.getText().toString().equals(""))) {
                    double total = Double.parseDouble(fuelRate.getText().toString()) * Double.parseDouble(fuelQuantity.getText().toString());
                    fuelAmount.setText(String.valueOf(total));
                }
            }
        });

        fuelQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if ((fuelRate.getText().toString() != null && !fuelRate.getText().toString().equals("")) && (fuelQuantity.getText().toString() != null && !fuelQuantity.getText().toString().equals(""))) {
                    double total = Double.parseDouble(fuelRate.getText().toString()) * Double.parseDouble(fuelQuantity.getText().toString());
                    fuelAmount.setText(String.valueOf(total));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_update_fuel, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.verify_fuel:
                startActivity(new Intent(FuelEntryOPActivity.this,FuelBusOPActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.verify_fuel) {
            startActivity(new Intent(FuelEntryOPActivity.this, FuelBusOPActivity.class));
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    /*@Override
    public void onClick(View view) {
        hideKeyboard(view);
        switch (view.getId()) {
            case R.id.submit:
                route = routeName.getText().toString();
                //driver = spnDriver.getText().toString();
                station = stationName.getText().toString();
                billno = billNo.getText().toString();
                coupon = couponCode.getText().toString();
                try {
                    date = Common.convertDateFormat(fuelDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (fuelRate.getText().toString() != null && !fuelRate.getText().toString().equals("")) {
                    rate = Double.parseDouble(fuelRate.getText().toString());
                }
                if (fuelQuantity.getText().toString() != null && !fuelQuantity.getText().toString().equals("")) {
                    quantity = Double.parseDouble(fuelQuantity.getText().toString());
                }
                if (fuelAmount.getText().toString() != null && !fuelAmount.getText().toString().equals("")) {
                    amount = Double.parseDouble(fuelAmount.getText().toString());
                }
                if (meterReading.getText().toString() != null && !meterReading.getText().toString().equals("")) {
                    meterreading = Integer.parseInt(meterReading.getText().toString());
                }


                if (isValidStationName(station) && isValidFuelRate(rate) && isValidQuantity(quantity) && isValidAmount(amount) && isValidMeterReading(meterreading)) {
                    BusDatum busDatum = (BusDatum) spnBus.getSelectedItem();
                    vehicleID = busDatum.getVehicleID();
                    FuelTypeDatum fuelTypeDatum = (FuelTypeDatum) spnFuelType.getSelectedItem();
                    fuelType = fuelTypeDatum.getFuelType();
                    DriverDatum driverDatum = (DriverDatum) spnDriver.getSelectedItem();
                    driver = driverDatum.getStaffCode();

                    AddFuelEntry();
                } else {
                    if (!isValidMeterReading(meterreading)) {
                        meterReading.requestFocus();
                        Common.showSnack(findViewById(R.id.fuelEntryLayout), "Please enter valid odometer reading");
                    }

                    if (!isValidAmount(amount)) {
                        fuelAmount.requestFocus();
                        Common.showSnack(findViewById(R.id.fuelEntryLayout), "Please enter valid amount");
                    }

                    if (!isValidQuantity(quantity)) {
                        fuelQuantity.requestFocus();
                        Common.showSnack(findViewById(R.id.fuelEntryLayout), "Please enter valid quantity");
                    }

                    if (!isValidQuantity(rate)) {
                        fuelRate.requestFocus();
                        Common.showSnack(findViewById(R.id.fuelEntryLayout), "Please enter valid rate");
                    }

//                    if (! isValidBillNo(billno)) {
//                        billNo.requestFocus();
//                        Common.showSnack(findViewById(R.id.fuelEntryLayout),"Please enter valid bill number");
//                    }

                    if (!isValidStationName(station)) {
                        stationName.requestFocus();
                        Common.showSnack(findViewById(R.id.fuelEntryLayout), "Please enter valid station name");
                    }

//                    if (! isValidDriverName(driver)) {
//                        spnDriver.requestFocus();
//                        Common.showSnack(findViewById(R.id.fuelEntryLayout),"Please enter valid driver name");
//                    }
//
//                    if (! isValidRouteName(route)) {
//                        routeName.requestFocus();
//                        Common.showSnack(findViewById(R.id.fuelEntryLayout),"Please enter valid route name");
//                    }
                }
                break;
        }
    }*/

    @Override
    public void onClick(View view) {
        hideKeyboard(view);
        int id = view.getId();

        if (id == R.id.submit) {
            route = routeName.getText().toString();
            //driver = spnDriver.getText().toString();
            station = stationName.getText().toString();
            billno = billNo.getText().toString();
            coupon = couponCode.getText().toString();
            try {
                date = Common.convertDateFormat(fuelDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (fuelRate.getText().toString() != null && !fuelRate.getText().toString().equals("")) {
                rate = Double.parseDouble(fuelRate.getText().toString());
            }
            if (fuelQuantity.getText().toString() != null && !fuelQuantity.getText().toString().equals("")) {
                quantity = Double.parseDouble(fuelQuantity.getText().toString());
            }
            if (fuelAmount.getText().toString() != null && !fuelAmount.getText().toString().equals("")) {
                amount = Double.parseDouble(fuelAmount.getText().toString());
            }
            if (meterReading.getText().toString() != null && !meterReading.getText().toString().equals("")) {
                meterreading = Integer.parseInt(meterReading.getText().toString());
            }

            if (isValidStationName(station) && isValidFuelRate(rate) && isValidQuantity(quantity) && isValidAmount(amount) && isValidMeterReading(meterreading)) {
                BusDatum busDatum = (BusDatum) spnBus.getSelectedItem();
                vehicleID = busDatum.getVehicleID();
                FuelTypeDatum fuelTypeDatum = (FuelTypeDatum) spnFuelType.getSelectedItem();
                fuelType = fuelTypeDatum.getFuelType();
                DriverDatum driverDatum = (DriverDatum) spnDriver.getSelectedItem();
                driver = driverDatum.getStaffCode();

                AddFuelEntry();
            } else {
                if (!isValidMeterReading(meterreading)) {
                    meterReading.requestFocus();
                    Common.showSnack(findViewById(R.id.fuelEntryLayout), "Please enter valid odometer reading");
                }

                if (!isValidAmount(amount)) {
                    fuelAmount.requestFocus();
                    Common.showSnack(findViewById(R.id.fuelEntryLayout), "Please enter valid amount");
                }

                if (!isValidQuantity(quantity)) {
                    fuelQuantity.requestFocus();
                    Common.showSnack(findViewById(R.id.fuelEntryLayout), "Please enter valid quantity");
                }

                if (!isValidFuelRate(rate)) {
                    fuelRate.requestFocus();
                    Common.showSnack(findViewById(R.id.fuelEntryLayout), "Please enter valid rate");
                }
                if (!isValidStationName(station)) {
                    stationName.requestFocus();
                    Common.showSnack(findViewById(R.id.fuelEntryLayout), "Please enter valid station name");
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
                    new AlertDialog.Builder(FuelEntryOPActivity.this, R.style.DialogBox);
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

            Call<BusResponse> call = apiInterface.getBusList(new BusRequest(sessionManager.getKeyCompanyId()));

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
                                busDataAdapter = new VehicleSpinnerAdapter(FuelEntryOPActivity.this, android.R.layout.simple_spinner_dropdown_item, busList);
                                spnBus.setAdapter(busDataAdapter);
                                busDataAdapter.notifyDataSetChanged();

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                Common.showSnack(findViewById(R.id.fuelEntryLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.fuelEntryLayout), response.body().getMessage());
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
                            new AlertDialog.Builder(FuelEntryOPActivity.this, R.style.DialogBox);
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

    private void getDriverListData() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(FuelEntryOPActivity.this, R.style.DialogBox);
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

            pDialog.setMessage("Getting Driver Data...");
            showDialog();

            Call<DriverResponse> call = apiInterface.getDriverList(new DriverRequest(sessionManager.getKeyCompanyId()));

            call.enqueue(new Callback<DriverResponse>() {
                @Override
                public void onResponse(Call<DriverResponse> call, Response<DriverResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);

                                driverList = response.body().getDriverData();
                                driverAdapter = new AdpSpinnerDriver(FuelEntryOPActivity.this, android.R.layout.simple_spinner_dropdown_item, driverList);

                                //driverDataAdapter = new DriverDataAdapter(FuelEntryOPActivity.this,driverList);
                                spnDriver.setAdapter(driverAdapter);
                                driverAdapter.notifyDataSetChanged();

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
//                                AlertDialog.Builder builder =
//                                        new AlertDialog.Builder(BusLoginActivity.this);
//                                builder.setTitle(response.body().getMessage());
//                                builder.setMessage(getResources().getString(R.string.BusListErrorMsg1));
//                                builder.setPositiveButton(getResources().getString(R.string.BusListErrorBtnTxt), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        onBackPressed();
//                                    }
//                                });
//                                builder.setCancelable(false);
//                                builder.show();
                                Common.showSnack(findViewById(R.id.fuelEntryLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.fuelEntryLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<DriverResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(FuelEntryOPActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getDriverListData();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void AddFuelEntry() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Please Wait...");
            showDialog();

            Call<AddFuelEntryDataResponse> call = apiInterface.addFuelEntry(new AddFuelEntryDataRequest(userID, companyID, date, fuelType, station, billno, quantity, rate, meterreading, amount, vehicleID, driver, userName));

            call.enqueue(new Callback<AddFuelEntryDataResponse>() {
                @Override
                public void onResponse(Call<AddFuelEntryDataResponse> call, Response<AddFuelEntryDataResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);

                                Common.showSnack(findViewById(R.id.fuelEntryLayout), response.body().getMessage());
                                clearUI();

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                Common.showSnack(findViewById(R.id.fuelEntryLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.fuelEntryLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<AddFuelEntryDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(FuelEntryOPActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddFuelEntry();
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
        spnFuelType.setSelection(0);
        //routeName.setText("");
        spnDriver.setSelection(0);
        //stationName.setText("");
        billNo.setText("");
        couponCode.setText("");
        //fuelRate.setText("");
        fuelQuantity.setText("");
        fuelAmount.setText("");
        meterReading.setText("");
        billno = null;
        coupon = null;
        rate = 0;
        quantity = 0;
        amount = 0;
        meterreading = 0;
        findViewById(R.id.fuelEntryLayout).requestFocus();
        findViewById(R.id.scrollFuelEntry).scrollTo(0, 0);
    }

    private boolean isValidRouteName(String route) {
        return route != null && route.length() > 0 && route.length() <= 50;
    }

    private boolean isValidDriverName(String driver) {
        return driver != null && driver.length() > 0 && driver.length() <= 50;
    }

    private boolean isValidStationName(String station) {
        return station != null && station.length() > 0 && station.length() <= 50;
    }

    private boolean isValidBillNo(String billno) {
        return billno != null && billno.length() > 0 && billno.length() <= 18;
    }

    private boolean isValidFuelRate(double fuelRate) {
        return fuelRate != 0;
    }

    private boolean isValidQuantity(double quantity) {
        return quantity != 0;
    }

    private boolean isValidAmount(double amount) {
        return amount != 0;
    }

    private boolean isValidMeterReading(int meterreading) {
        return meterreading != 0;
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
            Common.showSnack(this.findViewById(R.id.fuelEntryLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showView(isConnected);
    }
}
