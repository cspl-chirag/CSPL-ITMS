package com.example.hp.superadminitms.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ANDROID-PC on 02/01/2019.
 */

public class LastKmDatum {
    @SerializedName("LogoutKm")
    @Expose
    private Integer logoutKm;

    public Integer getLogoutKm() {
        return logoutKm;
    }

    public void setLogoutKm(Integer logoutKm) {
        this.logoutKm = logoutKm;
    }
}
