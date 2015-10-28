package com.example.projecttesting;

/**
 * Created by edmundlee on 7/18/15.
 */

import com.google.android.gms.gcm.GoogleCloudMessaging;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class GcmMessageHandler extends IntentService{
    String mes;
    Context context;
    private Handler handler;
    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        handler = new Handler();
        context = GcmMessageHandler.this;
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        mes = extras.getString("title");
        showToast();
        Log.i("GCM", "Received : (" +messageType+")  "+extras.getString("title"));

        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }
    public void showToast(){
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
                int icon = R.drawable.ic_launcher;
                long when = System.currentTimeMillis();
                NotificationManager notificationManager = (NotificationManager)
                        GcmMessageHandler.this.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notification = new Notification(icon, mes, when);

                String title = GcmMessageHandler.this.getString(R.string.app_name);

                Intent notificationIntent = new Intent(context, MainActivity.class);
                // set intent so it does not start a new activity
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent intent =
                        PendingIntent.getActivity(context, 0, notificationIntent, 0);
                notification.setLatestEventInfo(context, title, mes, intent);
                notification.flags |= Notification.FLAG_AUTO_CANCEL;

                // Play default notification sound
                notification.defaults |= Notification.DEFAULT_SOUND;

                // Vibrate if vibrate is enabled
                notification.defaults |= Notification.DEFAULT_VIBRATE;
                notificationManager.notify(0, notification);

            }
        });
    }
}
