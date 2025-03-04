package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.PartStoreListSpinnerAdapter;
import com.example.hp.superadminitms.Adapter.SpinnerCourierAgencyAdapter;
import com.example.hp.superadminitms.Adapter.WholeListPartAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.CourierAgencyDatum;
import com.example.hp.superadminitms.Model.PartStoreDatum;
import com.example.hp.superadminitms.Model.WholeListPartDatum;
import com.example.hp.superadminitms.Network.AddInwardEntryRequest;
import com.example.hp.superadminitms.Network.AddInwardEntryResponse;
import com.example.hp.superadminitms.Network.CourierAgencyRequest;
import com.example.hp.superadminitms.Network.CourierAgencyResponse;
import com.example.hp.superadminitms.Network.PartStoreListResponse;
import com.example.hp.superadminitms.Network.WholePartListRequest;
import com.example.hp.superadminitms.Network.WholePartListResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;
import com.example.hp.superadminitms.utils.SearchableSpinner;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Stock_InwardEntryActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, AdapterView.OnItemSelectedListener {
    private EditText etSelectInwardDate, etSelectInwardTime;
    private EditText etBillNo, etManufacturerName, etBillAmount, etCheckedBy;
    private EditText etCourierLrNo, etRemark;
    private SearchableSpinner spCourierAgency, spPartStore;
    private TextView btnSubmit;
    private LinearLayout llAddNewStore, llAddNewPart;
    private int UserID, CompanyID;
    private SessionManager sessionManager;
    private boolean isConnected;
    private String Selected_Inward_Date, Selected_Inward_Time, InwardDate;
    private Date d;
    private String InwardDateTime;
    private ProgressDialog pg;
    private String UserName;
    private int Courier_Agency_ID, Part_Store_ID, Part_ID;
    private List<CourierAgencyDatum> courierAgencyData = new ArrayList<>();
    private SpinnerCourierAgencyAdapter courierAgencyAdapter;
    private final List<CourierAgencyDatum> courierAgencyDataMainList = new ArrayList<>();
    private List<WholeListPartDatum> partsData = new ArrayList<>();
    private List<PartStoreDatum> storeData = new ArrayList<>();
    private final List<PartStoreDatum> storeDataMainList = new ArrayList<>();
    private PartStoreListSpinnerAdapter partStoreListAdapter;
    private SearchView searchView;
    private RecyclerView rvParts;
    private WholeListPartAdapter partsAdapter;
    private int quantity;
    private final List<WholeListPartDatum> partInsertionList = new ArrayList<>();
    private long mLastClickTime = 0;
    private final ArrayList<AddInwardEntryRequest> partInsertionMainList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inward_entry);
        initializeToolBar();
        initializeControls();
        clearList();
        getCourierAgencies();
        getPartList();
        getPartStoreList();
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
        getSupportActionBar().setTitle("Inward Entry");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
            }
        }
    }

    private void clearList() {
        courierAgencyData.clear();
        partsData.clear();
        storeData.clear();
        partInsertionMainList.clear();
        partInsertionList.clear();
        storeDataMainList.clear();
        courierAgencyDataMainList.clear();
    }


    private void initializeControls() {
        clearList();
        isConnected = ConnectivityReceiver.isConnected();
        pg = new ProgressDialog(this, R.style.DialogBox);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        sessionManager = new SessionManager(this);
        UserID = sessionManager.getKeyUserId();
        CompanyID = sessionManager.getKeyCompanyId();
        UserName = sessionManager.getKeyUserName();
        llAddNewPart = findViewById(R.id.llAddNewPart);
        llAddNewStore = findViewById(R.id.llAddNewStore);
        rvParts = findViewById(R.id.rvParts);
        etSelectInwardDate = findViewById(R.id.etInwardDate);
        etSelectInwardTime = findViewById(R.id.etInwardTime);
        etBillNo = findViewById(R.id.etBillNo);
        etManufacturerName = findViewById(R.id.etManufacturerName);
        etBillAmount = findViewById(R.id.etBillAmount);
        etCheckedBy = findViewById(R.id.etCheckedBy);
        etCourierLrNo = findViewById(R.id.etCourierLRNo);
        etRemark = findViewById(R.id.etRemark);
        spCourierAgency = findViewById(R.id.spCourierAgency);
        spCourierAgency.setOnItemSelectedListener(this);
        spPartStore = findViewById(R.id.spPartStore);
        spPartStore.setOnItemSelectedListener(this);
        btnSubmit = findViewById(R.id.btnSubmit);
        if (etSelectInwardDate.getText().toString().isEmpty() && etSelectInwardTime.getText().toString().isEmpty()) {
            Selected_Inward_Date = Common.getCurrentDate();
            Selected_Inward_Time = Common.getCurrentTime();
            etSelectInwardTime.setText(Selected_Inward_Time);
            etSelectInwardDate.setText(Selected_Inward_Date);
            try {
                InwardDate = Common.convertDateFormat(Selected_Inward_Date, "dd-mm-yyyy", "yyyy-mm-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        llAddNewPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    startActivity(new Intent(Stock_InwardEntryActivity.this, Stock_AddPartActivity.class));
                } else {
                    Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                }
            }
        });
        llAddNewStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    startActivity(new Intent(Stock_InwardEntryActivity.this, AddStoreActivity.class));
                } else {
                    Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                }
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    if (!partInsertionList.isEmpty()) {
                        if (IsNotZero(Part_Store_ID) && IsNotZero(Courier_Agency_ID) && IsValid(etBillAmount.getText().toString()) && IsValid(etBillNo.getText().toString())) {
                            inwardEntry();
                        } else {
                            if (!IsNotZero(Part_Store_ID)) {
                                Common.showSnackError(findViewById(R.id.rootlayout), "Select Store...!");
                            }
                            if (!IsNotZero(Courier_Agency_ID)) {
                                Common.showSnackError(findViewById(R.id.rootlayout), "Select Courier Agency...!");
                            }
                            if (!IsValid(etBillAmount.getText().toString())) {
                                Common.showSnackError(findViewById(R.id.rootlayout), "Enter Bill Amount...!");
                            }
                            if (!IsValid(etBillNo.getText().toString())) {
                                Common.showSnackError(findViewById(R.id.rootlayout), "Enter Bill Number...!");
                            }
                        }
                    } else {
                        Common.showSnackError(findViewById(R.id.rootlayout), "Select Part...!");
                    }

                } else {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                }
            }
        });
        etSelectInwardDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(Stock_InwardEntryActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year,
                                              int monthOfYear, int dayOfMonth) {
                            d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());
                            //Delivery_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            Selected_Inward_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            etSelectInwardDate.setText(Selected_Inward_Date);
                            try {
                                InwardDate = Common.convertDateFormat(Selected_Inward_Date, "dd-mm-yyyy", "yyyy-mm-dd");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                    etSelectInwardDate.clearFocus();
                }
            }
        });
        etSelectInwardTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(Stock_InwardEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());

                            Selected_Inward_Time = hourOfDay + ":" + minute;
                            etSelectInwardTime.setText(Selected_Inward_Time);
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mHour, mMinute, false);
                    timePickerDialog.show();
                    etSelectInwardTime.clearFocus();
                }
            }
        });

    }


    private boolean IsNotZero(int id) {

        return id != 0;
    }


    private boolean IsValid(String string) {
        return string != null && string.length() > 2 && string.length() <= 200;
    }


    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spCourierAgency:
                if (i == 0) {
                    Courier_Agency_ID = 0;
                } else {
                    Courier_Agency_ID = courierAgencyDataMainList.get(i).getCourierAgencyID();
                }
                Log.e("ID : ", String.valueOf(Courier_Agency_ID) + " - " + courierAgencyDataMainList.get(i).getCourierAgencyName());
                break;

            case R.id.spPartStore:
                if (i == 0) {
                    Part_Store_ID = 0;
                } else {
                    Part_Store_ID = storeDataMainList.get(i).getStoreID();
                }
                Log.e("ID : ", String.valueOf(Part_Store_ID) + " - " + storeDataMainList.get(i).getStoreName());
                break;
        }
    }*/


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int id = adapterView.getId();

        if (id == R.id.spCourierAgency) {
            if (i == 0) {
                Courier_Agency_ID = 0;
            } else {
                Courier_Agency_ID = courierAgencyDataMainList.get(i).getCourierAgencyID();
            }
            Log.e("ID : ", Courier_Agency_ID + " - " + courierAgencyDataMainList.get(i).getCourierAgencyName());
        } else if (id == R.id.spPartStore) {
            if (i == 0) {
                Part_Store_ID = 0;
            } else {
                Part_Store_ID = storeDataMainList.get(i).getStoreID();
            }
            Log.e("ID : ", Part_Store_ID + " - " + storeDataMainList.get(i).getStoreName());
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /*private void getPartList() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.llRootLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Getting Parts...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<WholePartListResponse> call = apiInterface.getWholePartList(new WholePartListRequest(CompanyID));
            call.enqueue(new Callback<WholePartListResponse>() {
                @Override
                public void onResponse(Call<WholePartListResponse> call, Response<WholePartListResponse> response) {
                    try {
                        pg.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("PART LIST RESPONSE : ", response.body().getMessage());
                                partsData = response.body().getPartsData();
                                rvParts.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                                partsAdapter = new WholeListPartAdapter(getApplicationContext(), partsData);
                                partsAdapter.notifyDataSetChanged();
                                rvParts.setAdapter(partsAdapter);
                                rvParts.setNestedScrollingEnabled(true);
                                rvParts.setVerticalScrollBarEnabled(true);
                                partsAdapter.SetOnItemClickListener(new WholeListPartAdapter.OnItemClickListener() {
                                    public ImageView ivPartMinus;
                                    public ImageView ivPartAdd;
                                    public TextView tvQuantity;

                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onItemClick(View view, int pos) {

                                        WholeListPartDatum part = partsData.get(pos);
                                        RecyclerView.ViewHolder holder = (RecyclerView.ViewHolder) rvParts.findViewHolderForAdapterPosition(pos);

                                        if (holder != null) {
                                            ivPartAdd = holder.itemView.findViewById(R.id.ivPartQuantityAdd);
                                            ivPartMinus = holder.itemView.findViewById(R.id.ivPartQuantityMinus);
                                            tvQuantity = holder.itemView.findViewById(R.id.tvPartQuantity);
                                        }

                                        if (part.getNewQuantity() == null) {
                                            quantity = 0;
                                        } else {
                                            quantity = part.getNewQuantity();
                                        }

                                        switch (view.getId()) {
                                            case R.id.ivPartQuantityAdd:
//                                                if (quantity < part.getQuantity()) {
                                                quantity = quantity + 1;
                                                part.setNewQuantity(quantity);
                                                tvQuantity.setText(String.valueOf(part.getNewQuantity()));

                                                if (partInsertionList.isEmpty()) {
                                                    partInsertionList.add(part);
                                                } else {
                                                    if (partInsertionList.contains(part)) {
                                                        for (WholeListPartDatum p : partInsertionList) {
                                                            if (part.getPartID() == p.getPartID()) {
                                                                p.setNewQuantity(quantity);
                                                                tvQuantity.setText(String.valueOf(quantity));
                                                            }
                                                        }
                                                    } else {
                                                        partInsertionList.add(part);
                                                        break;
                                                    }
                                                }
//                                                }
                                                Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
//                                                Toast.makeText(getApplicationContext(), "Pos" + pos, Toast.LENGTH_LONG).show();
                                                break;
                                            case R.id.ivPartQuantityMinus:

                                                if (!partInsertionList.isEmpty()) {
                                                    if (partInsertionList.contains(part)) {
                                                        for (int i = 0; i < partInsertionList.size(); i++) {
                                                            if (partInsertionList.get(i).getPartID() == part.getPartID()) {
                                                                if (partInsertionList.get(i).getNewQuantity() == 1) {
                                                                    quantity = 0;
                                                                    part.setNewQuantity(quantity);
                                                                    partInsertionList.remove(i);
                                                                    tvQuantity.setText("0");
                                                                    break;
                                                                } else {
                                                                    if (quantity > 0) {
                                                                        quantity = quantity - 1;
                                                                        part.setNewQuantity(quantity);
                                                                        tvQuantity.setText(String.valueOf(quantity));
                                                                        partInsertionList.get(i).setNewQuantity(quantity);
                                                                    }
                                                                    break;
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
//                                                Toast.makeText(getApplicationContext(), "Pos" + pos, Toast.LENGTH_LONG).show();
                                                break;
                                        }
                                    }
                                });
                            } else {
                                Log.e("PART LIST ERROR 1 : ", response.body().getMessage());
                                pg.dismiss();
                            }
                        } else {
                            Log.e("PART LIST ERROR 2 : ", response.errorBody().string());
                            pg.dismiss();
                        }
                    } catch (Exception e) {
                        Log.e("PART LIST EXCEPTION : ", e.getMessage());
                        pg.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<WholePartListResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("PART LIST FAILURE : ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Stock_InwardEntryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getPartList();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }*/


    private void getPartList() {
        isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected) {
            Common.showSnack(findViewById(R.id.llRootLayout), getResources().getString(R.string.NetworkErrorMsg));
            return;
        }

        pg.setMessage("Getting Parts...");
        pg.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<WholePartListResponse> call = apiInterface.getWholePartList(new WholePartListRequest(CompanyID));

        call.enqueue(new Callback<WholePartListResponse>() {
            @Override
            public void onResponse(Call<WholePartListResponse> call, Response<WholePartListResponse> response) {
                pg.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().getStatusCode() == 1) {
                        Log.e("PART LIST RESPONSE : ", response.body().getMessage());
                        partsData = response.body().getPartsData();

                        rvParts.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                        partsAdapter = new WholeListPartAdapter(getApplicationContext(), partsData);
                        rvParts.setAdapter(partsAdapter);
                        rvParts.setNestedScrollingEnabled(true);
                        rvParts.setVerticalScrollBarEnabled(true);

                        partsAdapter.SetOnItemClickListener(new WholeListPartAdapter.OnItemClickListener() {
                            public ImageView ivPartMinus;
                            public ImageView ivPartAdd;
                            public TextView tvQuantity;

                            @Override
                            public void onItemClick(View view, int pos) {
                                WholeListPartDatum part = partsData.get(pos);
                                RecyclerView.ViewHolder holder = rvParts.findViewHolderForAdapterPosition(pos);

                                if (holder != null) {
                                    ivPartAdd = holder.itemView.findViewById(R.id.ivPartQuantityAdd);
                                    ivPartMinus = holder.itemView.findViewById(R.id.ivPartQuantityMinus);
                                    tvQuantity = holder.itemView.findViewById(R.id.tvPartQuantity);
                                }

                                int quantity = part.getNewQuantity() != null ? part.getNewQuantity() : 0;

                                if (view.getId() == R.id.ivPartQuantityAdd) {
                                    quantity = quantity + 1;
                                    part.setNewQuantity(quantity);
                                    tvQuantity.setText(String.valueOf(part.getNewQuantity()));

                                    if (partInsertionList.isEmpty()) {
                                        partInsertionList.add(part);
                                    } else if (partInsertionList.contains(part)) {
                                        for (WholeListPartDatum p : partInsertionList) {
                                            if (part.getPartID() == p.getPartID()) {
                                                p.setNewQuantity(quantity);
                                                tvQuantity.setText(String.valueOf(quantity));
                                            }
                                        }
                                    } else {
                                        partInsertionList.add(part);
                                    }
                                    Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
                                } else if (view.getId() == R.id.ivPartQuantityMinus) {
                                    if (!partInsertionList.isEmpty() && partInsertionList.contains(part)) {
                                        for (int i = 0; i < partInsertionList.size(); i++) {
                                            if (partInsertionList.get(i).getPartID() == part.getPartID()) {
                                                if (partInsertionList.get(i).getNewQuantity() == 1) {
                                                    quantity = 0;
                                                    part.setNewQuantity(quantity);
                                                    partInsertionList.remove(i);
                                                    tvQuantity.setText("0");
                                                } else if (quantity > 0) {
                                                    quantity = quantity - 1;
                                                    part.setNewQuantity(quantity);
                                                    tvQuantity.setText(String.valueOf(quantity));
                                                    partInsertionList.get(i).setNewQuantity(quantity);
                                                }
                                                break;
                                            }
                                        }
                                    }
                                    Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
                                }
                            }
                        });
                    } else {
                        Log.e("PART LIST ERROR 1 : ", response.body().getMessage());
                    }
                } else {
                    try {
                        Log.e("PART LIST ERROR 2 : ", response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("PART LIST ERROR 2 : ", e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<WholePartListResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("PART LIST FAILURE : ", t.getMessage());
                AlertDialog.Builder builder = new AlertDialog.Builder(Stock_InwardEntryActivity.this, R.style.DialogBox);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getPartList();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });
    }


    private void getPartStoreList() {
        if (!storeDataMainList.isEmpty()) {
            storeDataMainList.clear();
        }
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            pg.setMessage("Getting Stores...");
            pg.show();
            Call<PartStoreListResponse> call = apiInterface.getStoreList(new WholePartListRequest(CompanyID));
            call.enqueue(new Callback<PartStoreListResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<PartStoreListResponse> call, Response<PartStoreListResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("STORE LIST RESPONSE : ", response.body().getMessage());
                                storeData = response.body().getStoreData();
                                storeDataMainList.clear();
                                storeDataMainList.add(new PartStoreDatum(0, "- - Select Store - -"));
                                for (int i = 0; i < storeData.size(); i++) {
                                    storeDataMainList.add(new PartStoreDatum(storeData.get(i).getStoreID(), storeData.get(i).getStoreName()));
                                }
                                partStoreListAdapter = new PartStoreListSpinnerAdapter(Stock_InwardEntryActivity.this, android.R.layout.simple_spinner_dropdown_item, storeDataMainList);
                                spPartStore.setAdapter(partStoreListAdapter);
                                partStoreListAdapter.notifyDataSetChanged();
                            } else {
                                pg.dismiss();
                                Log.e("STORE LIST RESPONSE ERROR 1: ", response.body().getMessage());
                            }
                        } else {
                            pg.dismiss();
                            Log.e("STORE LIST RESPONSE ERROR 2: ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        Log.e("STORE LIST RESPONSE EXCEPTION: ", e.getMessage());
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<PartStoreListResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("STORE LIST RESPONSE EXCEPTION: ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Stock_InwardEntryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getPartStoreList();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void getCourierAgencies() {
        if (!courierAgencyDataMainList.isEmpty()) {
            courierAgencyDataMainList.clear();
        }
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            pg.setMessage("Getting Courier Agencies...");
            pg.show();
            Call<CourierAgencyResponse> call = apiInterface.getCourierAgencyList(new CourierAgencyRequest(CompanyID));
            call.enqueue(new Callback<CourierAgencyResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<CourierAgencyResponse> call, Response<CourierAgencyResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("Courier Agency Response : ", response.body().getMessage());
                                courierAgencyData = response.body().getCourierAgencyData();
                                courierAgencyDataMainList.clear();
                                courierAgencyDataMainList.add(new CourierAgencyDatum(0, "- - Select Courier Agency - -"));
                                for (int i = 0; i < courierAgencyData.size(); i++) {
                                    courierAgencyDataMainList.add(new CourierAgencyDatum(courierAgencyData.get(i).getCourierAgencyID(), courierAgencyData.get(i).getCourierAgencyName()));
                                }
                                Log.e("courierAgencyDataMainList : ", String.valueOf(courierAgencyDataMainList.size()));
                                courierAgencyAdapter = new SpinnerCourierAgencyAdapter(Stock_InwardEntryActivity.this, android.R.layout.simple_spinner_dropdown_item, courierAgencyDataMainList);
                                spCourierAgency.setAdapter(courierAgencyAdapter);
                                courierAgencyAdapter.notifyDataSetChanged();
                            } else {
                                pg.dismiss();
                                Log.e("Courier Agency Error1 : ", response.body().getMessage());
                            }
                        } else {
                            pg.dismiss();
                            Log.e("Courier Agency Error2 : ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        Log.e("Courier Agency Exception : ", e.getMessage());
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<CourierAgencyResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("Courier Agency Failure : ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Stock_InwardEntryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getCourierAgencies();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void inwardEntry() {
        InwardDateTime = InwardDate + " " + etSelectInwardTime.getText().toString();
        String Rate, Remark, BillNo, ManufacturerName, CheckedBy, OperatorName, CourierLrNo;
        int BillAmount;
        OperatorName = UserName;
        if (etBillAmount.getText().toString().isEmpty()) {
            BillAmount = 0;
        } else {
            BillAmount = Integer.parseInt(etBillAmount.getText().toString());
        }
        if (etCourierLrNo.getText().toString().isEmpty()) {
            CourierLrNo = null;
        } else {
            CourierLrNo = etCourierLrNo.getText().toString();
        }
        if (etCheckedBy.getText().toString().isEmpty()) {
            CheckedBy = null;
        } else {
            CheckedBy = etCheckedBy.getText().toString();
        }
        if (etManufacturerName.getText().toString().isEmpty()) {
            ManufacturerName = null;
        } else {
            ManufacturerName = etManufacturerName.getText().toString();
        }

        BillNo = etBillNo.getText().toString();
        if (etRemark.getText().toString().isEmpty()) {
            Remark = null;
        } else {
            Remark = etRemark.getText().toString();
        }

        if (!partInsertionList.isEmpty()) {
            partInsertionMainList.clear();
            for (int i = 0; i < partInsertionList.size(); i++) {
                partInsertionMainList.add(new AddInwardEntryRequest(UserID,
                        CompanyID,
                        BillNo,
                        partInsertionList.get(i).getPartID(),
                        Part_Store_ID,
                        ManufacturerName,
                        InwardDateTime,
                        partInsertionList.get(i).getNewQuantity(),
                        0.0,
                        BillAmount,
                        CheckedBy,
                        OperatorName,
                        Courier_Agency_ID,
                        CourierLrNo,
                        Remark));
            }
        }
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        pg.setMessage("Please Wait...");
        pg.show();
        Call<AddInwardEntryResponse> call = apiInterface.addInwardEntry(partInsertionMainList);
        call.enqueue(new Callback<AddInwardEntryResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<AddInwardEntryResponse> call, Response<AddInwardEntryResponse> response) {
                pg.dismiss();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 1) {

                            Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("Inward Entry Response : ", response.body().getMessage());
                            finish();
                        } else {
                            pg.dismiss();
                            Log.e("Inward Entry ERROR 1 : ", response.body().getMessage());
                            Common.showSnackError(findViewById(R.id.rootlayout), response.body().getMessage());
                        }
                    } else {
                        pg.dismiss();
                        Log.e("Inward Entry ERROR 2 : ", response.errorBody().string());
                    }
                } catch (Exception e) {
                    pg.dismiss();
                    Log.e("Inward Entry Exception : ", e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<AddInwardEntryResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("Inward Entry Failure : ", t.getMessage());
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(Stock_InwardEntryActivity.this, R.style.DialogBox);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        inwardEntry();
                    }
                });
                builder.setCancelable(false);
                builder.show();

            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.llRootLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem search = menu.findItem(R.id.search);
        searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setQueryHint("Enter Partname to be seach");
        searchView.setPadding(0, 5, 0, 5);
        searchView.setBackground(getResources().getDrawable(R.drawable.bg_green_rounded_corner_8dp));
        searchView.setFocusableInTouchMode(true);
        search(searchView);

        return true;
    }

    private void search(final SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                if (partsData.size() > 0) {
//                    partsAdapter.getFilter().filter(newText);
//                    return true;
//                }
//                return true;
                for (int position = 0; position < partsData.size(); position++) {

                    if (partsData.get(position).getPartCode() != null) {
                        if (partsData.get(position).getPartCode().toUpperCase().contains(newText.toUpperCase()) || partsData.get(position).getPartCode().toLowerCase().contains(newText.toLowerCase())) {
                            rvParts.smoothScrollToPosition(position + 2);
                            break;
                        }
                    } else {
                        if (partsData.get(position).getPartName().toUpperCase().startsWith(newText.toUpperCase()) || partsData.get(position).getPartName().toLowerCase().startsWith(newText.toLowerCase())) {
                            rvParts.smoothScrollToPosition(position + 2);
                            break;
                        } else if (partsData.get(position).getPartName().toUpperCase().contains(newText.toLowerCase()) || partsData.get(position).getPartName().toLowerCase().contains(newText.toLowerCase())) {
//                        rvParts.scrollToPosition(position);
                            rvParts.smoothScrollToPosition(position + 2);
                            break;
                        }
                    }
                    if (partsData.get(position).getPartName().toUpperCase().startsWith(newText.toUpperCase()) || partsData.get(position).getPartName().toLowerCase().startsWith(newText.toLowerCase())) {
                        rvParts.smoothScrollToPosition(position + 2);
                        break;
                    } else if (partsData.get(position).getPartName().toUpperCase().contains(newText.toLowerCase()) || partsData.get(position).getPartName().toLowerCase().contains(newText.toLowerCase())) {
//                        rvParts.scrollToPosition(position);
                        rvParts.smoothScrollToPosition(position + 2);
                        break;
                    }
//                    else
//                    {
//                        if (partsData.get(position).getPartName().toUpperCase().contains(newText)) {
////                        rvParts.scrollToPosition(position);
//                            rvParts.smoothScrollToPosition(position + 1);
//                            break;
//                        }
//                        if (partsData.get(position).getPartName().toLowerCase().contains(newText))
//                        {
//                            rvParts.smoothScrollToPosition(position + 1);
//                            break;
//                        }
//
//
//                    }
//                    else {
//                        position++;
//                    }
                }
//                partsAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }


    private void hideSoftKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void diableDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.clearFocus();
            hideSoftKeyBoard();
            invalidateOptionsMenu();
            getPartList();
            getPartStoreList();
            getCourierAgencies();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        clearList();
    }
}
