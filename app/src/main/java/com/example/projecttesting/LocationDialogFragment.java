package com.example.projecttesting;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class LocationDialogFragment extends DialogFragment {

    public interface LocationDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);

        public void onDialogNegativeClick(DialogFragment dialog);
    }

    LocationDialogListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (LocationDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity cannot implement interface
            throw new ClassCastException(activity.toString()
                    + " must implement LocationDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        Typeface face_r = FontCache.getFont(getContext(), "sf_reg.ttf");

        View view = inflater.inflate(R.layout.dialog_no_location, null);

        builder.setView(view);

        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogPositiveClick(LocationDialogFragment.this);
            }
        })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(LocationDialogFragment.this);
                    }
                });

        TextView error_title = (TextView) view.findViewById(R.id.error_title);
        TextView error_no_internet_msg = (TextView) view.findViewById(R.id.error_no_location_msg);
        error_no_internet_msg.setTypeface(face_r);
        error_title.setTypeface(face_r);

        return builder.create();
    }


}

