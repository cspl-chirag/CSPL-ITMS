package com.example.hp.superadminitms.Network;

/**
 * Created by ANDROID-PC on 01/08/2018.
 */

public class LoginRequest {


    private final String username;
    private final String password;
    private final String Device_SN;
    private final int Battery;

    public LoginRequest(String username, String password, String device_SN, int battery) {
        this.username = username;
        this.password = password;
        this.Device_SN = device_SN;
        this.Battery = battery;
    }
}
