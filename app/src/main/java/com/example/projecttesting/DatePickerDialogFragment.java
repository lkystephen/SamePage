package com.example.projecttesting;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import org.joda.time.DateTime;

public class DatePickerDialogFragment extends DialogFragment {

    private OnDateSetListener mDateSetListener;
    private DatePickerDialog dateFragment;
    private String mStatus;
    private int minYear, minMonth, minDate;
    private long minMillis, maxMillis;

    public DatePickerDialogFragment() {
        // nothing to see here, move along
    }

    public DatePickerDialogFragment(OnDateSetListener callback, long minMillis) {
        mDateSetListener = (OnDateSetListener) callback;

        this.minMillis = minMillis;
        //this.maxMillis = maxMillis;
        //mStatus = status;

        java.util.Date juDate_min = new Date(minMillis);
        //java.util.Date juDate_max = new Date(maxMillis);
        DateTime dt_min = new DateTime(juDate_min);
      //  DateTime dt_max = new DateTime(juDate_max);

        minDate = dt_min.getDayOfMonth();
    //    maxDate = dt_max.getDayOfMonth();

        minMonth = dt_min.getMonthOfYear();
  //      maxMonth = dt_max.getMonthOfYear();

        minYear = dt_min.getYear();
//        maxYear = dt_max.getYear();

    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {


        dateFragment = new DatePickerDialog(getActivity(),
                mDateSetListener, minYear, minMonth - 1, minDate);
        //dateFragment.getDatePicker().setMinDate(minMillis);
        return dateFragment;

    }

}