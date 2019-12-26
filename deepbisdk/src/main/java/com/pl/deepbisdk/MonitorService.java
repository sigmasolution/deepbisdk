package com.pl.deepbisdk;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.pl.deepbisdk.datacollectors.DataCollectorManager;
import com.pl.deepbisdk.datacollectors.deviceinfo.DeviceInfoDataCollector;
import com.pl.deepbisdk.datacollectors.geolocation.GeolocationDataCollector;
import com.pl.deepbisdk.localdata.DatabaseAccess;
import com.pl.deepbisdk.localdata.dao.HitsObject;
import com.pl.deepbisdk.network.NetworkManager;
import com.pl.deepbisdk.queuemanager.DeepBiQueueManager;
import com.pl.deepbisdk.queuemanager.HitEvent;
import com.pl.deepbisdk.utilities.Utility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorService extends Service {
    private static final String LOG_TAG = "MonitorService";

    private static final int TIMER_TICK = 4000; // 4 seconds
    private static final long LENGTH_5MB = 5 * 1024 * 1024;

    public static void startService(Activity activity) {
        Intent intent = new Intent(activity, MonitorService.class);
        DeepBiManager.getAppContext().startService(intent);
    }

    public static void stopService() {
        Intent stopServiceIntent = new Intent(DeepBiManager.getAppContext(), MonitorService.class);
        DeepBiManager.getAppContext().stopService(stopServiceIntent);
    }

    private DataCollectorManager mDataCollectorManager;
    private DeepBiQueueManager mQueueManager;
    private DatabaseAccess mDatabaseAccess;
    private NetworkManager mNetworkManager;

    private long mTimePow = 0;
    private double mNextTick = 0;
    private double mCurrentTick = 0;

    private ArrayList<String> pageVisible = new ArrayList<>();
    private ArrayList<String> pageStack = new ArrayList<>();

    private Timer dataFiringTimer;
    private TimerTask dataFiringTimerTask;

    private int activeTime = 0;
    private int idleTime = 0;
    private int deltaTime = 0;
    private Timer appStatusCountingTimer;
    private TimerTask appStatusCountingTimerTask;

    Application.ActivityLifecycleCallbacks lifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            pageStack.add(0, activity.getLocalClassName());
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            pageVisible.add(0, activity.getLocalClassName());
            deltaTime = 0;
            fireEvents("page-open");
            stopDataTimer();
            startDataTimer();
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            pageVisible.remove(activity.getLocalClassName());
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
            pageStack.remove(activity.getLocalClassName());
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(LOG_TAG, "Test MonitorService onCreate");
        mDataCollectorManager = DataCollectorManager.getInstance();
        mQueueManager = DeepBiQueueManager.getInstance();
        mDatabaseAccess = DatabaseAccess.getInstance(this);
        mNetworkManager = NetworkManager.getInstance();

        // Load Queue
        ArrayList<HitsObject> storedHits = mDatabaseAccess.getAllHits();
        mQueueManager.clear();
        if (storedHits.size() > 0) {
            for (HitsObject hitobject : storedHits) {
                HitEvent he = new Gson().fromJson(hitobject.json_content, HitEvent.class);
                he.setTimemillis(hitobject.time_create);
                mQueueManager.addHitEvent(he);
                Log.d(LOG_TAG, "DeepBi SDK fireEvents: addHitEvent loaded " + mQueueManager.count());
            }
        }

        // Register Default collector
        mDataCollectorManager.registerDataCollector(DeviceInfoDataCollector.class);
        mDataCollectorManager.registerDataCollector(GeolocationDataCollector.class);

        // Start Timer
        String pageOpen1stTime = DeepBiManager.getPerference().getString("PageOpened1stTime", null);
        if (!TextUtils.isEmpty(pageOpen1stTime)) {
            deltaTime = 0;
            pageVisible.add(0, pageOpen1stTime);
            pageStack.add(0, pageOpen1stTime);
            fireEvents("page-open");
            DeepBiManager.unregisterLifeCycleCallBack();
        }

        // Life cycle callback
        ((Application) DeepBiManager.getAppContext()).registerActivityLifecycleCallbacks(lifecycleCallbacks);

        startDataTimer();
        startAppStatusCountingTimerTask();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopDataTimer();
        stopAppStatusCountingTimerTask();
        ((Application) DeepBiManager.getAppContext()).unregisterActivityLifecycleCallbacks(lifecycleCallbacks);

        deltaTime = 0;
        idleTime = 0;
        activeTime = 0;
    }

    private void startDataTimer() {
        stopDataTimer();

        mTimePow = 1;
        mNextTick = 2;
        mCurrentTick = 2;
        dataFiringTimer = new Timer();
        dataFiringTimerTask = new TimerTask() {
            @Override
            public void run() {
                Log.d(LOG_TAG, "DeepBi SDK fireEvents mCurrentTick=" + mCurrentTick + " , nextTick=" + mNextTick);
                if (pageStack.isEmpty()) {
                    stopSelf();
                    return;
                }

                deltaTime += TIMER_TICK / 1000;
                if (mCurrentTick == mNextTick) {
                    fireEvents("page-ping");
                    mTimePow++;
                    mNextTick = Math.pow(2, mTimePow);
                    deltaTime = 0;
                }

                mCurrentTick++;
            }
        };

        dataFiringTimer.schedule(dataFiringTimerTask, TIMER_TICK, TIMER_TICK);
    }

    private void stopDataTimer() {
        if (dataFiringTimer != null) {
            dataFiringTimer.cancel();
            dataFiringTimer = null;
            dataFiringTimerTask.cancel();
            dataFiringTimerTask = null;
        }
    }

    private void startAppStatusCountingTimerTask() {
        stopAppStatusCountingTimerTask();
        appStatusCountingTimer = new Timer();
        appStatusCountingTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (isInBackground()) {
                    idleTime++;
                } else {
                    activeTime++;
                }
            }
        };
        appStatusCountingTimer.schedule(appStatusCountingTimerTask, 1000, 1000);
    }

    private void stopAppStatusCountingTimerTask() {
        if (appStatusCountingTimer != null) {
            appStatusCountingTimer.cancel();
            appStatusCountingTimer = null;
            appStatusCountingTimerTask.cancel();
            appStatusCountingTimerTask = null;
        }
    }

    private void fireEvents(String eventType) {
        String currentPageTitle = getPageTitle();
        if (currentPageTitle == null) {
//            stopSelf();
            return;
        }
        Log.d(LOG_TAG, "DeepBi SDK fireEvents: eventType=" + eventType + ",pageTitle=" + currentPageTitle);

        // Check for expired hits
        mDatabaseAccess.clearExpiredHits();

        // Collect data
        HitEvent hitEvent = new HitEvent(eventType, currentPageTitle);
        hitEvent.setTimemillis(Calendar.getInstance().getTimeInMillis());
        mDataCollectorManager.putData(hitEvent);

        // Active/Idle Time
        hitEvent.getUser().getAttention().setIdle(idleTime);
        hitEvent.getUser().getAttention().setActive(activeTime);
        hitEvent.getUser().getAttention().setDeltatime(deltaTime);
        Log.d(LOG_TAG, "DeepBi SDK fireEvents: idleTime=" + idleTime + ",activeTime=" + activeTime + ",deltaTime=" + deltaTime);

        // Stored Hit event
        mQueueManager.addHitEvent(hitEvent);

        // Insert to db
        HitsObject hitsObject = new HitsObject();
        hitsObject.time_create = hitEvent.getTimemillis();
        hitsObject.json_content = hitEvent.toJsonString();
        mDatabaseAccess.insertHit(hitsObject);

        // Hit send
        int totalSize = 0;
        ArrayList<HitEvent> listSendHit = new ArrayList<>();
        while (mQueueManager.count() > 0) {
            HitEvent eventGet = mQueueManager.getFirstItem();
            int dataSize = Utility.byteCount(eventGet.toJsonString());
            if (totalSize + dataSize < LENGTH_5MB) {
                totalSize += dataSize;
                listSendHit.add(eventGet);
                mQueueManager.popHitEvent();
            } else {
                // Call api post hit
                sendHit(listSendHit);
                totalSize = 0;
                listSendHit = new ArrayList<>();
            }
        }
        if (listSendHit.size() > 0) {
            // Call api post hit
            sendHit(listSendHit);
        }
    }

    private void sendHit(ArrayList<HitEvent> listSendHit) {
        Utility.writeFile(listSendHit);

        // If not have network
        if (!Utility.hasNetworkConnection(this)) {
            for (HitEvent event : listSendHit) {
                mQueueManager.addHitEvent(event);
            }
            return;
        }
        mNetworkManager.postHit(listSendHit, new NetworkManager.PostHitListener() {
            @Override
            public void onPostHitFinish(boolean isSucess, ArrayList<HitEvent> listSent) {
                handlePostHitFinish(isSucess, listSent);
            }
        });
    }

    private void handlePostHitFinish(boolean isSucess, ArrayList<HitEvent> listSent) {
        if (isSucess) {
            Long [] listIds = new Long[listSent.size()];
            for (int i = 0; i < listIds.length; i++) {
                listIds[i] = listSent.get(i).getTimemillis();
            }
            mDatabaseAccess.clearHits(listIds);
        } else {
            for (HitEvent event : listSent) {
                mQueueManager.addHitEvent(event);
            }
        }
    }

    private String getPageTitle() {
        if (!pageVisible.isEmpty()) {
            return pageVisible.get(0);
        }
        if (pageStack.isEmpty()) {
            return null;
        } else {
            return pageStack.get(0);
        }
    }

    private boolean isInBackground() {
        return pageVisible.isEmpty();
    }

    private boolean isAppRunning() {
        return !pageStack.isEmpty();
    }
}
