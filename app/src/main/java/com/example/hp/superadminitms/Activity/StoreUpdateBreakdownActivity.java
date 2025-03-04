package com.example.hp.superadminitms.Activity;

import static com.example.hp.superadminitms.Helper.Common.convertDateFormat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BreakdownDatum;
import com.example.hp.superadminitms.Network.BreakdownUpdateRequest;
import com.example.hp.superadminitms.Network.UpdateResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoreUpdateBreakdownActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String EXTRA_MENU = "abcd";
    private static final String TAG = "**** UPDATE ****";
    private static int Problem_Id;
    private static String vehicle_reg_No, Problem, ProblemDate, RouteNo, driverName;
    private String Work_Done, Remark, SolvedBy, JobDate, Selected_Date, Selected_Time;
    private TextView tvProblem, tvVehicleRegNo, tvProblemDate, tvRouteNo, tvDriver;
    private EditText etSelectDate, etSelectTime;
    private TextView btnSubmit;
    private EditText etWorkDone, etRemark, etSolvedBy;
    private Date d;
    private boolean isConnected;
    private ProgressDialog pg;
    private SessionManager sessionManager;
    private int User_Id;

    public static Intent newIntent(Context context, BreakdownDatum breakdownDatum) {
        vehicle_reg_No = breakdownDatum.getVehicleCode() + " - " + breakdownDatum.getVehicleRegNo();
        Log.d("IDIDIDI::", vehicle_reg_No);
        Problem = breakdownDatum.getProblem();
        Problem_Id = breakdownDatum.getProblemID();
        RouteNo = breakdownDatum.getRouteNo();
        driverName = breakdownDatum.getDriverName();
        Log.d(TAG, "RouteNo" + breakdownDatum.getRouteNo());
        try {
            ProblemDate = convertDateFormat(breakdownDatum.getProblemDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "Problem" + breakdownDatum.getProblem());
        Intent intent = new Intent(context, StoreUpdateBreakdownActivity.class);
        intent.putExtra(EXTRA_MENU, breakdownDatum);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_st_update_breakdown);
        initializeToolBar();
        intializeControls();
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
        getSupportActionBar().setTitle("Update Breakdown Vehicle");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
            }
        }
    }

    private void intializeControls() {
        etSelectDate = findViewById(R.id.etSelectedDate);
        etSelectTime = findViewById(R.id.etSelectedTime);
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

                    DatePickerDialog datePickerDialog = new DatePickerDialog(StoreUpdateBreakdownActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    TimePickerDialog timePickerDialog = new TimePickerDialog(StoreUpdateBreakdownActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
        tvRouteNo = findViewById(R.id.tvRouteNo);
        tvProblem = findViewById(R.id.tvProblem);
        tvDriver = findViewById(R.id.tvDriver);
        tvVehicleRegNo = findViewById(R.id.tvBusRegNo);
        tvProblemDate = findViewById(R.id.tvProblemDate);
        btnSubmit = findViewById(R.id.btnSubmit);
        etRemark = findViewById(R.id.etRemark);
        etSolvedBy = findViewById(R.id.etSolvedBy);
        etWorkDone = findViewById(R.id.etWorkDone);
        tvDriver.setText(driverName);
        tvVehicleRegNo.setText(vehicle_reg_No);
        tvProblem.setText(Problem);
        tvProblemDate.setText(ProblemDate);
        tvRouteNo.setText(RouteNo);
        pg = new ProgressDialog(StoreUpdateBreakdownActivity.this, R.style.DialogBox);
        pg.setCancelable(false);
        pg.setIndeterminate(false);
        sessionManager = new SessionManager(StoreUpdateBreakdownActivity.this);
        User_Id = sessionManager.getKeyUserId();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Work_Done = etWorkDone.getText().toString();
                Remark = etRemark.getText().toString();
                SolvedBy = etSolvedBy.getText().toString();
                if (isValidInput(Work_Done) && isValidInput(SolvedBy)) {
                    updateStatus();
                } else {
                    if (!isValidInput(SolvedBy)) {
                        Common.showSnack(findViewById(R.id.rootlayout), "Please Enter SolvedBy...!");
                        etSolvedBy.requestFocus();
                    }
                    if (!isValidInput(Work_Done)) {
                        Common.showSnack(findViewById(R.id.rootlayout), "Please Enter WorkDone...!");
                        etWorkDone.requestFocus();
                    }
                }
            }
        });
    }

    private void updateStatus() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Updating Status...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<UpdateResponse> updateResponseCall = null;
            try {
                updateResponseCall = apiInterface.updateStatus(new BreakdownUpdateRequest(Problem_Id, User_Id, etWorkDone.getText().toString(), etRemark.getText().toString(), etSolvedBy.getText().toString(), convertDateFormat(Selected_Date, "dd-MM-yyyy", "yyyy-MM-dd"), Selected_Time));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            updateResponseCall.enqueue(new Callback<UpdateResponse>() {
                @Override
                public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.d(TAG, "Response" + response.body().getMessage());
                                Toast.makeText(getApplicationContext(), "Solved...", Toast.LENGTH_LONG).show();
//                                    startActivity(new Intent(StoreUpdateBreakdownActivity.this, BreakdownListActiviy.class));
//                                    finish();
                                onBackPressed();

                            } else {
                                pg.dismiss();
                                Log.d(TAG, "Error" + response.body().getMessage());
                            }
                        } else {
                            pg.dismiss();
                            Log.d(TAG, "Error" + response.body().getMessage());
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        e.printStackTrace();
                        Log.d(TAG, "Exception" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UpdateResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.d(TAG, "Failure" + t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(StoreUpdateBreakdownActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            updateStatus();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private boolean isValidInput(String input) {
        return input != null && input.length() > 2 && input.length() <= 200;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }
}
