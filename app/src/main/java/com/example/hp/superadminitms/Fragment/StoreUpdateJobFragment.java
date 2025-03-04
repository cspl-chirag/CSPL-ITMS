package com.example.hp.superadminitms.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.widget.CompoundButtonCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Adapter.WholeListPartAdapter;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.JobDatum;
import com.example.hp.superadminitms.Model.WholeListPartDatum;
import com.example.hp.superadminitms.Network.InsertPartReplaceRequest;
import com.example.hp.superadminitms.Network.JobResponse;
import com.example.hp.superadminitms.Network.JobUpdateRequest;
import com.example.hp.superadminitms.Network.UpdateResponse;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreUpdateJobFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String EXTRA_MENU = "ab";
    private static final String BUNDLE_VEHICLE_REG_NO = "vehicle_reg_no";
    private static final String BUNDLE_PROBLEM = "problem";
    private static final String BUNDLE_JOB_ID = "job_id";
    private static final String BUNDLE_VEHICLE_ID = "vehicle_id";
    private static final String BUNDLE_ROUTE_NO = "route_no";
    private static final String BUNDLE_DRIVER_NAME = "driver_name";
    private static final String BUNDLE_BRAKES = "brakes";
    private static final String BUNDLE_CLUTCH = "clutch";
    private static final String BUNDLE_ACCELERATOR = "accelerator";
    private static final String BUNDLE_HEADLIGHT = "headlight";
    private static final String BUNDLE_SIDELIGHT = "sidelight";
    private static final String BUNDLE_STEERING = "steering";
    private static final String BUNDLE_OTHER_PROBLEM = "other_problem";
    private static final String BUNDLE_JOB_DATE = "problem_date";
    private static String vehicle_reg_No;
    private static String Problem;
    private static Integer Job_ID;
    private static String RouteNo;
    private static String driverName;
    private static String ProblemDate;
    private static boolean Breaks, Clutch, Steering, HeadLight, SideLight, OtherProblem, Accelerator = false;
    private static Integer Vehicle_ID;
    private final String TAG = this.getClass().getSimpleName() + " ***** ";
    TextView tvVehicle, tvRoute, tvDriver, tvJobDate, tvProblem, btnSubmit;
    EditText etSolvedDesc, etSolvedBy, etSolvedDate, etSolvedTime;
    private boolean isConnected;
    private ProgressDialog pg;
    private List<JobDatum> jobData;
    private int i, UserID;
    private CheckBox ch;
    private LinearLayout llCheckBox;
    private RecyclerView rvParts;
    private CheckBox cbIsPartReplacing;
    private LinearLayout llPartReplacementSection;
    private Date d;
    private String Selected_Date, Selected_Time, JobDate;
    private Spinner spDriver, spRouteNo, spVehicleRegNo;
    private SessionManager sessionManager;
    private int Company_ID;
    private final List<WholeListPartDatum> partsData = new ArrayList<>();
    private final List<WholeListPartDatum> partInsertionList = new ArrayList<>();
    private WholeListPartAdapter partsAdapter;
    private SearchView searchView;
    private final Integer quantity = 0;
    //    private EditText etOutwardDate, etOutwardtime, etPersonName, etCheckedBy, etPartReplacedDesc, etPartReplaceRemark;
    private TextView btnSubmitParts;
    private String OutwardDateTime;
    private final ArrayList<InsertPartReplaceRequest> partInsertionMainList = new ArrayList<InsertPartReplaceRequest>();
    private String UserName;
    private View view;

    public StoreUpdateJobFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_store_update_job_fragement, container, false);
        initializeControls(view);
        gettingJobsCheckbox();
        return view;
    }

    private void initializeControls(View view) {
        pg = new ProgressDialog(getContext());
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        sessionManager = new SessionManager(getContext());
        btnSubmitParts = view.findViewById(R.id.btnSubmitParts);
        Company_ID = sessionManager.getKeyCompanyId();
        UserID = sessionManager.getKeyUserId();
        UserName = sessionManager.getKeyUserName();
        llPartReplacementSection = view.findViewById(R.id.llPartReplacementSection);
        rvParts = view.findViewById(R.id.rvParts);
        cbIsPartReplacing = view.findViewById(R.id.cbIsPartReplacing);
        llCheckBox = view.findViewById(R.id.llCheckBox);
        tvDriver = view.findViewById(R.id.tvDriver);
        tvJobDate = view.findViewById(R.id.tvJobDate);
        tvProblem = view.findViewById(R.id.tvProblem);
        tvRoute = view.findViewById(R.id.tvRouteNo);
        tvVehicle = view.findViewById(R.id.tvBusRegNo);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsValid(etSolvedBy.getText().toString()) && IsValid(etSolvedDesc.getText().toString())) {
                    updateJob();
                } else {
                    if (!IsValid(etSolvedBy.getText().toString())) {
                        etSolvedBy.requestFocus();
                        etSolvedBy.setError("Enter SolvedBy...!");
                    }
                    if (!IsValid(etSolvedDesc.getText().toString())) {
                        etSolvedDesc.requestFocus();
                        etSolvedDesc.setError("Enter Solved Description...!");
                    }
                }
            }
        });
        etSolvedDesc = view.findViewById(R.id.etSolvedDesc);
        etSolvedBy = view.findViewById(R.id.etSolvedBy);
        etSolvedDate = view.findViewById(R.id.etSelectedDate);
        etSolvedTime = view.findViewById(R.id.etSelectedTime);
        if (etSolvedDate.getText().toString().isEmpty() && etSolvedTime.getText().toString().isEmpty()) {
            Selected_Date = Common.getCurrentDate();
            Selected_Time = Common.getCurrentTime();
            etSolvedTime.setText(Selected_Time);
            etSolvedDate.setText(Selected_Date);
            try {
                JobDate = Common.convertDateFormat(Selected_Date, "dd-mm-yyyy", "yyyy-mm-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        etSolvedDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR);
                    int mMonth = c.get(Calendar.MONTH);
                    int mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicke, int year,
                                              int monthOfYear, int dayOfMonth) {
                            d = new Date();
                            Selected_Date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                            etSolvedDate.setText(Selected_Date);
                        }
                    }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                    etSolvedDate.clearFocus();
                }
            }
        });
        etSolvedTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    final Calendar c = Calendar.getInstance();
                    int mHour = c.get(Calendar.HOUR_OF_DAY);
                    int mMinute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                            d = new Date();
                            Selected_Time = hourOfDay + ":" + minute;
                            etSolvedTime.setText(Selected_Time);
                        }
                    }, mHour, mMinute, false);
                    timePickerDialog.show();
                    etSolvedTime.clearFocus();
                }
            }
        });
        Bundle b = this.getArguments();
        vehicle_reg_No = b.getString(BUNDLE_VEHICLE_REG_NO);
        Problem = b.getString(BUNDLE_PROBLEM);
        Job_ID = b.getInt(BUNDLE_JOB_ID);
        Vehicle_ID = b.getInt(BUNDLE_VEHICLE_ID);
        RouteNo = b.getString(BUNDLE_ROUTE_NO);
        driverName = b.getString(BUNDLE_DRIVER_NAME);
        Breaks = b.getBoolean(BUNDLE_BRAKES);
        Clutch = b.getBoolean(BUNDLE_CLUTCH);
        Accelerator = b.getBoolean(BUNDLE_ACCELERATOR);
        HeadLight = b.getBoolean(BUNDLE_HEADLIGHT);
        SideLight = b.getBoolean(BUNDLE_SIDELIGHT);
        Steering = b.getBoolean(BUNDLE_STEERING);
        OtherProblem = b.getBoolean(BUNDLE_OTHER_PROBLEM);
        ProblemDate = b.getString(BUNDLE_JOB_DATE);

        tvVehicle.setText(vehicle_reg_No);
        tvRoute.setText(RouteNo);
        tvProblem.setText(Problem);
        tvJobDate.setText(ProblemDate);
        tvDriver.setText(driverName);
    }

    private void gettingJobsCheckbox() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getContext(), R.style.DialogBox);
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
                @SuppressLint("ResourceType")
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
                                Log.d(TAG, String.valueOf(jobData.size()));
                                GridLayout gridLayout = new GridLayout(getContext());
                                gridLayout.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
                                gridLayout.setColumnCount(2);
                                gridLayout.setRowCount(GridLayout.UNDEFINED);
                                Log.d("******", String.valueOf(Breaks) + Clutch + Accelerator + HeadLight + SideLight + Steering + OtherProblem);
                                for (i = 0; i < jobData.size(); i++) {
                                    ch = new CheckBox(getContext());
                                    //ch.setEnabled(false);
                                    ch.setId(jobData.get(i).getJobID());
                                    ch.setText(jobData.get(i).getJobName());
                                    ch.setEnabled(false);
                                    ch.setTextColor(getResources().getColor(R.color.colorAccent));

                                    int[][] states = {{android.R.attr.state_checked}, {}};
                                    int[] colors = {getResources().getColor(R.color.colorAccentText), getResources().getColor(R.color.colorAccent)};
                                    CompoundButtonCompat.setButtonTintList(ch, new ColorStateList(states, colors));
                                    gridLayout.addView(ch);
                                    llCheckBox.removeAllViews();
                                    llCheckBox.addView(gridLayout);
                                }
                                ((CheckBox) view.findViewById(1)).setChecked(Breaks);
                                ((CheckBox) view.findViewById(2)).setChecked(Clutch);
                                ((CheckBox) view.findViewById(3)).setChecked(Steering);
                                ((CheckBox) view.findViewById(4)).setChecked(HeadLight);
                                ((CheckBox) view.findViewById(5)).setChecked(SideLight);
                                ((CheckBox) view.findViewById(6)).setChecked(Accelerator);
                                ((CheckBox) view.findViewById(7)).setChecked(OtherProblem);
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
                            new AlertDialog.Builder(getContext(), R.style.DialogBox);
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

    private boolean IsValid(String s) {
        return s != null && s.length() > 2 && s.length() <= 200;
    }

    private void updateJob() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(view.findViewById(R.id.llRootLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            pg.setMessage("Update Job...");
            pg.show();
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<UpdateResponse> call = apiInterface.updateJob(new JobUpdateRequest(Job_ID, UserID, etSolvedBy.getText().toString(), Selected_Time, etSolvedDesc.getText().toString()));
            call.enqueue(new Callback<UpdateResponse>() {
                @Override
                public void onResponse(Call<UpdateResponse> call, Response<UpdateResponse> response) {
                    pg.dismiss();
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Toast.makeText(getContext(), "Job Updated...", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(StoreUpdateJobActivity.this,StoreJobListActivity.class));
//                                finish();

                                getActivity().onBackPressed();
                            } else {
                                if (pg.isShowing()) {
                                    hideDialog();
                                }
                                Log.d(TAG, "Error1 : " + response.body().getMessage());
                            }
                        } else {
                            if (pg.isShowing()) {
                                hideDialog();
                            }
                            Log.d(TAG, "Error2 : " + response.body().getMessage());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing()) {
                            hideDialog();
                        }
                        e.printStackTrace();
                        Log.d(TAG, " Exception : " + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UpdateResponse> call, Throwable t) {
                    if (pg.isShowing())
                        hideDialog();
                    Log.d(TAG, "Job Update Failure : " + t.getMessage());
                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(getContext(), R.style.DialogBox);
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

    private void hideDialog() {
        if (pg.isShowing()) {
            pg.dismiss();
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(view.findViewById(R.id.llRootLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }


}
