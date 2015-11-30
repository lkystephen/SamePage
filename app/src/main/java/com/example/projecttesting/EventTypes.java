package com.example.projecttesting;

import android.location.Location;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by edmundlee on 8/29/15.
 */
public interface EventTypes extends Comparable<EventTypes>,Parcelable{
    //variables

    //For existing events, set event ID
    public void setEventId(String eventId);
    //updating a particular aspect of event
    public void setEventType(String type);
    public void setEventName(String name);
    public void setEventDetails(String details);
    //depending on organiser's pref, it can be set to one date time or three
    public void setEventDateTime(Calendar dateTime);
    public void setEventDateTimeOptions(List<SimpleDateFormat> dateOptions);
    public void setEventVenue(String venue, Location location);

    // update event details
    public void updateName (String name, EventHandler handler);
    public void updateType (String type, EventHandler handler);
    public void updateVenue (String venue, EventHandler handler);
    public void updateDateTime(Calendar dateTime, EventHandler handler);
    public void updateEventDetails(String details, EventHandler handler);
    public void updateVenueLoc(Location location, EventHandler handler);


    //delete an event
    public void deleteEvent();

    //II. Connecting with DataBas

    //III. get Event details
    //return event details
    public Boolean isOptional();
    public String getEventid();
    public String getEventType();
    public String getEventName();
    public String getEventDetails();
    public List<SimpleDateFormat> getEventDateTimeOptions();
    public Calendar getEventDateTime();
    public String getEventVenue();
    public double getVenueLat();
    public double getVenueLong();
    public int getStatus();
    public String getOrganiser();
    public String getAddress();

    // rsvp
    public String rsvp(String userid, int status);


    //return user ID of those who were invited
    public List<String> getEventInvitees();
    //return user ID of those who agree to attend
    public List<String> getAttendees();
    //return user ID of those who declines to attend
    public List<String> getRejects();
    //return user ID of those whose replies are pending
    public List<String> getPending();
}
