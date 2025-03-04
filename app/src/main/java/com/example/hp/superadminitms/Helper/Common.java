package com.example.hp.superadminitms.Helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.example.hp.superadminitms.R;
import com.google.android.material.snackbar.Snackbar;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;


/**
 * Created by ANDROID-PC on 02/08/2018.
 */

public class Common {
    public static void showToastMessage(Context context, @StringRes int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
    }

    public static ProgressDialog showProgressDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context, R.style.DialogBox);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        return dialog;
    }

    public static void showSnack(View view, String msg) {

        Snackbar snackbar;
        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#83000000"));
        TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(view.getResources().getColor(R.color.colorAccent));
        snackbar.show();
    }

    public static void showSnackError(View view, String msg) {

        Snackbar snackbar;
        snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(Color.parseColor("#83000000"));
        TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(view.getResources().getColor(R.color.colorError));
        snackbar.show();
    }

    public static void setLog(String message) {
        Log.d("format", message);
    }


    // get Ip Address
    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // get Mac Address
    public static String getMacAddress() {
        try {

            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:", b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";

    }

    // get Battery Level


    public static int getBatteryLevel(Context c, View view) {
        float batterypct = 0;
        try {
            IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = c.getApplicationContext().registerReceiver(null, filter);
            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;
            batterypct = level / (float) scale;
        } catch (Exception e) {
            showSnackError(view, e.getMessage());
        }
        return (int) (batterypct * 100);
    }

    public static boolean isEmptyEditext(EditText editText, String error_message) {
        if (editText.getText().toString().trim().isEmpty()) {
            Snackbar snackbar;
            snackbar = Snackbar.make(editText, error_message, Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(Color.WHITE);
            TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
            return true;
        } else {
            editText.setError(null);
            return false;
        }
    }

    public static String getDate(int selectedYear, int selectedMonth, int selectedDay) {
        return String.format("%02d", selectedDay) + "-" + String.format("%02d", selectedMonth) + "-" + String.format("%04d", selectedYear);
    }

    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String convertDateFormat(String date_string, String dateformat, String returnDateformat) throws ParseException {
        //SimpleDateFormat dateformat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date initDate = new SimpleDateFormat(dateformat).parse(date_string);
        SimpleDateFormat formatter = new SimpleDateFormat(returnDateformat);
        String Parsedate = formatter.format(initDate);
        return Parsedate;
    }

    public static String getCurrentDate_Time() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    public static String getCurrentTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }
}
