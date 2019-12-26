package com.pl.deepbisdksample;

import android.app.Application;

import com.pl.deepbisdk.DeepBiManager;

public class DeepSampleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        DeepBiManager.init(this, "Wc5mtvvGLixE", "6tzFKdHYAiwvIwrlsbW0cdQH");
    }
}
