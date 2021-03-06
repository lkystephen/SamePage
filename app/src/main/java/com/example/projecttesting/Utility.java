package com.example.projecttesting;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utility {


    private static final String TAG = Utility.class.getSimpleName();

    public static float convertPixelToDp(float px, Context context) {

        float density = context.getResources().getDisplayMetrics().density;

        return Math.round(px / density);
    }

    public static float convertDpToPixel(float dp, Context context) {

        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);

    }

    public static String getMonthTextFromInt(int month) {
        String monthText = new String();

        switch (month) {
            case 1:
                monthText = "01";
                break;
            case 2:
                monthText = "02";
                break;
            case 3:
                monthText = "03";
                break;
            case 4:
                monthText = "04";
                break;
            case 5:
                monthText = "05";
                break;
            case 6:
                monthText = "06";
                break;
            case 7:
                monthText = "07";
                break;
            case 8:
                monthText = "08";
                break;
            case 9:
                monthText = "09";
                break;
            case 10:
                monthText = "10";
                break;
            case 11:
                monthText = "11";
                break;
            case 12:
                monthText = "12";
                break;
        }
        return monthText;
    }

    public static String storeImage(Bitmap bitmap, String filename, Context context) {
        String stored = null;
        String state = Environment.getExternalStorageState();
        File folder2 = context.getExternalCacheDir();
        File folder = new File(folder2 + "/Samepage");
        Log.i(TAG, "Storage state is " + state + ", path is " + folder.toString());

        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            Log.e(TAG, "Storage is not mounted!");
        }

        if (!folder.exists()) {
            folder.mkdirs();
            Log.i(TAG, "Directory is created");
        }

        if (!folder.canWrite()) {
            Log.e(TAG, "Directory is not available");
        }

        //   File sdcard = Environment.getExternalStorageDirectory();
        if (folder.exists()) {
            File file = new File(folder, filename + ".png");
            if (file.exists())
                file.delete();

            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
                out.flush();
                out.close();
                stored = "success";
                Log.i(TAG, "User image created at " + file.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return stored;
    }

    public static File getImage(String imageName, Context context) {
        File mediaImage = null;
        try {
            File myDir2 = context.getExternalCacheDir();
            File myDir = new File(myDir2 + "/Samepage");
            if (myDir.exists()) {
                //File newDir = new File("sdcard");
                mediaImage = new File(myDir, imageName + ".png");
                Log.i("Image retrieved from", mediaImage.toString());

            } else {
                Log.e(TAG, "Directory does not exist when reading");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return mediaImage;
    }

    public static Bitmap downloadImage(String id) {
        URL img_value;
        Bitmap img = null;
        try {
            img_value = new URL("https://graph.facebook.com/" + id + "/picture?type=normal");
            //Log.i("Image URL",img_value.toString());
            img = BitmapFactory.decodeStream(img_value.openConnection().getInputStream());
            if (img == null) {
                Log.i("Image download", "failed, returns null");
            } else {
                Log.i("Image download", "success!");
            }
        } catch (MalformedURLException e) {
            Log.e("MalformedURL", "Error");
        } catch (IOException e) {
            Log.e("IOException", "Error");
        }
        return img;
    }

    public String timeLapsedFromNow(long millis) {

        long currentMillis = System.currentTimeMillis();
        long a = currentMillis - millis;
        int b = Math.round(a / 60000); // present the difference in minutes
        Log.i(TAG,"Last update from user is " + Long.toString(millis));
        Log.i(TAG,"Current time is " + Long.toString(currentMillis));
        Log.i(TAG,"Last update difference from now is " + Integer.toString(b)+ "mins");
        String result = new String();

        if (b < 60) {
            result = Integer.toString(b) + " mins ago";
        } else {
            if (b > 60 && b < 1440) {
                int c = Math.round(b / 60);
                result = Integer.toString(c) + " hours ago";
            } else if (b > 1440 && b < 525600) {
                int d = Math.round(b / 1440);
                result = Integer.toString(d) + " days ago";
            } else if (b > 525600) {
                int e = Math.round(b / 525600);
               result = Integer.toString(e) + " years ago";
            }
        }

        return result;
    }

    public boolean haveNetworkConnection(Context context){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        if (activeNetworkInfo != null){ // Connected to internet
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE){
                Log.i(TAG,"Mobile connection available");
                haveConnectedMobile = true;
            }
            if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                Log.i(TAG,"Wifi connection available");
                haveConnectedWifi = true;
            }
        }
        return haveConnectedMobile || haveConnectedWifi;
    }

}