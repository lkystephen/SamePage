package com.example.projecttesting;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.MissingFormatArgumentException;

public class EventOrganisingDialog extends DialogFragment implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private SupportMapFragment fragment;
    private LatLng latLng;
    // EventEntryItem event_details;
    long event_millis;
    String fbid, organiser_name, organiser_fbid;
    int event_name_changed_indicator = 0;
    int event_time_changed_indicator = 0;
    int hour, minute, year, month, day;
    TextView event_start_date, event_start_time;
    ImageView event_date_edit;


    public EventOrganisingDialog() {
        fragment = new SupportMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = getActivity().getLayoutInflater().inflate(R.layout.event_details_display, new LinearLayout(getActivity()), false);

        View view = inflater.inflate(R.layout.event_details_organising, container, false);

        // Get fonts
        Typeface normal = FontCache.getFont(getContext(), "sf_reg.ttf");
        Typeface bold = FontCache.getFont(getContext(), "sf_bold.ttf");


        // Remove title
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        //String start_hour, start_minute = new String();

        // Get information from bundle passed from Fragment
        final Bundle mArgs = getArguments();
        fbid = mArgs.getString("my_id");
        organiser_name = mArgs.getString("organiser_name");
        organiser_fbid = mArgs.getString("organiser_fbid");

        //event_details = (EventEntryItem) mArgs.getSerializable("data");
        //int position = mArgs.getInt("position");

        // Get original data
        final String original_name = mArgs.getString("event_name").toUpperCase();

        // Refer to Ids in view
        TextView organiser = (TextView) view.findViewById(R.id.organiser_info);
        organiser.setTypeface(normal);

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
                Log.i("temp2", temp);
                checkAmendment();
                if (!temp.equals(original_name)) {
                    event_Name.setTextColor(Color.YELLOW);
                    event_name_changed_indicator = 1;
                } else {
                    event_Name.setTextColor(Color.BLACK);
                    Log.i("changed back", "yes");
                    event_name_changed_indicator = 0;
                }
            }
        });

        ImageView event_organiser_photo = (ImageView) view.findViewById(R.id.organiser_photo);
        event_start_date = (TextView) view.findViewById(R.id.event_start_date);
        event_start_date.setTypeface(normal);

        event_date_edit = (ImageView) view.findViewById(R.id.event_date_edit);
        // TextView event_end_date = (TextView) view.findViewById(R.id.eventEndDisplay);
        event_start_time = (TextView) view.findViewById(R.id.event_start_time);
        event_start_time.setTypeface(normal);
        // TextView event_end_time = (TextView) view.findViewById(R.id.timeEndDisplay);
        final TextView event_location = (TextView) view.findViewById(R.id.event_loc);
        event_location.setTypeface(normal);
        //final TextView event_map_display = (TextView) view.findViewById(R.id.event_map_display);
        TextView eventInvitedNumber = (TextView) view.findViewById(R.id.invited_text);
        eventInvitedNumber.setTypeface(normal);

        final FrameLayout mapLayout = (FrameLayout) view.findViewById(R.id.event_map);
        final LinearLayout ind_bubbles = (LinearLayout) view.findViewById(R.id.invited_circles_display);

        // Get event position from user
        String org;

        organiser.setText("You are the organiser");

        // Set up event name
        event_Name.setText(mArgs.getString("event_name").toUpperCase());

        Bitmap bitmap = BitmapFactory.decodeFile(Utility.getImage(organiser_fbid).getPath());
        //int image = allocate.EventTypeDetermine(event_details.getTitle());

        event_organiser_photo.setImageBitmap(bitmap);

        // Set up start and end date
        event_millis = mArgs.getLong("event_start");
        Date juDate = new Date(event_millis);
        DateTime dt_s = new DateTime(juDate);
        year = dt_s.getYear();
        String s_year = Integer.toString(year);
        TimeConvertToText abc = new TimeConvertToText();
        month = dt_s.getMonthOfYear();
        String s_month = abc.ConvertMonthToText(month);
        day = dt_s.getDayOfMonth();
        String s_day = Integer.toString(day);

        String string1 = new StringBuilder().append(s_month).append(" ").append(s_day).append(", ").append(s_year).append(" ").toString();
        event_start_date.setText(string1);

        // Set up onclicklistener for changing event date
        event_date_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager ft = getActivity().getSupportFragmentManager();

                DialogFragment newFragment = new DatePickerDialogFragment(EventOrganisingDialog.this, event_millis);

                newFragment.show(ft, "date_dialog");
            }
        });


        // Set up start and end time
        hour = dt_s.getHourOfDay();
        minute = dt_s.getMinuteOfHour();
        EventDateConvert dateConvert = new EventDateConvert();
        String temp = dateConvert.TimeStringForDisplay(hour, minute);
        event_start_time.setText(temp);


        // Set up location
        if (mArgs.getString("event_location") != null) {
            event_location.setText(mArgs.getString("event_location"));
        } else {
            event_location.setText("No location specified");
        }

        // Set up Latlng
        Double lat = mArgs.getDouble("event_lat");
        Log.i("GET", Double.toString(lat));
        Double lng = mArgs.getDouble("event_lng");
        latLng = new LatLng(lat, lng);


        SupportMapFragment mMapFragment = new SupportMapFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.event_map, mMapFragment).commit();
        mMapFragment.getMapAsync(EventOrganisingDialog.this);
        ArrayList<String> invitees = mArgs.getStringArrayList("event_invitees");

        for (int i = 0; i < invitees.size(); i++) {
            if (i <= 4 || invitees.size() < 6) {
                CreateFriendsBubble createFriendsBubble = new CreateFriendsBubble();
                View v = createFriendsBubble.create(getContext(), 30, invitees.get(i));
                ind_bubbles.addView(v);
            }
        }

        // Set up invitees count
        if (invitees.size() == 1) {
            eventInvitedNumber.setText(
                    new StringBuilder().append(invitees.size()).append(" friend is invited").toString());
        } else {
            eventInvitedNumber.setText(
                    new StringBuilder().append(invitees.size()).append(" friends are invited").toString());
        }

        return view;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

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

    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {

        TimeConvertToText abc = new TimeConvertToText();
        String tMonth = abc.ConvertMonthToText(monthOfYear + 1);

        String string1 = new StringBuilder().append(tMonth).append(" ").append(dayOfMonth).append(", ").append(year).append(" ").toString();
        event_start_date.setText(string1);

        this.year = year;
        this.month = monthOfYear + 1;
        this.day = dayOfMonth;

        // Check if time / date has changed
        EventDateConvert dateConvert = new EventDateConvert();
        long mMillis = 0;
        try {
            mMillis = dateConvert.ReturnMillis(year, month, day, hour, minute);
        } catch (ParseException e) {
            Log.e("ParseException", e.getMessage());
        }

        if (event_millis != mMillis) {
            event_time_changed_indicator = 1;
        } else {
            event_time_changed_indicator = 0;
        }
        checkAmendment();
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        EventDateConvert dateConvert = new EventDateConvert();
        String temp = dateConvert.TimeStringForDisplay(hourOfDay, minute);

        this.hour = hourOfDay;
        this.minute = minute;

        // Check if time / date has changed
        long mMillis = 0;
        try {
            mMillis = dateConvert.ReturnMillis(year, month, day, hour, minute);
        } catch (ParseException e) {
            Log.e("ParseException", e.getMessage());
        }

        if (event_millis != mMillis) {
            event_time_changed_indicator = 1;
        } else {
            event_time_changed_indicator = 0;
        }

        checkAmendment();
        event_start_time.setText(temp);

    }

    public boolean checkAmendment() {
        boolean amend = false;
        int sum = event_name_changed_indicator + event_time_changed_indicator;
        if (sum == 0) {
            amend = true;
        } else
            amend = false;
        return amend;
    }
}
