package com.example.hp.superadminitms.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Adapter.AdpSpinnerDriver;
import com.example.hp.superadminitms.Adapter.DepotDataOPAdapter;
import com.example.hp.superadminitms.Adapter.LogsheetOPAdapter;
import com.example.hp.superadminitms.Adapter.RouteSpinnerAdapter;
import com.example.hp.superadminitms.Adapter.ShiftDataOPAdapter;
import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.DepotDatum;
import com.example.hp.superadminitms.Model.DriverDatum;
import com.example.hp.superadminitms.Model.LogsheetDatum;
import com.example.hp.superadminitms.Model.RouteDatum;
import com.example.hp.superadminitms.Model.ShiftDatum;
import com.example.hp.superadminitms.MyApplication;
import com.example.hp.superadminitms.Network.AddBusLoginDataRequest;
import com.example.hp.superadminitms.Network.AddBusLoginDataResponse;
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

public class BusLoginOPActivity extends BaseActivity implements View.OnClickListener, ConnectivityReceiver.ConnectivityReceiverListener, AdapterView.OnItemSelectedListener {
    private final DriverDatum[] dr = new DriverDatum[]{};
    private SessionManager sessionManager;
    private int userID;
    private int companyID;
    private String userName;
    private ProgressDialog pDialog;
    private SearchableSpinner spnBus;
    private SearchableSpinner spnLogsheet;
    private SearchableSpinner spnDriver;
    private EditText openingKm;
    private TextView submit;
    private List<BusDatum> busList;
    private VehicleSpinnerAdapter busDataAdapter;
    private int openingKM = 0;
    private String route;
    private String driver;
    private Integer vehicleID = 0;
    private EditText depotName;
    private EditText totalTrip;
    private EditText loginTime;
    private String depotname;
    private int totaltrip = 0;
    private String logintime;
    private List<DepotDatum> depotList;
    private DepotDataOPAdapter depotDataAdapter;
    private List<DriverDatum> driverList;
    private EditText routeNumber;
    private String logsheetCode;
    private List<LogsheetDatum> logsheetList;
    private RouteSpinnerAdapter routeDataOPAdapter;
    private Integer depotID;
    private AdpSpinnerDriver driverAdapter;
    private Spinner spnShift;
    private ArrayList<ShiftDatum> shiftList;
    private ShiftDataOPAdapter shiftAdapter;
    private Integer routeID = 0;
    private int shiftID = 0;
    private LogsheetOPAdapter logsheetAdapter;
    private EditText lastClosingKm;
    private Integer lastLogOutKm = 0;
    private SearchableSpinner spnRoute;
    private List<RouteDatum> routeList;
    private EditText differenceKm;
    private EditText logDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_login);

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
        getSupportActionBar().setTitle("Bus Login");
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

        spnBus.setOnItemSelectedListener(this);
        spnBus.setSelection(0);

        spnBus.setTitle("Select Bus");
        spnRoute.setTitle("Select Route");
        spnLogsheet.setTitle("Select Logsheet");
        spnDriver.setTitle("Select Driver");

        shiftList = new ArrayList<>();
        shiftList.add(new ShiftDatum(1, "1st Shift"));
        shiftList.add(new ShiftDatum(2, "2nd Shift"));
        //fuelList.add(new FuelTypeDatum("CNG"));
        shiftAdapter = new ShiftDataOPAdapter(BusLoginOPActivity.this, shiftList);
        spnShift.setAdapter(shiftAdapter);
        shiftAdapter.notifyDataSetChanged();

        logDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    Calendar cal = Calendar.getInstance(Locale.getDefault());
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            BusLoginOPActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int d, int m, int y) {
                            int month = m + 1;
                            logDate.setText(Common.getDate(d, month, y));

                            getBusListData();

                            getRouteListData();

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

                    TimePickerDialog timePickerDialog = new TimePickerDialog(BusLoginOPActivity.this, new TimePickerDialog.OnTimeSetListener() {
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

        spnRoute.setOnItemSelectedListener(this);

        spnLogsheet.setOnItemSelectedListener(this);

        submit.setOnClickListener(this);
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
                startActivity(new Intent(BusLoginOPActivity.this,BusLoginListOPActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Check if the item ID matches android.R.id.home
        if (item.getItemId() == android.R.id.home) {
            // Perform the action for android.R.id.home
            onBackPressed();
            return true;
        }
        // Check if the item ID matches R.id.verify_fuel
        else if (item.getItemId() == R.id.verify_fuel) {
            // Perform the action for R.id.verify_fuel
            startActivity(new Intent(BusLoginOPActivity.this, BusLoginListOPActivity.class));
            return true;
        }
        // If none of the above conditions match, fall back to the superclass implementation
        else {
            return super.onOptionsItemSelected(item);
        }
    }


   /* @Override
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
            // totaltrip=routeDatum.getTotalTrip();
        } else if (id == R.id.spnBus) {
            vehicleID = busList.get(i).getVehicleID();
            getLastLogOutKm();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /*@Override
    public void onClick(View view) {
        hideKeyboard(view);
        switch (view.getId()) {
            case R.id.submit:
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

                    AddBusLogin();
                } else {
                    if (!isValidloginTime(logintime)) {
                        Common.showSnack(findViewById(R.id.busLoginLayout), "Please enter valid login time");
                    }

                    if (!isValidMeterReading(openingKM)) {
                        openingKm.requestFocus();
                        Common.showSnack(findViewById(R.id.busLoginLayout), "Please enter valid Opening Km");
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

                AddBusLogin();
            } else {
                if (!isValidloginTime(logintime)) {
                    Common.showSnack(findViewById(R.id.busLoginLayout), "Please enter valid login time");
                }

                if (!isValidMeterReading(openingKM)) {
                    openingKm.requestFocus();
                    Common.showSnack(findViewById(R.id.busLoginLayout), "Please enter valid Opening Km");
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
                    new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
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

            Call<BusResponse> call = null;
            try {
                call = apiInterface.getBusListForLogin(new BusRequest(sessionManager.getKeyCompanyId(), Common.convertDateFormat(logDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd")));
            } catch (ParseException e) {
                e.printStackTrace();
            }

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
                                busDataAdapter = new VehicleSpinnerAdapter(BusLoginOPActivity.this, android.R.layout.simple_spinner_dropdown_item, busList);
                                spnBus.setAdapter(busDataAdapter);
                                busDataAdapter.notifyDataSetChanged();

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
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
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
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
                            new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
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
                    new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
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
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
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
                            new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
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
                    new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
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

            Call<RouteResponse> call = null;
            try {
                call = apiInterface.getRouteList(new RouteRequest(sessionManager.getKeyCompanyId(), Common.convertDateFormat(logDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd")));
            } catch (ParseException e) {
                e.printStackTrace();
            }

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
                                routeDataOPAdapter = new RouteSpinnerAdapter(BusLoginOPActivity.this, android.R.layout.simple_spinner_dropdown_item, routeList);
                                spnRoute.setAdapter(routeDataOPAdapter);
                                routeDataOPAdapter.notifyDataSetChanged();
                                spnRoute.setSelection(0);

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
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
                            new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
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
                    new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
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
                                driverAdapter = new AdpSpinnerDriver(BusLoginOPActivity.this, android.R.layout.simple_spinner_dropdown_item, driverList);
                                spnDriver.setAdapter(driverAdapter);
                                driverAdapter.notifyDataSetChanged();
                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
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
                            new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
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

    /*private void getDepotListData(){
        boolean isConnected = ConnectivityReceiver.isConnected();
        if(!isConnected){
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(BusLoginOPActivity.this,R.style.DialogBox);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getDepotListData();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Getting Depot Data...");
            showDialog();

            Call<DepotListDataResponse> call = apiInterface.getDepotList(new DepotListDataRequest(sessionManager.getKeyCompanyId()));

            call.enqueue(new Callback<DepotListDataResponse>() {
                @Override
                public void onResponse(Call<DepotListDataResponse> call, Response<DepotListDataResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);

                                depotList = response.body().getDepotData();
                                depotDataAdapter = new DepotDataOPAdapter(BusLoginOPActivity.this,depotList);
                                depotName.setAdapter(depotDataAdapter);
                                depotDataAdapter.notifyDataSetChanged();

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
//                                AlertDialog.Builder builder =
//                                        new AlertDialog.Builder(BusLoginOPActivity.this);
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
                                Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<DepotListDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(BusLoginOPActivity.this,R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getDepotListData();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }*/


    private void getLogsheetListData() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
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

            Call<LogsheetListDataResponse> call = null;
            try {
                call = apiInterface.getRouteWiseLogsheetList(new LogsheetListDataRequest(routeID, sessionManager.getKeyCompanyId(), Common.convertDateFormat(logDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd")));
            } catch (ParseException e) {
                e.printStackTrace();
            }

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

                                logsheetAdapter = new LogsheetOPAdapter(BusLoginOPActivity.this, android.R.layout.simple_spinner_dropdown_item, logsheetList);
                                spnLogsheet.setAdapter(logsheetAdapter);
                                logsheetAdapter.notifyDataSetChanged();
                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
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
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
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
                            new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
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

    private void AddBusLogin() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.busLoginLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Please Wait...");
            showDialog();

            Call<AddBusLoginDataResponse> call = null;
            try {
                call = apiInterface.addBusLoginEntry(new AddBusLoginDataRequest(userID, Common.convertDateFormat(logDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd"), logintime, companyID, vehicleID, logsheetCode, shiftID, routeID, driver, depotID, openingKM, userName));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            call.enqueue(new Callback<AddBusLoginDataResponse>() {
                @Override
                public void onResponse(Call<AddBusLoginDataResponse> call, Response<AddBusLoginDataResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);

                                Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                                clearUI();

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("Error: ", ex.getMessage());
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<AddBusLoginDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(BusLoginOPActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddBusLogin();
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

    private boolean isValidloginTime(String logintime) {
        return logintime != null && logintime.length() > 2;
    }

    private boolean isValidRouteName(String route) {
        return route != null && route.length() > 0 && route.length() <= 50;
    }

    private boolean isValidDriverName(String driver) {
        return driver != null && driver.length() > 0 && driver.length() <= 50;
    }

    private boolean isValidMeterReading(int meterreading) {
        return meterreading != 0;
    }

    private boolean isValidTrip(int totaltrip) {
        return totaltrip >= 0;
    }

    private boolean isValidDepot(String depotname) {
        return depotname != null && depotname.length() > 0 && depotname.length() <= 50;
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
            Common.showSnack(this.findViewById(R.id.busLoginLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showView(isConnected);
    }
}
