package com.example.projecttesting;

import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
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
import android.support.v4.app.FragmentManager;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class EventDisplayDialog extends DialogFragment implements OnMapReadyCallback {

    private SupportMapFragment fragment;
    LatLng latLng;
    String fbid, organiser_name, organiser_fbid;
    LinearLayout mapLoadingLayout;
    FrameLayout mapLayout;


    public EventDisplayDialog() {
        fragment = new SupportMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.event_details_display, container, false);


        // Set up the loading animation
        mapLoadingLayout = (LinearLayout) view.findViewById(R.id.loadingLayout);

        // Remove title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        String start_hour, start_minute = new String();

        // Get information from bundle passed from Fragment
        Bundle mArgs = getArguments();
        fbid = mArgs.getString("my_id");
        organiser_name = mArgs.getString("organiser_name");
        organiser_fbid = mArgs.getString("organiser_fbid");

        Typeface typeface_reg = FontCache.getFont(getContext(), "sf_reg.ttf");
        Typeface typeface_bold = FontCache.getFont(getContext(), "sf_bold.ttf");


        // Refer to Ids in view
        TextView organiser = (TextView) view.findViewById(R.id.organiser_info);
        TextView event_Name = (TextView) view.findViewById(R.id.event_name);
        ImageView event_organiser_photo = (ImageView) view.findViewById(R.id.organiser_photo);
        TextView event_start_date = (TextView) view.findViewById(R.id.event_start_date);
        // TextView event_end_date = (TextView) view.findViewById(R.id.eventEndDisplay);
        TextView event_start_time = (TextView) view.findViewById(R.id.event_start_time);
        // TextView event_end_time = (TextView) view.findViewById(R.id.timeEndDisplay);
        final TextView event_location = (TextView) view.findViewById(R.id.event_loc);
        TextView eventInvitedNumber = (TextView) view.findViewById(R.id.invited_text);
        TextView rsvp_response = (TextView) view.findViewById(R.id.rsvp_response);
        View rsvp_line = view.findViewById(R.id.rsvp_line);

        mapLayout = (FrameLayout) view.findViewById(R.id.event_map);
        final LinearLayout ind_bubbles = (LinearLayout) view.findViewById(R.id.invited_circles_display);

        // Set typeface
        organiser.setTypeface(typeface_reg);
        event_Name.setTypeface(typeface_bold);
        event_start_date.setTypeface(typeface_reg);
        event_start_time.setTypeface(typeface_reg);
        eventInvitedNumber.setTypeface(typeface_reg);
        event_location.setTypeface(typeface_reg);
        rsvp_response.setTypeface(typeface_reg);

        // Set event rsvp status
        rsvp_response.setText("You have yet to response. Swipe me to respond.");

        // Get event position from user
        String org = new StringBuilder().append(organiser_name).append(" invited you").toString();

        organiser.setText(org);

        // Set up event name
        String event_name = mArgs.getString("event_name");
        event_Name.setText(event_name.toUpperCase());

        // Set up event organiser facebook photo
        Bitmap bitmap = BitmapFactory.decodeFile(Utility.getImage(organiser_fbid, getContext()).getPath());
        event_organiser_photo.setImageBitmap(bitmap);

        // Set up start date
        java.util.Date juDate = new Date(mArgs.getLong("event_start"));
        DateTime dt_s = new DateTime(juDate);
        String s_year = Integer.toString(dt_s.getYear());
        TimeConvertToText abc = new TimeConvertToText();
        String s_month = abc.ConvertMonthToText(dt_s.getMonthOfYear());
        String s_day = Integer.toString(dt_s.getDayOfMonth());

        String string1 = new StringBuilder().append(s_month).append(" ").append(s_day).append(", ").append(s_year).append(" ").toString();
        event_start_date.setText(string1);

        // Set up start time
        int s_hour = dt_s.getHourOfDay();
        int s_minute = dt_s.getMinuteOfHour();
        if (s_hour < 10) {
            start_hour = new StringBuilder().append(0).append(s_hour).toString();
        } else {
            start_hour = Integer.toString(s_hour);
        }

        if (s_minute < 10) {
            start_minute = new StringBuilder().append(0).append(s_minute).toString();
        } else {
            start_minute = Integer.toString(s_minute);
        }

        String start_time = new StringBuilder().append(start_hour).append(":").append(start_minute).toString();
        event_start_time.setText(start_time);

        // Set up location
        String location = mArgs.getString("event_location");
        if (location.equals("")) {
            event_location.setText("No location specified");
        } else {
            event_location.setText(location);
        }

        // Set up event coordinates
        Double lat = mArgs.getDouble("event_lat");
        Double lng = mArgs.getDouble("event_lng");
        latLng = new LatLng(lat,lng);

        SupportMapFragment mMapFragment = new SupportMapFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.event_map, mMapFragment).commit();
        mMapFragment.getMapAsync(EventDisplayDialog.this);

        // Set up event invitees
        ArrayList<String> invitee = mArgs.getStringArrayList("event_invitees");
        for (int i = 0; i < invitee.size(); i++) {
            if (i <= 4 || invitee.size() < 6) {
                String id = invitee.get(i);
                CreateFriendsBubble createFriendsBubble = new CreateFriendsBubble();
                View v = createFriendsBubble.create(getContext(),30, id);

                ind_bubbles.addView(v);
            }
        }

        // Set up invitees count
        if (invitee.size() == 1) {
            eventInvitedNumber.setText(
                    new StringBuilder().append(invitee.size()).append(" friend is invited").toString());
        } else {
            eventInvitedNumber.setText(
                    new StringBuilder().append(invitee.size()).append(" friends are invited").toString());
        }

        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mapLoadingLayout.setVisibility(View.GONE);
        mapLayout.setVisibility(View.VISIBLE);
        MapObjectControl control = new MapObjectControl();
        control.AddSearchedMarker(latLng, googleMap, 14);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    }
