package com.example.projecttesting;

import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimePickerDialogFragment extends DialogFragment {

    private OnTimeSetListener mTimeSetListener;
    private TimePickerDialog timeFragment;
    private String mStatus;
    private int minHour, minMinute;


    public TimePickerDialogFragment(OnTimeSetListener callback, int aMinute, int aHour) {
        mTimeSetListener = (OnTimeSetListener) callback;
        minMinute = aMinute;
        minHour = aHour;
        // nothing to see here, move along
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {


            timeFragment = new TimePickerDialog(getActivity(),
                    mTimeSetListener, minHour, minMinute, true);

        return timeFragment;

    }

}