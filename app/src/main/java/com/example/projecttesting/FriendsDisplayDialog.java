package com.example.projecttesting;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FriendsDisplayDialog extends DialogFragment implements OnMapReadyCallback, ParseLocationHandler {

    FrameLayout map_frame;
    LinearLayout mapLoadingLayout;
    LatLng latLng, mLatLng;
    TextView friend_location, friend_direction;
    Location mLastLocation;
    User user;
    SupportMapFragment mMapFragment;

    public FriendsDisplayDialog() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.friends_detail_display, new LinearLayout(getActivity()), false);

        Bundle bundle = getArguments();
        Location location = bundle.getParcelable("location");
        String name = bundle.getString("name");
        mLastLocation = bundle.getParcelable("mLocation");
        user = bundle.getParcelable("user");
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mLatLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());

        // Set up the loading animation
        mapLoadingLayout = (LinearLayout) view.findViewById(R.id.loadingLayout);

        TextView friend_name = (TextView) view.findViewById(R.id.friend_name);
        friend_location = (TextView) view.findViewById(R.id.friend_location);
        friend_direction = (TextView) view.findViewById(R.id.friend_direction);

        Typeface face_r = FontCache.getFont(getContext(), "sf_reg.ttf");
        Typeface face_b = FontCache.getFont(getContext(), "sf_bold.ttf");

        friend_name.setTypeface(face_r);
        friend_location.setTypeface(face_r);
        friend_direction.setTypeface(face_r);

        friend_name.setText(name);

        map_frame = (FrameLayout) view.findViewById(R.id.map2);

        mMapFragment = new SupportMapFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map2, mMapFragment).commit();
        mMapFragment.getMapAsync(FriendsDisplayDialog.this);

        // Address (if not default to load)
        friend_location.setText("Loading map..");
        friend_direction.setText("Loading map..");

        return view;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapLoadingLayout.setVisibility(View.GONE);
        map_frame.setVisibility(View.VISIBLE);
        MapObjectControl control = new MapObjectControl();
        control.AddSearchedMarker(latLng, googleMap, 14);

        friend_location.setText("Click for details");
        friend_direction.setText("");
        friend_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friend_location.setText("Calculating..");
                friend_direction.setText("Calculating..");
                DirectionFromLatLng getDirection = new DirectionFromLatLng();
                String url = getDirection.execute(mLatLng, latLng);

                DownloadTask downloadTask = new DownloadTask(user);
                downloadTask.delegate = FriendsDisplayDialog.this;
                downloadTask.execute(url);
            }
        });

    }

    @Override
    public void update(ArrayList<String> output,List<List<HashMap<String, String>>> result) {
        String name = output.get(0);
        String distance = output.get(1);
        String destination = output.get(2);
        friend_location.setText(destination);
        ParsePolyLines parsePolyLines = new ParsePolyLines(result);
        mMapFragment.getMap().addPolyline(parsePolyLines.parseLine());

    }

}