package com.example.projecttesting;

import android.util.Log;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EventDateConvert {

    public EventDateConvert() {

    }

    public String DateStringForDisplay(int year, int month, int date) {
        String date_string = new String();

        TimeConvertToText converter = new TimeConvertToText();
        String month2 = converter.ConvertMonthToText(month);
        date_string = new StringBuilder().append(month2).append(", ").append(date).append(", ")
                .append(year).append(" ").toString();

        return date_string;
    }

    public String TimeStringForDisplay(int hour, int minute) {
        String time_string = new String();

        TimeConvertToText converter = new TimeConvertToText();
        String minute_string = converter.ConvertMinuteToText(minute);
        String hour_string = converter.ConvertHourToText(hour);

        time_string = new StringBuilder().append(hour_string).append(":").append(minute_string).toString();

        return time_string;
    }

    public long ReturnMillis(int year, int month, int date, int hour, int minute) throws ParseException {

        //SimpleDateFormat formatter = new SimpleDateFormat(
        //        "dd.MM.yyyy, HH:mm");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:00");
        formatter.setLenient(false);

        TimeConvertToText converter = new TimeConvertToText();
        String date2 = converter.ConvertDateToPresent(date);
        String month2 = converter.ConvertMonthToServerText(month);
        String hour2 = converter.ConvertHourToText(hour);
        String min2 = converter.ConvertMinuteToText(minute);

        String time = new StringBuilder().append(year).append("-")
                .append(month2).append("-").append(date2)
                .append(" ").append(hour2).append(":").append(min2).append(":00").toString();
        Log.i("Millis parsed in method", time);
        Date temp = formatter.parse(time);
        long millis = temp.getTime();
        return millis;
    }

    public String MillisToStringForServer(long millis) {

        java.util.Date juDate = new Date(millis);
        DateTime dt = new DateTime(juDate);
        int date = dt.getDayOfMonth();
        int month = dt.getMonthOfYear();
        int year = dt.getYear();
        int hour = dt.getHourOfDay();
        int minute = dt.getMinuteOfHour();

        TimeConvertToText converter = new TimeConvertToText();
        String date2 = converter.ConvertDateToPresent(date);
        String month2 = converter.ConvertMonthToServerText(month);
        String hour2 = converter.ConvertHourToText(hour);
        String minute2 = converter.ConvertMinuteToText(minute);
        Log.i("better date2",date2);

        String temp = new StringBuilder().append(year).
                append("-").append(month2).append("-").append(date2).
                append(" ").append(hour2).append(":").append(minute2).append(":").append("00").toString();


        return temp;
    }
}