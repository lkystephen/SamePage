package com.example.projecttesting;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    public static float convertPixelToDp(float px, Context context) {

        float density = context.getResources().getDisplayMetrics().density;

        return Math.round(px / density);
    }

    public static float convertDpToPixel(float dp, Context context) {

        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);

    }

    /*
        public static float getHeightofLowerActionBar (Activity baseActivity) {
            LinearLayout bottomActionBar = (LinearLayout) baseActivity.findViewById(R.id.bottom_actionbar);
            float heightOfActionBar = bottomActionBar.getHeight();

            return heightOfActionBar;
        }
    */
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

    public static String storeImage(Bitmap bitmap, String filename) {
        String stored = null;
        String folder_main = "Samepage";
        File folder = new File(Environment.getExternalStorageDirectory(),folder_main);
        //Log.i("External directory",folder.toString());

        if (!folder.exists()){
            folder.mkdirs();
        Log.i("External created","created");
        }

        File sdcard = Environment.getExternalStorageDirectory();
        File file = new File(sdcard+"/Samepage", filename + ".png");

        if (file.exists())
            file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            //Log.i("okok","k");
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            stored = "success";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stored;
    }

    public static File getImage(String imageName) {
        File mediaImage = null;
        try {
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            if (!myDir.exists()){
                Log.i("my Dir","Error, does not exist");
                return null;}
            if (myDir.getPath() != null){
            mediaImage = new File(myDir.getPath()+"/Samepage/" + imageName+ ".png");
                Log.i("Image retrieved from",mediaImage.toString());
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
                Log.i("Image download","failed, returns null");
            } else {
                Log.i("Image download","success!");
            }
        } catch (MalformedURLException e) {
            Log.e("MalformedURL","Error");
        } catch (IOException e){
            Log.e("IOException","Error");
        }
        return img;
    }


}