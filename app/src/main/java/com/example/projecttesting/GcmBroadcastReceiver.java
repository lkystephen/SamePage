package com.example.projecttesting;

/**
 * Created by edmundlee on 7/18/15.
 */

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Explicitly specify that GcmMessageHandler will handle the intent.
            Log.i("GCMReceiver", "waking up");

            ComponentName comp = new ComponentName(context.getPackageName(),
                    GcmMessageHandler.class.getName());

            // Start the service, keeping the device awake while it is launching.
            startWakefulService(context, (intent.setComponent(comp)));
            setResultCode(Activity.RESULT_OK);
        }
}

