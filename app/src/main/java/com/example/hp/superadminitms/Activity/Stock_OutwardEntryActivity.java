package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.PartListSpinnerAdapter;
import com.example.hp.superadminitms.Adapter.PartsAdapter;
import com.example.hp.superadminitms.Adapter.VehicleSpinnerAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.BusDatum;
import com.example.hp.superadminitms.Model.PartsDatum;
import com.example.hp.superadminitms.Network.BusRequest;
import com.example.hp.superadminitms.Network.BusResponse;
import com.example.hp.superadminitms.Network.OutwardEntryRequest;
import com.example.hp.superadminitms.Network.OutwardEntryResponse;
import com.example.hp.superadminitms.Network.PartListResponse;
import com.example.hp.superadminitms.Network.WholePartListRequest;
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

public class Stock_OutwardEntryActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = "*************";
    private SessionManager sessionManager;
    private EditText etOutwardDate, etOutwardtime, etPartReplaceRemark, etGivenBy;
    private TextView btnSubmitParts;
    private RecyclerView rvParts;
    private String UserName;
    private int UserID, Company_ID;
    private String Selected_Date, Selected_Time, OutwardDateTime;
    private Date d;
    private boolean isConnected;
    private List<PartsDatum> partsData = new ArrayList<>();
    private TextView tvVehicle;
    private ProgressDialog pg;
    private long mLastClickTime = 0;
    private PartListSpinnerAdapter partAdapter;
    private int Part_ID;
    private final int Actual_Part_Quantity = 0;
    private TextView tvQuantity;
    private final int Quantity = 0;
    private SearchView searchView;
    private PartsAdapter partsAdapter;
    private int quantity;
    private final List<PartsDatum> partInsertionList = new ArrayList<>();
    private final List<OutwardEntryRequest> partInsertionMainList = new ArrayList<>();
    private int Vehicle_Id;
    private List<BusDatum> busDatum = new ArrayList<>();
    private VehicleSpinnerAdapter spVehicleAdapter;
    private SearchableSpinner spVehicleRegNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock__outward_entry);
        initializeToolBar();
        initializeControls();
        getPartList();
        getVehicleInfoSpinner();
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
        getSupportActionBar().setTitle("Outward Entry");
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
        pg = new ProgressDialog(this, R.style.DialogBox);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        rvParts = findViewById(R.id.rvParts);
        tvQuantity = findViewById(R.id.tvPartQuantity);
        sessionManager = new SessionManager(this);
        etOutwardDate = findViewById(R.id.etOutwardDate);
        etOutwardtime = findViewById(R.id.etOutwardTime);
        etPartReplaceRemark = findViewById(R.id.etPartReplaceRemark);
        etGivenBy = findViewById(R.id.etGivenBy);
        btnSubmitParts = findViewById(R.id.btnSubmitParts);
        Company_ID = sessionManager.getKeyCompanyId();
        UserID = sessionManager.getKeyUserId();
        spVehicleRegNo = findViewById(R.id.spVehileRegNo);
        UserName = sessionManager.getKeyUserName();
        btnSubmitParts.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                diableDoubleClick();
//                hideSoftKeyBoard();
//                searchView.clearFocus();
                if (partInsertionList.isEmpty()) {
                    Common.showSnackError(findViewById(R.id.llRootLayout), "Select Part...!");
                } else {
                    addOutwardEntry();
                }
            }
        });
        if (etOutwardDate.getText().toString().isEmpty() && etOutwardtime.getText().toString().isEmpty()) {
            Selected_Date = Common.getCurrentDate();
            Selected_Time = Common.getCurrentTime();
            etOutwardtime.setText(Selected_Time);
            etOutwardDate.setText(Selected_Date);
            try {
                OutwardDateTime = Common.convertDateFormat(Selected_Date, "dd-mm-yyyy", "yyyy-mm-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        etOutwardDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                diableDoubleClick();
//                hideSoftKeyBoard();
//                searchView.clearFocus();
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(Stock_OutwardEntryActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year,
                                              int monthOfYear, int dayOfMonth) {
                            d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());
                            //Delivery_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            Selected_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            etOutwardDate.setText(Selected_Date);
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
//                    etOutwardDate.clearFocus();
                }
            }
        });
        etOutwardtime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                diableDoubleClick();
//                hideSoftKeyBoard();
//                searchView.clearFocus();
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(Stock_OutwardEntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            d = new Date();
                            //Order_Date  = (String) DateFormat.format("yyyy/MM/dd", d.getTime());

                            Selected_Time = hourOfDay + ":" + minute;
                            etOutwardtime.setText(Selected_Time);
                            //Log.i("SELECTED DATE :::",Delivery_Date.toString());
                        }
                    }, mHour, mMinute, false);
                    timePickerDialog.show();
//                    etOutwardtime.clearFocus();
                }
            }
        });
    }

    private boolean IsNotZero(int id) {
        return id != 0;
    }

    private void diableDoubleClick() {
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
    }


    @SuppressLint("LongLogTag")
    private void addOutwardEntry() {
        if (IsValid(etGivenBy.getText().toString())) {
            try {
                OutwardDateTime = Common.convertDateFormat(etOutwardDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd") + " " + etOutwardtime.getText().toString();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String OperatorName = UserName;
            String PartReplaceRemark;
            String GivenBy = etGivenBy.getText().toString();
            if (!etPartReplaceRemark.getText().toString().isEmpty()) {
                PartReplaceRemark = etPartReplaceRemark.getText().toString();
            } else {
                PartReplaceRemark = null;
            }
            insertOutwardList(OutwardDateTime, OperatorName, GivenBy, PartReplaceRemark);
        } else {
            if (!IsValid(etGivenBy.getText().toString())) {
                etGivenBy.requestFocus();
                etGivenBy.setError("Enter Given By...!");
            }
        }
    }

    private void insertOutwardList(String outwardDateTime, String operatorName, String givenBy, String partReplaceRemark) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.llRootLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            if (!partInsertionList.isEmpty()) {
                partInsertionMainList.clear();
                for (int i = 0; i < partInsertionList.size(); i++) {
                    partInsertionMainList.add(new OutwardEntryRequest(UserID,
                            Company_ID,
                            partInsertionList.get(i).getPartID(),
                            Vehicle_Id,
                            outwardDateTime,
                            partInsertionList.get(i).getNewQuantity(),
                            givenBy,
                            operatorName,
                            partReplaceRemark));
                }
            }

            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            pg.setMessage("Please Wait...");
            pg.show();
            Call<OutwardEntryResponse> call = apiInterface.addOutwadEntry(partInsertionMainList);
            call.enqueue(new Callback<OutwardEntryResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<OutwardEntryResponse> call, Response<OutwardEntryResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("Outward Entry RESPONSE : ", response.body().getMessage());
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);

                                Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                clearList();
                                finish();
                            } else {
                                pg.dismiss();
                                Log.e("Outward Entry ERROR 1 : ", response.body().getMessage());
                            }
                        } else {
                            pg.dismiss();
                            Log.e("Outward Entry ERROR 2 : ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        pg.dismiss();
                        Log.e("Outward Entry Exception : ", e.getMessage());
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<OutwardEntryResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("Outward Entry Failure : ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Stock_OutwardEntryActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            addOutwardEntry();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });
        }
    }

    private void getVehicleInfoSpinner() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(Stock_OutwardEntryActivity.this, R.style.DialogBox);
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
                                        hideDialog();
                                    }
                                }, 1000);
                                busDatum = response.body().getBusData();
                                spVehicleAdapter = new VehicleSpinnerAdapter(Stock_OutwardEntryActivity.this, android.R.layout.simple_spinner_dropdown_item, busDatum);
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
                            new AlertDialog.Builder(Stock_OutwardEntryActivity.this, R.style.DialogBox);
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

    private boolean IsValid(String s) {
        return s != null && s.length() > 2 && s.length() <= 200;
    }

    private void hideDialog() {
        if (pg.isShowing()) {
            pg.dismiss();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.llRootLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

   /* private void getPartList() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(findViewById(R.id.llRootLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Getting Parts...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<PartListResponse> call = apiInterface.getPartList(new WholePartListRequest(Company_ID));
            call.enqueue(new Callback<PartListResponse>() {
                @Override
                public void onResponse(Call<PartListResponse> call, Response<PartListResponse> response) {
                    try {
                        pg.dismiss();
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.e("PART LIST RESPONSE : ", response.body().getMessage());
                                partsData = response.body().getPartsData();
                                rvParts.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                                partsAdapter = new PartsAdapter(getApplicationContext(), partsData);
                                partsAdapter.notifyDataSetChanged();
                                rvParts.setAdapter(partsAdapter);
                                rvParts.setNestedScrollingEnabled(true);
                                rvParts.setVerticalScrollBarEnabled(true);
                                partsAdapter.SetOnItemClickListener(new PartsAdapter.OnItemClickListener() {
                                    public ImageView ivPartMinus;
                                    public ImageView ivPartAdd;
                                    public TextView tvQuantity;

                                    @SuppressLint("LongLogTag")
                                    @Override
                                    public void onItemClick(View view, int pos) {

                                        PartsDatum part = partsData.get(pos);
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
                                                if (quantity < part.getQuantity()) {
                                                    quantity = quantity + 1;
                                                    part.setNewQuantity(quantity);
                                                    tvQuantity.setText(String.valueOf(part.getNewQuantity()));

                                                    if (partInsertionList.isEmpty()) {
                                                        partInsertionList.add(part);
                                                    } else {
                                                        if (partInsertionList.contains(part)) {
                                                            for (PartsDatum p : partInsertionList) {
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
//                                                    Toast.makeText(getApplicationContext(), "Pos" + pos, Toast.LENGTH_LONG).show();
                                                }
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
                public void onFailure(Call<PartListResponse> call, Throwable t) {
                    pg.dismiss();
                    Log.e("PART LIST FAILURE : ", t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(Stock_OutwardEntryActivity.this, R.style.DialogBox);
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
        Call<PartListResponse> call = apiInterface.getPartList(new WholePartListRequest(Company_ID));

        call.enqueue(new Callback<PartListResponse>() {
            @Override
            public void onResponse(Call<PartListResponse> call, Response<PartListResponse> response) {
                pg.dismiss();

                if (response.isSuccessful()) {
                    if (response.body().getStatusCode() == 1) {
                        Log.e("PART LIST RESPONSE : ", response.body().getMessage());
                        partsData = response.body().getPartsData();
                        rvParts.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
                        partsAdapter = new PartsAdapter(getApplicationContext(), partsData);
                        rvParts.setAdapter(partsAdapter);
                        rvParts.setNestedScrollingEnabled(true);
                        rvParts.setVerticalScrollBarEnabled(true);

                        partsAdapter.SetOnItemClickListener(new PartsAdapter.OnItemClickListener() {
                            public ImageView ivPartMinus;
                            public ImageView ivPartAdd;
                            public TextView tvQuantity;

                            @Override
                            public void onItemClick(View view, int pos) {
                                PartsDatum part = partsData.get(pos);
                                RecyclerView.ViewHolder holder = rvParts.findViewHolderForAdapterPosition(pos);

                                if (holder != null) {
                                    ivPartAdd = holder.itemView.findViewById(R.id.ivPartQuantityAdd);
                                    ivPartMinus = holder.itemView.findViewById(R.id.ivPartQuantityMinus);
                                    tvQuantity = holder.itemView.findViewById(R.id.tvPartQuantity);
                                }

                                int quantity = part.getNewQuantity() != null ? part.getNewQuantity() : 0;

                                if (view.getId() == R.id.ivPartQuantityAdd) {
                                    if (quantity < part.getQuantity()) {
                                        quantity += 1;
                                        part.setNewQuantity(quantity);
                                        tvQuantity.setText(String.valueOf(part.getNewQuantity()));

                                        if (partInsertionList.isEmpty()) {
                                            partInsertionList.add(part);
                                        } else {
                                            boolean found = false;
                                            for (PartsDatum p : partInsertionList) {
                                                if (part.getPartID() == p.getPartID()) {
                                                    p.setNewQuantity(quantity);
                                                    tvQuantity.setText(String.valueOf(quantity));
                                                    found = true;
                                                    break;
                                                }
                                            }
                                            if (!found) {
                                                partInsertionList.add(part);
                                            }
                                        }
                                        Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
                                    }
                                } else if (view.getId() == R.id.ivPartQuantityMinus) {
                                    if (!partInsertionList.isEmpty()) {
                                        boolean found = false;
                                        for (int i = 0; i < partInsertionList.size(); i++) {
                                            if (partInsertionList.get(i).getPartID() == part.getPartID()) {
                                                if (partInsertionList.get(i).getNewQuantity() == 1) {
                                                    quantity = 0;
                                                    part.setNewQuantity(quantity);
                                                    partInsertionList.remove(i);
                                                    tvQuantity.setText("0");
                                                } else if (quantity > 0) {
                                                    quantity -= 1;
                                                    part.setNewQuantity(quantity);
                                                    tvQuantity.setText(String.valueOf(quantity));
                                                    partInsertionList.get(i).setNewQuantity(quantity);
                                                }
                                                found = true;
                                                break;
                                            }
                                        }
                                        Log.e("POSITION_____" + pos + "______Part Insertion List Size : ", String.valueOf(partInsertionList.size()));
                                    }
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
            public void onFailure(Call<PartListResponse> call, Throwable t) {
                pg.dismiss();
                Log.e("PART LIST FAILURE : ", t.getMessage());
                AlertDialog.Builder builder = new AlertDialog.Builder(Stock_OutwardEntryActivity.this, R.style.DialogBox);
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


    /*@Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spVehileRegNo:
                Vehicle_Id = busDatum.get(i).getVehicleID();
                break;
        }
    }*/

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (adapterView.getId() == R.id.spVehileRegNo) {
            Vehicle_Id = busDatum.get(i).getVehicleID();
        }
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                        rvParts.smoothScrollToPosition(position + 2);
                        break;
                    }
                }
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

    @Override
    public void onResume() {
        super.onResume();
        if (searchView != null) {
            searchView.clearFocus();
            hideSoftKeyBoard();
            invalidateOptionsMenu();
            clearList();
            getPartList();
            getVehicleInfoSpinner();
        }
    }

    @Override
    public void onBackPressed() {
        clearList();
        super.onBackPressed();
    }

    public void clearList() {
        partInsertionList.clear();
        partInsertionMainList.clear();
        busDatum.clear();
    }
}
