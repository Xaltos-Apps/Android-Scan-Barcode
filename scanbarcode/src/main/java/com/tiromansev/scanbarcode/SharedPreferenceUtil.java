package com.tiromansev.scanbarcode;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {

    private final SharedPreferences sharedPreferences;

    public SharedPreferenceUtil(Context context) {
        sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void switchCamera() {
        sharedPreferences.edit()
                .putInt(Constants.PREF_CAMERA_ID, (getCameraId() == 1) ? 0 : 1)
                .commit();
    }

    public int getCameraId() {
        return sharedPreferences.getInt(Constants.PREF_CAMERA_ID, 0);
    }
}
