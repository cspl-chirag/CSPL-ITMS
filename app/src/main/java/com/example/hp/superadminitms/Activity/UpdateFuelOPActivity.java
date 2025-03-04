package com.example.hp.superadminitms.Activity;

import static com.example.hp.superadminitms.Helper.Common.convertDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Adapter.AdpSpinnerDriver;
import com.example.hp.superadminitms.Adapter.FuelDataOPAdapter;
import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.DriverDatum;
import com.example.hp.superadminitms.Model.FuelBusDatum;
import com.example.hp.superadminitms.Model.FuelTypeDatum;
import com.example.hp.superadminitms.MyApplication;
import com.example.hp.superadminitms.Network.BusRequest;
import com.example.hp.superadminitms.Network.BusResponse;
import com.example.hp.superadminitms.Network.DriverRequest;
import com.example.hp.superadminitms.Network.DriverResponse;
import com.example.hp.superadminitms.Network.UpdateFuelEntryDataRequest;
import com.example.hp.superadminitms.Network.UpdateFuelEntryDataResponse;
import com.example.hp.superadminitms.R;
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

public class UpdateFuelOPActivity extends BaseActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private SessionManager sessionManager;
    private ProgressDialog pDialog;
    private List<BusDatum> busList;
    private VehicleSpinnerAdapter busDataAdapter;
    private SearchableSpinner spnBus;
    private SearchableSpinner spnDriver;
    private SearchableSpinner spnFuelType;
    private EditText stationName;
    private EditText fuelDate;
    private EditText billNo;
    private EditText fuelRate;
    private EditText fuelQuantity;
    private EditText fuelAmount;
    private EditText meterReading;
    private TextView submit;
    private List<DriverDatum> driverList;
    private AdpSpinnerDriver driverAdapter;
    private ArrayList<FuelTypeDatum> fuelList;
    private FuelDataOPAdapter fuelAdapter;
    private FuelBusDatum fuelBusDatum;
    private String station;
    private String billno;
    private double rate = 0;
    private double quantity = 0;
    private double amount = 0;
    private int meterreading = 0;
    private String fuelType;
    private String driver;
    private int userID;
    private int companyID;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_fuel);

        initViews();

        fuelBusDatum = (FuelBusDatum) getIntent().getSerializableExtra("FuelBusData");
        Log.e("Data", fuelBusDatum.getVehicleName());

        try {
            fuelDate.setText(convertDateFormat(fuelBusDatum.getFuelDate(), "yyyy-MM-dd", "dd-MM-yyyy"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        meterReading.setText(String.valueOf(fuelBusDatum.getMeterReading()));
        fuelRate.setText(String.valueOf(fuelBusDatum.getRate()));
        fuelQuantity.setText(String.valueOf(fuelBusDatum.getQuantity()));
        fuelAmount.setText(String.valueOf(fuelBusDatum.getAmount()));
        billNo.setText(String.valueOf(fuelBusDatum.getBillNo()));

        initializeToolBar();

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
        getSupportActionBar().setTitle("Update Fuel Data");
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

        pDialog = new ProgressDialog(this, R.style.DialogBox);
        spnBus = findViewById(R.id.spnBus);
        spnDriver = findViewById(R.id.spnDriver);
        spnFuelType = findViewById(R.id.spnFuelType);
        stationName = findViewById(R.id.stationName);
        fuelDate = findViewById(R.id.fuelDate);
        billNo = findViewById(R.id.billNo);
        fuelRate = findViewById(R.id.fuelRate);
        fuelQuantity = findViewById(R.id.fuelQuantity);
        fuelAmount = findViewById(R.id.fuelAmount);
        meterReading = findViewById(R.id.openingKm);
        submit = findViewById(R.id.submit);

        spnBus.setEnabled(false);
        submit.setOnClickListener(this);

        fuelList = new ArrayList<>();
        fuelList.add(new FuelTypeDatum("DIESEL"));
        fuelList.add(new FuelTypeDatum("PETROL"));
        //fuelList.add(new FuelTypeDatum("CNG"));
        fuelAdapter = new FuelDataOPAdapter(UpdateFuelOPActivity.this, fuelList);

        spnFuelType.setAdapter(fuelAdapter);

        fuelAdapter.notifyDataSetChanged();

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

    /*@Override
    public void onClick(View view) {
        hideKeyboard(view);
        switch (view.getId()) {
            case R.id.submit:
                station = stationName.getText().toString();
                billno = billNo.getText().toString();

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
//                    BusDatum busDatum = (BusDatum) spnBus.getSelectedItem();
//                    vehicleID = busDatum.getVehicleID();
                    FuelTypeDatum fuelTypeDatum = (FuelTypeDatum) spnFuelType.getSelectedItem();
                    fuelType = fuelTypeDatum.getFuelType();
                    DriverDatum driverDatum = (DriverDatum) spnDriver.getSelectedItem();
                    driver = driverDatum.getStaffCode();

                    UpdateFuelEntry();
                } else {
                    if (!isValidMeterReading(meterreading)) {
                        meterReading.requestFocus();
                        Common.showSnack(findViewById(R.id.updateFuelLayout), "Please enter valid odometer reading");
                    }

                    if (!isValidAmount(amount)) {
                        fuelAmount.requestFocus();
                        Common.showSnack(findViewById(R.id.updateFuelLayout), "Please enter valid amount");
                    }

                    if (!isValidQuantity(quantity)) {
                        fuelQuantity.requestFocus();
                        Common.showSnack(findViewById(R.id.updateFuelLayout), "Please enter valid quantity");
                    }

                    if (!isValidQuantity(rate)) {
                        fuelRate.requestFocus();
                        Common.showSnack(findViewById(R.id.updateFuelLayout), "Please enter valid rate");
                    }

                    if (!isValidStationName(station)) {
                        stationName.requestFocus();
                        Common.showSnack(findViewById(R.id.updateFuelLayout), "Please enter valid station name");
                    }
                }
        }

    }*/


    @Override
    public void onClick(View view) {
        hideKeyboard(view);

        if (view.getId() == R.id.submit) {
            station = stationName.getText().toString();
            billno = billNo.getText().toString();

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

            boolean isValid = true;

            if (!isValidStationName(station)) {
                stationName.requestFocus();
                Common.showSnack(findViewById(R.id.updateFuelLayout), "Please enter valid station name");
                isValid = false;
            }
            if (!isValidFuelRate(rate)) {
                fuelRate.requestFocus();
                Common.showSnack(findViewById(R.id.updateFuelLayout), "Please enter valid rate");
                isValid = false;
            }
            if (!isValidQuantity(quantity)) {
                fuelQuantity.requestFocus();
                Common.showSnack(findViewById(R.id.updateFuelLayout), "Please enter valid quantity");
                isValid = false;
            }
            if (!isValidAmount(amount)) {
                fuelAmount.requestFocus();
                Common.showSnack(findViewById(R.id.updateFuelLayout), "Please enter valid amount");
                isValid = false;
            }
            if (!isValidMeterReading(meterreading)) {
                meterReading.requestFocus();
                Common.showSnack(findViewById(R.id.updateFuelLayout), "Please enter valid odometer reading");
                isValid = false;
            }

            if (isValid) {
                FuelTypeDatum fuelTypeDatum = (FuelTypeDatum) spnFuelType.getSelectedItem();
                fuelType = fuelTypeDatum.getFuelType();
                DriverDatum driverDatum = (DriverDatum) spnDriver.getSelectedItem();
                driver = driverDatum.getStaffCode();

                UpdateFuelEntry();
            }
        }
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

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void getBusListData() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(UpdateFuelOPActivity.this, R.style.DialogBox);
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
                                busDataAdapter = new VehicleSpinnerAdapter(UpdateFuelOPActivity.this, android.R.layout.simple_spinner_dropdown_item, busList);
                                spnBus.setAdapter(busDataAdapter);
                                busDataAdapter.notifyDataSetChanged();

                                for (int i = 0; i < busList.size(); i++) {
                                    if (busList.get(i).getVehicleID() == Integer.parseInt(fuelBusDatum.getVehicleId())) {
                                        spnBus.setSelection(i);
                                    }
                                }

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                Common.showSnack(findViewById(R.id.updateFuelLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.updateFuelLayout), response.body().getMessage());
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
                            new AlertDialog.Builder(UpdateFuelOPActivity.this, R.style.DialogBox);
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
                    new AlertDialog.Builder(UpdateFuelOPActivity.this, R.style.DialogBox);
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
                                driverAdapter = new AdpSpinnerDriver(UpdateFuelOPActivity.this, android.R.layout.simple_spinner_dropdown_item, driverList);

                                //driverDataAdapter = new DriverDataAdapter(FuelEntryActivity.this,driverList);
                                spnDriver.setAdapter(driverAdapter);
                                driverAdapter.notifyDataSetChanged();

                                for (int i = 0; i < driverList.size(); i++) {
                                    if (driverList.get(i).getStaffCode().equals(fuelBusDatum.getDriverName())) {
                                        spnDriver.setSelection(i);
                                    }
                                }
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
                                Common.showSnack(findViewById(R.id.updateFuelLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.updateFuelLayout), response.body().getMessage());
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
                            new AlertDialog.Builder(UpdateFuelOPActivity.this, R.style.DialogBox);
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

    private void UpdateFuelEntry() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.updateFuelLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Please Wait...");
            showDialog();

            Call<UpdateFuelEntryDataResponse> call = apiInterface.updateFuelEntry(new UpdateFuelEntryDataRequest(fuelBusDatum.getFuelID(), userID, companyID, fuelType, station, billno, quantity, rate, meterreading, amount, driver, userName));

            call.enqueue(new Callback<UpdateFuelEntryDataResponse>() {
                @Override
                public void onResponse(Call<UpdateFuelEntryDataResponse> call, Response<UpdateFuelEntryDataResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                        finish();
                                    }
                                }, 500);

                                Common.showSnack(findViewById(R.id.updateFuelLayout), response.body().getMessage());

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                Common.showSnack(findViewById(R.id.updateFuelLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.updateFuelLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<UpdateFuelEntryDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(UpdateFuelOPActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UpdateFuelEntry();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private boolean isValidStationName(String station) {
        return station != null && station.length() > 0 && station.length() <= 50;
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
