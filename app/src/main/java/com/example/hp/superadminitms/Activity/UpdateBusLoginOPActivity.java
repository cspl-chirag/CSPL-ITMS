package com.example.hp.superadminitms.Activity;

import static com.example.hp.superadminitms.Helper.Common.convertDateFormat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Adapter.AdpSpinnerDriver;
import com.example.hp.superadminitms.Adapter.LogsheetOPAdapter;
import com.example.hp.superadminitms.Adapter.RouteSpinnerAdapter;
import com.example.hp.superadminitms.Adapter.ShiftDataOPAdapter;
import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.DriverDatum;
import com.example.hp.superadminitms.Model.LoggedInBusDatum;
import com.example.hp.superadminitms.Model.LogsheetDatum;
import com.example.hp.superadminitms.Model.RouteDatum;
import com.example.hp.superadminitms.Model.ShiftDatum;
import com.example.hp.superadminitms.MyApplication;
import com.example.hp.superadminitms.Network.BusRequest;
import com.example.hp.superadminitms.Network.BusResponse;
import com.example.hp.superadminitms.Network.DriverRequest;
import com.example.hp.superadminitms.Network.DriverResponse;
import com.example.hp.superadminitms.Network.GetLastLogOutKmDataRequest;
import com.example.hp.superadminitms.Network.GetLastLogOutKmDataResponse;
import com.example.hp.superadminitms.Network.LogsheetListDataRequest;
import com.example.hp.superadminitms.Network.LogsheetListDataResponse;
import com.example.hp.superadminitms.Network.RouteRequest;
import com.example.hp.superadminitms.Network.RouteResponse;
import com.example.hp.superadminitms.Network.UpdateBusLoginDataRequest;
import com.example.hp.superadminitms.Network.UpdateBusLoginDataResponse;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class UpdateBusLoginOPActivity extends BaseActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener, AdapterView.OnItemSelectedListener {

    private SessionManager sessionManager;
    private int userID;
    private int companyID;
    private String userName;
    private ProgressDialog pDialog;
    private SearchableSpinner spnBus;
    private EditText depotName;
    private SearchableSpinner spnRoute;
    private SearchableSpinner spnLogsheet;
    private EditText routeNumber;
    private EditText totalTrip;
    private EditText loginTime;
    private SearchableSpinner spnDriver;
    private EditText lastClosingKm;
    private EditText openingKm;
    private EditText differenceKm;
    private TextView submit;
    private List<BusDatum> busList;
    private VehicleSpinnerAdapter busDataAdapter;
    private List<DriverDatum> driverList;
    private String logsheetCode;
    private int totaltrip = 0;
    private String logintime;
    private int openingKM = 0;
    private String driver;
    private List<LogsheetDatum> logsheetList;
    private RouteSpinnerAdapter routeDataOPAdapter;
    private Integer depotID;
    private AdpSpinnerDriver driverAdapter;
    private Spinner spnShift;
    private ArrayList<ShiftDatum> shiftList;
    private ShiftDataOPAdapter shiftAdapter;
    private List<RouteDatum> routeList;
    private Integer routeID = 0;
    private int shiftID = 0;
    private Integer vehicleID = 0;
    private Integer lastLogOutKm;
    private LogsheetOPAdapter logsheetAdapter;
    private LoggedInBusDatum loggedInBusDatum;
    private Integer logID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_bus_login);

        initializeToolBar();

        initViews();

        checkConnection();

        getBusListData();

        getRouteListData();

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
        getSupportActionBar().setTitle("Update Bus Login");
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
        loggedInBusDatum = (LoggedInBusDatum) getIntent().getSerializableExtra("LoggedInBusData");
        logID = loggedInBusDatum.getLogID();

        sessionManager = new SessionManager(this);
        userID = sessionManager.getKeyUserId();
        companyID = sessionManager.getKeyCompanyId();
        userName = sessionManager.getKeyUserName();
        // Progress dialog
        pDialog = new ProgressDialog(this, R.style.DialogBox);
        pDialog.setCancelable(false);

        spnBus = findViewById(R.id.spnBus);
        depotName = findViewById(R.id.depotName);
        spnRoute = findViewById(R.id.spnRoute);
        spnLogsheet = findViewById(R.id.spnLogsheet);
        spnShift = findViewById(R.id.spnShift);
        routeNumber = findViewById(R.id.routeNumber);
        totalTrip = findViewById(R.id.totalTrip);
        spnDriver = findViewById(R.id.spnDriver);
        lastClosingKm = findViewById(R.id.lastClosingKm);
        openingKm = findViewById(R.id.openingKm);
        differenceKm = findViewById(R.id.differenceKm);
        loginTime = findViewById(R.id.loginTime);
        submit = findViewById(R.id.submit);

        spnBus.setTitle("Select Bus");
        spnRoute.setTitle("Select Route");
        spnLogsheet.setTitle("Select Logsheet");
        spnDriver.setTitle("Select Driver");

        spnBus.setOnItemSelectedListener(this);

        shiftList = new ArrayList<>();
        shiftList.add(new ShiftDatum(1, "1st Shift"));
        shiftList.add(new ShiftDatum(2, "2nd Shift"));
        //fuelList.add(new FuelTypeDatum("CNG"));
        shiftAdapter = new ShiftDataOPAdapter(UpdateBusLoginOPActivity.this, shiftList);

        spnShift.setAdapter(shiftAdapter);

        shiftAdapter.notifyDataSetChanged();

        for (int i = 0; i < shiftList.size(); i++) {
            if (shiftList.get(i).getShiftNo() == loggedInBusDatum.getShift()) {
                spnShift.setSelection(i);
            }
        }


        if (loginTime.getText().toString().isEmpty()) {
            loginTime.setText(Common.getCurrentTime());
        }

        loginTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    hideKeyboard(view);
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(UpdateBusLoginOPActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            loginTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, false);
                    timePickerDialog.show();
                    loginTime.clearFocus();
                }
            }
        });

        openingKm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (openingKm.getText().toString() != null && !openingKm.getText().toString().equals("")) {
                    int diffKm = Integer.parseInt(openingKm.getText().toString()) - lastLogOutKm;
                    if (diffKm >= 0) {
                        differenceKm.setText(String.valueOf(diffKm));
                        differenceKm.setTextColor(Color.parseColor("#ffff7c"));
                    } else {
                        differenceKm.setText(String.valueOf(diffKm));
                        differenceKm.setTextColor(Color.parseColor("#ff8800"));
                    }
                } else {
                    differenceKm.setText("0");
                    differenceKm.setTextColor(Color.parseColor("#ffff7c"));
                }
            }
        });

        try {
            loginTime.setText(convertDateFormat(loggedInBusDatum.getLoginTime(), "HH:mm:ss", "HH:mm"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        spnRoute.setOnItemSelectedListener(this);

        spnLogsheet.setOnItemSelectedListener(this);

        submit.setOnClickListener(this);
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

    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spnRoute:
                RouteDatum routeDatum = (RouteDatum) spnRoute.getSelectedItem();
                routeID = routeDatum.getRouteID();
                getLogsheetListData();
                break;
            case R.id.spnLogsheet:
                LogsheetDatum logsheetDatum = (LogsheetDatum) spnLogsheet.getSelectedItem();
                routeID = logsheetDatum.getRouteID();
                routeNumber.setText(logsheetDatum.getRouteNo());
                logsheetCode = logsheetDatum.getLogsheetCode();
                depotName.setText(logsheetDatum.getDeptName());
                depotID = logsheetDatum.getDeptID();
                //totaltrip=routeDatum.getTotalTrip();
                break;
            case R.id.spnBus:
                vehicleID = busList.get(i).getVehicleID();
                getLastLogOutKm();
                break;
        }

    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();

        if (id == R.id.spnRoute) {
            RouteDatum routeDatum = (RouteDatum) spnRoute.getSelectedItem();
            routeID = routeDatum.getRouteID();
            getLogsheetListData();
        } else if (id == R.id.spnLogsheet) {
            LogsheetDatum logsheetDatum = (LogsheetDatum) spnLogsheet.getSelectedItem();
            routeID = logsheetDatum.getRouteID();
            routeNumber.setText(logsheetDatum.getRouteNo());
            logsheetCode = logsheetDatum.getLogsheetCode();
            depotName.setText(logsheetDatum.getDeptName());
            depotID = logsheetDatum.getDeptID();
            //totaltrip=routeDatum.getTotalTrip();
        } else if (id == R.id.spnBus) {
            vehicleID = busList.get(i).getVehicleID();
            getLastLogOutKm();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

   /* @Override
    public void onClick(View view) {
        hideKeyboard(view);
        switch (view.getId()) {
            case R.id.submit:
                //depotname = depotName.getText().toString();
                //route = routeNumber.getText().toString();
                //driver = spnDriver.getText().toString();
                logintime = loginTime.getText().toString();
                //depotname = depotName.getText().toString();

//                if (totalTrip.getText().toString() != null && !totalTrip.getText().toString().equals("")) {
//                    totaltrip = Integer.parseInt(totalTrip.getText().toString());
//                }

                if (openingKm.getText().toString() != null && !openingKm.getText().toString().equals("")) {
                    openingKM = Integer.parseInt(openingKm.getText().toString());
                }

                if (isValidMeterReading(openingKM) && isValidloginTime(logintime)) {
                    BusDatum busDatum = (BusDatum) spnBus.getSelectedItem();
//                    DepotDatum depotDatum = (DepotDatum) depotName.getSelectedItem();
                    ShiftDatum shiftDatum = (ShiftDatum) spnShift.getSelectedItem();
                    DriverDatum driverDatum = (DriverDatum) spnDriver.getSelectedItem();

                    shiftID = shiftDatum.getShiftNo();
                    vehicleID = busDatum.getVehicleID();
                    driver = driverDatum.getStaffCode();

                    UpdateBusLogin();
                } else {
                    if (!isValidloginTime(logintime)) {
                        //loginTime.requestFocus();
                        Common.showSnack(findViewById(R.id.updateBusLoginLayout), "Please enter valid login time");
                    }

                    if (!isValidMeterReading(openingKM)) {
                        openingKm.requestFocus();
                        Common.showSnack(findViewById(R.id.updateBusLoginLayout), "Please enter valid Opening Km");
                    }

//                    if (! isValidDriverName(driver)) {
//                        spnDriver.requestFocus();
//                        Common.showSnack(findViewById(R.id.busLoginLayout),"Please enter valid driver name");
//                    }

//                    if (! isValidTrip(totaltrip)) {
//                        totalTrip.requestFocus();
//                        Common.showSnack(findViewById(R.id.busLoginLayout),"Please enter valid total trips");
//                    }

//                    if (! isValidDepot(depotname)) {
//                        depotName.requestFocus();
//                        Common.showSnack(findViewById(R.id.busLoginLayout),"Please enter valid depot name");
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

            logintime = loginTime.getText().toString();

            if (openingKm.getText().toString() != null && !openingKm.getText().toString().equals("")) {
                openingKM = Integer.parseInt(openingKm.getText().toString());
            }

            if (isValidMeterReading(openingKM) && isValidloginTime(logintime)) {
                BusDatum busDatum = (BusDatum) spnBus.getSelectedItem();
                ShiftDatum shiftDatum = (ShiftDatum) spnShift.getSelectedItem();
                DriverDatum driverDatum = (DriverDatum) spnDriver.getSelectedItem();

                shiftID = shiftDatum.getShiftNo();
                vehicleID = busDatum.getVehicleID();
                driver = driverDatum.getStaffCode();

                UpdateBusLogin();
            } else {
                if (!isValidloginTime(logintime)) {
                    Common.showSnack(findViewById(R.id.updateBusLoginLayout), "Please enter valid login time");
                }

                if (!isValidMeterReading(openingKM)) {
                    openingKm.requestFocus();
                    Common.showSnack(findViewById(R.id.updateBusLoginLayout), "Please enter valid Opening Km");
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
                    new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
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
                                busDataAdapter = new VehicleSpinnerAdapter(UpdateBusLoginOPActivity.this, android.R.layout.simple_spinner_dropdown_item, busList);
                                spnBus.setAdapter(busDataAdapter);
                                busDataAdapter.notifyDataSetChanged();

                                for (int i = 0; i < busList.size(); i++) {
                                    if (busList.get(i).getVehicleCode().equals(loggedInBusDatum.getVehicleCode())) {
                                        spnBus.setSelection(i);
                                    }
                                }

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
                                builder.setTitle(response.body().getMessage());
                                builder.setMessage(getResources().getString(R.string.BusListErrorMsg1));
                                builder.setPositiveButton(getResources().getString(R.string.BusListErrorBtnTxt), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        onBackPressed();
                                    }
                                });
                                builder.setCancelable(false);
                                builder.show();
                                //Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.updateBusLoginLayout), response.body().getMessage());
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
                            new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
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

    private void getLastLogOutKm() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getLastLogOutKm();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Getting Last Km Data...");
            showDialog();

            Call<GetLastLogOutKmDataResponse> call = apiInterface.getLastLogOutKm(new GetLastLogOutKmDataRequest(companyID, vehicleID));

            call.enqueue(new Callback<GetLastLogOutKmDataResponse>() {
                @Override
                public void onResponse(Call<GetLastLogOutKmDataResponse> call, Response<GetLastLogOutKmDataResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);

                                lastLogOutKm = response.body().getLastKmData().get(0).getLogoutKm();
                                lastClosingKm.setText(String.valueOf(lastLogOutKm));
                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                lastClosingKm.setText("0");
                                //Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                            }
                            openingKm.setText(String.valueOf(loggedInBusDatum.getLoginKm()));
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.updateBusLoginLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<GetLastLogOutKmDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getLastLogOutKm();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void getRouteListData() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getRouteListData();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Getting Route Data...");
            showDialog();

            Call<RouteResponse> call = apiInterface.getAllRouteListForBusLoginUpdate(new RouteRequest(sessionManager.getKeyCompanyId()));

            call.enqueue(new Callback<RouteResponse>() {
                @Override
                public void onResponse(Call<RouteResponse> call, Response<RouteResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);

                                routeList = response.body().getRouteData();
                                routeDataOPAdapter = new RouteSpinnerAdapter(UpdateBusLoginOPActivity.this, android.R.layout.simple_spinner_dropdown_item, routeList);
                                spnRoute.setAdapter(routeDataOPAdapter);
                                routeDataOPAdapter.notifyDataSetChanged();

                                for (int i = 0; i < routeList.size(); i++) {
                                    if (routeList.get(i).getRouteNo().equals(loggedInBusDatum.getRouteNo())) {
                                        spnRoute.setSelection(i);
                                    }
                                }

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
//                                AlertDialog.Builder builder =
//                                        new AlertDialog.Builder(BusLoginActivity.this,R.style.DialogBox);
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
                                Common.showSnack(findViewById(R.id.updateBusLoginLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.updateBusLoginLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<RouteResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getRouteListData();
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
                    new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
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
//                                ArrayAdapter<DriverDatum> dAdap=new ArrayAdapter<DriverDatum> (BusLoginActivity.this,android.R.layout.simple_spinner_dropdown_item,driverList.toArray(dr));
//                                spnDriver.setAdapter(dAdap);
                                driverAdapter = new AdpSpinnerDriver(UpdateBusLoginOPActivity.this, android.R.layout.simple_spinner_dropdown_item, driverList);
                                //driverAdapter.setDropDownViewResource(R.layout.list_item_driver);
                                spnDriver.setAdapter(driverAdapter);
                                driverAdapter.notifyDataSetChanged();

                                for (int i = 0; i < driverList.size(); i++) {
                                    if (driverList.get(i).getStaffCode().equals(loggedInBusDatum.getStaffCode())) {
                                        spnDriver.setSelection(i);
                                    }
                                }
//                                driverDataAdapter = new DriverDataAdapter(BusLoginActivity.this,driverList);
//                                spnDriver.setAdapter(driverDataAdapter);
//                                driverDataAdapter.notifyDataSetChanged();

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
                                Common.showSnack(findViewById(R.id.updateBusLoginLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.updateBusLoginLayout), response.body().getMessage());
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
                            new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
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

    private void getLogsheetListData() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getLogsheetListData();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Getting Logsheet Data...");
            showDialog();

            Call<LogsheetListDataResponse> call = apiInterface.getAllLogsheetListForBusLoginUpdate(new LogsheetListDataRequest(routeID, sessionManager.getKeyCompanyId()));

            call.enqueue(new Callback<LogsheetListDataResponse>() {
                @Override
                public void onResponse(Call<LogsheetListDataResponse> call, Response<LogsheetListDataResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);

                                logsheetList = response.body().getLogsheetData();

                                logsheetAdapter = new LogsheetOPAdapter(UpdateBusLoginOPActivity.this, android.R.layout.simple_spinner_dropdown_item, logsheetList);
                                spnLogsheet.setAdapter(logsheetAdapter);
                                logsheetAdapter.notifyDataSetChanged();

                                for (int i = 0; i < logsheetList.size(); i++) {
                                    if (logsheetList.get(i).getLogsheetCode().equals(loggedInBusDatum.getLogsheetCode())) {
                                        spnLogsheet.setSelection(i);
                                    }
                                }
//                                routeDataOPAdapter = new RouteSpinnerAdapter(BusLoginActivity.this,logsheetList);
//                                spnLogsheet.setAdapter(routeDataOPAdapter);
//                                routeDataOPAdapter.notifyDataSetChanged();

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
                                builder.setTitle(response.body().getMessage());
                                builder.setMessage(getResources().getString(R.string.BusListErrorMsg1));
                                builder.setPositiveButton(getResources().getString(R.string.BusListErrorBtnTxt), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        onBackPressed();
                                    }
                                });
                                builder.setCancelable(false);
                                builder.show();
//                                Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.updateBusLoginLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<LogsheetListDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getLogsheetListData();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void UpdateBusLogin() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.updateBusLoginLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Please Wait...");
            showDialog();

            Call<UpdateBusLoginDataResponse> call = apiInterface.updateBusLogin(new UpdateBusLoginDataRequest(logID, userID, logintime, companyID, vehicleID, logsheetCode, shiftID, routeID, driver, depotID, openingKM, userName));

            call.enqueue(new Callback<UpdateBusLoginDataResponse>() {
                @Override
                public void onResponse(Call<UpdateBusLoginDataResponse> call, Response<UpdateBusLoginDataResponse> response) {
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

                                Common.showSnack(findViewById(R.id.updateBusLoginLayout), response.body().getMessage());

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                Common.showSnack(findViewById(R.id.updateBusLoginLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.updateBusLoginLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<UpdateBusLoginDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(UpdateBusLoginOPActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            UpdateBusLogin();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void clearUI() {
        getBusListData();
        getRouteListData();
        getLogsheetListData();
        spnBus.setSelection(0);
        spnDriver.setSelection(0);
        //depotName.setText("");
        spnLogsheet.setSelection(0);
        totalTrip.setText("");
        totaltrip = 0;
        //spnDriver.setText("");
        openingKm.setText("");
        openingKM = 0;
        loginTime.setText(Common.getCurrentTime());
        findViewById(R.id.scrollFuelEntry).scrollTo(0, 0);
        findViewById(R.id.busLoginLayout).requestFocus();
    }

    private boolean isValidMeterReading(int meterreading) {
        return meterreading != 0;
    }

    private boolean isValidloginTime(String logintime) {
        return logintime != null && logintime.length() > 2;
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
            Common.showSnack(this.findViewById(R.id.updateBusLoginLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showView(isConnected);
    }
}
