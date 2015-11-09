package com.example.projecttesting;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.pkmmte.view.CircularImageView;

public class CreateFriendsBubble{

    public View create(Context context, int dp, String id) {
        LayoutInflater m = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View ind_layout = m.inflate(R.layout.event_invited_friend_bubble, null);

        final float scale = context.getResources().getDisplayMetrics().density;
        int pixels = (int) (dp* scale + 0.5f);

        CircularImageView invitees_bubble = (CircularImageView) ind_layout.findViewById(R.id.individual_bubble);
        invitees_bubble.setLayoutParams(new ActionBar.LayoutParams(pixels, pixels));
        //int image2 = R.drawable.edmund;
        Bitmap bm = BitmapFactory.decodeFile(Utility.getImage(id).getPath());
        //RoundImage displayImage = new RoundImage(bm);


        invitees_bubble.setImageBitmap(bm);

        return ind_layout;

    }

}