package com.kozzztya.hamsteratlas.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    public static final String KEY_REFRESH_INTERVAL = "pref_key_refresh_interval";
    public static final String DEFAULT_VALUE_REFRESH_INTERVAL = "30000";

    public static long getUpdateInterval(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return Long.valueOf(sharedPreferences.getString(KEY_REFRESH_INTERVAL,
                DEFAULT_VALUE_REFRESH_INTERVAL));
    }
}
