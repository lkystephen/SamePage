package com.example.projecttesting;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by edmundlee on 8/29/15.
 */
public class EventType implements EventTypes {

    String eventId;
    String eventType;
    Calendar dateTime;
    List<SimpleDateFormat> dateTimeOpt;
    String eventName;
    String address;
    Boolean existingEvent;
    String eventDetails;
    String venue;
    double venueLat;
    double venueLong;
    List<String> invitees;
    List<String> attendees;
    List<String> rejectees;
    List<String> pendingInvitees;
    String organiser;
    Boolean isOptional;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    int status = 0;
    private final String TAG = "EventType";
    private final String BACKDOOR = "Wessi";

    // for testing
    public EventType(String password){
        Log.i(TAG, "backdoor is open..");
        if (password.compareTo("Wessi") == 0) {
            invitees = new ArrayList<String>();
            attendees = new ArrayList<String>();
            rejectees = new ArrayList<String>();
            pendingInvitees = new ArrayList<String>();
            invitees.add("Stephen");
            invitees.add("Avery");
            invitees.add("Wahid");
            attendees.add("LKK");
            attendees.add("Nigel");
            attendees.add("Yuen");
            rejectees.add("Halim");
            rejectees.add("Rich7");

            eventId = "007";
            eventName = "testing name";
            eventDetails = "testing details";
            venue = "testing venue";
            eventType = "testing type";
            organiser = "Edmund, ofc";
            dateTime = Calendar.getInstance();
        }
    };

    public EventType (JSONObject input) {
        try {
            //initialising
            invitees = new ArrayList<String>();
            attendees = new ArrayList<String>();
            rejectees = new ArrayList<String>();
            pendingInvitees = new ArrayList<String>();
            eventId = input.getString("id");
        //    status = new Integer(input.get("status"));
            eventName = input.getString("name");
            eventDetails = input.getString("details");
            venue = input.getString("venue");
            venueLat = input.getDouble("venueLat");
            venueLong = input.getDouble("venueLong");
            eventType = input.getString("type");
            organiser = input.getString("organiser");
            address = input.getString("address");
            String date_tmp = input.getString("datetime");
            dateTime = Calendar.getInstance();
            try {
                dateTime.setTime(sdf.parse(date_tmp));
            } catch (ParseException e) {
                //Handle exception here, most of the time you will just log it.
                e.printStackTrace();
            }
            //get ppl
            JSONArray json_att = input.getJSONArray("attending");
            int arrSize_att = json_att.length();
            for (int i = 0; i < arrSize_att; ++i) {
                attendees.add(json_att.getString(i));
            }
            JSONArray json_rej = input.getJSONArray("rejected");
            int arrSize_rej = json_rej.length();
            for (int i = 0; i < arrSize_rej; ++i) {
                rejectees.add(json_rej.getString(i));
            }
            JSONArray json_pend = input.getJSONArray("pending");
            int arrSize_pend = json_pend.length();
            for (int i = 0; i < arrSize_pend; ++i) {
            //    Log.i(TAG, i+": "+json_pend.getString(i));
                pendingInvitees.add(json_pend.getString(i));
            }
            invitees.addAll(attendees);
            invitees.addAll(pendingInvitees);
            invitees.addAll(rejectees);
        } catch (JSONException e) {
            Log.i(TAG, e.toString());
        }

    }

    //constructor for Parcelable
    public EventType (Parcel in) {
        // get strings first
        String[] parcelData = new String[7];
        in.readStringArray(parcelData);
        eventId = parcelData[0];
        eventType = parcelData[1];
        eventName = parcelData[2];
        eventDetails = parcelData[3];
        venue = parcelData[4];
        organiser = parcelData[5];
        String dateTimeString = parcelData[6];
      //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateTime = Calendar.getInstance();
        try {
            dateTime.setTime(sdf.parse(dateTimeString));
        } catch (ParseException e) {
            //Handle exception here, most of the time you will just log it.
            e.printStackTrace();
        }
        invitees = new ArrayList<String>();
        attendees = new ArrayList<String>();
        rejectees = new ArrayList<String>();
        pendingInvitees = new ArrayList<String>();
        in.readStringList(invitees);
        in.readStringList(attendees);
        in.readStringList(rejectees);
        in.readStringList(pendingInvitees);
        //   Location venueLoc;

        // stage 2
        //    List<SimpleDateFormat> dateTimeOpt;
        //Boolean isOptional;
    }

    @Override
    public void setEventId(String eventId) {

    }

    @Override
    public void setEventType(String type) {

    }

    @Override
    public void setEventName(String name) {

    }

    @Override
    public void setEventDetails(String details) {

    }

    @Override
    public void setEventDateTime(Calendar dateTime) {

    }

    @Override
    public void setEventDateTimeOptions(List<SimpleDateFormat> dateOptions) {

    }

    @Override
    public void setEventVenue(String venue, Location location) {

    }

    @Override
    public void updateName(String name, EventHandler handler) {
        handler.handleEventUpdates();
    }

    @Override
    public void updateType(String type, EventHandler handler) {
        handler.handleEventUpdates();
    }

    @Override
    public void updateVenue(String venue, EventHandler handler) {
        handler.handleEventUpdates();
    }

    @Override
    public void updateDateTime(Calendar dateTime, EventHandler handler) {
        handler.handleEventUpdates();
    }

    @Override
    public void updateEventDetails(String details, EventHandler handler) {
        handler.handleEventUpdates();
    }

    @Override
    public void updateVenueLoc(Location location, EventHandler handler) {
        handler.handleEventUpdates();
    }

    @Override
    public void deleteEvent() {

    }

    @Override
    public Boolean isOptional() {
        return null;
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
        return eventName;
    }

    @Override
    public String getEventDetails() {
        return eventDetails;
    }

    @Override
    public List<SimpleDateFormat> getEventDateTimeOptions() {
        return dateTimeOpt;
    }

    @Override
    public Calendar getEventDateTime() {
        return dateTime;
    }

    @Override
    public String getEventVenue() {
        return venue;
    }

    @Override
    public double getVenueLat() {
        return venueLat;
    }

    @Override
    public double getVenueLong() {
        return venueLong;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public List<String> getEventInvitees() {
        return invitees;
    }

    @Override
    public List<String> getAttendees() {
        return attendees;
    }

    @Override
    public String getOrganiser() {
        return organiser;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String rsvp(final String userid, int status) {
        final String userId = userid;
        final int event_status = status;
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                String link = "http://letshangout.netau.net/rsvp.php";
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(link);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setRequestProperty("Accept", "application/json");

                    JSONObject json_toSend = new JSONObject();
                    json_toSend.put("userid", userId);
                    json_toSend.put("status", event_status);
                    json_toSend.put("eventid", eventId);

                    //send the POST out
                    //sending out the POST
                    PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
                    out.print(json_toSend.toString());
                    Log.i("json", json_toSend.toString());
                    out.flush();
                    out.close();

                    // read
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    Log.i("jsonFrmServer", sb.toString());
                } catch (Exception ex) {
                    Log.i("User", "Error :" + ex.getMessage());
                }
                return true;
            }

            @Override
            protected void onPostExecute(Boolean results) {
                if (results){
                    Log.i("RSVP-ing","RSVP-ed") ;
                }
                else {
                    Log.i("RSVP-ing","fuck") ;
                }
            }
        }.execute(null, null, null);
        return null;
    }

    @Override
    public List<String> getRejects() {
        return rejectees;
    }

    @Override
    public List<String> getPending() {
        return pendingInvitees;
    }
    // for testing
    public String toString() {
        String tmp = new String();
        tmp = "\nid: "+ getEventid() +"\nname: "+getEventName() +"\ntype: "+getEventType()+"\ndetails: "+getEventDetails()+"\nDate: "+getEventDateTime().getTime().toString()+"\nvenue: "+getEventVenue() + "\nattending: "+getAttendees().toString() + "\ninviteed: "+getEventInvitees().toString()+ "\npending: "+getPending().toString()+ "\nrejected: "+getRejects().toString()+"\nOrganiser: "+ getOrganiser();
        return tmp;
    }

    @Override
    public int compareTo(EventTypes anotherEvent) {
        return this.getEventDateTime().compareTo(anotherEvent.getEventDateTime()) ;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        // convert datetime
        String dateTimeString = sdf.format(dateTime.getTime());
        parcel.writeStringArray(new String[] {  this.eventId,
                                                this.eventType,
                                                this.eventName,
                                                this.eventDetails,
                                                this.venue,
                                                this.organiser,
                                                dateTimeString}
        );
        parcel.writeStringList(this.invitees);
        parcel.writeStringList(this.attendees);
        parcel.writeStringList(this.rejectees);
        parcel.writeStringList(this.pendingInvitees);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public EventType createFromParcel(Parcel in) {
            return new EventType(in);
        }

        public EventType[] newArray(int size) {
            return new EventType[size];
        }
    };

}
