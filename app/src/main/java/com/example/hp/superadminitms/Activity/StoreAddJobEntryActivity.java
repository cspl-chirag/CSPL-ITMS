package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.CompoundButtonCompat;

import com.example.hp.superadminitms.Adapter.AdpSpinnerDriver;
import com.example.hp.superadminitms.Adapter.RouteSpinnerAdapter;
import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.DriverDatum;
import com.example.hp.superadminitms.Model.JobDatum;
import com.example.hp.superadminitms.Model.RouteDatum;
import com.example.hp.superadminitms.Network.AddJobRequest;
import com.example.hp.superadminitms.Network.AddJobResponse;
import com.example.hp.superadminitms.Network.BusRequest;
import com.example.hp.superadminitms.Network.BusResponse;
import com.example.hp.superadminitms.Network.DriverRequest;
import com.example.hp.superadminitms.Network.DriverResponse;
import com.example.hp.superadminitms.Network.JobResponse;
import com.example.hp.superadminitms.Network.RouteRequest;
import com.example.hp.superadminitms.Network.RouteResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;
import com.example.hp.superadminitms.utils.SearchableSpinner;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoreAddJobEntryActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, AdapterView.OnItemSelectedListener {
    private final String TAG = this.getClass().getSimpleName() + " ***** ";
    private LinearLayout llCheckBox;
    private boolean isConnected;
    private ProgressDialog pg;
    private List<JobDatum> jobData;
    private int i;
    private CheckBox ch;
    private TextView btnSubmit, tvList;
    private boolean BreakProblems, ClutchProblems, SteeringProblems, HeadLightProblems, SideLightProblems, OtherProblems, AcceleratorProblems = false;
    private SessionManager sessionManager;
    private String Selected_Date, Selected_Time, Problem_Desc;
    private EditText etSelectDate, etSelectTime;
    private int vehicleID = 0, routeId = 0, Bus_Compnay_Id, UserID;
    private Date d;
    private List<DriverDatum> driverData;
    private AdpSpinnerDriver driverAdapter;
    private SearchableSpinner spDriver, spRouteNo, spVehicleRegNo;
    private EditText etProblemDesc, etOdometerReading;
    private List<RouteDatum> routeData;
    private RouteSpinnerAdapter routeAdapter;
    private List<BusDatum> busDatum;
    private VehicleSpinnerAdapter spVehicleAdapter;
    private String JobDate, driverId;
    private String text1 = "", text2 = "", text3 = "", text4 = "", text5 = "", text6 = "";
    private String problemtext, problems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_st_job_entry);

        initializeToolBar();
        initializeControls();
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
        getSupportActionBar().setTitle("Maintanance Entry");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
            }
        }
    }

    @SuppressLint("ResourceType")
    private void initializeControls() {
        llCheckBox = findViewById(R.id.llCheckBox);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvList = findViewById(R.id.tvList);
        tvList.setVisibility(View.GONE);
        etSelectDate = findViewById(R.id.etSelectedDate);
        etSelectTime = findViewById(R.id.etSelectedTime);
        etOdometerReading = findViewById(R.id.etOdometerReading);
        spDriver = findViewById(R.id.spDriver);
        spDriver.setOnItemSelectedListener(this);
        spRouteNo = findViewById(R.id.spRouteNo);
        spRouteNo.setOnItemSelectedListener(this);
        spVehicleRegNo = findViewById(R.id.spVehileRegNo);
        spVehicleRegNo.setOnItemSelectedListener(this);
        sessionManager = new SessionManager(this);
        UserID = sessionManager.getKeyUserId();
        Bus_Compnay_Id = sessionManager.getKeyCompanyId();
        etProblemDesc = findViewById(R.id.etProblemDesc);
        pg = new ProgressDialog(StoreAddJobEntryActivity.this, R.style.DialogBox);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        setVehicleInfoSpinner();
        setRouteSpinner();
        setDriverSpinner();
        gettingJobsCheckbox();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View view) {
                BreakProblems = ((CheckBox) findViewById(1)).isChecked();
                ClutchProblems = ((CheckBox) findViewById(2)).isChecked();
                SteeringProblems = ((CheckBox) findViewById(3)).isChecked();
                HeadLightProblems = ((CheckBox) findViewById(4)).isChecked();
                SideLightProblems = ((CheckBox) findViewById(5)).isChecked();
                AcceleratorProblems = ((CheckBox) findViewById(6)).isChecked();
                OtherProblems = ((CheckBox) findViewById(7)).isChecked();

                if ((isValidInput(etProblemDesc.getText().toString())) && (isChecked(BreakProblems, ClutchProblems, SteeringProblems, HeadLightProblems, SideLightProblems, AcceleratorProblems, OtherProblems))) {
                    addJobEntry();
                } else {
                    if (!isChecked(BreakProblems, ClutchProblems, SteeringProblems, HeadLightProblems, SideLightProblems, AcceleratorProblems, OtherProblems)) {
                        Common.showSnack(findViewById(R.id.rootlayout), "Select atleast one Problem...!");
                    }
                }
            }
        });
        if (etSelectDate.getText().toString().isEmpty() && etSelectTime.getText().toString().isEmpty()) {
            Selected_Date = Common.getCurrentDate();
            Selected_Time = Common.getCurrentTime();
            etSelectTime.setText(Selected_Time);
            etSelectDate.setText(Selected_Date);
            try {
                JobDate = Common.convertDateFormat(Selected_Date, "dd-mm-yyyy", "yyyy-mm-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        etSelectDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(StoreAddJobEntryActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year,
                                              int monthOfYear, int dayOfMonth) {
                            d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());
                            //Delivery_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            Selected_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            etSelectDate.setText(Selected_Date);
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
                    TimePickerDialog timePickerDialog = new TimePickerDialog(StoreAddJobEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        switch (adapterView.getId()) {
            case R.id.spVehileRegNo:
                vehicleID = busDatum.get(i).getVehicleID();
                break;
            case R.id.spRouteNo:
                routeId = routeData.get(i).getRouteID();
                break;
            case R.id.spDriver:
                driverId = driverData.get(i).getStaffCode();
        }

    }*/


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spVehileRegNo) {
            vehicleID = busDatum.get(i).getVehicleID();
        } else if (adapterView.getId() == R.id.spRouteNo) {
            routeId = routeData.get(i).getRouteID();
        } else if (adapterView.getId() == R.id.spDriver) {
            driverId = driverData.get(i).getStaffCode();
        }
    }

    private boolean isValidInput(String string) {
        return string != null && string.length() > 2 && string.length() <= 200;
    }

    private boolean isChecked(boolean flag1, boolean flag2, boolean flag3, boolean flag4, boolean flag5, boolean flag6, boolean flag7) {
        return flag1 || flag2 || flag3 || flag4 || flag5 || flag6 || flag7;
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void setDriverSpinner() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(StoreAddJobEntryActivity.this, R.style.DialogBox);
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
                                driverAdapter = new AdpSpinnerDriver(StoreAddJobEntryActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, driverData);
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
                            new AlertDialog.Builder(StoreAddJobEntryActivity.this, R.style.DialogBox);
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
                    new AlertDialog.Builder(StoreAddJobEntryActivity.this, R.style.DialogBox);
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
                                routeAdapter = new RouteSpinnerAdapter(StoreAddJobEntryActivity.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, routeData);
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
                            new AlertDialog.Builder(StoreAddJobEntryActivity.this, R.style.DialogBox);
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
                    new AlertDialog.Builder(StoreAddJobEntryActivity.this, R.style.DialogBox);
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
            Call<BusResponse> call = apiInterface.getVehicleForJobCard(new BusRequest(Bus_Compnay_Id));
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
                                spVehicleAdapter = new VehicleSpinnerAdapter(StoreAddJobEntryActivity.this, android.R.layout.simple_spinner_dropdown_item, busDatum);
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
                            new AlertDialog.Builder(StoreAddJobEntryActivity.this, R.style.DialogBox);
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

    private void addJobEntry() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Adding Job...");
            pg.setCancelable(false);
            pg.setIndeterminate(false);
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Log.d(TAG, "User:" + UserID);
            Log.d(TAG, "CompanyId," + Bus_Compnay_Id);
            Log.d(TAG, "VehicleId" + vehicleID);
            Log.d(TAG, "Driver" + driverId);
            Log.d(TAG, "Route" + routeId);
            try {
                JobDate = Common.convertDateFormat(etSelectDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd");

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "Selected_Date" + JobDate);
            Log.d(TAG, "Selected_Time" + etSelectTime.getText().toString());

            Call<AddJobResponse> call = apiInterface.addJob(new AddJobRequest(UserID, Bus_Compnay_Id, vehicleID, driverId, routeId, JobDate, Selected_Time, Integer.parseInt(etOdometerReading.getText().toString()), etProblemDesc.getText().toString(), BreakProblems, ClutchProblems, SteeringProblems, HeadLightProblems, SideLightProblems, AcceleratorProblems, OtherProblems));
            call.enqueue(new Callback<AddJobResponse>() {
                @Override
                public void onResponse(Call<AddJobResponse> call, Response<AddJobResponse> response) {
                    if (pg.isShowing()) {
                        hideDialog();
                    }
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.d(TAG, "JobEntryResponse : " + response.body().getMessage());
                                Toast.makeText(getApplicationContext(), "Job Added...", Toast.LENGTH_LONG).show();
//                                startActivity(new Intent(StoreAddJobEntryActivity.this,StoreDashboard.class));
//                                finish();
                                onBackPressed();
                            } else {
                                Log.d(TAG, "JobEntryError1 : " + response.body().getMessage());
                                if (pg.isShowing()) {
                                    hideDialog();
                                }
                            }
                        } else {
//                            Log.d(TAG,"JobEntryError2 : "+response.body().getMessage());
                            if (pg.isShowing()) {
                                hideDialog();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d(TAG, "JobEntryExcptn : " + e.getMessage());
                        if (pg.isShowing()) {
                            hideDialog();
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddJobResponse> call, Throwable t) {
                    Log.d(TAG, "JobEntryFail : " + t.getMessage());
                    if (pg.isShowing()) {
                        hideDialog();
                    }
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(StoreAddJobEntryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            gettingJobsCheckbox();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void gettingJobsCheckbox() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(StoreAddJobEntryActivity.this, R.style.DialogBox);
            builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
            builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
            builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    gettingJobsCheckbox();
                }
            });
            builder.setCancelable(false);
            builder.show();
        } else {
            pg.setMessage("Getting Jobs...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<JobResponse> call = apiInterface.getJobs();
            call.enqueue(new Callback<JobResponse>() {
                @Override
                public void onResponse(Call<JobResponse> call, Response<JobResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);
                                jobData = response.body().getJobCardData();
                                GridLayout gridLayout = new GridLayout(getApplicationContext());
                                gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
                                gridLayout.setColumnCount(2);
                                gridLayout.setRowCount(GridLayout.UNDEFINED);
                                for (i = 0; i < jobData.size(); i++) {
                                    ch = new CheckBox(StoreAddJobEntryActivity.this);
                                    ch.setId(jobData.get(i).getJobID());
                                    ch.setText(jobData.get(i).getJobName());
                                    ch.setTextColor(getResources().getColor(R.color.colorAccent));
                                    int[][] states = {{android.R.attr.state_checked}, {}};
                                    final int[] colors = {getResources().getColor(R.color.colorAccentText), getResources().getColor(R.color.colorAccent)};
                                    CompoundButtonCompat.setButtonTintList(ch, new ColorStateList(states, colors));
                                    gridLayout.addView(ch);
                                    llCheckBox.removeAllViews();
                                    llCheckBox.addView(gridLayout);
                                    ch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                        @SuppressLint("ResourceType")
                                        @Override
                                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                            if (compoundButton.isChecked()) {
                                                if (((CheckBox) findViewById(1)).isChecked()) {
                                                    text1 = " " + ((CheckBox) findViewById(1)).getText() + " Problem,";
                                                }
                                                if (((CheckBox) findViewById(2)).isChecked()) {
                                                    text2 = " " + ((CheckBox) findViewById(2)).getText() + "Problem,";
                                                }
                                                if (((CheckBox) findViewById(3)).isChecked()) {
                                                    text3 = " " + ((CheckBox) findViewById(3)).getText() + "Problem,";
                                                }
                                                if (((CheckBox) findViewById(4)).isChecked()) {
                                                    text4 = " " + ((CheckBox) findViewById(4)).getText() + "Problem,";
                                                }
                                                if (((CheckBox) findViewById(5)).isChecked()) {
                                                    text5 = " " + ((CheckBox) findViewById(5)).getText() + "Problem,";
                                                }
                                                if (((CheckBox) findViewById(6)).isChecked()) {
                                                    text6 = " " + ((CheckBox) findViewById(6)).getText() + "Problem,";
                                                }
                                                if (((CheckBox) findViewById(7)).isChecked()) {
                                                    etProblemDesc.setEnabled(true);
                                                }
//                                                etProblemDesc.setText(text);


//                                                if (((CheckBox) findViewById(1)).isChecked() || ((CheckBox) findViewById(2)).isChecked() || ((CheckBox) findViewById(3)).isChecked() || ((CheckBox) findViewById(4)).isChecked() || ((CheckBox) findViewById(5)).isChecked() || ((CheckBox) findViewById(6)).isChecked()) {
//                                                    //text += (((CheckBox) findViewById(1)).getText());
//                                                    text += compoundButton.getText();
//                                                    //text += ((CheckBox)findViewById(i)).getText();
//                                                }
//                                                etProblemDesc.setText(text);
//                                                if (((CheckBox) findViewById(7)).isChecked()) {
//                                                    etProblemDesc.setEnabled(true);
//                                                    etProblemDesc.requestFocus();
//                                                }
                                            } else {
                                                if (!((CheckBox) findViewById(1)).isChecked()) {
                                                    text1 = "";
                                                }
                                                if (!((CheckBox) findViewById(2)).isChecked()) {
                                                    text2 = "";
                                                }
                                                if (!((CheckBox) findViewById(3)).isChecked()) {
                                                    text3 = "";
                                                }
                                                if (!((CheckBox) findViewById(4)).isChecked()) {
                                                    text4 = "";
                                                }
                                                if (!((CheckBox) findViewById(5)).isChecked()) {
                                                    text5 = "";
                                                }
                                                if (!((CheckBox) findViewById(6)).isChecked()) {
                                                    text6 = "";
                                                }
                                                if (!((CheckBox) findViewById(7)).isChecked()) {
                                                    etProblemDesc.setEnabled(false);
                                                }
                                            }
                                            problems = text1 + text2 + text3 + text4 + text5 + text6;
                                            if (problems.length() > 0) {
                                                problemtext = problems.substring(0, problems.length() - 1);
                                            }
                                            if (!((CheckBox) findViewById(1)).isChecked() && !((CheckBox) findViewById(2)).isChecked() && !((CheckBox) findViewById(3)).isChecked() && !((CheckBox) findViewById(4)).isChecked() && !((CheckBox) findViewById(5)).isChecked()) {
                                                etProblemDesc.setText("");
                                            } else {
                                                etProblemDesc.setText(problemtext);
                                            }
                                        }
                                    });
                                }
                            } else {
                                if (pg.isShowing())
                                    hideDialog();
                                Log.d(TAG, "JobListError1 : " + response.body().getMessage());
                            }
                        } else {
                            if (pg.isShowing())
                                hideDialog();
                            Log.d(TAG, "JobListError2 : " + response.body().getMessage());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (pg.isShowing())
                            hideDialog();
                        Log.d(TAG, "JobListxcption : " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<JobResponse> call, Throwable t) {
                    if (pg.isShowing())
                        hideDialog();
                    Log.d(TAG, "JobFailure : " + t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(StoreAddJobEntryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            gettingJobsCheckbox();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    private void hideDialog() {
        if (pg.isShowing())
            pg.dismiss();
    }
}
