package com.example.projecttesting;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ConstructNewsFeedItem implements Serializable {
    public User user;

    public ConstructNewsFeedItem(User user) {
        this.user = user;

    }

    public ArrayList<String> getNewsFeed() {
        ArrayList<String> result = new ArrayList<>();
        return result;
    }


    // This determines the max number of newsfeed to be presented
    public int getNumberOfNewsFeed() {
        int number = 0;

        int numberOfEventsOrganized = user.getEventsOrganised().size();
        int numberOfEventsAttending = user.getEventsAttending().size();

        long currentMillis = System.currentTimeMillis();

        for (int i = 0; i < numberOfEventsOrganized; i++){
            // Retrieve event in millis
            Calendar datetime = user.getEventsOrganised().get(i).getEventDateTime();
            long event_time = datetime.getTimeInMillis();

            // Find out if the event is within alert threshold (going to start soon)
            long thresholdInMillis = 1000 * 60 * 45;
            if (event_time - currentMillis < thresholdInMillis){
                number = number + 1;
            }

            // Find out if the event is on-going
            long event_length = 1000 * 60 * 120; // Assume 2 hours long
            if (currentMillis - event_time < event_length ) {
                number = number + 1;
            }
        }

        return number;
    }

}