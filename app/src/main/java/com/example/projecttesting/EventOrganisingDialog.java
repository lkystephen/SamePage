package com.example.projecttesting;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;

import java.util.Date;

public class EventOrganisingDialog extends DialogFragment implements OnMapReadyCallback {

    private SupportMapFragment fragment;
    private LatLng latLng;
    String fbid, organiser_name, organiser_fbid;
    int event_name_changed_indicator = 0;


    public EventOrganisingDialog() {
        fragment = new SupportMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = getActivity().getLayoutInflater().inflate(R.layout.event_details_display, new LinearLayout(getActivity()), false);

        View view = inflater.inflate(R.layout.event_details_organising, container, false);

        // Remove title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        String start_hour, start_minute = new String();

        Typeface normal = Typeface.createFromAsset(getActivity().getAssets(), "sf_reg.ttf");
        Typeface bold = Typeface.createFromAsset(getActivity().getAssets(), "sf_bold.ttf");
        // Get information from bundle passed from Fragment
        Bundle mArgs = getArguments();
        fbid = mArgs.getString("my_id");
        organiser_name = mArgs.getString("organiser_name");
        organiser_fbid = mArgs.getString("organiser_fbid");

        final EventEntryItem event_details = (EventEntryItem) mArgs.getSerializable("data");
        //int position = mArgs.getInt("position");

        // Get original data
        final String original_name = event_details.getTitle().toUpperCase();

        // Refer to Ids in view
        TextView organiser = (TextView) view.findViewById(R.id.organiser_info);

        // Event Name
        final EditText event_Name = (EditText) view.findViewById(R.id.event_name);
        event_Name.setTypeface(bold);
        final ImageView event_name_edit = (ImageView) view.findViewById(R.id.event_name_edit);
        final ImageView event_name_edited = (ImageView) view.findViewById(R.id.event_name_edited);
        event_name_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event_Name.setFocusableInTouchMode(true);
                event_Name.setClickable(true);
                event_Name.setEnabled(true);
                event_name_edit.setVisibility(View.GONE);
                event_name_edited.setVisibility(View.VISIBLE);
                event_Name.setTextColor(Color.RED);
            }
        });

        event_name_edited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                event_Name.setFocusableInTouchMode(false);
                event_Name.setEnabled(false);
                event_name_edit.setVisibility(View.VISIBLE);
                event_name_edited.setVisibility(View.GONE);
                String temp = event_Name.getText().toString().toUpperCase();
                Log.i("original", original_name);
                Log.i("temp2",temp);
                if (!temp.equals(original_name)){
                    event_Name.setTextColor(Color.YELLOW);
                    event_name_changed_indicator = 1;
                } else {
                    event_Name.setTextColor(Color.BLACK);
                    Log.i("changed back","yes");
                    event_name_changed_indicator = 0;
                }
            }
        });

        ImageView event_organiser_photo = (ImageView) view.findViewById(R.id.organiser_photo);
        TextView event_start_date = (TextView) view.findViewById(R.id.event_start_date);
        // TextView event_end_date = (TextView) view.findViewById(R.id.eventEndDisplay);
        TextView event_start_time = (TextView) view.findViewById(R.id.event_start_time);
        // TextView event_end_time = (TextView) view.findViewById(R.id.timeEndDisplay);
        final TextView event_location = (TextView) view.findViewById(R.id.event_loc);
        final TextView event_map_display = (TextView) view.findViewById(R.id.event_map_display);
        TextView eventInvitedNumber = (TextView) view.findViewById(R.id.invited_text);
        LinearLayout rsvp = (LinearLayout) view.findViewById(R.id.rsvp_block);
        View rsvp_line = (View) view.findViewById(R.id.rsvp_line);

        final FrameLayout mapLayout = (FrameLayout) view.findViewById(R.id.event_map);
        final LinearLayout ind_bubbles = (LinearLayout) view.findViewById(R.id.invited_circles_display);

        // Get event position from user
        String org;
        if (organiser_name.equals("myself")){
            rsvp.setVisibility(View.GONE);
            rsvp_line.setVisibility(View.GONE);
            org = "You are the organiser";
        }
        else {

            org = new StringBuilder().append(organiser_name).append(" invited you").toString();
        }
        organiser.setText(org);

        // Set up event name
        event_Name.setText(event_details.getTitle().toUpperCase());


        Bitmap bitmap = BitmapFactory.decodeFile(Utility.getImage(organiser_fbid).getPath());
        //int image = allocate.EventTypeDetermine(event_details.getTitle());

        event_organiser_photo.setImageBitmap(bitmap);

        // Set up start and end date
        Date juDate = new Date(event_details.getStartTime());
        DateTime dt_s = new DateTime(juDate);
        String s_year = Integer.toString(dt_s.getYear());
        TimeConvertToText abc = new TimeConvertToText();
        String s_month = abc.ConvertMonthToText(dt_s.getMonthOfYear());
        String s_day = Integer.toString(dt_s.getDayOfMonth());

        String string1 = new StringBuilder().append(s_month).append(" ").append(s_day).append(", ").append(s_year).append(" ").toString();
        event_start_date.setText(string1);


        // Set up start and end time

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
        if (event_details.getEventLocation() != null) {
            event_location.setText(event_details.getEventLocation());
        } else {
            event_location.setText("No location specified");
        }

        if (event_details.getEventLocation() != null || event_details.getLatLng() != null) {
            event_map_display.setVisibility(View.VISIBLE);
            event_map_display.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    event_map_display.setVisibility(View.GONE);
                    mapLayout.setVisibility(View.VISIBLE);
                    latLng = event_details.getLatLng();

                    SupportMapFragment mMapFragment = new SupportMapFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.add(R.id.event_map, mMapFragment).commit();
                    mMapFragment.getMapAsync(EventOrganisingDialog.this);
                }
            });
        }

        for (int i = 0; i < event_details.getFriendsInvited().size(); i++) {
            if (i <= 4 || event_details.getFriendsInvited().size() < 6) {
                View v = CreateFriendsBubble(i);

                ind_bubbles.addView(v);
            }
        }

        // Set up invitees count
        if (event_details.getFriendsInvited().size() == 1) {
            eventInvitedNumber.setText(
                    new StringBuilder().append(event_details.getFriendsInvited().size()).append(" friend is invited").toString());
        } else {
            eventInvitedNumber.setText(
                    new StringBuilder().append(event_details.getFriendsInvited().size()).append(" friends are invited").toString());
    }

        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //final LatLng test_QC_location = new LatLng(22.2814,114.1916);
        MapObjectControl control = new MapObjectControl();
        control.AddSearchedMarker(latLng,googleMap,14);
    }

    @Override
    public void onStart(){
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null){
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width,height);
        }
    }

    public View CreateFriendsBubble (final int i){
        LayoutInflater m = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View ind_layout = m.inflate(R.layout.event_invited_friend_bubble, null);

        ImageView invitees_bubble = (ImageView) ind_layout.findViewById(R.id.individual_bubble);
        int image2 = R.drawable.edmund;
        Bitmap bm = BitmapFactory.decodeResource(getResources(), image2);
        RoundImage displayImage = new RoundImage(bm);


        invitees_bubble.setImageDrawable(displayImage);

        return ind_layout;

    }
}
