package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Adapter.StaffListSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.StaffDatum;
import com.example.hp.superadminitms.Network.OffenceRequest;
import com.example.hp.superadminitms.Network.RouteRequest;
import com.example.hp.superadminitms.Network.StaffListResponse;
import com.example.hp.superadminitms.Network.StaffOffenceEntryResponse;
import com.example.hp.superadminitms.R;
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

public class Staff_Offence_Entry_Activity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private ProgressDialog pg;
    private SessionManager session;
    private int Company_ID, User_Id;
    private EditText etPenaltyBy, etOffenceAmount, etOffence, etOffeneceDate, etDesignation;
    private TextView btnSubmit;
    private String Selected_Date;
    private Date d;
    private List<StaffDatum> staffData = new ArrayList<>();
    private final List<StaffDatum> staffMainList = new ArrayList<>();
    private StaffListSpinnerAdapter staffListSpinnerAdapter;
    private Spinner spStaffMembers;
    private boolean isConnected;
    private Integer Staff_Member_Id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_offence_entry);
        initializeToolBar();
        initializeControls();
        getStaffMemberList();
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
        getSupportActionBar().setTitle("Staff Fine");
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
        User_Id = session.getKeyUserId();
        etOffeneceDate = findViewById(R.id.etOffeneceDate);
        etPenaltyBy = findViewById(R.id.etPenaltyBy);
        etOffence = findViewById(R.id.etOffence);
        etOffenceAmount = findViewById(R.id.etOffenceAmount);
        etDesignation = findViewById(R.id.etDesignation);
        spStaffMembers = findViewById(R.id.spStaffMembers);
        btnSubmit = findViewById(R.id.btnSubmit);

        if (etOffeneceDate.getText().toString().isEmpty()) {
            try {
                Selected_Date = Common.convertDateFormat(Common.getCurrentDate(), "dd-mm-yyyy", "yyyy-mm-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            Selected_Time = Common.getCurrentTime();
//            etOutwardtime.setText(Selected_Time);
            etOffeneceDate.setText(Selected_Date);
//            try {
//                OutwardDateTime = Common.convertDateFormat(Selected_Date, "dd-mm-yyyy", "yyyy-mm-dd");
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
        }
        etOffeneceDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
//                hideSoftKeyBoard();
//                searchView.clearFocus();
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(Staff_Offence_Entry_Activity.this, new DatePickerDialog.OnDateSetListener() {
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
                            etOffeneceDate.setText(Selected_Date);
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
//                    etDate.clearFocus();
                }
            }
        });
        btnSubmit.setOnClickListener(this);
        spStaffMembers.setOnItemSelectedListener(this);
    }

    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spStaffMembers:
                if (i == 0) {
                    Staff_Member_Id = 0;
                    etDesignation.getText().clear();
                } else {
                    Staff_Member_Id = staffMainList.get(i).getStaffID();
                    etDesignation.setText(staffMainList.get(i).getDesignation());
                }
                break;
        }
    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();

        if (id == R.id.spStaffMembers) {
            if (i == 0) {
                Staff_Member_Id = 0;
                etDesignation.getText().clear();
            } else {
                Staff_Member_Id = staffMainList.get(i).getStaffID();
                etDesignation.setText(staffMainList.get(i).getDesignation());
            }
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    addOffenceEntry();
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
                Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                addOffenceEntry();
            }
        }
    }


    private void addOffenceEntry() {
        if (IsValid(etDesignation.getText().toString()) &&
                IsValid(etPenaltyBy.getText().toString()) &&
                IsValid(etOffence.getText().toString()) &&
                IsValid(etOffenceAmount.getText().toString()) &&
                IsValid(etOffeneceDate.getText().toString()) &&
                IsNotZero(Staff_Member_Id)) {
            pg.setMessage("Please Wait...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<StaffOffenceEntryResponse> call = apiInterface.addStaffOffenceEntry(new OffenceRequest(
                    User_Id, Company_ID, Staff_Member_Id, etOffence.getText().toString(), Selected_Date, Double.parseDouble(etOffenceAmount.getText().toString()), etPenaltyBy.getText().toString()));
            call.enqueue(new Callback<StaffOffenceEntryResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<StaffOffenceEntryResponse> call, Response<StaffOffenceEntryResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("Offence Entry Response : ", response.body().getMessage());
                                Toast.makeText(getApplicationContext(), "Offence Entry Added...", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                pg.dismiss();
                                Log.e("Offence Entry ERROR 1 : ", response.body().getMessage());
                            }
                        } else {
                            pg.dismiss();
                            Log.e("Offence Entry ERROR 2 : ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        Log.e("Offence Entry ERROR 2 : ", e.getMessage());
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<StaffOffenceEntryResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("Offence Entry FAILURE : ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Staff_Offence_Entry_Activity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getStaffMemberList();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });

        } else {
            if (!IsValid(etDesignation.getText().toString())) {
                etDesignation.requestFocus();
                etDesignation.setError("Enter Designation...!");
            }
            if (!IsValid(etPenaltyBy.getText().toString())) {
                etPenaltyBy.requestFocus();
                etPenaltyBy.setError("Enter Penalty By...!");
            }
            if (!IsValid(etOffence.getText().toString())) {
                etOffence.requestFocus();
                etOffence.setError("Enter Offence...!");
            }
            if (!IsValid(etOffenceAmount.getText().toString())) {
                etOffenceAmount.requestFocus();
                etOffenceAmount.setError("Enter Offence Amount...!");
            }
            if (!IsNotZero(Staff_Member_Id)) {
                Common.showSnackError(findViewById(R.id.rootlayout), "Select Staff Member...!");
            }
        }
    }

    private boolean IsNotZero(Integer id) {
        return id != 0;
    }

    private boolean IsValid(String s) {
        return !s.isEmpty() && !s.equals(null);
    }

    private void getStaffMemberList() {
        if (!staffMainList.isEmpty()) {
            staffMainList.clear();
        }
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Getting Staff Members...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<StaffListResponse> call = apiInterface.getStaffList(new RouteRequest(Company_ID));
            call.enqueue(new Callback<StaffListResponse>() {
                @Override
                public void onResponse(Call<StaffListResponse> call, Response<StaffListResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("PART LIST RESPONSE : ", response.body().getMessage());
                                staffMainList.clear();
                                staffData = response.body().getStaffData();
                                staffMainList.add(new StaffDatum(0, "- -", "Select Staff Member", "- - -", ""));
                                for (int i = 0; i < staffData.size(); i++) {
                                    staffMainList.add(new StaffDatum(staffData.get(i).getStaffID(),
                                            staffData.get(i).getStaffCode(),
                                            staffData.get(i).getFirstName(),
                                            staffData.get(i).getMiddleName(),
                                            staffData.get(i).getLastName(),
                                            staffData.get(i).getCurrentAddress1(),
                                            staffData.get(i).getCurrentAddress2(),
                                            staffData.get(i).getCurrentLocality(),
                                            staffData.get(i).getPermanentAddress1(),
                                            staffData.get(i).getPermanentAddress2(),
                                            staffData.get(i).getDesignation(),
                                            staffData.get(i).getCurrentPincode()));
                                }
                                staffListSpinnerAdapter = new StaffListSpinnerAdapter(Staff_Offence_Entry_Activity.this, android.R.layout.simple_spinner_dropdown_item, staffMainList);
                                spStaffMembers.setAdapter(staffListSpinnerAdapter);
                                staffListSpinnerAdapter.notifyDataSetChanged();
                            } else {
                                pg.dismiss();
                                Log.e("PART LIST ERROR 1 : ", response.body().getMessage());
                            }
                        } else {
                            pg.dismiss();
                            Log.e("PART LIST ERROR 2 : ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        Log.e("PART LIST EXCEPTION : ", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<StaffListResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("PART LIST FAILURE : ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Staff_Offence_Entry_Activity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getStaffMemberList();
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
            Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }


}
