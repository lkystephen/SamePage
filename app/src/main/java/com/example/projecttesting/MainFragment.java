package com.example.projecttesting;

import android.*;
import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.Util;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.pkmmte.view.CircularImageView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends Fragment implements LocationListener, TextWatcher, ParseLocationHandler {

    //public static GoogleMap googleMap;
    public static Marker selectedMarker;
    public LatLng myPosition;
    Location mLocation;
    public String distance, destination;
    int travelTime;
    public AutoCompleteTextView autoCompView;
    ListView listView;
    MainPageAdapter adapter;
    CircularImageView closest_friend_image;
    //ImageButton delButton;
    LatLng currentLatLng;
    TextView closest_friend_name, closest_location_details, closest_location_details2;
    public static SupportMapFragment mMapFragment;
    User user;
    // Testing data
    //String id = "106808403007880";

    // Define testing location data
    final LatLng test_QC_location = new LatLng(22.2814, 114.1916);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");
        mLocation = bundle.getParcelable("location");
        if (mLocation == null)
            Log.e("MainFragment", "mLocation is null");

        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.mainmap, container, false);

        // Typeface
        Typeface typeface_reg = FontCache.getFont(getContext(), "sf_reg.ttf");

        listView = (ListView) rootView.findViewById(R.id.main_listview);

        closest_friend_name = (TextView) rootView.findViewById(R.id.closest_friend_name);
        closest_location_details = (TextView) rootView.findViewById(R.id.closest_location_details);
        closest_location_details2 = (TextView) rootView.findViewById(R.id.closest_location_details2);
        closest_friend_image = (CircularImageView) rootView.findViewById(R.id.closest_friend_image);

        closest_friend_name.setTypeface(typeface_reg);
        closest_location_details.setTypeface(typeface_reg);
        closest_location_details2.setTypeface(typeface_reg);

        // Compare my location with friends
        String position = searchClosestFriend(user);

        if (!position.equals("NULL")) {
            String id = user.getMasterList().get(Integer.parseInt(position)).fbid;
            closest_friend_image.setImageBitmap(BitmapFactory.decodeFile(Utility.getImage(id, getContext()).getPath()));
            closest_friend_name.setText(user.getMasterList().get(Integer.parseInt(position)).username);
        } else {
            // set any image here
        }

        ConstructNewsFeedItem item = new ConstructNewsFeedItem(user);
        ArrayList<HashMap<String, Integer>> result = item.setEventsFeedPriority();

        Log.i("result size", Integer.toString(result.size()));

        adapter = new MainPageAdapter(getActivity(), R.layout.newsfeed_list_display, result, user);
        listView.setAdapter(adapter);

        // Get names of friends
        final List<OtherUser> master_list = user.getMasterList();

        // Calculating location now
        // Getting URL to the Google Directions API
        myPosition = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        DirectionFromLatLng getDirection = new DirectionFromLatLng();
        String url = getDirection.execute(myPosition, test_QC_location);
        DownloadTask downloadTask = new DownloadTask(user);

        downloadTask.delegate = this;
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        return rootView;
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = mLocation.getLatitude();
        double longitude = mLocation.getLongitude();
        myPosition = new LatLng(latitude, longitude);
        // destroy the old marker to prevent duplicated markers when new
        // location is received
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }
    @Override
    public void afterTextChanged(Editable arg0) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String input = autoCompView.getText().toString();

        if (input.equals("")) {
            //delButton.setVisibility(View.INVISIBLE);
        } else {
            //delButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int before, int count) {
    }


    //this override the implemented method from asyncTask
    public void update(ArrayList<String> output,List<List<HashMap<String, String>>> result) {
        String name = output.get(0);
        String distance = output.get(1);
        String destination = output.get(2);

        // Set the distance measured in minutes
        closest_location_details.setText("@ " + destination);
        closest_location_details2.setText(distance + " away");

    }

    public String searchClosestFriend(User user) {
        String id = "NULL";
        int meter = 999999;
        int size = user.getMasterList().size();
        Location closestLocation = new Location("TEST");

        for (int i = 0; i < size; i++) {
            if (user.getMasterList().get(i).hasLoc) {

                closestLocation.setLatitude(user.getMasterList().get(i).lat);
                closestLocation.setLongitude(user.getMasterList().get(i).longitude);
                int temp = Math.round(mLocation.distanceTo(closestLocation));
                if (temp < meter) {
                    meter = temp;
                }
                id = Integer.toString(i);
            }
        }

        return id;
    }

}
