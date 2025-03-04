package com.example.hp.superadminitms.Fragment;


import static com.example.hp.superadminitms.Helper.Common.convertDateFormat;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.Model.RoutineDataToday;
import com.example.hp.superadminitms.Model.RoutineDataYesterday;
import com.example.hp.superadminitms.Model.RoutineRequest;
import com.example.hp.superadminitms.Model.RoutineTodayResponse;
import com.example.hp.superadminitms.Model.RoutineYesterdayResponse;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class BusRoutineSAFragment extends Fragment implements ConnectivityReceiver.ConnectivityReceiverListener {

    private TextView tvTodayFirstBusOnRoad, tvTodayFirstBusOnRoadBusNo, tvTodayFirstBusOnRoadRouteNo;
    private TextView tvTodayLastBusOnRoad, tvTodayLastBusOnRoadBusNo, tvTodayLastBusOnRoadRouteNo;
    private TextView tvTodayFirstScheduleOver, tvTodayFirstScheduleOverBusNo, tvTodayFirstScheduleOverRouteNo;
    private TextView tvTodayLastScheduleOver, tvTodayLastScheduleOverBusNo, tvTodayLastScheduleOverRouteNo;

    private TextView tvYesterdayFirstBusOnRoad, tvYesterdayFirstBusOnRoadBusNo, tvYesterdayFirstBusOnRoadRouteNo;
    private TextView tvYesterdayLastBusOnRoad, tvYesterdayLastBusOnRoadBusNo, tvYesterdayLastBusOnRoadRouteNo;
    private TextView tvYesterdayFirstScheduleOver, tvYesterdayFirstScheduleOverBusNo, tvYesterdayFirstScheduleOverRouteNo;
    private TextView tvYesterdayLastScheduleOver, tvYesterdayLastScheduleOverBusNo, tvYesterdayLastScheduleOverRouteNo;

    private ProgressDialog pg;
    private SessionManager sessionManager;
    private boolean isConnected;
    private CountDownTimer mTimer1, mTimer2;
    private Boolean IsFirstTime = true;
    private List<RoutineDataToday> todayRoutineLogin = new ArrayList<>();
    private List<RoutineDataToday> todayRoutineLogout = new ArrayList<>();
    private List<RoutineDataYesterday> yesterdaysRoutieLogin = new ArrayList<>();
    private List<RoutineDataYesterday> yesterdaysRoutieLogout = new ArrayList<>();
    private int Flag_ID;
    private int Bus_Company_ID;
    private View view;

    public BusRoutineSAFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sa_bus_routine, container, false);

//        Toolbar toolbar = (Toolbar) view.view.findViewById(R.id.toolbar);
//        view.setSupportActionBar(toolbar);
//
//        getSupportActionBar().setTitle(R.string.app_name);
//        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_white_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        initializeControls(view);
        return view;

    }

    private void initializeControls(View view) {
        Bus_Company_ID = getArguments().getInt("COMPANY_ID", 0);
        pg = new ProgressDialog(getContext(), R.style.DialogBox);
        pg.setIndeterminate(false);
        pg.setCancelable(false);
        //Bus_Company_ID = getArguments().getInt("ID",0);
        sessionManager = new SessionManager(getContext());


        tvTodayFirstBusOnRoad = view.findViewById(R.id.tvTodayFirstLogin);
        tvTodayFirstBusOnRoadBusNo = view.findViewById(R.id.tvTodayFirstLoginBusNo);
        tvTodayFirstBusOnRoadRouteNo = view.findViewById(R.id.tvTodayFirstLoginRoadNo);
        tvTodayFirstScheduleOver = view.findViewById(R.id.tvTodayFirstLogout);
        tvTodayFirstScheduleOverBusNo = view.findViewById(R.id.tvTodayFirstLogoutBusNo);
        tvTodayFirstScheduleOverRouteNo = view.findViewById(R.id.tvTodayFirstLogoutRoadNo);

        tvTodayLastBusOnRoad = view.findViewById(R.id.tvTodayLastLogin);
        tvTodayLastBusOnRoadBusNo = view.findViewById(R.id.tvTodayLastLoginBusNo);
        tvTodayLastBusOnRoadRouteNo = view.findViewById(R.id.tvTodayLastLoginRoadNo);
        tvTodayLastScheduleOver = view.findViewById(R.id.tvTodayLastLogout);
        tvTodayLastScheduleOverBusNo = view.findViewById(R.id.tvTodayLastLogoutBusNo);
        tvTodayLastScheduleOverRouteNo = view.findViewById(R.id.tvTodayLastLogoutRoadNo);


        tvYesterdayFirstBusOnRoad = view.findViewById(R.id.tvYesterdayFirstLogin);
        tvYesterdayFirstBusOnRoadBusNo = view.findViewById(R.id.tvYesterdayFirstLoginBusNo);
        tvYesterdayFirstBusOnRoadRouteNo = view.findViewById(R.id.tvYesterdayFirstLoginRoadNo);
        tvYesterdayFirstScheduleOver = view.findViewById(R.id.tvYesterdayFirstLogout);
        tvYesterdayFirstScheduleOverBusNo = view.findViewById(R.id.tvYesterdayFirstLogoutBusNo);
        tvYesterdayFirstScheduleOverRouteNo = view.findViewById(R.id.tvYesterdayFirstLogoutRoadNo);

        tvYesterdayLastBusOnRoad = view.findViewById(R.id.tvYesterdayLastLogin);
        tvYesterdayLastBusOnRoadBusNo = view.findViewById(R.id.tvYesterdayLastLoginBusNo);
        tvYesterdayLastBusOnRoadRouteNo = view.findViewById(R.id.tvYesterdayLastLoginRoadNo);
        tvYesterdayLastScheduleOver = view.findViewById(R.id.tvYesterdayLastLogout);
        tvYesterdayLastScheduleOverBusNo = view.findViewById(R.id.tvYesterdayLastLogoutBusNo);
        tvYesterdayLastScheduleOverRouteNo = view.findViewById(R.id.tvYesterdayLastLogoutRoadNo);
        settingDashboardTodayLogin();
        settingDashboardTodayLogout();
        settingDashboardYesterdayLogin();
        settingDashboardYesterdayLogout();
        mTimer1 = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long l) {
                //settingDashboardTodayFirstLogin();
                //settingDashboardTodayLastLogin();
            }

            @Override
            public void onFinish() {
                mTimer1.start();
            }
        }.start();
    }

    private void settingDashboardTodayLogin() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            if (IsFirstTime) {
                pg.setMessage("Getting Dashboard Data...");
                pg.show();
                IsFirstTime = false;
            }
            Flag_ID = 1;
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<RoutineTodayResponse> call = apiInterface.getTodayBusRoutine(new RoutineRequest(Flag_ID, Bus_Company_ID));
            call.enqueue(new Callback<RoutineTodayResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<RoutineTodayResponse> call, Response<RoutineTodayResponse> response) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                Log.d("TodayLoginResponse :; ", response.body().getMessage());
                                todayRoutineLogin = response.body().getTodayRoutineData();
                                tvTodayFirstBusOnRoad.setText(convertDateFormat(todayRoutineLogin.get(0).getTodayFirstBusLogin(), "HH:mm:ss", "hh:mm a"));
                                tvTodayFirstBusOnRoadBusNo.setText(String.valueOf(todayRoutineLogin.get(0).getTodayFirstBusLoginVehicle()));
                                tvTodayFirstBusOnRoadRouteNo.setText(String.valueOf(todayRoutineLogin.get(0).getTodayFirstBusLoginRoute()));
                                tvTodayLastBusOnRoad.setText(convertDateFormat(todayRoutineLogin.get(0).getTodayLastBusLogin(), "HH:mm:ss", "hh:mm a"));
                                tvTodayLastBusOnRoadBusNo.setText(String.valueOf(todayRoutineLogin.get(0).getTodayLastBusLoginVehicle()));
                                tvTodayLastBusOnRoadRouteNo.setText(String.valueOf(todayRoutineLogin.get(0).getTodayLastBusLoginRoute()));
                                settingDashboardTodayLogout();
                            } else {
                                if (pg.isShowing()) {
                                    pg.dismiss();
                                }
                                tvTodayFirstBusOnRoad.setText("N/A");
                                tvTodayFirstBusOnRoadBusNo.setText("N/A");
                                tvTodayFirstBusOnRoadRouteNo.setText("N/A");
                                tvTodayLastBusOnRoad.setText("N/A");
                                tvTodayLastBusOnRoadBusNo.setText("N/A");
                                tvTodayLastBusOnRoadRouteNo.setText("N/A");
                                Log.d("TodayLoginResponseError1 : ", response.errorBody().string());

                            }
                        } else {
                            if (pg.isShowing()) {
                                pg.dismiss();
                            }
                            Log.d("TodayLoginResponseError2 : ", response.errorBody().string());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing()) {
                            pg.dismiss();
                        }
                        Log.d("TodayLoginResponseException : ", e.getMessage());
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<RoutineTodayResponse> call, Throwable t) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    if (IsFirstTime) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogBox);
                        builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                        builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                        builder.setPositiveButton(R.string.NetworkErrorBtnTxt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                settingDashboardTodayLogin();
                                builder.show();
                            }
                        });

                        IsFirstTime = false;
                    }
                    Log.d("TodayLoginResponseFailure : ", t.getMessage());
                }
            });
        }

    }

    private void settingDashboardTodayLogout() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            if (IsFirstTime) {
                pg.setMessage("Getting Dashboard Data...");
                pg.show();
                IsFirstTime = false;
            }
            Flag_ID = 2;
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<RoutineTodayResponse> call = apiInterface.getTodayBusRoutine(new RoutineRequest(Flag_ID, Bus_Company_ID));
            call.enqueue(new Callback<RoutineTodayResponse>() {
                @Override
                public void onResponse(Call<RoutineTodayResponse> call, Response<RoutineTodayResponse> response) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                todayRoutineLogout = response.body().getTodayRoutineData();
                                tvTodayFirstScheduleOver.setText(convertDateFormat(todayRoutineLogout.get(0).getTodayFirstBusLogout(), "HH:mm:ss", "hh:mm a"));
                                tvTodayFirstScheduleOverBusNo.setText(String.valueOf(todayRoutineLogout.get(0).getTodayFirstBusLogoutVehicle()));
                                tvTodayFirstScheduleOverRouteNo.setText(String.valueOf(todayRoutineLogout.get(0).getTodayFirstBusLogoutRoute()));
                                tvTodayLastScheduleOver.setText(convertDateFormat(todayRoutineLogout.get(0).getTodayLastBusLogout(), "HH:mm:ss", "hh:mm a"));
                                tvTodayLastScheduleOverBusNo.setText(String.valueOf(todayRoutineLogout.get(0).getTodayLastBusLogoutVehicle()));
                                tvTodayLastScheduleOverRouteNo.setText(String.valueOf(todayRoutineLogout.get(0).getTodayLastBusLogoutRoute()));

                            } else {
                                if (pg.isShowing()) {
                                    pg.dismiss();
                                }
                                Log.d("TodayError :; ", response.body().getMessage());
                                tvTodayFirstScheduleOver.setText("N/A");
                                tvTodayFirstScheduleOverBusNo.setText("N/A");
                                tvTodayFirstScheduleOverRouteNo.setText("N/A");
                                tvTodayLastScheduleOver.setText("N/A");
                                tvTodayLastScheduleOverBusNo.setText("N/A");
                                tvTodayLastScheduleOverRouteNo.setText("N/A");
                            }
                        } else {
                            if (pg.isShowing()) {
                                pg.dismiss();
                            }
                            Log.d("TodayError :; ", response.body().getMessage());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing()) {
                            pg.dismiss();
                        }
                        Log.d("Routine Exception : ", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<RoutineTodayResponse> call, Throwable t) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    if (IsFirstTime) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogBox);
                        builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                        builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                        builder.setPositiveButton(R.string.NetworkErrorBtnTxt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                settingDashboardTodayLogout();
                                builder.show();
                            }
                        });

                        IsFirstTime = false;
                    }
                    Log.d("Routine Failure : ", t.getMessage());
                }
            });
        }
    }

    private void settingDashboardYesterdayLogin() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            if (IsFirstTime) {
                pg.setMessage("Getting Dashboard Data...");
                pg.show();
                IsFirstTime = false;
            }
            Flag_ID = 1;
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<RoutineYesterdayResponse> call = apiInterface.getYesterdayBusRoutine(new RoutineRequest(Flag_ID, Bus_Company_ID));
            call.enqueue(new Callback<RoutineYesterdayResponse>() {
                @Override
                public void onResponse(Call<RoutineYesterdayResponse> call, Response<RoutineYesterdayResponse> response) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                yesterdaysRoutieLogin = response.body().getYesterdayRoutineData();
                                Log.d("YestrdayResponse : ", response.body().getMessage());
                                tvYesterdayFirstBusOnRoad.setText(convertDateFormat(yesterdaysRoutieLogin.get(0).getYesterdayFirstBusLogin(), "HH:mm:ss", "hh:mm a"));
                                tvYesterdayFirstBusOnRoadBusNo.setText(String.valueOf(yesterdaysRoutieLogin.get(0).getYesterdayFirstBusLoginVehicle()));
                                tvYesterdayFirstBusOnRoadRouteNo.setText(String.valueOf(yesterdaysRoutieLogin.get(0).getYesterdayFirstBusLoginRoute()));
                                tvYesterdayLastBusOnRoad.setText(convertDateFormat(yesterdaysRoutieLogin.get(0).getYesterdayLastBusLogin(), "HH:mm:ss", "hh:mm a"));
                                tvYesterdayLastBusOnRoadBusNo.setText(String.valueOf(yesterdaysRoutieLogin.get(0).getYesterdayLastBusLoginVehicle()));
                                tvYesterdayLastBusOnRoadRouteNo.setText(String.valueOf(yesterdaysRoutieLogin.get(0).getYesterdayLastBusLoginRoute()));
                            } else {
                                if (pg.isShowing()) {
                                    pg.dismiss();
                                }
                                Log.d("YestrdayNoFound : ", response.body().getMessage());
                                tvYesterdayFirstBusOnRoad.setText("N/A");
                                tvYesterdayFirstBusOnRoadBusNo.setText("N/A");
                                tvYesterdayFirstBusOnRoadRouteNo.setText("N/A");
                                tvYesterdayLastBusOnRoad.setText("N/A");
                                tvYesterdayLastBusOnRoadBusNo.setText("N/A");
                                tvYesterdayLastBusOnRoadRouteNo.setText("N/A");
                            }
                        } else {
                            if (pg.isShowing()) {
                                pg.dismiss();
                            }
                            Log.d("YestrdayError : ", response.body().getMessage());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing()) {
                            pg.dismiss();
                        }
                        Log.d("YesterdayExcptn : ", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<RoutineYesterdayResponse> call, Throwable t) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    if (IsFirstTime) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogBox);
                        builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                        builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                        builder.setPositiveButton(R.string.NetworkErrorBtnTxt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                settingDashboardYesterdayLogin();
                                builder.show();
                            }
                        });

                        IsFirstTime = false;
                    }
                    Log.d("Routine Failure : ", t.getMessage());
                }
            });
        }
    }

    private void settingDashboardYesterdayLogout() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            if (IsFirstTime) {
                pg.setMessage("Getting Dashboard Data...");
                pg.show();
                IsFirstTime = false;
            }
            Flag_ID = 2;
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            Call<RoutineYesterdayResponse> call = apiInterface.getYesterdayBusRoutine(new RoutineRequest(Flag_ID, Bus_Company_ID));
            call.enqueue(new Callback<RoutineYesterdayResponse>() {
                @Override
                public void onResponse(Call<RoutineYesterdayResponse> call, Response<RoutineYesterdayResponse> response) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {
                                yesterdaysRoutieLogout = response.body().getYesterdayRoutineData();
                                Log.d("YesterdayResponse:", response.body().getMessage());
                                tvYesterdayFirstScheduleOver.setText(convertDateFormat(yesterdaysRoutieLogout.get(0).getYesterdayFirstBusLogout(), "HH:mm:ss", "hh:mm a"));
                                tvYesterdayFirstScheduleOverBusNo.setText(String.valueOf(yesterdaysRoutieLogout.get(0).getYesterdayFirstBusLogoutVehicle()));
                                tvYesterdayFirstScheduleOverRouteNo.setText(String.valueOf(yesterdaysRoutieLogout.get(0).getYesterdayFirstBusLogoutRoute()));
                                tvYesterdayLastScheduleOver.setText(convertDateFormat(yesterdaysRoutieLogout.get(0).getYesterdayLastBusLogout(), "HH:mm:ss", "hh:mm a"));
                                tvYesterdayLastScheduleOverBusNo.setText(String.valueOf(yesterdaysRoutieLogout.get(0).getYesterdayLastBusLogoutVehicle()));
                                tvYesterdayLastScheduleOverRouteNo.setText(String.valueOf(yesterdaysRoutieLogout.get(0).getYesterdayLastBusLogoutRoute()));
                            } else {
                                if (pg.isShowing()) {
                                    pg.dismiss();
                                }
                                Log.d("YesterdayNoFound : ", response.body().getMessage());
                                tvYesterdayFirstScheduleOver.setText("N/A");
                                tvYesterdayFirstScheduleOverBusNo.setText("N/A");
                                tvYesterdayFirstScheduleOverRouteNo.setText("N/A");
                                tvYesterdayLastScheduleOver.setText("N/A");
                                tvYesterdayLastScheduleOverBusNo.setText("N/A");
                                tvYesterdayLastScheduleOverRouteNo.setText("N/A");
                            }
                        } else {
                            if (pg.isShowing()) {
                                pg.dismiss();
                            }
                            Log.d("YesterdayError:", response.body().getMessage());
                        }
                    } catch (Exception e) {
                        if (pg.isShowing()) {
                            pg.dismiss();
                        }
                        Log.d("YesterExcptn : ", e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<RoutineYesterdayResponse> call, Throwable t) {
                    if (pg.isShowing()) {
                        pg.dismiss();
                    }
                    if (IsFirstTime) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogBox);
                        builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                        builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                        builder.setPositiveButton(R.string.NetworkErrorBtnTxt, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                settingDashboardYesterdayLogin();
                                builder.show();
                            }
                        });

                        IsFirstTime = false;
                    }
                    Log.d("Routine Failure : ", t.getMessage());
                }
            });
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(this.view.findViewById(R.id.rootlayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //MyApplication.getInstance().setConnectivityListener(getContext());
        mTimer1.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mTimer1.cancel();
    }


}
