package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Adapter.AdpSpinnerDriver;
import com.example.hp.superadminitms.Adapter.RouteSpinnerAdapter;
import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.DriverDatum;
import com.example.hp.superadminitms.Model.RouteDatum;
import com.example.hp.superadminitms.Network.AccidentEntryRequest;
import com.example.hp.superadminitms.Network.AccidentEntryResponse;
import com.example.hp.superadminitms.Network.BusRequest;
import com.example.hp.superadminitms.Network.BusResponse;
import com.example.hp.superadminitms.Network.DriverRequest;
import com.example.hp.superadminitms.Network.DriverResponse;
import com.example.hp.superadminitms.Network.RouteRequest;
import com.example.hp.superadminitms.Network.RouteResponse;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAccidentEntryActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    private EditText etDate, etLocation, etDescription;
    private String Selected_Date;
    private Spinner spVehileRegNo;
    private Date d;
    private ProgressDialog pg;
    private int Company_ID, User_ID;
    private SessionManager session;
    private VehicleSpinnerAdapter spVehicleAdapter;
    private List<BusDatum> busDatum = new ArrayList<>();
    private final String TAG = "******";
    private List<DriverDatum> driverList = new ArrayList<>();
    private AdpSpinnerDriver driverAdapter;
    private List<RouteDatum> routeList = new ArrayList<>();
    private RouteSpinnerAdapter routeDataOPAdapter;
    private Spinner spDriver, spRoute;
    private TextView btnSubmit;
    private boolean isConnected;
    private Integer Driver_Id, Route_Id, Vehicle_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accident_entry);
        initializeToolBar();
        initializeControls();
        getVehicleInfoSpinner();
        getDriverListData();
        getRouteListData();
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
        getSupportActionBar().setTitle("Accident Entry");
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
        pg.setCancelable(false);
        pg.setIndeterminate(false);
        session = new SessionManager(this);
        Company_ID = session.getKeyCompanyId();
        User_ID = session.getKeyUserId();
        etDate = findViewById(R.id.etDate);
        spVehileRegNo = findViewById(R.id.spVehileRegNo);
        spDriver = findViewById(R.id.spDriver);
        spRoute = findViewById(R.id.spRoute);
        btnSubmit = findViewById(R.id.btnSubmit);
        etDescription = findViewById(R.id.etDescription);
        etLocation = findViewById(R.id.etLocation);
        if (etDate.getText().toString().isEmpty()) {
            try {
                Selected_Date = Common.convertDateFormat(Common.getCurrentDate(), "dd-MM-yyyy", "yyyy-MM-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            etDate.setText(Selected_Date);
        }
        etDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                hideSoftKeyBoard();
//                searchView.clearFocus();
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddAccidentEntryActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year,
                                              int monthOfYear, int dayOfMonth) {
                            d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());
                            //Delivery_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            try {
                                Selected_Date = Common.convertDateFormat(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year, "dd-MM-yyyy", "yyyy-MM-dd");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            etDate.setText(Selected_Date);
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
//                    etDate.clearFocus();
                }
            }
        });

        btnSubmit.setOnClickListener(this);
        spRoute.setOnItemSelectedListener(this);
        spVehileRegNo.setOnItemSelectedListener(this);
        spDriver.setOnItemSelectedListener(this);
    }

    private void getVehicleInfoSpinner() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(AddAccidentEntryActivity.this, R.style.DialogBox);
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
            Call<BusResponse> call = apiInterface.getVehicleInfo(new BusRequest(Company_ID));
            call.enqueue(new Callback<BusResponse>() {
                @Override
                public void onResponse(Call<BusResponse> call, Response<BusResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        pg.dismiss();
                                    }
                                }, 1000);
                                Log.d(TAG, "BusListResponse : " + response.body().getMessage());

                                busDatum = response.body().getBusData();
                                spVehicleAdapter = new VehicleSpinnerAdapter(AddAccidentEntryActivity.this, android.R.layout.simple_spinner_dropdown_item, busDatum);
                                spVehileRegNo.setAdapter(spVehicleAdapter);
                                spVehicleAdapter.notifyDataSetChanged();
                            } else {
                                if (pg.isShowing())
                                    pg.dismiss();
                                Log.d(TAG, "BusListError1 : " + response.body().getMessage());
                            }
                        } else {
                            if (pg.isShowing())
                                pg.dismiss();
                            Log.d(TAG, "BusListEerror2 : " + response.body().getMessage());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing())
                            pg.dismiss();
                        e.printStackTrace();
                        Log.d(TAG, "BusListExcption : " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<BusResponse> call, Throwable t) {
                    if (pg.isShowing())
                        pg.dismiss();
                    Log.d(TAG, "BusListFailure : " + t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(AddAccidentEntryActivity.this, R.style.DialogBox);
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

    private void getRouteListData() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(AddAccidentEntryActivity.this, R.style.DialogBox);
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

            pg.setMessage("Getting Route Data...");
            pg.show();

            Call<RouteResponse> call = null;
            try {
                call = apiInterface.getRouteList(new RouteRequest(Company_ID, Common.convertDateFormat(Common.getCurrentDate(), "dd-MM-yyyy", "yyyy-MM-dd")));
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
                                        pg.dismiss();
                                    }
                                }, 1000);
                                Log.d(TAG, "RouteListResponse : " + response.body().getMessage());

                                routeList = response.body().getRouteData();
                                routeDataOPAdapter = new RouteSpinnerAdapter(AddAccidentEntryActivity.this, android.R.layout.simple_spinner_dropdown_item, routeList);
                                spRoute.setAdapter(routeDataOPAdapter);
                                routeDataOPAdapter.notifyDataSetChanged();
                                spRoute.setSelection(0);

                            } else {
                                if (pg.isShowing())
                                    pg.dismiss();
                                Log.d(TAG, "RouteListError1 : " + response.body().getMessage());
                                Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                            }
                        } else {
                            if (pg.isShowing())
                                pg.dismiss();
                            Log.d(TAG, "RouteListError2 : " + response.errorBody().string());
                            Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("RouteListException: ", ex.getMessage());
                        if (pg.isShowing())
                            pg.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<RouteResponse> call, Throwable t) {
                    if (pg.isShowing())
                        pg.dismiss();
                    Log.e("RouteListFailure: ", t.getMessage());

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(AddAccidentEntryActivity.this, R.style.DialogBox);
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
                    new AlertDialog.Builder(AddAccidentEntryActivity.this, R.style.DialogBox);
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

            pg.setMessage("Getting Driver Data...");
            pg.show();

            Call<DriverResponse> call = apiInterface.getDriverList(new DriverRequest(Company_ID));

            call.enqueue(new Callback<DriverResponse>() {
                @Override
                public void onResponse(Call<DriverResponse> call, Response<DriverResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        pg.dismiss();
                                    }
                                }, 1000);
                                Log.d(TAG, "DriverListResponse : " + response.body().getMessage());
                                driverList = response.body().getDriverData();
                                driverAdapter = new AdpSpinnerDriver(AddAccidentEntryActivity.this, android.R.layout.simple_spinner_dropdown_item, driverList);
                                spDriver.setAdapter(driverAdapter);
                                driverAdapter.notifyDataSetChanged();

                            } else {
                                Log.d(TAG, "DriverListError 1 : " + response.body().getMessage());
                                if (pg.isShowing())
                                    pg.dismiss();
                                Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                            }
                        } else {
                            Log.d(TAG, "DriverListError 2 : " + response.errorBody().string());
                            if (pg.isShowing())
                                pg.dismiss();
                            Common.showSnack(findViewById(R.id.busLoginLayout), response.body().getMessage());
                        }
                    } catch (Exception ex) {
                        Log.e("DriverListException : ", ex.getMessage());
                        if (pg.isShowing())
                            pg.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<DriverResponse> call, Throwable t) {
                    if (pg.isShowing())
                        pg.dismiss();
                    Log.e("DriverListFailure : ", t.getMessage());

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(AddAccidentEntryActivity.this, R.style.DialogBox);
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

    private void addAccidentEntry() {
        if (IsValid(etLocation.getText().toString()) && IsNotZero(Route_Id) && IsNotZero(Driver_Id) && IsNotZero(Vehicle_Id)) {
            pg.setMessage("Please Wait...");
            pg.dismiss();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<AccidentEntryResponse> call = apiInterface.addAccidentEntry(new AccidentEntryRequest(User_ID, Company_ID, Vehicle_Id, Selected_Date, Driver_Id, Route_Id, etLocation.getText().toString(), etDescription.getText().toString()));
            call.enqueue(new Callback<AccidentEntryResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<AccidentEntryResponse> call, Response<AccidentEntryResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("Accident Entry Response : ", response.body().getMessage());
                                Toast.makeText(getApplicationContext(), "Accident Entry Added...", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                pg.dismiss();
                                Log.e("Accident Entry Error 1 : ", response.body().getMessage());
                            }
                        } else {
                            pg.dismiss();
                            Log.e("Accident Entry Error 2 : ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        Log.e("Accident Entry Exception : ", e.getMessage());
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<AccidentEntryResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("Accident Entry Failure : ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(AddAccidentEntryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            addAccidentEntry();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        } else {
            if (!IsValid(etLocation.getText().toString())) {
                etLocation.requestFocus();
                etLocation.setError("Enter Location...!");
            }
            if (!IsNotZero(Route_Id)) {
                Common.showSnackError(findViewById(R.id.rootlayout), "Select Route...!");
            }
            if (!IsNotZero(Driver_Id)) {
                Common.showSnackError(findViewById(R.id.rootlayout), "Select Driver...!");
            }
            if (!IsNotZero(Vehicle_Id)) {
                Common.showSnackError(findViewById(R.id.rootlayout), "Select Bus...!");
            }
        }
    }

    private boolean IsNotZero(Integer id) {
        return id != 0 && id != null;
    }

    private boolean IsValid(String s) {
        return !s.isEmpty() && !s.equals(null);
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    addAccidentEntry();
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
                Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                addAccidentEntry();
            }
        }
    }


    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spDriver:
                Driver_Id = driverList.get(i).getStaffID();
                break;
            case R.id.spRoute:
                Route_Id = routeList.get(i).getRouteID();
                break;
            case R.id.spVehileRegNo:
                Vehicle_Id = busDatum.get(i).getVehicleID();
                break;
        }
    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();

        if (id == R.id.spDriver) {
            Driver_Id = driverList.get(i).getStaffID();
        } else if (id == R.id.spRoute) {
            Route_Id = routeList.get(i).getRouteID();
        } else if (id == R.id.spVehileRegNo) {
            Vehicle_Id = busDatum.get(i).getVehicleID();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}
