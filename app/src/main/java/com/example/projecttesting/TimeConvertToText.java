package com.example.projecttesting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class TimeConvertToText {

    private String typeOfInput, displayText;
    int minHour, minMinute, month, date;

    public TimeConvertToText() {


    }

    public String ConvertDateToPresent(int date) {
        this.date = date;

        if (date < 10) {
            switch (date) {
                case 1:
                    displayText = "01";
                    break;
                case 2:
                    displayText = "02";
                    break;
                case 3:
                    displayText = "03";
                    break;
                case 4:
                    displayText = "04";
                    break;
                case 5:
                    displayText = "05";
                    break;
                case 6:
                    displayText = "06";
                    break;
                case 7:
                    displayText = "07";
                    break;
                case 8:
                    displayText = "08";
                    break;
                case 9:
                    displayText = "09";
                    break;
            }
        } else {
            displayText = Integer.toString(date);
        }
        return displayText;
    }

    public String ConvertHourToText(int minHour) {
        this.minHour = minHour;
        if (minHour < 10) {

            switch (minHour) {

                case 0:
                    displayText = "00";
                    break;
                case 1:
                    displayText = "01";
                    break;
                case 2:
                    displayText = "02";
                    break;
                case 3:
                    displayText = "03";
                    break;
                case 4:
                    displayText = "04";
                    break;
                case 5:
                    displayText = "05";
                    break;
                case 6:
                    displayText = "06";
                    break;
                case 7:
                    displayText = "07";
                    break;
                case 8:
                    displayText = "08";
                    break;
                case 9:
                    displayText = "09";
                    break;
            }
        } else {
            displayText = Integer.toString(minHour);
        }


        return displayText;
    }

    public String ConvertMinuteToText(int minMinute) {
        this.minMinute = minMinute;

        if (minMinute < 10) {

            switch (minMinute) {

                case 0:
                    displayText = "00";
                    break;
                case 1:
                    displayText = "01";
                    break;
                case 2:
                    displayText = "02";
                    break;
                case 3:
                    displayText = "03";
                    break;
                case 4:
                    displayText = "04";
                    break;
                case 5:
                    displayText = "05";
                    break;
                case 6:
                    displayText = "06";
                    break;
                case 7:
                    displayText = "07";
                    break;
                case 8:
                    displayText = "08";
                    break;
                case 9:
                    displayText = "09";
                    break;


            }
        } else {
            displayText = Integer.toString(minMinute);
        }

        return displayText;
    }

    public String ConvertMonthToText(int month) {
        this.month = month;

        switch (month) {

            case 1:
                displayText = "Jan";
                break;
            case 2:
                displayText = "Feb";
                break;
            case 3:
                displayText = "Mar";
                break;
            case 4:
                displayText = "Apr";
                break;
            case 5:
                displayText = "May";
                break;
            case 6:
                displayText = "Jun";
                break;
            case 7:
                displayText = "Jul";
                break;
            case 8:
                displayText = "Aug";
                break;
            case 9:
                displayText = "Sept";
                break;
            case 10:
                displayText = "Oct";
                break;
            case 11:
                displayText = "Nov";
                break;
            case 12:
                displayText = "Dec";
                break;

        }

        return displayText;
    }


    public String ConvertMonthToServerText(int month) {
        this.month = month;

        if (month < 10) {

            switch (month) {

                case 1:
                    displayText = "01";
                    break;

                case 2:
                    displayText = "02";
                    break;

                case 3:
                    displayText = "03";
                    break;

                case 4:
                    displayText = "04";
                    break;

                case 5:
                    displayText = "05";
                    break;

                case 6:
                    displayText = "06";
                    break;

                case 7:
                    displayText = "07";
                    break;


                case 8:
                    displayText = "08";
                    break;

                case 9:
                    displayText = "09";
                    break;
            }

        } else {
            displayText = Integer.toString(month);
        }
        return displayText;


    }
}

;
