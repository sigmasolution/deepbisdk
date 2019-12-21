package com.pl.deepbisdk.utilities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.pl.deepbisdk.DeepBiManager;
import com.pl.deepbisdk.queuemanager.HitEvent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;

public class Utility {
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static int[] getScreenSizePixel() {
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        return new int[] {width, height};
    }

    private boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static int byteCount(String content) {
        try {
            return content.getBytes("UTF-8").length;
        } catch (UnsupportedEncodingException e) {
            return 0;
        }
    }

    public static void writeFile(ArrayList<HitEvent> listHits) {
        JSONArray jsonArr = new JSONArray();
        for (HitEvent hitevent : listHits) {
            try {
                JSONObject jobject = new JSONObject(hitevent.toJsonString());
                jsonArr.put(jobject);
            } catch (JSONException e) { }
        }

        try {
            File folder = new File(Environment.getExternalStorageDirectory().toString() + "/hitsDeep");
            if (!folder.exists()) {
                folder.mkdir();
            }
            File hitFile = new File(folder, "hits" + Calendar.getInstance().getTimeInMillis() + ".txt");
//            if (!hitFile.exists()) {
//                hitFile.mkdir();
//            }
            FileOutputStream fos = new FileOutputStream(hitFile);
            fos.write(jsonArr.toString().getBytes("UTF-8"));
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasNetworkConnection(Context c) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) DeepBiManager.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) DeepBiManager.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static double getScreenSizeInches(Activity activity){
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        // since SDK_INT = 1;
        int mWidthPixels = displayMetrics.widthPixels;
        int mHeightPixels = displayMetrics.heightPixels;

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 14 && Build.VERSION.SDK_INT < 17) {
            try{
                mWidthPixels = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                mHeightPixels = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (Exception ignored) {}
        }

        // includes window decorations (statusbar bar/menu bar)
        if (Build.VERSION.SDK_INT >= 17) {
            try {
                Point realSize = new Point();
                Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
                mWidthPixels = realSize.x;
                mHeightPixels = realSize.y;
            } catch (Exception ignored) {}
        }

        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double x = Math.pow(mWidthPixels / dm.xdpi, 2);
        double y = Math.pow(mHeightPixels / dm.ydpi, 2);
        return Math.sqrt(x + y);
    }
}
