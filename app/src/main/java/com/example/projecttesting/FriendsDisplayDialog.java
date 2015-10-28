package com.example.projecttesting;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.w3c.dom.Text;

import java.util.Date;

public class FriendsDisplayDialog extends DialogFragment {

    public FriendsDisplayDialog() {
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.friends_detail_display, new LinearLayout(getActivity()), false);

        Bundle bundle = getArguments();
        String username = bundle.getString("username");
        int online_time = bundle.getInt("online");
        int image = bundle.getInt("image");

        Bitmap bm = BitmapFactory.decodeResource(getResources(), image);
        RoundImage r_image = new RoundImage(bm);


        //float heightofLowerActionBar = Utility.getHeightofLowerActionBar(getActivity());
        //Log.i("height", Float.toString(heightofLowerActionBar));

        // Refer to Ids in view
        TextView friends_name = (TextView) view.findViewById(R.id.friend_detail_username);
        //TextView friends_online = (TextView) view.findViewById(R.id.friend_detail_online);
        ImageView friends_image = (ImageView) view.findViewById(R.id.friend_detail_image);

        // Get event position from user

        // Set up value
        friends_name.setText(username);
        friends_image.setImageDrawable(r_image);
        //friends_online.setText(Integer.toString(online_time));

        // Build dialog
        Dialog builder = new Dialog(getActivity());
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.setContentView(view);
        return builder;

    }

}