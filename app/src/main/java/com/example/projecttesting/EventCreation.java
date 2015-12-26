package com.example.projecttesting;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.android.gms.maps.GoogleMap;

import org.joda.time.DateTime;
import org.w3c.dom.Text;


public class EventCreation extends FragmentActivity implements OnDateSetListener, OnTimeSetListener, EventAct {

    final EventDateConvert dateConvert = new EventDateConvert();
    MaterialRefreshLayout refreshLayout;
    TextView mStartDate, mStartTime, update_status;
    EditText eventName;
    LinearLayout update_status_bg;
    AutoCompleteTextView placeInputET;
    AutoCompleteTextView peopleInputET;
    ArrayList<String> selectedPeople, selectedPeopleName;
    int minYear, minMonth, minDate, minMinute, minHour;
    RadioButton button1, button2, button3, button4;
    long minMillis, lengthMillis;
    String address;
    EventEntryItem item;
    double lat, lng;

    private static final String TAG = EventCreation.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_create);

        Bundle bundle = getIntent().getExtras();
        final User user = bundle.getParcelable("user");

        final Typeface typeface = FontCache.getFont(this, "sf_reg.ttf");

        List masterList = user.getMasterList();

        // Set refresh view for event creation
        refreshLayout = (MaterialRefreshLayout) findViewById(R.id.refresh);

        refreshLayout.setMaterialRefreshListener(
                new MaterialRefreshListener() {
                    @Override
                    public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                        // Get event name input
                        String event_name = eventName.getText().toString();

                        // Get Location
                        String location = placeInputET.getText().toString();

                        // Send stuff to server
                        Bundle event_bundle = new Bundle();
                        event_bundle.putString("EVENT_NAME", event_name);
                        event_bundle.putString("EVENT_TYPE", "test");
                        event_bundle.putString("EVENT_DETAILS", "testing");
                        event_bundle.putString("VENUE", location);
                        event_bundle.putStringArrayList("INVITEES", selectedPeople);
                        //Log.i("no fo people",Integer.toString(selectedPeople.size()));

                        String event_start_submit = dateConvert.MillisToStringForServer(minMillis);

                        // Determine event length
                        if (button1.isChecked()){
                            lengthMillis = 1000 * 60 * 30;
                        } else if (button2.isChecked()){
                            lengthMillis = 1000 * 60* 60;
                        } else if (button3.isChecked()){
                            lengthMillis = 1000 * 60 * 60 *2;
                        } else if (button4.isChecked()){
                            lengthMillis = 1000 *60;
                        }
                        String event_end_submit = dateConvert.MillisToStringForServer(minMillis + lengthMillis);
                        event_bundle.putString("DATETIME", event_start_submit);
                        event_bundle.putString("ENDTIME", event_end_submit);
                        Log.i("ENDTIME",event_end_submit);
                        event_bundle.putString("ORGANISER", user.getUserId());
                        event_bundle.putDouble("LAT", lat);
                        event_bundle.putDouble("LONG", lng);
                        event_bundle.putString("ADDRESS", address);
                        event_bundle.putBoolean("ISREPEAT",false);

                        Event event_new = new Event(event_bundle, EventCreation.this);
                        event_new.createEventAtServer();
                        update_status_bg.setVisibility(View.GONE);

                    }
                });


        //TransitionInflater inflater = TransitionInflater.from(this);
        //Transition transition = inflater.inflateTransition(R.transition.change_people_or_event_input);
        //getWindow().setSharedElementEnterTransition(transition);

        update_status = (TextView) findViewById(R.id.update_status);
        update_status.setTypeface(typeface);

        update_status_bg = (LinearLayout) findViewById(R.id.update_status_bg);

        eventName = (EditText) findViewById(R.id.eventNameInput);
        eventName.setTypeface(typeface);

        // Set the intent for people invitation
        peopleInputET = (AutoCompleteTextView) findViewById(R.id.eventPeopleAutocomplete);
        peopleInputET.setTypeface(typeface);

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
                    startActivityForResult(intent, 2);

                }
            }
        };

        peopleInputET.setOnFocusChangeListener(peopleOnClickListener);

        // Set the intent for location input
        placeInputET = (AutoCompleteTextView) findViewById(R.id.eventPlaceAutocomplete);
        placeInputET.setTypeface(typeface);
        OnFocusChangeListener locationOnClickListener = new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    Log.i("locationOnClickListener", "onFocusChange");

                    Intent intent = new Intent(EventCreation.this,
                            EventLocationInput.class);

                    startActivityForResult(intent, 1);

                }
            }

        };

        placeInputET.setOnFocusChangeListener(locationOnClickListener);

        // Setting buttons view
        button1 = (RadioButton) findViewById(R.id.end1);
        button2 = (RadioButton) findViewById(R.id.end2);
        button3 = (RadioButton) findViewById(R.id.end3);
        button4 = (RadioButton) findViewById(R.id.end4);

        button1.setTypeface(typeface);
        button2.setTypeface(typeface);
        button3.setTypeface(typeface);
        button4.setTypeface(typeface);

        // Custom time end button
        button4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(EventCreation.this);
                LayoutInflater inflater = (LayoutInflater)EventCreation.this.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.seekbardialog, (ViewGroup)findViewById(R.id.seekbar_dialog));
                SeekArc seekArc = (SeekArc) layout.findViewById(R.id.seekArc);
                final TextView seekArc_progress = (TextView) layout.findViewById(R.id.seekArc_progress);
                seekArc_progress.setTypeface(typeface);

                seekArc.setOnSeekArcChangeListener(new SeekArc.OnSeekArcChangeListener() {
                    @Override
                    public void onProgressChanged(SeekArc seekArc, int progress, boolean fromUser) {
                        int real_hour = progress + 1;
                        String display;
                        if (real_hour == 1){
                            display = "1 hour";
                        } else {
                            display = real_hour + " hours";
                        }
                        seekArc_progress.setText(display);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekArc seekArc) {
                    }
                    @Override
                    public void onStopTrackingTouch(SeekArc seekArc) {

                    }
                });

                dialog.setContentView(layout);
                dialog.show();

            }
        });

        // Defaulting the start/finish date of the event as today
        java.util.Date juDate = new Date();
        DateTime dt = new DateTime(juDate);
        minMonth = dt.getMonthOfYear();
        minYear = dt.getYear();
        minDate = dt.getDayOfMonth();
        minHour = dt.getHourOfDay();
        minMinute = dt.getMinuteOfHour();

        mStartDate = (TextView) findViewById(R.id.eventStartInput);

        String temp = dateConvert.DateStringForDisplay(minYear, minMonth, minDate);
        mStartDate.setText(temp);
        mStartDate.setTypeface(typeface);

        // Set up listener for time pickers
        mStartTime = (TextView) findViewById(R.id.timeStartInput);
        mStartTime.setTypeface(typeface);

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

        // Re-confirm the minMillis
        try {
            minMillis = dateConvert.ReturnMillis(minYear, minMonth, minDate, minHour, minMinute);
        } catch (ParseException e){
            Log.e(TAG,"Error parsing date");
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
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String temp = dateConvert.TimeStringForDisplay(hourOfDay, minute);

        minHour = hourOfDay;
        minMinute = minute;
        mStartTime.setText(temp);

        try {
            minMillis = dateConvert.ReturnMillis(minYear, minMonth, minDate, minHour, minMinute);
            //Log.i("temp better", Long.toString(minMillis));

        } catch (ParseException e) {
            Log.i("Time set parse error", "error");
        }
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear,
                          int dayOfMonth) {

        String temp = dateConvert.DateStringForDisplay(year, monthOfYear + 1, dayOfMonth);

        mStartDate.setText(temp);
        minYear = year;
        minMonth = monthOfYear + 1;
        minDate = dayOfMonth;

        try {
//            Log.i("date better work",Integer.toString(minDate));
            minMillis = dateConvert.ReturnMillis(minYear, minMonth, minDate, minHour, minMinute);
            //Log.i("temp better", Long.toString(minMillis));

        } catch (ParseException e) {
            Log.i("Date set parse error", "error");
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

        if (success) {
            refreshLayout.finishRefresh();
            Toast.makeText(EventCreation.this, "Event added successfully", Toast.LENGTH_LONG).show();
        } else {
            refreshLayout.finishRefresh();
            Toast.makeText(EventCreation.this, "Server error. Event cannot be added", Toast.LENGTH_LONG).show();
        }
        // Pass the things back to event display
        Intent intent = new Intent(EventCreation.this, EventFragment.class);
        intent.putExtra("data", item);
        intent.putExtra("requestCode", 10);
        setResult(RESULT_OK, intent);
        finish();

    }


}
