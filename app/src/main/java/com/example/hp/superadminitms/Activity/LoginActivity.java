package com.example.hp.superadminitms.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.hp.superadminitms.Activity.Dashboards.Admin_SuperAdminDashboard;
import com.example.hp.superadminitms.Activity.Dashboards.OperatorDashboard;
import com.example.hp.superadminitms.Activity.Dashboards.Stock_Dashboard;
import com.example.hp.superadminitms.Activity.Dashboards.StoreDashboard;
import com.example.hp.superadminitms.Activity.Dashboards.SuperVisor_Dashboard;
import com.example.hp.superadminitms.Helper.Common;
import com.example.hp.superadminitms.Helper.SessionManager;
import com.example.hp.superadminitms.MyApplication;
import com.example.hp.superadminitms.Network.LoginRequest;
import com.example.hp.superadminitms.Network.LoginResponse;
import com.example.hp.superadminitms.R;
import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;
import com.example.hp.superadminitms.Retrofit.ApiClient;
import com.example.hp.superadminitms.Retrofit.ApiInterface;
import com.example.hp.superadminitms.Service.BaseActivity;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity implements ConnectivityReceiver.ConnectivityReceiverListener {

    private RelativeLayout loginlayout;
    private EditText etUname, etPassword;
    private TextView tvBtnSigIn;
    private ProgressDialog pg;
    private boolean isConnected;
    private String username, password;
    private int usertype, utype;
    private SessionManager sessionManger;
    private String manufacturer;
    private String Token;
    private final boolean notification_status = true;
    private String serialNo;
    private int battery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeFirebaseService();
        initializeControls();
        if (sessionManger.isLoggedIn()) {
            if (usertype == 1 || usertype == 4 || usertype == 5 || usertype == 2 || usertype == 6 || usertype == 7) {
                if (usertype == 1) {
                    startActivity(new Intent(LoginActivity.this, Admin_SuperAdminDashboard.class));
                    finish();
                } else if (usertype == 2) {
                    startActivity(new Intent(LoginActivity.this, Admin_SuperAdminDashboard.class));
                    finish();
                } else if (usertype == 4) {
                    startActivity(new Intent(LoginActivity.this, OperatorDashboard.class));
                    finish();
                } else if (usertype == 5) {
                    startActivity(new Intent(LoginActivity.this, StoreDashboard.class));
                    finish();
                } else if (usertype == 6) {
                    startActivity(new Intent(LoginActivity.this, Stock_Dashboard.class));
                    finish();
                } else if (usertype == 7) {
                    startActivity(new Intent(LoginActivity.this, SuperVisor_Dashboard.class));
                    finish();
                }
            }
        }
    }

    private void initializeControls() {

        loginlayout = findViewById(R.id.loginLayout);
        etUname = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassWord);
        tvBtnSigIn = findViewById(R.id.tvSignIn);

        tvBtnSigIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });

        pg = new ProgressDialog(LoginActivity.this, R.style.DialogBox);
        sessionManger = new SessionManager(LoginActivity.this);
        usertype = sessionManger.getUserType();
        battery = Common.getBatteryLevel(getApplicationContext(), this.findViewById(R.id.loginLayout));
        serialNo = Build.SERIAL.toUpperCase();
    }

    private void initializeFirebaseService() {
        manufacturer = Build.MANUFACTURER;
        if ("xiaomi".equalsIgnoreCase(manufacturer) || "oppo".equalsIgnoreCase(manufacturer) || "vivo".equalsIgnoreCase(manufacturer) || "huawei".equalsIgnoreCase(manufacturer) || "oneplus".equalsIgnoreCase(manufacturer)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.DialogBox);
            builder.setTitle("AutoStart").setMessage("The app requires the permission to Autostart please turn on the AutoStart");
            builder.setPositiveButton("TurnOn", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    Log.d("MYLOG", "CuurentToken:" + FirebaseInstanceId.getInstance().getToken());
                    //AutoStartPermissionHelper.getInstance().getAutoStartPermission(getApplicationContext());
                    try {
                        Intent intent = new Intent();
                        if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                            Log.d("MATCHCASE :: ", manufacturer);
                            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
                        } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                            Log.d("MATCHCASE :: ", manufacturer);
                            intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
                        } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                            intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                            Log.d("MATCHCASE :: ", manufacturer);
                        } else if ("huawei".equalsIgnoreCase(manufacturer)) {
                            Log.d("MATCHCASE :: ", manufacturer);
                            intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
                        } else if ("oneplus".equalsIgnoreCase(manufacturer)) {
                            Log.d("MATCHCASE :: ", manufacturer);
                            intent.setComponent(new ComponentName("com.oneplus.security", "com.oneplus.security.chainlaunch.view.ChainLaunchAppListAct‌​ivity"));
                        }
                        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        if (list.size() > 0) {
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
            });
            builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
//        FirebaseMessaging.getInstance().subscribeToTopic("DMS-ITMS")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = "Success";
//                        if (!task.isSuccessful()) {
//                            msg = "Failed";
//                        }
//                        Log.d("DebOnreate", msg);
//                    }
//                });
    }


    private void SignIn() {
        isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.loginLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            username = etUname.getText().toString();
            password = etPassword.getText().toString();
            if (isValidUserName(username) && isValidPass(password)) {
                checkLogin();
            } else {
                if (!isValidPass(password)) {
                    etPassword.requestFocus();
                    Common.showSnack(this.findViewById(R.id.loginLayout), "Invalid Password");
                }
                if (!isValidUserName(username)) {
                    etUname.requestFocus();
                    Common.showSnack(this.findViewById(R.id.loginLayout), "Invalid Username");
                }
            }
        }
    }

    private void checkLogin() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.loginLayout), getResources().getString(R.string.NetworkErrorMsg));
        } else {
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);

            pg.setMessage("Please Wait...");
            showDialog();

            Call<LoginResponse> call = apiInterface.checkLogin(new LoginRequest(username, password, serialNo, battery));

            call.enqueue(new Callback<LoginResponse>() {
                @SuppressLint("LongLogTag")
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    try {
                        if (response.isSuccessful()) {
                            if (response.body().getStatusCode() == 1) {

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    public void run() {
                                        hideDialog();
                                    }
                                }, 500);

                                Log.e("LOGIN RESPONSE: ", response.body().getMessage());
                                SetUserData(response.body().getUserData().get(0).getUserID(), response.body().getUserData().get(0).getUserCompanyID(), response.body().getUserData().get(0).getUserName(), response.body().getUserData().get(0).getUserType(), notification_status);
                                utype = response.body().getUserData().get(0).getUserType();
                                if (utype == 1 || utype == 4 || utype == 5 || utype == 2 || utype == 6 || utype == 7) {
                                    if (utype == 1) {
                                        startActivity(new Intent(LoginActivity.this, Admin_SuperAdminDashboard.class));
                                        finish();
                                    } else if (utype == 2) {
                                        startActivity(new Intent(LoginActivity.this, Admin_SuperAdminDashboard.class));
                                        finish();
                                    } else if (utype == 4) {
                                        startActivity(new Intent(LoginActivity.this, OperatorDashboard.class));
                                        finish();
                                    } else if (utype == 5) {
                                        startActivity(new Intent(LoginActivity.this, StoreDashboard.class));
                                        finish();
                                    } else if (utype == 6) {
                                        startActivity(new Intent(LoginActivity.this, Stock_Dashboard.class));
                                        finish();
                                    } else if (utype == 7) {
                                        startActivity(new Intent(LoginActivity.this, SuperVisor_Dashboard.class));
                                        finish();
                                    }
                                }

                            } else {
                                Log.e("LOGIN RESPONSE ERROR 1: ", response.body().getMessage());
                                if (pg.isShowing())
                                    hideDialog();
                                Common.showSnack(findViewById(R.id.loginLayout), response.body().getMessage());
                            }
                        } else {
                            if (pg.isShowing())
                                hideDialog();
                            Log.e("LOGIN RESPONSE ERROR 2: ", response.errorBody().string());
                            Common.showSnack(findViewById(R.id.loginLayout), response.errorBody().string());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        Log.e("LOGIN RESPONSE EXCEPTION : ", ex.getMessage());
                        if (pg.isShowing())
                            hideDialog();
                    }
                }

                @SuppressLint("LongLogTag")
                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    Log.e("LOGIN RESPONSE FAILURE : ", t.getMessage());
                    if (pg.isShowing())
                        hideDialog();

                    AlertDialog.Builder builder =
                            new AlertDialog.Builder(LoginActivity.this, R.style.DialogBox);
                    builder.setTitle(getResources().getString(R.string.NetworkErrorTitle));
                    builder.setMessage(getResources().getString(R.string.NetworkErrorMsg));
                    builder.setPositiveButton(getResources().getString(R.string.NetworkErrorBtnTxt), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            checkLogin();
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
        MyApplication.getInstance().setConnectivityListener(this);
    }

    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showView(isConnected);
    }

    private void showView(boolean isConnected) {
        if (!isConnected) {
            Common.showSnack(this.findViewById(R.id.loginLayout), getResources().getString(R.string.NetworkErrorMsg));
        }
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showView(isConnected);
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private boolean isValidPass(String pass) {
        return pass != null && pass.length() > 0;
    }

    private boolean isValidUserName(String user) {
        return user != null && user.length() > 0;
    }

    private void showDialog() {
        if (!pg.isShowing())
            pg.show();
    }

    private void hideDialog() {
        if (pg.isShowing())
            pg.dismiss();
    }

    private void SetUserData(int userID, int companyID, String username, int UserType, boolean notification_status) {
        sessionManger.setLogin(true);
        sessionManger.setKeyUserId(userID);
        sessionManger.setKeyCompanyId(companyID);
        sessionManger.setKeyUserName(username);
        sessionManger.setUserType(UserType);
        sessionManger.setKeyNotificationStatus(notification_status);
    }
}
