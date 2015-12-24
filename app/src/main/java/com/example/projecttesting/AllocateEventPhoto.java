package com.example.projecttesting;

import android.app.Activity;
import android.content.Context;
import android.widget.LinearLayout;

import java.util.StringTokenizer;

public class AllocateEventPhoto {
    int resourceId;

    public int EventTypeDetermine(String event_name){

        // Create and get Image of event
        String event_name_lower_case = event_name.toLowerCase();
        StringTokenizer tokens = new StringTokenizer(event_name_lower_case, " ");
        String[] split = new String[tokens.countTokens()];
        int index = 0;
        while (tokens.hasMoreTokens()) {
            split[index] = tokens.nextToken();
            ++index;
        }

        Boolean control_boolean = false;
        int image_result = R.drawable.event;
        for (int a = 0; a < index; a++) {
            if (!control_boolean) {
                image_result = this.AllocateEventPhoto(split[a]);
                if (image_result != R.drawable.event) {
                    control_boolean = true;
                }

            }
        }

        return image_result;
    }

    public int AllocateEventPhoto(String eventTitle){

        switch (eventTitle){
            case "football":
                resourceId = R.drawable.football;
                break;
            case "soccer":
                resourceId = R.drawable.football;
                break;
            case "sports":
                resourceId = R.drawable.football;
                break;
            case "film":
                resourceId = R.drawable.movie;
                break;
            case "study":
                resourceId = R.drawable.school;
                break;
            case "class":
                resourceId = R.drawable.school;
                break;
            case "school":
                resourceId = R.drawable.school;
                break;
            case "picnic":
                resourceId = R.drawable.picnic;
                break;
            case "trekking":
                resourceId = R.drawable.trek;
                break;
            case "hiking":
                resourceId = R.drawable.trek;
                break;
            case "mountain":
                resourceId = R.drawable.trek;
                break;
            case "hill":
                resourceId = R.drawable.trek;
                break;
            case "movie":
                resourceId = R.drawable.movie;
                break;
            case "theatre":
                resourceId = R.drawable.theatre;
                break;
            case "basketball":
                resourceId = R.drawable.basketball;
                break;
            case "baseball":
                resourceId = R.drawable.baseball;
                break;
            case "bowling":
                resourceId = R.drawable.bowling;
                break;
            case "boxing":
                resourceId = R.drawable.boxing;
                break;
            case "cricket":
                resourceId = R.drawable.cricket;
                break;
            case "cycling":
                resourceId = R.drawable.cycling;
                break;
            case "bicycle":
                resourceId = R.drawable.cycling;
                break;
            case "golf":
                resourceId = R.drawable.golf;
                break;
            case "gym":
                resourceId = R.drawable.gym;
                break;
            case "exercise":
                resourceId = R.drawable.gym;
                break;
            case "handball":
                resourceId = R.drawable.handball;
                break;
            case "pingpong":
                resourceId = R.drawable.pingpong;
                break;
            case "running":
                resourceId = R.drawable.run;
                break;
            case "ski":
                resourceId = R.drawable.ski;
                break;
            case "swim":
                resourceId = R.drawable.swim;
                break;
            case "swimming":
                resourceId = R.drawable.swim;
                break;
            case "tennis":
                resourceId = R.drawable.tennis;
                break;
            case "volleyball":
                resourceId = R.drawable.volleyball;
                break;
            case "yoga":
                resourceId = R.drawable.yoga;
                break;
            case "drink":
                resourceId = R.drawable.drink;
                break;
            case "drinking":
                resourceId = R.drawable.drink;
                break;
            case "wine":
                resourceId = R.drawable.drink;
                break;
            case "beer":
                resourceId = R.drawable.beer;
                break;
            case "alcohol":
                resourceId = R.drawable.beer;
                break;
            case "dinner":
                resourceId = R.drawable.dinner;
                break;
            case "meal":
                resourceId = R.drawable.dinner;
                break;
            case "eat":
                resourceId = R.drawable.dinner;
                break;
            case "gathering":
                resourceId = R.drawable.dinner;
                break;
            case "mcdonalds":
                resourceId = R.drawable.mcdonalds;
                break;
            case "breakfast":
                resourceId = R.drawable.breakfast;
                break;
            case "brunch":
                resourceId = R.drawable.breakfast;
                break;
            case "lunch":
                resourceId = R.drawable.lunch;
                break;
            case "shopping":
                resourceId = R.drawable.shopping;
                break;
            case "shop":
                resourceId = R.drawable.shopping;
                break;
            case "bank":
                resourceId = R.drawable.bank;
                break;
            case "poor":
                resourceId = R.drawable.bank;
                break;
            case "snooker":
                resourceId = R.drawable.snooker;
                break;
            case "snook":
                resourceId = R.drawable.snooker;
                break;
            case "party":
                resourceId = R.drawable.party;
                break;
            case "musical":
                resourceId = R.drawable.musical;
                break;
            case "birthday":
                resourceId = R.drawable.birthday;
                break;
            case "concert":
                resourceId = R.drawable.concert;
                break;
            case "anniversary":
                resourceId = R.drawable.anniversary;
                break;
            case "church":
                resourceId = R.drawable.church;
                break;
            case "computer":
                resourceId = R.drawable.computer;
                break;
            case "pc":
                resourceId = R.drawable.computer;
                break;
            case "fifa":
                resourceId = R.drawable.fifa;
                break;
            case "gaming":
                resourceId = R.drawable.gaming;
                break;
            default:
                resourceId = R.drawable.event;
        }

        return resourceId;
    }
}