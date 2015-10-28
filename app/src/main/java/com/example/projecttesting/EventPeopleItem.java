package com.example.projecttesting;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class EventPeopleItem implements Serializable{
    private int image, position;
    private String name, selection, id;

    public EventPeopleItem(String name, String selection, int image, String id, int position) {
        //this.event_image = event_image;
        this.selection = selection;
        this.name = name;
        this.image = image;
        this.id = id;
        this.position = position;
    }

    public int getImageId() {
        return image;
    }

    public void setImageId(int image) {
        this.image = image;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSelection(String selection){this.selection = selection;}

    public String getSelection() {return selection;}

    public void setId (String id){this.id = id;}

    public String getId(){return id;}

    public void setPosition (int position){this.position = position;}

    public int getPosition() {return position;}
}