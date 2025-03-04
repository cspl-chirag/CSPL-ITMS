package com.example.hp.superadminitms.Activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Adapter.AdpSpinnerDriver;
import com.example.hp.superadminitms.Adapter.RouteSpinnerAdapter;
import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.DriverDatum;
import com.example.hp.superadminitms.Model.RouteDatum;
import com.example.hp.superadminitms.Network.AddBreakdownRequest;
import com.example.hp.superadminitms.Network.AddBreakdownResponse;
import com.example.hp.superadminitms.Network.BusRequest;
import com.example.hp.superadminitms.Network.BusResponse;
import com.example.hp.superadminitms.Network.DriverRequest;
import com.example.hp.superadminitms.Network.DriverResponse;
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
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreAddBreakdownEntryActivity extends BaseActivity implements AdapterView.OnItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName() + "  **********  ";
    private String Selected_Date, Selected_Time;
    private EditText etSelectDate, etSelectTime;
    private Date d;
    private boolean isConnected;
    private ProgressDialog pg;
    private int Bus_Compnay_Id, User_Id;
    private SessionManager sessionManager;
    private SearchableSpinner spVehicleRegNo, spRouteNo, spDriver;
    private List<BusDatum> busDatum = new ArrayList<>();
    private VehicleSpinnerAdapter spVehicleAdapter;
    private TextView btnSubmit;
    private EditText etProblem, etLocation;
    private Integer vehicleID = 0, routeId = 0, driverId = 0;
    private List<RouteDatum> routeData;
    private RouteSpinnerAdapter routeAdapter;
    private List<DriverDatum> driverData;
    private AdpSpinnerDriver driverAdapter;
    private LinearLayout ll;
    private String JobDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_st_add_breakdown_entry);
        initializeToolBar();
        initializeControls();
        setVehicleInfoSpinner();
        setRouteSpinner();
        setDriverSpinner();
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
        getSupportActionBar().setTitle("Breakdown Entry");
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
        pg = new ProgressDialog(StoreAddBreakdownEntryActivity.this, R.style.DialogBox);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        ll = findViewById(R.id.rootlayout);
        sessionManager = new SessionManager(StoreAddBreakdownEntryActivity.this);
        Bus_Compnay_Id = sessionManager.getKeyCompanyId();
        User_Id = sessionManager.getKeyUserId();
        spVehicleRegNo = findViewById(R.id.spVehileRegNo);
        spVehicleRegNo.setOnItemSelectedListener(this);
        spRouteNo = findViewById(R.id.spRouteNo);
        spRouteNo.setOnItemSelectedListener(this);
        spDriver = findViewById(R.id.spDriver);
        spDriver.setOnItemSelectedListener(this);
        etSelectDate = findViewById(R.id.etSelectedDate);
        etSelectTime = findViewById(R.id.etSelectedTime);
        etProblem = findViewById(R.id.etProblem);
        etLocation = findViewById(R.id.etLocation);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValidInput(etProblem.getText().toString()) && isValidInput(etLocation.getText().toString())) {
                    addBreakdownEntry();
                } else {

                    if (isValidInput(etLocation.getText().toString())) {
                        Common.showSnack(findViewById(R.id.rootlayout), "Please Enter Location...!");
                        etLocation.requestFocus();
                    }
                    if (isValidInput(etProblem.getText().toString())) {
                        Common.showSnack(findViewById(R.id.rootlayout), "Please Enter Problem...!");
                        etProblem.requestFocus();
                    }
                }
            }
        });
//        Selected_Date = Common.getCurrentDate();
//        Selected_Time = Common.getCurrentTime();
//        etSelectDate.setText(Selected_Date);
//        etSelectTime.setText(Selected_Time);
        if (etSelectDate.getText().toString().isEmpty() && etSelectTime.getText().toString().isEmpty()) {
            Selected_Date = Common.getCurrentDate();
            Selected_Time = Common.getCurrentTime();
            try {
                JobDate = Common.convertDateFormat(Selected_Date, "dd-mm-yyyy", "yyyy-mm-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            etSelectTime.setText(Selected_Time);
            etSelectDate.setText(Selected_Date);
        }
        etSelectDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(StoreAddBreakdownEntryActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year,
                                              int monthOfYear, int dayOfMonth) {
                            d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());

                            //Delivery_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            JobDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            etSelectDate.setText(JobDate);
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                    etSelectDate.clearFocus();
                }
            }
        });
        etSelectTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(StoreAddBreakdownEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());

                            Selected_Time = hourOfDay + ":" + minute;
                            etSelectTime.setText(Selected_Time);
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mHour, mMinute, false);
                    timePickerDialog.show();
                    etSelectTime.clearFocus();
                }
            }
        });
    }


    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId())
        {
            case R.id.spVehileRegNo:
                vehicleID=busDatum.get(i).getVehicleID();
                break;
            case R.id.spRouteNo:
                routeId = routeData.get(i).getRouteID();
                break;
            case R.id.spDriver:
                driverId = driverData.get(i).getStaffID();
        }


        //Toast.makeText(StoreAddBreakdownEntryActivity.this,"ID :: "+vehicleID,Toast.LENGTH_LONG).show();
        //addBreakdownEntry();
    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spVehileRegNo) {
            vehicleID = busDatum.get(i).getVehicleID();
        } else if (adapterView.getId() == R.id.spRouteNo) {
            routeId = routeData.get(i).getRouteID();
        } else if (adapterView.getId() == R.id.spDriver) {
            driverId = driverData.get(i).getStaffID();
        }

        // Uncomment these lines if needed
        // Toast.makeText(StoreAddBreakdownEntryActivity.this, "ID :: " + vehicleID, Toast.LENGTH_LONG).show();
        // addBreakdownEntry();
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private boolean isValidInput(String input) {
        return input != null && input.length() > 2 && input.length() <= 200;
    }

    private void addBreakdownEntry() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Adding Breakdown Entry...!");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            Call<AddBreakdownResponse> call = apiInterface.addBreakdownEntry(new AddBreakdownRequest(User_Id, Bus_Compnay_Id, vehicleID, routeId, driverId, JobDate, Selected_Time, etProblem.getText().toString(), etLocation.getText().toString()));
            call.enqueue(new Callback<AddBreakdownResponse>() {
                @Override
                public void onResponse(Call<AddBreakdownResponse> call, Response<AddBreakdownResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.d(TAG, "AddBreaqkdownResponse : " + response.body().getMessage());
                                Toast.makeText(getApplicationContext(), "Vehicle Added To BreakdownList", Toast.LENGTH_LONG).show();
                                //startActivity(new Intent(StoreAddBreakdownEntryActivity.this,HomeActivity.class));
                                onBackPressed();
                            } else {
                                if (pg.isShowing())
                                    hideDialog();
                                Log.d(TAG, "AddBreaqkdownError : " + response.body().getMessage());
                            }
                        } else {
                            if (pg.isShowing())
                                hideDialog();
//                            Log.d(TAG,"AddBreaqkdownError : "+response.body().getMessage());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing())
                            hideDialog();
                        Log.d(TAG, "AddBreakdownExcption : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<AddBreakdownResponse> call, Throwable t) {
                    if (pg.isShowing())
                        hideDialog();
                    Log.d(TAG, "AddBreakdownFailure : " + t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(StoreAddBreakdownEntryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setVehicleInfoSpinner();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });


        }
    }

    private void setDriverSpinner() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(StoreAddBreakdownEntryActivity.this, R.style.DialogBox);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setVehicleInfoSpinner();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            pg.setMessage("Getting Driver List...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<DriverResponse> call = apiInterface.getDriverInfo(new DriverRequest(Bus_Compnay_Id));
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
                                driverData = response.body().getDriverData();
                                driverAdapter = new AdpSpinnerDriver(StoreAddBreakdownEntryActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, driverData);
                                spDriver.setAdapter(driverAdapter);
                                driverAdapter.notifyDataSetChanged();
                            } else {
                                if (pg.isShowing())
                                    hideDialog();
                                Log.d(TAG, "RouteError1 : " + response.body().getMessage());
                            }
                        } else {
                            if (pg.isShowing())
                                hideDialog();
                            Log.d(TAG, "RouteError2 : " + response.body().getMessage());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing())
                            hideDialog();
                        Log.d(TAG, "RouteExcption : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<DriverResponse> call, Throwable t) {
                    if (pg.isShowing())
                        hideDialog();
                    Log.d(TAG, "RouteFailure : " + t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(StoreAddBreakdownEntryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setVehicleInfoSpinner();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void setRouteSpinner() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(StoreAddBreakdownEntryActivity.this, R.style.DialogBox);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setVehicleInfoSpinner();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            pg.setMessage("Getting Bus List...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<RouteResponse> call = apiInterface.getRouteInfo(new RouteRequest(Bus_Compnay_Id));
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
                                routeData = response.body().getRouteData();
                                routeAdapter = new RouteSpinnerAdapter(StoreAddBreakdownEntryActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, routeData);
                                spRouteNo.setAdapter(routeAdapter);
                                routeAdapter.notifyDataSetChanged();
                            } else {
                                if (pg.isShowing())
                                    hideDialog();
                                Log.d(TAG, "RouteError1 : " + response.body().getMessage());
                            }
                        } else {
                            if (pg.isShowing())
                                hideDialog();
                            Log.d(TAG, "RouteError2 : " + response.body().getMessage());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing())
                            hideDialog();
                        Log.d(TAG, "RouteExcption : " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<RouteResponse> call, Throwable t) {
                    if (pg.isShowing())
                        hideDialog();
                    Log.d(TAG, "RouteFailure : " + t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(StoreAddBreakdownEntryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setVehicleInfoSpinner();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void setVehicleInfoSpinner() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(StoreAddBreakdownEntryActivity.this, R.style.DialogBox);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setVehicleInfoSpinner();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            pg.setMessage("Getting Bus List...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<BusResponse> call = apiInterface.getVehicleInfo(new BusRequest(Bus_Compnay_Id));
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
                                busDatum = response.body().getBusData();
                                spVehicleAdapter = new VehicleSpinnerAdapter(StoreAddBreakdownEntryActivity.this, android.R.layout.simple_spinner_dropdown_item, busDatum);
                                spVehicleRegNo.setAdapter(spVehicleAdapter);
                                spVehicleAdapter.notifyDataSetChanged();
                            } else {
                                if (pg.isShowing())
                                    hideDialog();
                                Log.d(TAG, "BusListError1 : " + response.body().getMessage());
                            }
                        } else {
                            if (pg.isShowing())
                                hideDialog();
                            Log.d(TAG, "BusListEerror2 : " + response.body().getMessage());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing())
                            hideDialog();
                        e.printStackTrace();
                        Log.d(TAG, "BusListExcption : " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<BusResponse> call, Throwable t) {
                    if (pg.isShowing())
                        hideDialog();
                    Log.d(TAG, "BusListFailure : " + t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(StoreAddBreakdownEntryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            setVehicleInfoSpinner();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });

        }
    }

    private void hideDialog() {
        if (pg.isShowing())
            pg.dismiss();
    }

}
