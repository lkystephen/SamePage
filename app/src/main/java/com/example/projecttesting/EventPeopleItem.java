package com.example.projecttesting;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class EventPeopleItem implements Serializable{
    private int image, position;
    private String name, selection, id;

    public EventPeopleItem(String name, String selection, String id, int position) {
        //this.event_image = event_image;
        this.selection = selection;
        this.name = name;
        this.id = id;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelection(String selection){this.selection = selection;}

    public String getSelection() {return selection;}

    public String getId(){return id;}

    public void setPosition (int position){this.position = position;}

    public int getPosition() {return position;}
}