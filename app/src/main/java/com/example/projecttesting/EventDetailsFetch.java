package com.example.projecttesting;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventDetailsFetch {

    public ArrayList<EventEntryItem> FetchDetails (List<EventTypes> list){

        ArrayList<EventEntryItem> data = new ArrayList<>();

        int total = list.size();

        for (int i = 0; i < total; i++) {
            String event_title = list.get(i).getEventName();
            String event_location = new String();
            if (list.get(i).getEventVenue() != null) {
                event_location = list.get(i).getEventVenue().toString();
            } else {
                event_location = "No location specified";
            }
            ArrayList<String> invitees = new ArrayList<String>();
            for (int j = 0; j < list.get(i).getPending().size(); j++) {
                invitees.add(list.get(i).getPending().get(j));
            }

            Calendar datetime = list.get(i).getEventDateTime();
            long event_time = datetime.getTimeInMillis();

            String organiser = list.get(i).getOrganiser();


            double lat = list.get(i).getVenueLat();
            double lng = list.get(i).getVenueLong();

            LatLng latLng = new LatLng(lat,lng);

            EventEntryItem event = new EventEntryItem(event_title, event_location, event_time, invitees, organiser,latLng);
            data.add(event);
        }

        return data;
    }


}