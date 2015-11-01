/*package com.example.projecttesting;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class LoadingImage {

    public LoadingImage(Context context, int resource){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), resource, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;

        }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
        // raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;

        int inSampleize =1;

        if (height > reqHeight || width > reqWidth){

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight/inSampleize)>reqWidth && (halfWidth/inSampleize > reqWidth){
                inSampleize *=2;
            }
        }

        return inSampleize;
    }
}*/