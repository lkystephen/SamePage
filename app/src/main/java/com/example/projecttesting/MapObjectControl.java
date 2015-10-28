package com.example.projecttesting;

import android.app.Fragment;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapObjectControl {

    Marker current_marker;

    public MapObjectControl() {

    }

    public void MovedToCurrentLoc (LatLng coordinates, GoogleMap map, int zoom){
        map.clear();

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                coordinates, zoom);

        map.moveCamera(cameraUpdate);
    }

    public void AddSearchedMarker (LatLng coordinates, GoogleMap map, int zoom){

        map.clear();

        current_marker = map.addMarker(new MarkerOptions().position(
                coordinates).title("Searched location"));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                coordinates, zoom);

        map.moveCamera(cameraUpdate);
    }

}