package com.example.projecttesting;

import android.widget.TextView;

import org.w3c.dom.Text;

public class SelectedPeopleObject {

    private TextView textViewContent;
    private String name;
    private String selection_status;
    private int star_status;

    public SelectedPeopleObject() {
    }

    // Method for getting and storing textviews
    public void setTextViewContent(TextView textViewContent) {
        this.textViewContent = textViewContent;
    }

    public TextView getTextViewContent() {
        return textViewContent;
    }

    // Method for getting and storing names
    public void setName (String name){
        this.name = name;
    };

    public String getName(){
        return name;
    }

    // Method for getting and storing friends star status
    public void setFriendStar(int star_status){
        this.star_status = star_status;
    }

    public int getFriendStar(){
        return star_status;
    }

    // Method for getting selection status
    public void setSelection_status(String selection_status){
        this.selection_status= selection_status;
    }

    public String getSelection_status (){return selection_status;}
}