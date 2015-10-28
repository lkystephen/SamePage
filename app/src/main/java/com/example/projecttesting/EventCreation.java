package com.example.projecttesting;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.util.Pair;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import org.joda.time.DateTime;

public class EventCreation extends FragmentActivity implements OnDateSetListener, OnTimeSetListener, EventAct {

    TextView mStartDate, mStartTime;
    AutoCompleteTextView placeInputET;
    AutoCompleteTextView peopleInputET;
    ArrayList<String> selectedPeople, selectedPeopleName;
    int minYear, minMonth, minDate, minMinute, minHour;
    public static GoogleMap eventGoogleMap;
    long minMillis, abc;
    String address;
    EventEntryItem item;
    double lat, lng;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create);

        Bundle bundle = getIntent().getExtras();
        final User user = bundle.getParcelable("user");

        List masterList = user.getMasterList();

        // Set animation for next activity

        //TransitionInflater inflater = TransitionInflater.from(this);
        //Transition transition = inflater.inflateTransition(R.transition.change_people_or_event_input);
        //getWindow().setSharedElementEnterTransition(transition);

        // Set the intent for people invitation
        peopleInputET = (AutoCompleteTextView) findViewById(R.id.eventPeopleAutocomplete);

        OnFocusChangeListener peopleOnClickListener = new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    Intent intent = new Intent(EventCreation.this, EventPeopleInput.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("selectedName", selectedPeopleName);
                    bundle.putStringArrayList("selectedId", selectedPeople);
                    bundle.putParcelable("user", user);
                    intent.putExtras(bundle);
                    //Pair<View, String> p1 = Pair.create(v, "People");
                    //Pair<View, String> p2 = Pair.create(v, "PeopleImage");
                    //Pair<View, String> p3 = Pair.create(v, "PeopleLine");
                    //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(EventCreation.this, p1, p3);

                    //startActivityForResult(intent, 2, options.toBundle());
                    startActivityForResult(intent, 2);

                }
            }
        };

        peopleInputET.setOnFocusChangeListener(peopleOnClickListener);

        // Set the intent for location input
        placeInputET = (AutoCompleteTextView) findViewById(R.id.eventPlaceAutocomplete);
        OnFocusChangeListener locationOnClickListener = new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    Log.i("locationOnClickListener", "onFocusChange");

                    Intent intent = new Intent(EventCreation.this,
                            EventLocationInput.class);
                    //Pair<View, String> p1 = Pair.create(v, "Place");
                    //Pair<View, String> p2 = Pair.create(v, "PlaceLine");
                    //ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(EventCreation.this, p1, p2);

                    startActivityForResult(intent, 1);

                }
            }

        };

        placeInputET.setOnFocusChangeListener(locationOnClickListener);

        //SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager()
        //		.findFragmentById(R.id.eventCreateMap);

        //eventGoogleMap = supportMapFragment.getMap();
        //supportMapFragment.getView().setVisibility(View.GONE);

        // Set the more option menu and its onclick

        LinearLayout allowInvitation = (LinearLayout) findViewById(R.id.allow_Invite);
        final TextView allowInvitationResult = (TextView) findViewById(R.id.allow_Invite_result);
        allowInvitation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allowInvitationResult.getText().equals("Yes")) {
                    allowInvitationResult.setText("No");
                } else {
                    allowInvitationResult.setText("Yes");
                }
            }
        });
        final TextView moreOptionTextView = (TextView) findViewById(R.id.more_options);
        final View DividerBeforeMoreOption = findViewById(R.id.dividerBeforeMoreOption);
        final LinearLayout allowInvitationLayout = (LinearLayout) findViewById(R.id.more_options_menu);
        moreOptionTextView.setOnClickListener(new

                                                      OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {
                                                              allowInvitationLayout.setVisibility(View.VISIBLE);
                                                              DividerBeforeMoreOption.setVisibility(View.INVISIBLE);
                                                              moreOptionTextView.setVisibility(View.GONE);
                                                          }
                                                      });
        // Set spinner
        Spinner notificationTimeSpinner = (Spinner) findViewById(R.id.spinner_notify_time);

        // Defaulting the start/finish date of the event as today
        java.util.Date juDate = new Date();
        DateTime dt = new DateTime(juDate);
        minMonth = dt.getMonthOfYear();
        minYear = dt.getYear();
        minDate = dt.getDayOfMonth();
        minHour = dt.getHourOfDay();
        minMinute = dt.getMinuteOfHour();

        mStartDate = (TextView) findViewById(R.id.eventStartInput);

        final EventDateConvert dateConvert = new EventDateConvert();

        String temp = dateConvert.DateStringForDisplay(minYear, minMonth, minDate);
        mStartDate.setText(temp);

        // Set up listener for time pickers
        mStartTime = (TextView) findViewById(R.id.timeStartInput);

        minMillis = dt.getMillis();

        if (minMinute < 30 && minMinute > 0) {

            minMinute = 30;

            String timeText = dateConvert.TimeStringForDisplay(minHour, minMinute);

            mStartTime.setText(timeText);
        } else {
            minMinute = 0;
            minHour++;

            if (minHour == 24) {
                minHour = 0;

                minMillis = minMillis + (1000 * 60 * 60 * 24); //Add a day to minMillis

                java.util.Date minJuDate = new Date(minMillis);
                DateTime dt_min = new DateTime(minJuDate);
                minDate = dt_min.getDayOfMonth();
                minMonth = dt_min.getMonthOfYear();
                minYear = dt_min.getYear();

                String temp3 = dateConvert.DateStringForDisplay(minYear, minMonth, minDate);

                mStartDate.setText(temp3);

            } else {

                String temp4 = dateConvert.DateStringForDisplay(minYear, minMonth, minDate);

                mStartDate.setText(temp4);
            }
            String temp5 = dateConvert.TimeStringForDisplay(minHour, minMinute);
            mStartTime.setText(temp5);
        }


        mStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentManager ft = getSupportFragmentManager();

                DialogFragment newFragment = new TimePickerDialogFragment(EventCreation.this, minMinute, minHour);

                newFragment.show(ft, "time_dialog");
            }
        });

        // Onclicklistener for datepickers

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager ft = getSupportFragmentManager();

                DialogFragment newFragment = new DatePickerDialogFragment(
                        EventCreation.this, minMillis);

                newFragment.show(ft, "date_dialog");
            }
        });

        // Input stuff into sharedpref manager by triggering submit
        Button submit_button = (Button) findViewById(R.id.submit);

        submit_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get event name input
                EditText eventName = (EditText) findViewById(R.id.eventNameInput);
                String event_name = eventName.getText().toString();

                // Get Location
                String location = placeInputET.getText().toString();

                // Get People
                ArrayList<String> people = selectedPeople;

                // Send stuff to server
                Bundle event_bundle = new Bundle();
                event_bundle.putString("EVENT_NAME", event_name);
                event_bundle.putString("EVENT_TYPE", "test");
                event_bundle.putString("EVENT_DETAILS", "testing");
                event_bundle.putString("VENUE", location);
                event_bundle.putStringArrayList("INVITEES", selectedPeople);

                String event_start_submit = dateConvert.MillisToStringForServer(minMillis);
                String event_end_submit = dateConvert.MillisToStringForServer(minMillis + 1000 * 60 * 60 * 2);
                Log.i("this better work", event_start_submit);
                //Log.i("this end work", event_end_submit);
                event_bundle.putString("DATETIME", event_start_submit);
                event_bundle.putString("ENDTIME", event_end_submit);
                event_bundle.putString("ORGANISER", user.getUserId());
                event_bundle.putDouble("LAT", lat);
                event_bundle.putDouble("LONG", lng);
                event_bundle.putString("ADDRESS", address);

                Event event_new = new Event(event_bundle, EventCreation.this);
                event_new.createEventAtServer();


            }
        });

        // Start the sharedpref manager
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        EventDateConvert dateConvert = new EventDateConvert();
        String temp = dateConvert.TimeStringForDisplay(hourOfDay, minute);

        minHour = hourOfDay;
        minMinute = minute;
        mStartTime.setText(temp);
    }


    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {

        EventDateConvert converter = new EventDateConvert();

        String temp = converter.DateStringForDisplay(year, monthOfYear + 1, dayOfMonth);

        mStartDate.setText(temp);
        minYear = year;
        minMonth = monthOfYear + 1;
        minDate = dayOfMonth;

        try {
//            Log.i("date better work",Integer.toString(minDate));
            minMillis = converter.ReturnMillis(minYear, minMonth, minDate, minHour, minMinute);
            Log.i("temp better", Long.toString(minMillis));

        } catch (ParseException e) {
            Log.i("temp better2", "error");
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("onActivityResult", "no requestCode");

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            placeInputET.setText(data.getStringExtra("selectedLocation"));
            lat = data.getDoubleExtra("LATITUDE", 0.00);
            lng = data.getDoubleExtra("LONGITUDE", 0.00);
            address = data.getStringExtra("selectedLocation");
            //Log.i("diu lng",Double.toString(lng));

            // placeInputET.clearFocus();
            Log.i("onActivityResult", "requestCode 1");
            Log.i("succeed", placeInputET.toString());
            placeInputET.clearFocus();
        }

        if (resultCode == Activity.RESULT_CANCELED && requestCode == 1) {
            placeInputET.clearFocus();
        }

        if (resultCode == Activity.RESULT_CANCELED && requestCode == 2) {
            peopleInputET.clearFocus();
        }

        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            selectedPeople = data.getStringArrayListExtra("selectedPeopleId");
            selectedPeopleName = data.getStringArrayListExtra("selectedPeopleName");
            String temp = new String();
            if (selectedPeopleName != null) {

                for (int i = 0; i < selectedPeopleName.size(); i++) {

                    if (i + 1 != selectedPeopleName.size()) {

                        temp = new StringBuilder().append(temp).append(selectedPeopleName.get(i)).append(", ").toString();
                    } else {
                        temp = new StringBuilder().append(temp).append(selectedPeopleName.get(i)).toString();
                    }
                }

                peopleInputET.setText(temp);

            } else {

                peopleInputET.setText("No friends selected");

            }
            peopleInputET.clearFocus();
        }
    }

    @Override
    public void handleEventCreation(boolean success, String eventid) {

        // Pass the things back to event display
        Intent intent = new Intent(EventCreation.this, EventFragment.class);
        intent.putExtra("data", item);
        intent.putExtra("requestCode", 10);
        setResult(RESULT_OK, intent);
        finish();

    }
}