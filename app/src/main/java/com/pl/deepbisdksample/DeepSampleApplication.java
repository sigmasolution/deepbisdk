package com.pl.deepbisdksample;

import android.app.Application;

import com.pl.deepbisdk.DeepBiManager;

public class DeepSampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DeepBiManager.init(this, "eRjHk30JNe3R", "sa6zwp0rmXdzv1c96CJDW1kP");
    }
}
