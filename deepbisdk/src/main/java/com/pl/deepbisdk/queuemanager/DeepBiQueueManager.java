package com.pl.deepbisdk.queuemanager;


import java.util.ArrayList;

public class DeepBiQueueManager {

    private static DeepBiQueueManager instance;

    public static DeepBiQueueManager getInstance() {
        if (instance == null) {
            instance = new DeepBiQueueManager();
        }
        return instance;
    }

    private DeepBiQueueManager() {

    }

    private ArrayList<HitEvent> mHitArray = new ArrayList<>();

    public void addHitEvent(HitEvent hitEvent) {
        synchronized (this) {
            mHitArray.add(hitEvent);
        }
    }

    public HitEvent popHitEvent() {
        synchronized (this) {
            return mHitArray.remove(0);
        }
    }

    public HitEvent getFirstItem() {
        synchronized (this) {
            return mHitArray.get(0);
        }
    }

    public void clear() {
        synchronized (this) {
            mHitArray.clear();
        }
    }

    public int count() {
        return mHitArray.size();
    }
}
