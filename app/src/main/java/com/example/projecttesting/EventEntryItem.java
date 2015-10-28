package com.example.projecttesting;

import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class EventEntryItem implements Serializable{
    private int event_image, event_start_hour, event_end_hour, event_start_min, event_end_min;
    private long event_time;
    private ArrayList<String> friends_invited;
    private String event_title, event_location, organiser, organiser_fbid;
    private LatLng latLng;
    //int attend_status;

    public EventEntryItem(String event_title, String event_location, long event_time, ArrayList<String> friends_invited,
                          String organiser, LatLng latLng) {
        //this.event_image = event_image;
        this.event_title = event_title;
        this.organiser = organiser;
        this.event_location = event_location;
        this.friends_invited = friends_invited;
        this.event_time = event_time;
        this.latLng = latLng;
        //this.organiser_fbid = organiser_fbid;
        //this.attend_status = attend_status;
        //this.event_start_hour = event_start_hour;
        //this.event_end_hour = event_end_hour;
        //this.event_end_min = event_end_min;
        //this.event_start_min = event_start_min;

    }



/*    public int getStartMinute() {
        return event_start_min;
    }*/

    /*public void setStartMinute(int event_start_min) {
        this.event_start_min = event_start_min;
    }*/


    /*public int getEndMinute() {
        return event_end_min;
    }*/

    /*public void setEndMinute(int event_end_min) {
        this.event_end_min = event_end_min;
    }*/

/*    public int getStartHour() {
        return event_start_hour;
    }*/

   /* public String getOrganiserFbId(){
        return organiser_fbid;
    }
*/
    public void setStartHour(int event_start_hour) {
        this.event_start_hour = event_start_hour;
    }

    public int getEndHour() {
        return event_end_hour;
    }

    public void setEndHour(int event_end_hour) {
        this.event_end_hour = event_end_hour;
    }

    public long getStartTime() {
        return event_time;
    }

    public void setStartTime(long event_time) {
        this.event_time = event_time;
    }

    //public long getEndDate() {
      //  return event_end_date;
    //}

    //public void setEndDate(long event_end_date) {
      //  this.event_end_date = event_end_date;
    //}

    public void setFriendsInvited(ArrayList<String> friends_invited) {
        this.friends_invited = friends_invited;
    }

    //public int getAttend (){
    //    return attend_status;
    //}

    public ArrayList<String> getFriendsInvited() {
        return friends_invited;
    }

    public String getEventLocation() {
        return event_location;
    }

    public void setEventLocation(String event_location) {
        this.event_location = event_location;
    }

    public String getTitle() {
        return event_title;
    }

    public void setTitle(String event_title) {
        this.event_title = event_title;
    }

    public String getOrganiser() { return organiser;}

    public void setOrganiser(String organiser){this.organiser = organiser;}

    public void setLatLng(LatLng latLng){this.latLng = latLng;}

    public LatLng getLatLng() {return latLng;}
}