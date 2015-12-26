package com.example.projecttesting;

import android.graphics.Color;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParsePolyLines {

    List<List<HashMap<String,String>>> input = new ArrayList<>();

    public ParsePolyLines(List<List<HashMap<String,String>>> input) {

        this.input = input;
    }

    public PolylineOptions parseLine() {

        PolylineOptions lineOptions = new PolylineOptions();
        // Traversing through all the routes
        for (int i = 0; i < input.size(); i++){
            ArrayList<LatLng> points = new ArrayList<>();

            // Fetching i-th route
            List<HashMap<String, String>> path = input.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(8);
            lineOptions.color(Color.RED);

        }

        return lineOptions;
    }
}