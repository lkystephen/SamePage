package com.example.projecttesting;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationResult;

public class MyLocationHandler extends IntentService {

    User user;

    private String TAG = this.getClass().getSimpleName();

    public MyLocationHandler() {
        super("MyLocationHandler");

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Bundle bundle = intent.getExtras();
        bundle.setClassLoader(User.class.getClassLoader());
        user = (User) bundle.getParcelable("user");
        if (user == null) {
            Log.e(TAG, "user is null");
        }

        if (LocationResult.hasResult(intent)) {
            LocationResult locationResult = LocationResult.extractResult(intent);
            Location location = locationResult.getLastLocation();
            Log.i(TAG, Double.toString(location.getLatitude()) + ", " + Double.toString(location.getLongitude()));
            user.updateLocation(location);

            // Test to see if it works
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(R.drawable.ic_launcher);
            mBuilder.setContentTitle("Location");
            mBuilder.setContentText(Double.toString(location.getLatitude()) + ", " + Double.toString(location.getLongitude()));

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // notificationID allows you to update the notification later on.
            mNotificationManager.notify(1, mBuilder.build());

        } else
            Log.e(TAG, "Null object returned for location API");
    }

}



