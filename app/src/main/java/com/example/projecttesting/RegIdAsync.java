package com.example.projecttesting;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.facebook.Profile;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

public class RegIdAsync extends AsyncTask<Void, Void, String> {

    //for GCM
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "603098203110";
    String fbid;
    String username;
    User user;
    MainAct ticket;

    Context context;

    public RegIdAsync (Context context, MainAct ticket) {
        this.context = context;
        this.ticket = ticket;
    }

    //get RegId
        @Override
        protected String doInBackground(Void... params) {
            Log.i("GCM", "starting");

            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance((context));
                }
                regid = gcm.register(PROJECT_NUMBER);
                // msg = "Device registered, registration ID=" + regid;
                Log.i("GCM", regid);

            } catch (IOException ex) {
                Log.i("GCM", "Error :" + ex.getMessage());
            }
            return regid;
        }

        @Override
        protected void onPostExecute(String regId_input) {
            // get with db
            fbid = Profile.getCurrentProfile().getId();
            Log.i("name", Profile.getCurrentProfile().getName());
            username = Profile.getCurrentProfile().getName();

            user = new User(fbid, username, regId_input, ticket);
            user.execute();
        }
}