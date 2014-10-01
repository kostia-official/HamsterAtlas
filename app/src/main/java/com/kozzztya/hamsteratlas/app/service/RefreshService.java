package com.kozzztya.hamsteratlas.app.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.kozzztya.hamsteratlas.app.Constants;
import com.kozzztya.hamsteratlas.app.Preferences;

public class RefreshService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = "my" + RefreshService.class.getSimpleName();

    private AlarmManager mAlarmManager;
    private PendingIntent mPendingIntent;

    public RefreshService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mPendingIntent = PendingIntent.getBroadcast(this, 0,
                new Intent(Constants.ACTION_REFRESH), 0);
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

        setUpRepeating();
    }

    /**
     * Set repeating with interval specified in the settings
     */
    private void setUpRepeating() {
        long updateInterval = Preferences.getUpdateInterval(this);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC,
                System.currentTimeMillis(), updateInterval, mPendingIntent);
    }

    /**
     * Stop AlarmManager repeating
     */
    private void stopRepeating() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        mAlarmManager.cancel(mPendingIntent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        setUpRepeating();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopRepeating();
    }

    @Override
    public void onDestroy() {
        stopRepeating();
    }

    public static class RefreshReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent serviceIntent = new Intent(context, FetchService.class);
            context.startService(serviceIntent);
        }
    }
}
