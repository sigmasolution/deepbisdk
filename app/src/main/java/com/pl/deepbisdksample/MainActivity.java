package com.pl.deepbisdksample;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    },
                    1000);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1: {
                Intent intent = new Intent(this, ActivitySample1.class);
                startActivity(intent);
                break;
            }
            case R.id.btn2: {
                Intent intent = new Intent(this, ActivitySample2.class);
                startActivity(intent);
                break;
            }
            case R.id.btn3: {
                Intent intent = new Intent(this, ActivitySample3.class);
                startActivity(intent);
                break;
            }
        }
    }


}
