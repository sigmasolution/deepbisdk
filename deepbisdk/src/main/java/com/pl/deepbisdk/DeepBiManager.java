package com.pl.deepbisdk;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.pl.deepbisdk.network.ApiClient;
import com.pl.deepbisdk.utilities.RandomString;
import com.pl.deepbisdk.utilities.Utility;

public class DeepBiManager {
    private static final String LOG_TAG = "MonitorService";
    public static final String PREF_ATTENTION_ACTIVE = "PREF_ATTENTION_ACTIVE";
    public static final String PREF_ATTENTION_TOTAL = "PREF_ATTENTION_TOTAL";
    public static String DEVICE_ID = "";
    public static String INGESTION_KEY;
    public static String SESSION_ID;
    public static double SCREEN_SIZE_INCH = 0;
    static Context mAppContext;
    static SharedPreferences mPerference;

    public static void init(Context context, String dataSetKey, String ingestionKey) {
        Log.d("TEST SDK", "Init DeepBi sdk here + dataSetKey=" + dataSetKey + ";ingestionKey=" + ingestionKey);
        mAppContext = context;
        mPerference = context.getSharedPreferences("DeepBiSdk_Preference", Context.MODE_PRIVATE);
        DEVICE_ID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        INGESTION_KEY = ingestionKey;

        ((Application) DeepBiManager.getAppContext()).registerActivityLifecycleCallbacks(lifecycleCallbacks);
        RandomString randomString = new RandomString();
        SESSION_ID = randomString.nextString();
        ApiClient.setBaseUrl(dataSetKey);
    }

    public static void startCollecting(Activity acitivity) {
        MonitorService.startService(acitivity);
    }

    public static void stopCollecting() {
        MonitorService.stopService();
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public static SharedPreferences getPerference() {
        return mPerference;
    }

    public static boolean isTablet() {
        return SCREEN_SIZE_INCH >= 7;
    }

    public static boolean isSmallScreen() {
        return SCREEN_SIZE_INCH < 2.5;
    }

    public static void unregisterLifeCycleCallBack() {
        ((Application) DeepBiManager.getAppContext()).unregisterActivityLifecycleCallbacks(lifecycleCallbacks);
        mPerference.edit().putString("PageOpened1stTime", "").commit();
    }
    static Application.ActivityLifecycleCallbacks lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            // Call page ping
            Log.d(LOG_TAG, "MonitorService 1 onActivityCreated");
            SCREEN_SIZE_INCH = Utility.getScreenSizeInches(activity);
            mPerference.edit().putString("PageOpened1stTime", activity.getLocalClassName()).commit();
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) { }

        @Override
        public void onActivityResumed(@NonNull Activity activity) { }

        @Override
        public void onActivityPaused(@NonNull Activity activity) { }

        @Override
        public void onActivityStopped(@NonNull Activity activity) { }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) { }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) { }
    };
}
