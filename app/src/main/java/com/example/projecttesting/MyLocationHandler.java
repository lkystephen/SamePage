package com.example.projecttesting;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MyLocationHandler extends IntentService {

    User user;
    private String TAG = this.getClass().getSimpleName();

    public MyLocationHandler() {
        super("MyLocationHandler");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        // Get user id from sharedpref manager
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String id = preferences.getString("id","DEFAULT");


        if (LocationResult.hasResult(intent)) {
            LocationResult locationResult = LocationResult.extractResult(intent);
            Location location = locationResult.getLastLocation();
            Log.i(TAG, Double.toString(location.getLatitude()) + ", " + Double.toString(location.getLongitude()));

            if (!id.equals("DEFAULT")) {
                User user = new User();
                user.updateLocation(location,id);

            }
        } else
            Log.e(TAG, "Null object returned for location API");
    }

}



