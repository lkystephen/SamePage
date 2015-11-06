package com.example.projecttesting;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ConstructNewsFeedItem {
    public User user;

    public ConstructNewsFeedItem(User user) {
        this.user = user;

    }

    public ArrayList<String> getNewsFeed() {
        ArrayList<String> result = new ArrayList<>();
        return result;
    }


    // This determines the max number of newsfeed to be presented
    public ArrayList<HashMap<String, Integer>> setEventsFeedPriority() {

        int numberOfEventsOrganized = user.getEventsOrganised().size();
        int numberOfEventsInvited = user.getEventsInvited().size();
        ArrayList<HashMap<String, Integer>> list = new ArrayList<>();

        long currentMillis = System.currentTimeMillis();

        for (int i = 0; i < numberOfEventsOrganized; i++) {
            HashMap<String, Integer> scoreHashMap = new HashMap<>();
            ;
            // Retrieve event in millis
            Calendar datetime = user.getEventsOrganised().get(i).getEventDateTime();
            long event_time = datetime.getTimeInMillis();
            Log.i("time",Long.toString(event_time));
            int eventId = Integer.parseInt(user.getEventsOrganised().get(i).getEventid());

            int score = SetScore(event_time);

            // Find out if the event is happening, they are given the highest priority, baselined 100 points
            /*long event_length = 1000 * 60 * 60 * 2; // Assume 2 hours long
            if (currentMillis - event_time < event_length ) {
                score = Math.round(150 + // Add 150 base first
                        (event_time - currentMillis) / 60000); // Prioritize based on the event proximity by minutes
                scoreHashMap.put("eventId",eventId);
                scoreHashMap.put("score", score);
            }*/

            // Find out if the event is within alert threshold (going to start soon)
            /*long thresholdInMillis = 1000 * 60 * 45;
            if (event_time - currentMillis < thresholdInMillis){
                score = Math.round(50 // Add 50 base first
                 +  45 - ((event_time - currentMillis) / 60000)); // Prioritize based on event proximity, closer it is, higher the score
                scoreHashMap.put("eventId",eventId);
                scoreHashMap.put("score", score);
            }*/

            // Prioritize other events
            /*if (event_time - currentMillis > thresholdInMillis) {

                score = Math.round(45 - (event_time - currentMillis) / 60000);
                scoreHashMap.put("eventId",eventId);
                scoreHashMap.put("score", score);
            }*/
            scoreHashMap.put("eventId", eventId);
            scoreHashMap.put("score", score);
            list.add(scoreHashMap);
        }

        for (int i = 0; i < numberOfEventsInvited; i++) {

            HashMap<String, Integer> scoreHashMap = new HashMap<>();

            // Retrieve event in millis
            Calendar datetime = user.getEventsInvited().get(i).getEventDateTime();
            long event_time = datetime.getTimeInMillis();
            int eventId = Integer.parseInt(user.getEventsInvited().get(i).getEventid());
            int score = SetScore(event_time);
/*
            // Find out if the event is happening, they are given the highest priority, baselined 100 points
            /*long event_length = 1000 * 60 * 60 * 2; // Assume 2 hours long
            if (currentMillis - event_time < event_length ) {
                score = Math.round(150 + // Add 150 base first
                        (event_time - currentMillis) / 60000); // Prioritize based on the event proximity by minutes
                scoreHashMap.put("eventId",eventId);
                scoreHashMap.put("score", score);
            }

            // Find out if the event is within alert threshold (going to start soon)
            long thresholdInMillis = 1000 * 60 * 45;
            if (event_time - currentMillis < thresholdInMillis){
                score = Math.round(50 // Add 50 base first
                        +  45 - ((event_time - currentMillis) / 60000)); // Prioritize based on event proximity, closer it is, higher the score
                scoreHashMap.put("eventId",eventId);
                scoreHashMap.put("score", score);
            }

            // Prioritize other events
            if (event_time - currentMillis > thresholdInMillis) {
                score = Math.round(45 - (event_time - currentMillis) / 60000);
                scoreHashMap.put("eventId",eventId);
                scoreHashMap.put("score", score);
            }*/
            scoreHashMap.put("eventId", eventId);
            scoreHashMap.put("score", score);
            list.add(scoreHashMap);
        }

        Collections.sort(list, new mComparator());

        return list;
    }

    public int SetScore(long eventStart) {

        int score_final = Math.round(eventStart/10000000);
        Log.i("score",Integer.toString(score_final));

        return score_final;
    }


    public class mComparator implements Comparator<HashMap<String, Integer>> {
        @Override
        public int compare(HashMap<String, Integer> o1, HashMap<String, Integer> o2) {

            Integer firstValue = o1.get("score");
            Integer secondValue = o2.get("score");


            return firstValue.compareTo(secondValue);

        }
    }

}