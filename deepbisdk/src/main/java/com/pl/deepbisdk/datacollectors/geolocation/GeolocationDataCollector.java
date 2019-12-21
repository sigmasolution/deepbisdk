package com.pl.deepbisdk.datacollectors.geolocation;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;

import com.pl.deepbisdk.DeepBiManager;
import com.pl.deepbisdk.datacollectors.BaseDataCollector;
import com.pl.deepbisdk.queuemanager.HitEvent;

import static android.content.Context.LOCATION_SERVICE;

public class GeolocationDataCollector extends BaseDataCollector {

    @Override
    public void putData(HitEvent event) {
        HitEvent.User user;
        if (event.getUser() == null) {
            user = new HitEvent.User();
            event.setUser(user);
        } else {
            user = event.getUser();
        }
        user.setLocation(getCurrentLocation());
    }

    private HitEvent.UserLocation getCurrentLocation() {
        HitEvent.UserLocation location = new HitEvent.UserLocation();

        try {
            LocationManager locationManager = (LocationManager) DeepBiManager.getAppContext().getSystemService(LOCATION_SERVICE);

            //default had permission
            if (locationManager != null) {
                @SuppressLint("MissingPermission")
                Location networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                @SuppressLint("MissingPermission")
                Location gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (networkLocation != null) {
                    location.setLongitude(networkLocation.getLongitude());
                    location.setLattitude(networkLocation.getLatitude());

                } else if (gpsLocation != null) {
                    location.setLongitude(gpsLocation.getLongitude());
                    location.setLattitude(gpsLocation.getLatitude());
                }
            }
        } catch (Exception ex) {

        }
        return location;
    }
}
