package com.example.hp.superadminitms;

import android.app.Application;

import com.example.hp.superadminitms.Receiver.ConnectivityReceiver;


/**
 * Created by ANDROID-PC on 01/08/2018.
 */

public class MyApplication extends Application {

    private static MyApplication mInstance;

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
