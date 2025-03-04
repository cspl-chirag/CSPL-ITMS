package com.example.hp.superadminitms.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by ANDROID-PC on 02/08/2018.
 */

public class SessionManager {
    private static final String PREF_NAME = "ITMSPref";
    private static final String KEY_IS_LOGGEDIN = "isLoggedIn";
    private static final String KEY_COMPANY_ID = "CompanyID";
    private static final String KEY_USERNAME = "UserName";
    private static final String KEY_PASSWORD = "Password";
    private static final String KEY_STAFFID = "StaffId";
    private static final String KEY_USERID = "UserId";
    private static final String KEY_USER_TYPE = "UserType";
    private static final String KEY_NOTIFICATION_STATUS = "NotificationStatus";
    private static final String TAG = SessionManager.class.getSimpleName();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setLogin(boolean isLoggedIn) {

        editor.putBoolean(KEY_IS_LOGGEDIN, isLoggedIn);

        editor.commit();

        Log.d(TAG, "session modified!");
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_IS_LOGGEDIN, false);
    }

    public String getKeyPassword() {
        return pref.getString(KEY_PASSWORD, null);
    }

    public void setKeyPassword(String password) {
        editor.putString(KEY_PASSWORD, password);
        editor.commit();
    }

    public String getKeyUserName() {
        return pref.getString(KEY_USERNAME, null);
    }

    public void setKeyUserName(String username) {
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public int getKeyStaffId() {
        return pref.getInt(KEY_STAFFID, 0);
    }

    public void setKeyStaffId(int staffId) {
        editor.putInt(KEY_STAFFID, staffId);
        editor.commit();
    }

    public int getKeyUserId() {
        return pref.getInt(KEY_USERID, 0);
    }

    public void setKeyUserId(int userId) {
        editor.putInt(KEY_USERID, userId);
        editor.commit();
    }

    public int getKeyCompanyId() {
        return pref.getInt(KEY_COMPANY_ID, 0);
    }

    public void setKeyCompanyId(int companyId) {
        editor.putInt(KEY_COMPANY_ID, companyId);
        editor.commit();
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public int getUserType() {
        return pref.getInt(KEY_USER_TYPE, 0);
    }

    public void setUserType(int userType) {
        editor.putInt(KEY_USER_TYPE, userType);
        editor.commit();
    }

    public Boolean getKeyNotificationStatus() {
        return pref.getBoolean(KEY_NOTIFICATION_STATUS, false);
    }

    public void setKeyNotificationStatus(Boolean notificationStatus) {
        editor.putBoolean(KEY_NOTIFICATION_STATUS, notificationStatus);
        editor.commit();
    }
}
