package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Adapter.WholeListPartAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.StockOutwardDatum;
import com.example.hp.superadminitms.Model.WholeListPartDatum;
import com.example.hp.superadminitms.Network.BusRequest;
import com.example.hp.superadminitms.Network.BusResponse;
import com.example.hp.superadminitms.Network.UpdateOutwardEntryRequest;
import com.example.hp.superadminitms.Network.UpdateOutwardEntryResponse;
import com.example.hp.superadminitms.Network.WholePartListRequest;
import com.example.hp.superadminitms.Network.WholePartListResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockOutwardUpdateActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, AdapterView.OnItemSelectedListener, View.OnClickListener {
    private List<StockOutwardDatum> outwardListData = new ArrayList<>();
    private String givenby, outwardDate, outwardTime, remark;
    private EditText etOutwardDate, etOutwardTime, etRemark, etGivenBy;
    private Spinner spVehileRegNo;
    private RecyclerView rvParts;
    private TextView btnSubmit;
    private boolean isConnected;
    private List<WholeListPartDatum> partsData = new ArrayList<>();
    private final List<WholeListPartDatum> partInsertionList = new ArrayList<>();
    private ProgressDialog pg;
    private WholeListPartAdapter partsAdapter;
    private int quantity;
    private SessionManager session;
    private int CompanyID;
    private Date d;
    private String Selected_Outward_Date, Selected_Outward_Time, OutwardDateTime, OutwardDate, UserName;
    private int UserID;
    private Integer vehicleId = 0, BatchId;
    private List<BusDatum> busDatum = new ArrayList<>();
    private VehicleSpinnerAdapter spVehicleAdapter;
    private final List<UpdateOutwardEntryRequest> partInsertionMainList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_outward_update);
        initializeToolBar();
        initializeControls();
        getVehicleInfoSpinner();
        getPartList();
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
        getSupportActionBar().setTitle("Outward Entry Update");
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
        session = new SessionManager(this);
        CompanyID = session.getKeyCompanyId();
        UserID = session.getKeyUserId();
        UserName = session.getKeyUserName();
        pg = Common.showProgressDialog(this);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        etGivenBy = findViewById(R.id.etGivenBy);
        etOutwardDate = findViewById(R.id.etOutwardDate);
        etOutwardTime = findViewById(R.id.etOutwardTime);
        etRemark = findViewById(R.id.etRemark);
        rvParts = findViewById(R.id.rvParts);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        spVehileRegNo = findViewById(R.id.spVehileRegNo);
        spVehileRegNo.setOnItemSelectedListener(this);

        outwardListData = (List<StockOutwardDatum>) getIntent().getSerializableExtra("OutwardList");
        BatchId = outwardListData.get(0).getOutwardBatchID();
        givenby = outwardListData.get(0).getGivenBy();
        vehicleId = outwardListData.get(0).getVehicleID();
        try {
            outwardDate = Common.convertDateFormat(outwardListData.get(0).getOutwardDTM(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            outwardTime = Common.convertDateFormat(outwardListData.get(0).getOutwardDTM(), "yyyy-MM-dd'T'HH:mm:ss", "HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        remark = outwardListData.get(0).getRemark();
        etRemark.setText(remark);
        etGivenBy.setText(givenby);
        etOutwardTime.setText(outwardTime);
        etOutwardDate.setText(outwardDate);

        etOutwardDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(StockOutwardUpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year,
                                              int monthOfYear, int dayOfMonth) {
                            d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());
                            //Delivery_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            Selected_Outward_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            etOutwardDate.setText(Selected_Outward_Date);
                            try {
                                OutwardDate = Common.convertDateFormat(Selected_Outward_Date, "dd-mm-yyyy", "yyyy-mm-dd");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                    etOutwardDate.clearFocus();
                }
            }
        });
        etOutwardTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(StockOutwardUpdateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());

                            Selected_Outward_Time = hourOfDay + ":" + minute;
                            etOutwardTime.setText(Selected_Outward_Time);
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mHour, mMinute, false);
                    timePickerDialog.show();
                    etOutwardTime.clearFocus();
                }
            }
        });
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                Log.e("Vehicle ID : ", String.valueOf(vehicleId));
                isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    if (!partInsertionList.isEmpty()) {
                        if (IsNotZero(vehicleId) && IsValid(etGivenBy.getText().toString())) {
                            updateOutwardEntry();
                        } else {

                            if (!IsNotZero(vehicleId)) {
                                Common.showSnackError(findViewById(R.id.rootlayout), "Select Bus...!");
                            }
                            if (!IsValid(etGivenBy.getText().toString())) {
                                Common.showSnackError(findViewById(R.id.rootlayout), "Enter Given By...!");
                            }
                        }
                    } else {
                        Common.showSnackError(findViewById(R.id.rootlayout), "Select Part...!");
                    }

                } else {
                    Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
                }
                break;
        }
    }*/


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            Log.e("Vehicle ID : ", String.valueOf(vehicleId));

            isConnected = ConnectivityReceiver.isConnected();
            if (!isConnected) {
                // No network connection
                Common.showSnackError(findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
            } else if (partInsertionList.isEmpty()) {
                // No parts selected
                Common.showSnackError(findViewById(R.id.rootlayout), "Select Part...!");
            } else if (!IsNotZero(vehicleId)) {
                // Vehicle ID is zero or invalid
                Common.showSnackError(findViewById(R.id.rootlayout), "Select Bus...!");
            } else if (!IsValid(etGivenBy.getText().toString())) {
                // "Given By" field is invalid
                Common.showSnackError(findViewById(R.id.rootlayout), "Enter Given By...!");
            } else {
                // All checks passed
                updateOutwardEntry();
            }
        }
    }


    private boolean IsNotZero(int id) {

        return id != 0;
    }


    private boolean IsValid(String string) {
        return string != null && string.length() > 2 && string.length() <= 200;
    }

   /* @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spVehileRegNo:
                    vehicleId = busDatum.get(i).getVehicleID();
                break;
        }
    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spVehileRegNo) {
            vehicleId = busDatum.get(i).getVehicleID();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void getVehicleInfoSpinner() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(StockOutwardUpdateActivity.this, R.style.DialogBox);
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
            Call<BusResponse> call = apiInterface.getVehicleInfo(new BusRequest(CompanyID));
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
                                busDatum = response.body().getBusData();
                                spVehicleAdapter = new VehicleSpinnerAdapter(StockOutwardUpdateActivity.this, android.R.layout.simple_spinner_dropdown_item, busDatum);
                                spVehileRegNo.setAdapter(spVehicleAdapter);
                                spVehicleAdapter.notifyDataSetChanged();

                                for (int i = 0; i < busDatum.size(); i++) {
                                    if (vehicleId.equals(busDatum.get(i).getVehicleID())) {
                                        spVehileRegNo.setSelection(i);
                                    }
                                }
                            } else {
                                if (pg.isShowing())
                                    pg.dismiss();
                                Log.d("BusListError1 : ", response.body().getMessage());
                            }
                        } else {
                            if (pg.isShowing())
                                pg.dismiss();
                            Log.d("BusListEerror2 : ", response.body().getMessage());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing())
                            pg.dismiss();
                        e.printStackTrace();
                        Log.d("BusListExcption : ", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<BusResponse> call, Throwable t) {
                    if (pg.isShowing())
                        pg.dismiss();
                    Log.d("BusListFailure : ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(StockOutwardUpdateActivity.this, R.style.DialogBox);
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

                                for (StockOutwardDatum datum : outwardListData)
                                {
                                    for (int i = 0 ; i < partsData.size(); i++)
                                    {
                                        Log.e("ERRRRR", String.valueOf(datum.getPartID() + " - " + String.valueOf(partsData.get(i).getPartID())));

                                        if (partsData.get(i).getPartID().equals(datum.getPartID())) {
                                            WholeListPartDatum part = partsData.get(i);
                                            part.setNewQuantity(datum.getQuantity());
                                            partInsertionList.add(part);
                                        }
                                    }
                                }
                                Log.e("ERRRRR",String.valueOf(partInsertionList.size()));

                                partsAdapter.notifyDataSetChanged();

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
                                                Log.e("ADD CLICKED...Quantity__", String.valueOf(part.getQuantity()));
                                                if (quantity < part.getQuantity()) {
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
                                                }
                                                Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
//                                                Toast.makeText(getApplicationContext(), "Pos" + pos, Toast.LENGTH_LONG).show();
                                                break;
                                            case R.id.ivPartQuantityMinus:
                                                Log.e("MINUS CLICKED...Quantity__", String.valueOf(part.getQuantity()));
                                                if (!partInsertionList.isEmpty()) {
                                                    if (partInsertionList.contains(part)) {
                                                        for (int i = 0; i < partInsertionList.size(); i++) {
                                                            if (partInsertionList.get(i).getPartID() == part.getPartID()) {
                                                                if (partInsertionList.get(i).getNewQuantity() == 1) {
                                                                    if(isExistInInwardData(part)) {
                                                                        quantity = 1;
                                                                        part.setNewQuantity(quantity);
                                                                        tvQuantity.setText("1");
                                                                    } else {
                                                                        quantity = 0;
                                                                        part.setNewQuantity(quantity);
                                                                        partInsertionList.remove(i);
                                                                        tvQuantity.setText("0");
                                                                    }
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
                            new AlertDialog.Builder(StockOutwardUpdateActivity.this, R.style.DialogBox);
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

                if (!response.isSuccessful()) {
                    try {
                        Log.e("PART LIST ERROR 2 : ", response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("PART LIST ERROR IO : ", e.getMessage());
                    }
                    return;
                }

                WholePartListResponse body = response.body();
                if (body.getStatusCode() != 1) {
                    Log.e("PART LIST ERROR 1 : ", body.getMessage());
                    return;
                }

                Log.e("PART LIST RESPONSE : ", body.getMessage());
                partsData = body.getPartsData();
                rvParts.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                partsAdapter = new WholeListPartAdapter(getApplicationContext(), partsData);
                rvParts.setAdapter(partsAdapter);
                rvParts.setNestedScrollingEnabled(true);
                rvParts.setVerticalScrollBarEnabled(true);

                for (StockOutwardDatum datum : outwardListData) {
                    for (int i = 0; i < partsData.size(); i++) {
                        if (partsData.get(i).getPartID().equals(datum.getPartID())) {
                            WholeListPartDatum part = partsData.get(i);
                            part.setNewQuantity(datum.getQuantity());
                            partInsertionList.add(part);
                        }
                    }
                }

                Log.e("ERRRRR", String.valueOf(partInsertionList.size()));
                partsAdapter.notifyDataSetChanged();

                partsAdapter.SetOnItemClickListener(new WholeListPartAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int pos) {
                        WholeListPartDatum part = partsData.get(pos);
                        RecyclerView.ViewHolder holder = rvParts.findViewHolderForAdapterPosition(pos);

                        if (holder != null) {
                            ImageView ivPartAdd = holder.itemView.findViewById(R.id.ivPartQuantityAdd);
                            ImageView ivPartMinus = holder.itemView.findViewById(R.id.ivPartQuantityMinus);
                            TextView tvQuantity = holder.itemView.findViewById(R.id.tvPartQuantity);

                            int quantity = (part.getNewQuantity() != null) ? part.getNewQuantity() : 0;

                            if (view.getId() == R.id.ivPartQuantityAdd) {
                                Log.e("ADD CLICKED...Quantity__", String.valueOf(part.getQuantity()));

                                if (quantity < part.getQuantity()) {
                                    quantity += 1;
                                    part.setNewQuantity(quantity);
                                    tvQuantity.setText(String.valueOf(quantity));

                                    if (partInsertionList.isEmpty() || !partInsertionList.contains(part)) {
                                        partInsertionList.add(part);
                                    } else {
                                        for (WholeListPartDatum p : partInsertionList) {
                                            if (part.getPartID().equals(p.getPartID())) {
                                                p.setNewQuantity(quantity);
                                                tvQuantity.setText(String.valueOf(quantity));
                                            }
                                        }
                                    }
                                }

                                Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
                            } else if (view.getId() == R.id.ivPartQuantityMinus) {
                                Log.e("MINUS CLICKED...Quantity__", String.valueOf(part.getQuantity()));

                                if (!partInsertionList.isEmpty() && partInsertionList.contains(part)) {
                                    for (int i = 0; i < partInsertionList.size(); i++) {
                                        if (partInsertionList.get(i).getPartID().equals(part.getPartID())) {
                                            if (partInsertionList.get(i).getNewQuantity() == 1) {
                                                if (isExistInInwardData(part)) {
                                                    quantity = 1;
                                                    part.setNewQuantity(quantity);
                                                    tvQuantity.setText("1");
                                                } else {
                                                    quantity = 0;
                                                    part.setNewQuantity(quantity);
                                                    partInsertionList.remove(i);
                                                    tvQuantity.setText("0");
                                                }
                                            } else {
                                                if (quantity > 0) {
                                                    quantity -= 1;
                                                    part.setNewQuantity(quantity);
                                                    tvQuantity.setText(String.valueOf(quantity));
                                                    partInsertionList.get(i).setNewQuantity(quantity);
                                                }
                                            }
                                            break;
                                        }
                                    }
                                }

                                Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<WholePartListResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("PART LIST FAILURE : ", t.getMessage());

                AlertDialog.Builder builder = new AlertDialog.Builder(StockOutwardUpdateActivity.this, R.style.DialogBox);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), (dialogInterface, i) -> getPartList());
                builder.setCancelable(false);
                builder.show();
            }
        });
    }


    private boolean isExistInInwardData(WholeListPartDatum wholeListPartDatum) {
        for (StockOutwardDatum datum : outwardListData) {
            if (datum.getPartID().equals(wholeListPartDatum.getPartID())) {
                return true;
            }
        }
        return false;
    }

    private void updateOutwardEntry() {
        try {
            OutwardDate = Common.convertDateFormat(outwardDate, "dd-MM-yyyy", "yyyy-MM-dd");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        OutwardDateTime = OutwardDate + " " + etOutwardTime.getText().toString();
        String GivenBy, Remark, OperatorName;
        double BillAmount;
        OperatorName = UserName;

        GivenBy = etGivenBy.getText().toString();
        if (etRemark.getText().toString().isEmpty()) {
            Remark = null;
        } else {
            Remark = etRemark.getText().toString();
        }

        if (!partInsertionList.isEmpty()) {
            partInsertionMainList.clear();
            for (int i = 0; i < partInsertionList.size(); i++) {
                partInsertionMainList.add(new UpdateOutwardEntryRequest(UserID,
                        CompanyID,
                        BatchId,
                        partInsertionList.get(i).getPartID(),
                        vehicleId,
                        OutwardDateTime,
                        partInsertionList.get(i).getNewQuantity(),
                        GivenBy,
                        OperatorName,
                        Remark));
            }
        }
        pg.setMessage("Please Wait...");
        pg.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UpdateOutwardEntryResponse> call = apiInterface.updateOutwardEntry(partInsertionMainList);
        call.enqueue(new Callback<UpdateOutwardEntryResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<UpdateOutwardEntryResponse> call, Response<UpdateOutwardEntryResponse> response) {
                pg.dismiss();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 1) {
                            Log.e("UPDATE INWARD ENTRY RESPONSE : ", response.body().getMessage());
                            Toast.makeText(getApplicationContext(), "Outward Entry Update...", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            pg.dismiss();
                            Log.e("UPDATE INWARD ENTRY ERROR 1 : ", response.body().getMessage());
                        }
                    } else {
                        pg.dismiss();
                        Log.e("UPDATE INWARD ENTRY ERROR 2 : ", response.errorBody().string());
                    }
                } catch (Exception e) {
                    pg.dismiss();
                    Log.e("UPDATE INWARD ENTRY EXCEPTION : ", e.getMessage());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<UpdateOutwardEntryResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("UPDATE INWARD ENTRY FAILURE : ", t.getMessage());
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(StockOutwardUpdateActivity.this, R.style.DialogBox);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateOutwardEntry();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });
    }
}
