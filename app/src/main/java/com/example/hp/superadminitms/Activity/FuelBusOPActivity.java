package com.example.hp.superadminitms.Activity;

import static com.example.hp.superadminitms.Helper.Common.convertDateFormat;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.Adapter.FuelBusOPAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.FuelBusDatum;
import com.example.hp.superadminitms.MyApplication;
import com.example.hp.superadminitms.Network.GetFuelBusDataRequest;
import com.example.hp.superadminitms.Network.GetFuelBusDataResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FuelBusOPActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener, View.OnClickListener {

    private RecyclerView rvFuelBusList;
    private ArrayList<FuelBusDatum> fuelBusList;
    private FuelBusOPAdapter fuelBusDataAdapter;
    private SessionManager sessionManager;
    private int companyID;
    private ProgressDialog pDialog;
    private String fueldate;
    private LinearLayout llEmpty;
    private List<FuelBusDatum> fuelBusData;
    private TextView fuelDate;
    DatePickerDialog.OnDateSetListener onFuelDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int d, int m, int y) {
            int month = m + 1;
            fuelDate.setText(Common.getDate(d, month, y));
        }
    };
    private TextView submit;
    private Calendar cal;
    private boolean FirstLoad = true;
    private Double totalQuantity = 0.0;
    private TextView tvTotalQuantity;
    private TextView tvTotalAmount;
    private TextView tvFuelRate;
    private Double totalAmount = 0.0;
    private LinearLayout summaryLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fuel_bus);
        initializeToolBar();
        initViews();
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
        getSupportActionBar().setTitle("Fuelling Bus List");
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "fonts/LatoRegular.ttf");
                textView.setTypeface(myCustomFont);
            }
        }
    }

    private void initViews() {
        sessionManager = new SessionManager(this);
        companyID = sessionManager.getKeyCompanyId();

        // Progress dialog
        pDialog = new ProgressDialog(this, R.style.DialogBox);
        pDialog.setCancelable(false);

        fuelDate = findViewById(R.id.fuelDate);
        submit = findViewById(R.id.submit);
        tvTotalQuantity = findViewById(R.id.totalQuantity);
        tvTotalAmount = findViewById(R.id.totalAmount);
        tvFuelRate = findViewById(R.id.fuelRate);
        summaryLayout = findViewById(R.id.summaryLayout);

        fuelDate.setOnClickListener(this);
        submit.setOnClickListener(this);

        rvFuelBusList = findViewById(R.id.rvFuelBusList);
        llEmpty = findViewById(R.id.llEmpty);
        rvFuelBusList.setLayoutManager(new LinearLayoutManager(FuelBusOPActivity.this));

        if (fuelDate.getText().toString().isEmpty()) {
            fuelDate.setText(Common.getCurrentDate());
        }
    }

    /*@Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fuelDate:
                cal = Calendar.getInstance(Locale.getDefault());
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        this,onFuelDateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)

                );
                datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
                datePickerDialog.show();
                break;
            case R.id.submit:
                try {
                    fueldate = convertDateFormat(fuelDate.getText().toString(),"dd-MM-yyyy","yyyy-MM-dd");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                getFuelBusData();
                break;
//            case R.id.btnClear:
//                txtStartDate.setText(String.valueOf(Common.getCurrentDate()));
//                clearUI();
//                break;
            default:
        }
    }*/

    @Override
    public void onClick(View view) {
        int viewId = view.getId();

        if (viewId == R.id.fuelDate) {
            cal = Calendar.getInstance(Locale.getDefault());
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this, onFuelDateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.getDatePicker().setMaxDate(cal.getTimeInMillis());
            datePickerDialog.show();
        } else if (viewId == R.id.submit) {
            try {
                fueldate = convertDateFormat(fuelDate.getText().toString(), "dd-MM-yyyy", "yyyy-MM-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            getFuelBusData();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void getFuelBusData() {

        boolean isConnected = ConnectivityReceiver.isConnected();

        if (!isConnected) {
            Common.showSnack(findViewById(R.id.fuelBusLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pDialog.setMessage("Getting Fuel Data...");
            showDialog();

            Call<GetFuelBusDataResponse> call = apiInterface.getFuelBusData(new GetFuelBusDataRequest(companyID, fueldate));

            call.enqueue(new Callback<GetFuelBusDataResponse>() {
                @Override
                public void onResponse(Call<GetFuelBusDataResponse> call, Response<GetFuelBusDataResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 1000);
                                FirstLoad = false;
                                llEmpty.setVisibility(View.GONE);
                                rvFuelBusList.setVisibility(View.VISIBLE);

                                fuelBusData = response.body().getFuelBusData();
                                //Collections.reverse(ignitionReportData);
                                fuelBusDataAdapter = new FuelBusOPAdapter(fuelBusData, FuelBusOPActivity.this);

                                rvFuelBusList.setAdapter(fuelBusDataAdapter);
                                fuelBusDataAdapter.notifyDataSetChanged();
                                totalQuantity = 0.0;
                                totalAmount = 0.0;
                                for (int i = 0; i < fuelBusData.size(); i++) {
                                    totalQuantity += fuelBusData.get(i).getQuantity();
                                    totalAmount += fuelBusData.get(i).getAmount();
                                }
                                summaryLayout.setVisibility(View.VISIBLE);
                                tvFuelRate.setText(String.valueOf(fuelBusData.get(0).getRate()));
                                tvTotalQuantity.setText(String.valueOf(totalQuantity));
                                tvTotalAmount.setText(String.valueOf(totalAmount));


                                fuelBusDataAdapter.SetOnItemClickListener(new FuelBusOPAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        Log.d("", fuelBusData.get(position).getVehicleName());
//                                        FuelBusDatum fuelBusDatum = fuelBusData.get(position);
//                                        Intent intent = new Intent(FuelBusOPActivity.this,UpdateFuelOPActivity.class);
//                                        intent.putExtra("FuelBusData",fuelBusDatum);
//                                        startActivity(intent);
                                    }
                                });

                            } else {
                                if (pDialog.isShowing())
                                    hideDialog();
                                rvFuelBusList.setVisibility(View.GONE);
                                llEmpty.setVisibility(View.VISIBLE);
                                summaryLayout.setVisibility(View.GONE);
                            }
                        } else {
                            if (pDialog.isShowing())
                                hideDialog();
                            rvFuelBusList.setVisibility(View.GONE);
                            llEmpty.setVisibility(View.VISIBLE);
                            summaryLayout.setVisibility(View.GONE);
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        if (pDialog.isShowing())
                            hideDialog();
                    }
                }

                @Override
                public void onFailure(Call<GetFuelBusDataResponse> call, Throwable t) {
                    if (pDialog.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(FuelBusOPActivity.this);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            getFuelBusData();
                        }
                    });
                    builder.setCancelable(false);
                    builder.show();
                }
            });

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!FirstLoad) {
            getFuelBusData();
        }
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showView(isConnected);
    }

    private void showView(boolean isConnected) {
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.fuelBusLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showView(isConnected);
    }

}
