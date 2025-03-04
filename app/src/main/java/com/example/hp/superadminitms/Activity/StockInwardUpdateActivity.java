package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
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

import com.example.hp.superadminitms.Adapter.PartStoreListSpinnerAdapter;
import com.example.hp.superadminitms.Adapter.SpinnerCourierAgencyAdapter;
import com.example.hp.superadminitms.Adapter.WholeListPartAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.CourierAgencyDatum;
import com.example.hp.superadminitms.Model.PartStoreDatum;
import com.example.hp.superadminitms.Model.StockInwardDatum;
import com.example.hp.superadminitms.Model.WholeListPartDatum;
import com.example.hp.superadminitms.Network.CourierAgencyRequest;
import com.example.hp.superadminitms.Network.CourierAgencyResponse;
import com.example.hp.superadminitms.Network.PartStoreListResponse;
import com.example.hp.superadminitms.Network.UpdateInwardEntryRequest;
import com.example.hp.superadminitms.Network.UpdateInwardEntryResponse;
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

public class StockInwardUpdateActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener, AdapterView.OnItemSelectedListener {
    private List<StockInwardDatum> inwardListData = new ArrayList<>();
    private Float billAmount;
    private Integer courierAgencyId, storeId;
    private String checkedBy, inwardDate, inwardTime, remark, manufacturer, billNo, courierLrNo;
    private EditText etBillNo, etInwardDate, etInwardTime, etCourierLRNo, etCheckedBy, etBillAmount, etRemark;
    private Spinner spPartStore, spCourierAgency;
    private RecyclerView rvParts;
    private TextView btnSubmit;
    private boolean isConnected;
    private int Courier_Agency_ID, Part_Store_ID;
    private List<WholeListPartDatum> partsData = new ArrayList<>();
    private List<PartStoreDatum> storeData = new ArrayList<>();
    private List<CourierAgencyDatum> courierAgencyData = new ArrayList<>();
    private final List<PartStoreDatum> storeDataMainList = new ArrayList<>();
    private final List<WholeListPartDatum> partInsertionList = new ArrayList<>();
    private final List<CourierAgencyDatum> courierAgencyDataMainList = new ArrayList<>();
    private ProgressDialog pg;
    private WholeListPartAdapter partsAdapter;
    private int quantity;
    private SessionManager session;
    private int CompanyID;
    private PartStoreListSpinnerAdapter partStoreListAdapter;
    private SpinnerCourierAgencyAdapter courierAgencyAdapter;
    private Date d;
    private String Selected_Inward_Date, Selected_Inward_Time, InwardDateTime, InwardDate, UserName;
    private int UserID;
    private final List<UpdateInwardEntryRequest> partInsertionMainList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_inward_update);
        initializeToolBar();
        initializeControls();
        getPartList();
        getPartStoreList();
        getCourierAgencies();
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
        getSupportActionBar().setTitle("Inward Entry Update");
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
        etBillNo = findViewById(R.id.etBillNo);
        etInwardDate = findViewById(R.id.etInwardDate);
        etInwardTime = findViewById(R.id.etInwardTime);
        etCourierLRNo = findViewById(R.id.etCourierLRNo);
        etCheckedBy = findViewById(R.id.etCheckedBy);
        etBillAmount = findViewById(R.id.etBillAmount);
        etRemark = findViewById(R.id.etRemark);
        spPartStore = findViewById(R.id.spPartStore);
        spCourierAgency = findViewById(R.id.spCourierAgency);
        rvParts = findViewById(R.id.rvParts);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);


        spCourierAgency.setOnItemSelectedListener(this);
        spPartStore.setOnItemSelectedListener(this);

        inwardListData = (List<StockInwardDatum>) getIntent().getSerializableExtra("InwardList");

        billNo = inwardListData.get(0).getBillNo();
        billAmount = inwardListData.get(0).getBillAmount();
        courierLrNo = inwardListData.get(0).getCourierLRNumber();
        courierAgencyId = inwardListData.get(0).getCourierAgencyID();
        storeId = inwardListData.get(0).getStoreID();
        checkedBy = inwardListData.get(0).getCheckedBy();
        try {
            inwardDate = Common.convertDateFormat(inwardListData.get(0).getInwardDTM(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            inwardTime = Common.convertDateFormat(inwardListData.get(0).getInwardDTM(), "yyyy-MM-dd'T'HH:mm:ss", "HH:mm:ss");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        remark = inwardListData.get(0).getRemark();
        manufacturer = inwardListData.get(0).getManufacturerName();
        etBillNo.setText(billNo);
        etRemark.setText(remark);
        etBillAmount.setText(String.valueOf(billAmount));
        etCheckedBy.setText(checkedBy);
        etCourierLRNo.setText(courierLrNo);
        etInwardTime.setText(inwardTime);
        etInwardDate.setText(inwardDate);

        etInwardDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(StockInwardUpdateActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year,
                                              int monthOfYear, int dayOfMonth) {
                            d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());
                            //Delivery_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            Selected_Inward_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            etInwardDate.setText(Selected_Inward_Date);
                            try {
                                InwardDate = Common.convertDateFormat(Selected_Inward_Date, "dd-mm-yyyy", "yyyy-mm-dd");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                    etInwardDate.clearFocus();
                }
            }
        });
        etInwardTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(StockInwardUpdateActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());

                            Selected_Inward_Time = hourOfDay + ":" + minute;
                            etInwardTime.setText(Selected_Inward_Time);
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mHour, mMinute, false);
                    timePickerDialog.show();
                    etInwardTime.clearFocus();
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
        switch (view.getId())
        {
            case R.id.btnSubmit:
                isConnected = ConnectivityReceiver.isConnected();
                if (isConnected) {
                    if (!partInsertionList.isEmpty()) {
                        if (IsNotZero(Part_Store_ID) && IsNotZero(Courier_Agency_ID) && IsValid(etBillAmount.getText().toString()) && IsValid(etBillNo.getText().toString())) {
                            updateInwardEntry();
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
                break;
        }
    }*/


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSubmit) {
            isConnected = ConnectivityReceiver.isConnected();

            if (isConnected) {
                if (!partInsertionList.isEmpty()) {
                    if (IsNotZero(Part_Store_ID) && IsNotZero(Courier_Agency_ID) && IsValid(etBillAmount.getText().toString()) && IsValid(etBillNo.getText().toString())) {
                        updateInwardEntry();
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
        if (adapterView.getId() == R.id.spCourierAgency) {
            if (i == 0) {
                Courier_Agency_ID = 0;
            } else {
                Courier_Agency_ID = courierAgencyDataMainList.get(i).getCourierAgencyID();
            }
            Log.e("ID : ", Courier_Agency_ID + " - " + courierAgencyDataMainList.get(i).getCourierAgencyName());
        } else if (adapterView.getId() == R.id.spPartStore) {
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

    private void updateInwardEntry() {
        InwardDateTime = InwardDate + " " + etInwardTime.getText().toString();
        String Rate, Remark, BillNo, ManufacturerName, CheckedBy, OperatorName, CourierLrNo;
        double BillAmount;
        OperatorName = UserName;
        if (etBillAmount.getText().toString().isEmpty()) {
            BillAmount = 0;
        } else {
            BillAmount = Double.parseDouble(etBillAmount.getText().toString());
        }
        if (etCourierLRNo.getText().toString().isEmpty()) {
            CourierLrNo = null;
        } else {
            CourierLrNo = etCourierLRNo.getText().toString();
        }
        if (etCheckedBy.getText().toString().isEmpty()) {
            CheckedBy = null;
        } else {
            CheckedBy = etCheckedBy.getText().toString();
        }
//        if (et.getText().toString().isEmpty()) {
        ManufacturerName = null;
//        } else {
//            ManufacturerName = etManufacturerName.getText().toString();
//        }

        BillNo = etBillNo.getText().toString();
        if (etRemark.getText().toString().isEmpty()) {
            Remark = null;
        } else {
            Remark = etRemark.getText().toString();
        }

        if (!partInsertionList.isEmpty()) {
            partInsertionMainList.clear();
            for (int i = 0; i < partInsertionList.size(); i++) {
                partInsertionMainList.add(new UpdateInwardEntryRequest(UserID,
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
        pg.setMessage("Please Wait...");
        pg.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<UpdateInwardEntryResponse> call = apiInterface.updateInwardEntry(partInsertionMainList);
        call.enqueue(new Callback<UpdateInwardEntryResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<UpdateInwardEntryResponse> call, Response<UpdateInwardEntryResponse> response) {
                pg.dismiss();
                try {
                    if (response.isSuccessful()) {
                        if (response.body().getStatusCode() == 1) {
                            Log.e("UPDATE INWARD ENTRY RESPONSE : ", response.body().getMessage());
                            Toast.makeText(getApplicationContext(), "Inward Entry Update...", Toast.LENGTH_SHORT).show();
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
            public void onFailure(Call<UpdateInwardEntryResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("UPDATE INWARD ENTRY FAILURE : ", t.getMessage());
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(StockInwardUpdateActivity.this, R.style.DialogBox);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateInwardEntry();
                    }
                });
                builder.setCancelable(false);
                builder.show();
            }
        });
    }

    private boolean isExistInInwardData(WholeListPartDatum wholeListPartDatum) {
        for (StockInwardDatum stockInwardDatum : inwardListData) {
            if (stockInwardDatum.getPartID().equals(wholeListPartDatum.getPartID())) {
                return true;
            }
        }
        return false;
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

                                for (StockInwardDatum datum : inwardListData)
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
                            new AlertDialog.Builder(StockInwardUpdateActivity.this, R.style.DialogBox);
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

                for (StockInwardDatum datum : inwardListData) {
                    for (WholeListPartDatum part : partsData) {
                        if (part.getPartID().equals(datum.getPartID())) {
                            part.setNewQuantity(datum.getQuantity());
                            partInsertionList.add(part);
                        }
                    }
                }
                Log.e("ERRRRR", String.valueOf(partInsertionList.size()));

                partsAdapter.notifyDataSetChanged();
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

                        quantity = part.getNewQuantity() != null ? part.getNewQuantity() : 0;

                        if (view.getId() == R.id.ivPartQuantityAdd) {
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

                            Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
                        } else if (view.getId() == R.id.ivPartQuantityMinus) {
                            if (partInsertionList.contains(part)) {
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
                });
            }

            @Override
            public void onFailure(Call<WholePartListResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("PART LIST FAILURE : ", t.getMessage());

                AlertDialog.Builder builder = new AlertDialog.Builder(StockInwardUpdateActivity.this, R.style.DialogBox);
                builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), (dialogInterface, i) -> getPartList());
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
                                partStoreListAdapter = new PartStoreListSpinnerAdapter(StockInwardUpdateActivity.this, android.R.layout.simple_spinner_dropdown_item, storeDataMainList);
                                spPartStore.setAdapter(partStoreListAdapter);
                                partStoreListAdapter.notifyDataSetChanged();
                                for (int i = 0; i < storeData.size(); i++) {
                                    if (storeDataMainList.get(i).getStoreID().equals(storeId)) {
                                        spPartStore.setSelection(i);
                                    }
                                }
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
                            new AlertDialog.Builder(StockInwardUpdateActivity.this, R.style.DialogBox);
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
                                courierAgencyAdapter = new SpinnerCourierAgencyAdapter(StockInwardUpdateActivity.this, android.R.layout.simple_spinner_dropdown_item, courierAgencyDataMainList);
                                spCourierAgency.setAdapter(courierAgencyAdapter);
                                courierAgencyAdapter.notifyDataSetChanged();

                                for (int i = 0; i < courierAgencyDataMainList.size(); i++) {
                                    if (courierAgencyDataMainList.get(i).getCourierAgencyID().equals(courierAgencyId)) {
                                        spCourierAgency.setSelection(i);
                                    }
                                }
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
                            new AlertDialog.Builder(StockInwardUpdateActivity.this, R.style.DialogBox);
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
}
