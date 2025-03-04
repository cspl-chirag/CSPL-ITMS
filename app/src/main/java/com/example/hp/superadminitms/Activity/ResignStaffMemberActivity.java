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
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.hp.superadminitms.Adapter.StaffListSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.StaffDatum;
import com.example.hp.superadminitms.Network.ResigningStaffMemberRequest;
import com.example.hp.superadminitms.Network.ResigningStaffMemberResponse;
import com.example.hp.superadminitms.Network.RouteRequest;
import com.example.hp.superadminitms.Network.StaffListResponse;
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

public class ResignStaffMemberActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private SearchableSpinner spStaffMembers;
    private EditText etCrntLocality, etCrntAddress1, etCrntAddress2, etPrmntAddress1, etPrmntAddress2, etCrntPincode, etDesignation, etResignReason, etResignDate;
    private TextView btnSubmit;
    private int CompanyID;
    private ProgressDialog pg;
    private SessionManager sessionManager;
    private boolean isConnected;
    private List<StaffDatum> staffData = new ArrayList<>();
    private final List<StaffDatum> staffMainList = new ArrayList<>();
    private StaffListSpinnerAdapter staffListSpinnerAdapter;
    private int Staff_ID;
    private int UserID;
    private String ResignDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resign_staff_member);
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
        getSupportActionBar().setTitle("Resign Staff Member");
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
        sessionManager = new SessionManager(this);
        CompanyID = sessionManager.getKeyCompanyId();
        UserID = sessionManager.getKeyUserId();
        pg = Common.showProgressDialog(this);
        pg.setMessage("Please Wait...");
        pg.setCancelable(false);
        pg.setIndeterminate(false);
        spStaffMembers = findViewById(R.id.spStaffMembers);
        etCrntAddress1 = findViewById(R.id.etCrntAddress1);
        etCrntAddress2 = findViewById(R.id.etCrntAddress2);
        etCrntLocality = findViewById(R.id.etCrntLocality);
        etPrmntAddress1 = findViewById(R.id.etPrmntAddress1);
        etPrmntAddress2 = findViewById(R.id.etPrmntAddress2);
        etCrntPincode = findViewById(R.id.etCrntPincode);
        etDesignation = findViewById(R.id.etDesignation);
        etResignReason = findViewById(R.id.etResignReason);
        etResignDate = findViewById(R.id.etResignDate);
        etResignDate.setText(Common.getCurrentDate());
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);
        spStaffMembers.setOnItemSelectedListener(this);
        etResignDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(ResignStaffMemberActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year,
                                              int monthOfYear, int dayOfMonth) {
                            Date d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());
                            //Delivery_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            String Resign_Select_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            etResignDate.setText(Resign_Select_Date);
                            try {
                                ResignDate = Common.convertDateFormat(Resign_Select_Date, "dd-mm-yyyy", "yyyy-mm-dd");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                    etResignDate.clearFocus();
                }
            }
        });
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
            Call<StaffListResponse> call = apiInterface.getStaffList(new RouteRequest(CompanyID));
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
                                staffListSpinnerAdapter = new StaffListSpinnerAdapter(ResignStaffMemberActivity.this, android.R.layout.simple_spinner_dropdown_item, staffMainList);
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
                            new AlertDialog.Builder(ResignStaffMemberActivity.this, R.style.DialogBox);
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

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                isConnected = ConnectivityReceiver.isConnected();
                if (!isConnected) {
                    Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                } else {
                    if (IsZero(Staff_ID) && IsValid(etResignReason.getText().toString())) {
                        resignStaffMember();
                    } else {
                        if (!IsValid(etResignReason.getText().toString())) {
                            Common.showSnack(findViewById(R.id.rootlayout), "Enter Resignation Reason...!");
                        }
                        if (!IsZero(Staff_ID)) {
                            Common.showSnack(findViewById(R.id.rootlayout), "Select Staff Member...!");
                        }
                    }
                }
                break;
        }
    }*/


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            isConnected = ConnectivityReceiver.isConnected();
            if (!isConnected) {
                Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else {
                if (IsZero(Staff_ID) && IsValid(etResignReason.getText().toString())) {
                    resignStaffMember();
                } else {
                    if (!IsValid(etResignReason.getText().toString())) {
                        Common.showSnack(findViewById(R.id.rootlayout), "Enter Resignation Reason...!");
                    }
                    if (!IsZero(Staff_ID)) {
                        Common.showSnack(findViewById(R.id.rootlayout), "Select Staff Member...!");
                    }
                }
            }
        }
    }


    private void resignStaffMember() {
        pg.setMessage("Resigning Member...");
        pg.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResigningStaffMemberResponse> call = apiInterface.resigningStaffMember(new ResigningStaffMemberRequest(CompanyID, UserID, Staff_ID, "2020-01-02", etResignReason.getText().toString()));
        call.enqueue(new Callback<ResigningStaffMemberResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<ResigningStaffMemberResponse> call, Response<ResigningStaffMemberResponse> response) {
                pg.dismiss();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 1) {
                            Log.e("RESIGN STAFF RESPONSE : ", response.body().getMessage());
                            Common.showSnack(findViewById(R.id.rootlayout), response.body().getMessage());
                            onBackPressed();
                        } else {
                            pg.dismiss();
                            Log.e("RESIGN STAFF RESPONSE ERROR 1: ", response.body().getMessage());
                        }
                    } else {
                        pg.dismiss();
                        Log.e("RESIGN STAFF RESPONSE ERROR 2: ", response.errorBody().string());
                    }
                } catch (Exception e) {
                    pg.dismiss();
                    Log.e("RESIGN STAFF RESPONSE EXCEPTION : ", e.getMessage());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<ResigningStaffMemberResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("RESIGN STAFF RESPONSE FAILURE : ", t.getMessage());
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(ResignStaffMemberActivity.this, R.style.DialogBox);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resignStaffMember();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    private boolean IsValid(String string) {
        return !string.isEmpty() && !string.equals(null);
    }

    private boolean IsZero(int id) {
        return id != 0;
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spStaffMembers:
                if (i == 0) {
                    Staff_ID = 0;
                    etCrntLocality.getText().clear();
                    etDesignation.getText().clear();
                    etCrntPincode.getText().clear();
                    etPrmntAddress1.getText().clear();
                    etPrmntAddress2.getText().clear();
                    etCrntAddress1.getText().clear();
                    etCrntAddress2.getText().clear();
                } else {
                    Staff_ID = staffMainList.get(i).getStaffID();
                    etCrntLocality.setText(staffMainList.get(i).getCurrentLocality());
                    etDesignation.setText(staffMainList.get(i).getDesignation());
                    etCrntPincode.setText(staffMainList.get(i).getCurrentPincode());
                    etPrmntAddress1.setText(staffMainList.get(i).getPermanentAddress1());
                    etPrmntAddress2.setText(staffMainList.get(i).getPermanentAddress2());
                    etCrntAddress1.setText(staffMainList.get(i).getCurrentAddress1());
                    etCrntAddress2.setText(staffMainList.get(i).getCurrentAddress2());
                }
                Log.e("ID : ", String.valueOf(Staff_ID) + " - " + staffMainList.get(i).getCurrentLocality());
        }
    }*/
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spStaffMembers) {
            if (i == 0) {
                // Reset fields when the first item is selected
                Staff_ID = 0;
                etCrntLocality.getText().clear();
                etDesignation.getText().clear();
                etCrntPincode.getText().clear();
                etPrmntAddress1.getText().clear();
                etPrmntAddress2.getText().clear();
                etCrntAddress1.getText().clear();
                etCrntAddress2.getText().clear();
            } else {
                // Update fields based on the selected item
                Staff_ID = staffMainList.get(i).getStaffID();
                etCrntLocality.setText(staffMainList.get(i).getCurrentLocality());
                etDesignation.setText(staffMainList.get(i).getDesignation());
                etCrntPincode.setText(staffMainList.get(i).getCurrentPincode());
                etPrmntAddress1.setText(staffMainList.get(i).getPermanentAddress1());
                etPrmntAddress2.setText(staffMainList.get(i).getPermanentAddress2());
                etCrntAddress1.setText(staffMainList.get(i).getCurrentAddress1());
                etCrntAddress2.setText(staffMainList.get(i).getCurrentAddress2());
            }
            Log.e("ID : ", Staff_ID + " - " + staffMainList.get(i).getCurrentLocality());
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
