package com.pl.deepbisdk.datacollectors;

import com.pl.deepbisdk.queuemanager.HitEvent;

public abstract class BaseDataCollector {

    protected boolean isCollecting = false;

    protected void startCollecting() {
        isCollecting = true;
    }

    /**
     * Stop collect data
     */
    protected void stopCollecting() {
        isCollecting = false;
    }

    public abstract void putData(HitEvent event);
}
