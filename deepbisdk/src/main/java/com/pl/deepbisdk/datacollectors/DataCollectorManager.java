package com.pl.deepbisdk.datacollectors;

import com.pl.deepbisdk.queuemanager.DeepBiQueueManager;
import com.pl.deepbisdk.queuemanager.HitEvent;

import java.util.ArrayList;

public class DataCollectorManager {

    private static DataCollectorManager instance;

    public static DataCollectorManager getInstance() {
        if (instance == null) {
            instance = new DataCollectorManager();
        }
        return instance;
    }

    private DataCollectorManager() {
        dataCollectors = new ArrayList<>();
    }

    private ArrayList<BaseDataCollector> dataCollectors = new ArrayList<>();
    private ArrayList<Class<?>> listDataCollectorClass = new ArrayList<>();

    public void registerDataCollector(Class<?> dataCollectorClass) {
        if (listDataCollectorClass.contains(dataCollectorClass)) {
            return;
        }
        try {
            BaseDataCollector collector = (BaseDataCollector) dataCollectorClass.newInstance();
            dataCollectors.add(collector);
            listDataCollectorClass.add(dataCollectorClass);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    public void putData(HitEvent event) {
        int size = dataCollectors.size();
        for (int i = 0; i < size; i++) {
            dataCollectors.get(i).putData(event);
        }
    }

}
