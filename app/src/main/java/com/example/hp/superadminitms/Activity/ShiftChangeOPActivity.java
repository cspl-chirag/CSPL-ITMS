package com.example.hp.superadminitms.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Adapter.AdpSpinnerDriver;
import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.DriverDatum;
import com.example.hp.superadminitms.MyApplication;
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

import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShiftChangeOPActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    private SessionManager sessionManager;
    private int userID;
    private int companyID;
    private String userName;
    private ProgressDialog pDialog;
    private SearchableSpinner spnBus;
    private SearchableSpinner spnDriver;
    private EditText depotName;
    private EditText logsheetNumber;
    private EditText routeNumber;
    private EditText startTime;
    private TextView submit;
    private List<BusDatum> busList;
    private VehicleSpinnerAdapter busDataAdapter;
    private Integer vehicleID = 0;
    private String start_time;
    private List<DriverDatum> driverList;
    private AdpSpinnerDriver driverAdapter;
    private String driverID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_change);
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

        spnBus = findViewById(R.id.spnBus);
        spnDriver = findViewById(R.id.spnDriver);
        depotName = findViewById(R.id.depotName);
        logsheetNumber = findViewById(R.id.logsheetNumber);
        routeNumber = findViewById(R.id.routeNumber);
        startTime = findViewById(R.id.startTime);
        submit = findViewById(R.id.submit);

        submit.setOnClickListener(this);

        spnBus.setTitle("Select Bus");
        spnDriver.setTitle("Select Driver");

        spnBus.setOnItemSelectedListener(this);
        spnDriver.setOnItemSelectedListener(this);
        spnBus.setSelection(0);

        if (startTime.getText().toString().isEmpty()) {
            startTime.setText(Common.getCurrentTime());
        }

        startTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    hideKeyboard(view);
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);

                    TimePickerDialog timePickerDialog = new TimePickerDialog(ShiftChangeOPActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            startTime.setText(selectedHour + ":" + selectedMinute);
                        }
                    }, hour, minute, false);
                    timePickerDialog.show();
                    startTime.clearFocus();
                }
            }
        });
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
            case R.id.spnBus:
                vehicleID = busList.get(i).getVehicleID();
                //getBusLoginDataForLogout();
                break;
            case R.id.spnDriver:
                driverID = driverList.get(i).getStaffCode();
        }
    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();

        if (id == R.id.spnBus) {
            vehicleID = busList.get(i).getVehicleID();
            //getBusLoginDataForLogout();
        } else if (id == R.id.spnDriver) {
            driverID = driverList.get(i).getStaffCode();
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

                start_time = startTime.getText().toString();

                if (isValidStartTime(start_time)) {
                    BusDatum busDatum = (BusDatum) spnBus.getSelectedItem();
                    vehicleID = busDatum.getVehicleID();

                    //AddBusLogout();
                } else {
                    if (!isValidStartTime(start_time)) {
                        //loginTime.requestFocus();
                        Common.showSnack(findViewById(R.id.shiftChangeLayout), "Please enter valid start time");
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
            start_time = startTime.getText().toString();

            if (isValidStartTime(start_time)) {
                BusDatum busDatum = (BusDatum) spnBus.getSelectedItem();
                vehicleID = busDatum.getVehicleID();

                //AddBusLogout();
            } else {
                if (!isValidStartTime(start_time)) {
                    //loginTime.requestFocus();
                    Common.showSnack(findViewById(R.id.shiftChangeLayout), "Please enter valid start time");
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
                    new AlertDialog.Builder(ShiftChangeOPActivity.this, R.style.DialogBox);
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
                                busDataAdapter = new VehicleSpinnerAdapter(ShiftChangeOPActivity.this, android.R.layout.simple_spinner_dropdown_item, busList);
                                spnBus.setAdapter(busDataAdapter);
                                busDataAdapter.notifyDataSetChanged();

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                //clearUI();
                                AlertDialog.Builder builder =
                                        new AlertDialog.Builder(ShiftChangeOPActivity.this, R.style.DialogBox);
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
                            Common.showSnack(findViewById(R.id.shiftChangeLayout), response.body().getMessage());
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
                            new AlertDialog.Builder(ShiftChangeOPActivity.this, R.style.DialogBox);
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
                    new AlertDialog.Builder(ShiftChangeOPActivity.this, R.style.DialogBox);
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
                                driverAdapter = new AdpSpinnerDriver(ShiftChangeOPActivity.this, android.R.layout.simple_spinner_dropdown_item, driverList);
                                //driverAdapter.setDropDownViewResource(R.layout.list_item_driver);
                                spnDriver.setAdapter(driverAdapter);
                                driverAdapter.notifyDataSetChanged();
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
                            new AlertDialog.Builder(ShiftChangeOPActivity.this, R.style.DialogBox);
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

    //    private void getBusLoginDataForLogout(){
//        boolean isConnected = ConnectivityReceiver.isConnected();
//        if(!isConnected){
//            AlertDialog.Builder builder =
//                    new AlertDialog.Builder(BusLogoutActivity.this,R.style.DialogBox);
//            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
//            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
//            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    getBusListData();
//                }
//            });
//            builder.setCancelable(false);
//            builder.show();
//        } else {
//            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
//
//            pDialog.setMessage("Getting Login Data...");
//            showDialog();
//
//            Call<GetBusLoginDataResponse> call = apiInterface.getBusLoginDataForLogout(new GetBusLoginDataRequest(sessionManager.getKeyCompanyId(),vehicleID));
//
//            call.enqueue(new Callback<GetBusLoginDataResponse>() {
//                @Override
//                public void onResponse(Call<GetBusLoginDataResponse> call, Response<GetBusLoginDataResponse> response) {
//                    try {
//                        if (response.isSuccessful()) {
//                            if (response.body().getStatusCode() == 1) {
//
//                                Handler handler = new Handler();
//                                handler.postDelayed(new Runnable() {
//                                    public void run() {
//                                        hideDialog();
//                                    }
//                                }, 1000);
//
//                                busLoginData = response.body().getBusLoginData();
//                                depotName.setText(busLoginData.get(0).getDeptName());
//                                logsheetNumber.setText(busLoginData.get(0).getLogsheetCode());
//                                routeNumber.setText(busLoginData.get(0).getRouteNo());
//                                //totalTrip.setText(String.valueOf(busLoginData.get(0).getTotalTrip()));
//                                driverName.setText(busLoginData.get(0).getDriverName());
//                                openingKm.setText(String.valueOf(busLoginData.get(0).getLoginKm()));
//                                logID = busLoginData.get(0).getLogID();
//
//                            } else {
//                                if (pDialog.isShowing())
//                                    hideDialog();
//                                Common.showSnack(findViewById(R.id.busLogoutLayout), response.body().getMessage());
//                            }
//                        } else {
//                            if (pDialog.isShowing())
//                                hideDialog();
//                            Common.showSnack(findViewById(R.id.busLogoutLayout), response.body().getMessage());
//                        }
//                    } catch (Exception ex) {
//                        Log.e("Error: ", ex.getMessage());
//                        if (pDialog.isShowing())
//                            hideDialog();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<GetBusLoginDataResponse> call, Throwable t) {
//                    if (pDialog.isShowing())
//                        hideDialog();
//
//                    AlertDialog.Builder builder =
//                            new AlertDialog.Builder(BusLogoutActivity.this,R.style.DialogBox);
//                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
//                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
//                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            getBusListData();
//                        }
//                    });
//                    builder.setCancelable(false);
//                    builder.show();
//                }
//            });
//        }
//    }
    private boolean isValidStartTime(String start_time) {
        return start_time != null && start_time.length() > 3;
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
            Common.showSnack(this.findViewById(R.id.shiftChangeLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showView(isConnected);
    }
}
