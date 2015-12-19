package com.example.projecttesting;

/**
 * Created by edmundlee on 6/20/15.
 */
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Fragment;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CalendarView.OnDateChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

public class Event extends AsyncTask<Void,Void,Void> implements Events {
    View rootView;
    String eventId;
    String eventType;
    public String dateTime;
    public String endTime;
    List<Calendar> dateTimeOpt;
    String eventName;
    String organiser;
    Boolean existingEvent;
    String eventDetails;
    String venue;
    String address;
    Location venueLoc;
    List<String> invitees;
    List<String> attendees;
    List<String> rejectees;
    List<String> pendingInvitees;
    Boolean isOptional;
    Integer status = 0;
    String eventOrganiser;
    EventAct eventhandler;
    Boolean isRepeat;
    double venueLat;
    double venueLong;
    private static final String TAG = "EventCreation";
    private static final String TAG2 = "Retrieving event";
    TextView testing = null;


    //Constructor 1 - for new events
    public Event (Bundle eventBundle, EventAct eventAct){
        existingEvent = false;
        eventhandler = eventAct;
       // rootView = inputView;
        /*
        final EditText inputEventName = (EditText)rootView.findViewById(R.id.eventName);
        final EditText inputEventType = (EditText)rootView.findViewById(R.id.eventType);
        final EditText inputEventLocation = (EditText)rootView.findViewById(R.id.eventLocation);
        final EditText inputEventDetails = (EditText)rootView.findViewById(R.id.eventDetails);
        final CalendarView inputCalendar = (CalendarView)rootView.findViewById(R.id.calendarEvent);
        final Button finButton = (Button)rootView.findViewById(R.id.eventFinishButton);
        final TimePicker timePicker1 = (TimePicker)rootView.findViewById(R.id.timePicker1);
        testing = (TextView)rootView.findViewById(R.id.testingText);
        dateTime = Calendar.getInstance();*/

        eventName = (String) eventBundle.get("EVENT_NAME");
        eventType = (String) eventBundle.get("EVENT_TYPE");
        eventDetails = (String) eventBundle.get("EVENT_DETAILS");
        venue = (String) eventBundle.get("VENUE");
        invitees = eventBundle.getStringArrayList("INVITEES");
        dateTime = (String) eventBundle.get("DATETIME");
        organiser = (String) eventBundle.get("ORGANISER");
        //stage 2 implementation
        endTime = (String) eventBundle.get("ENDTIME");
        venueLat = eventBundle.getDouble("LAT");
        venueLong = eventBundle.getDouble("LONG");
        address = (String)eventBundle.get("ADDRESS");
        isRepeat = (Boolean)eventBundle.get("ISREPEAT");

        // testing
        /*
        invitees = new ArrayList();
        invitees.add("Stephen");
        invitees.add("Edmund");
        invitees.add("Avery");
        invitees.add("Nigel");
        eventOrganiser = "Wahid";
        */
    /*
        inputCalendar.setOnDateChangeListener(new OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                dateTime.set(Calendar.YEAR, year);
                dateTime.set(Calendar.MONTH, month);
                dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        });
        timePicker1.setOnTimeChangedListener(new OnTimeChangedListener(){
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                dateTime.set(Calendar.HOUR, hourOfDay);
                dateTime.set(Calendar.MINUTE, minute);
            }
        });
        finButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                eventName = inputEventName.getText().toString();
                eventType = inputEventType.getText().toString();
                venue = inputEventLocation.getText().toString();
                eventDetails = inputEventDetails.getText().toString();
                dateTime.set(Calendar.SECOND,0);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
                //testing.setText(sdf.format(dateTime.getTime()));
                createEventAtServer();
            }
        });*/
    }

    //Constructor 2
    public Event (String eventid_in, View v){
        rootView = v;
        eventId = eventid_in;
        isOptional = false;
        existingEvent = true;
        //initialise var
        eventName = null;
        eventDetails = null;
        venue = null;
        eventType = null;
        attendees = new ArrayList<String>();
        pendingInvitees = new ArrayList<String>();
        rejectees = new ArrayList<String>();
        //this.execute();
        return;
    }

    @Override
    public void createEvent() {
        isOptional = false;
    }

    @Override
    public void createEventWithOpt() {
        isOptional = true;
    }

    @Override
    public String createEventAtServer() {
        status = 1;
        this.execute();
        return eventId;
    }

    @Override
    public void setEventId(String inputId) {
        // get eventID
        eventId = inputId;
    }

    @Override
    public void setEventType(String type) {
        eventType = type;
    }

    @Override
    public void setOrganiser(String organiser) {
        eventOrganiser = organiser;
    }


    @Override
    public void setEventName(String name) {
        eventName = name;
    }

    @Override
    public void setEventDetails(String details) {
        eventDetails = details;
    }

    @Override
    public void setEventDateTime(Calendar inputDateTime) {
     //   dateTime = 	inputDateTime;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
      //  Date tmpDate = dateTime.getTime();
       // String dateToDisplay = sdf.format(tmpDate);
        Log.i(TAG2,"1");
    }

    @Override
    public void setEventDateTimeOptions(List<Calendar> inputDateTimeOptions) {
        dateTimeOpt = inputDateTimeOptions;
    }

    @Override
    public void setEventVenue(String inputVenue, Location location) {
        venue = inputVenue;
        venueLoc = location;
    }

    @Override
    public void inviteFriends(List<String> listOfFrds) {
        invitees = listOfFrds;
    }

    @Override
    public boolean updateDB() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void sendNotifications(int typeOfNotification) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getEventid() {
        return eventId;
    }

    @Override
    public String getEventType() {
        return eventType;
    }

    @Override
    public String getEventName() {
        Log.i(TAG2,"getting event Name");
        return eventName;
    }

    @Override
    public String getEventDetails() {
        return eventDetails;
    }

    @Override
    public List<Calendar> getEventDateTimeOptions() {
        if(isOptional){
            return dateTimeOpt;
        }
        else {return null;}
    }

    @Override
    public Calendar getEventDateTime() {
        Log.i(TAG2, "getting event date time");
      //  return dateTime;
        return null;
    }

    @Override
    public String getEventVenue() {
        return venue;
    }

    @Override
    public Location getEventLoc() {
        return venueLoc;
    }

    @Override
    public List<String> getEventInvitees() {
        invitees = new ArrayList<String>();
        invitees.addAll(pendingInvitees);
        invitees.addAll(attendees);
        invitees.addAll(rejectees);
        return invitees;
    }

    @Override
    public List<String> getAttendees() {
        return attendees;
    }

    @Override
    public List<String> getRejects() {
        // TODO Auto-generated method stub
        return rejectees;
    }

    @Override
    public List<String> getPending() {
        return pendingInvitees;
    }

    @Override
    public void deleteEvent() {
        // TODO Auto-generated method stub

    }

    @Override
    public Boolean isOptional() {
        return isOptional;
    }

    @Override
    protected Void doInBackground(Void... params) {
        //check if new or existing event
        if (existingEvent){
            retrieveAnEvent();
        }
        else {
            createAnEvent();
        }
        return null;
    }

    private void createAnEvent() {
        // for event creation
        HttpURLConnection urlConnection = null;
        try {

            URL url = new URL("http://letshangout.netau.net/createevent.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            //convert data types
            //Date date = dateTime.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //convert location to lat and log (to be implemented)
            //double venueLat = venueLoc.getLatitude();
            //	double venueLong = venueLoc.getLongitude();

            //creating JSON to send to server
            JSONObject json_toSend = new JSONObject();
            json_toSend.put("eventType", eventType);
            json_toSend.put("eventName", eventName);
            json_toSend.put("eventDetails", eventDetails);
            json_toSend.put("venue", venue);
            json_toSend.put("date", dateTime);
            json_toSend.put("organiser", organiser);
            json_toSend.put("invitees", new JSONArray(invitees));
            json_toSend.put("venueLat", venueLat);
            json_toSend.put("venueLong",venueLong);
            json_toSend.put("endTime",endTime);
            json_toSend.put("address",address);
            json_toSend.put("repeat", isRepeat);

           // JSONArray json_invitees = new JSONArray(invitees);
      //      Log.i("JSONdiudiu",json_invitees.toString());
        //    json_toSend.put("invitees", json_invitees);
            Log.i("JSON INVITEE",json_toSend.toString());
            //sending out the POST
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(json_toSend.toString());
            out.flush();
            out.close();

            // read response from server
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader (new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            Log.i(TAG, sb.toString());
            eventId = sb.toString();
        }catch (Exception e){
            Log.i(TAG, "the error is " + e.toString());
        }finally {
            urlConnection.disconnect();
        }
        return;
    }
    private void retrieveAnEvent() {
        HttpURLConnection urlConnection = null;
        try {

            URL url = new URL("http://letshangout.netau.net/getevent.php");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            //convert data types
            String postParameters = "eventid=" + eventId;
            Log.i(TAG2, postParameters);
            //sending out the POST
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postParameters);
            //   out.flush();
            out.close();

            // read response from server
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader (new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }

            //shd be a json
            JSONObject json_fromServer= new JSONObject(sb.toString());

            //delineating parameters for the event
            setEventName(json_fromServer.getString("name"));
            setEventType(json_fromServer.getString("type"));
            setEventDetails(json_fromServer.getString("details"));
            setEventVenue(json_fromServer.getString("venue"), null);
            eventOrganiser = json_fromServer.getString("organiser");
            // get time of event too
            if (json_fromServer.getString("datetime").isEmpty())
            {
                //do nth
            }else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = sdf.parse(json_fromServer.getString("datetime"));
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                setEventDateTime(calendar);
            }

            // get invitees pending
            JSONArray json_pending = json_fromServer.getJSONArray("pending");
            int arrSize_p = json_pending.length();
            for (int i = 0; i < arrSize_p; ++i) {
                //get user ids
                String userid_tmp = json_pending.getString(i);
                pendingInvitees.add(userid_tmp);
            }
            // get attendees
            JSONArray json_attendees = json_fromServer.getJSONArray("attending");
            int arrSize_a = json_attendees.length();
            for (int i = 0; i < arrSize_a; ++i) {
                //get user ids
                String userid_tmp = json_attendees.getString(i);
                attendees.add(userid_tmp);
            }
            // get rejectees
            JSONArray json_rejected = json_fromServer.getJSONArray("rejected");
            int arrSize_r = json_rejected.length();
            for (int i = 0; i < arrSize_r; ++i) {
                //get user ids
                String userid_tmp = json_rejected.getString(i);
                rejectees.add(userid_tmp);
            }
            Log.i(TAG,"2");
        }catch (Exception e){
            Log.i(TAG2, "the error is " + e.toString());
        }finally {
            urlConnection.disconnect();
        }
        return;
    }

    @Override
    protected void onPostExecute(Void v) {
        Log.i(TAG,"done");
        eventhandler.handleEventCreation(true,eventId);
     //   Testing t = new Testing(this, rootView);
        //Testing testing = new Testing(this, rootView);

    }
}