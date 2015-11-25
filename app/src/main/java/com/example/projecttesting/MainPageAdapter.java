package com.example.projecttesting;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class MainPageAdapter extends ArrayAdapter<HashMap<String, Integer>> {

    Context context;
    User user;
    int total;
    Boolean organise;
    EventTypes event_info;

    public MainPageAdapter(Context context, int resourceId,
                           ArrayList<HashMap<String, Integer>> items, User user) {
        super(context, resourceId, items);
        this.context = context;
        this.user = user;

        total = items.size();
    }

    /*private view holder school*/
    private class ViewHolder {
        TextView number;
        TextView dayOrHour, event_name;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        //EventTypes event_info;
        organise = false;

        int eventId = getItem(position).get("eventId");
        for (int i = 0; i < user.getEventsOrganised().size(); i++) {
            int id = Integer.parseInt(user.getEventsOrganised().get(i).getEventid());
            if (id == eventId) {
                event_info = user.getEventsOrganised().get(i);
                organise = true;
                break;
            }
        }

        if (!organise) {
            for (int i = 0; i < user.getEventsInvited().size(); i++) {
                int id = Integer.parseInt(user.getEventsInvited().get(i).getEventid());
                if (id == eventId) {
                    event_info = user.getEventsInvited().get(i);
                    break;
                }
            }
        }

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Typeface face_r, face_b;
        face_r = FontCache.getFont(getContext(), "sf_reg.ttf");
        face_b = FontCache.getFont(getContext(), "sf_bold.ttf");

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.newsfeed_list_display, null);

            holder = new ViewHolder();
            holder.number = (TextView) convertView.findViewById(R.id.number_count);
            holder.dayOrHour = (TextView) convertView.findViewById(R.id.hourorday);
            holder.event_name = (TextView) convertView.findViewById(R.id.event_name2);

            holder.number.setTypeface(face_b);
            holder.event_name.setTypeface(face_b);
            holder.dayOrHour.setTypeface(face_r);


            // Set content value

            Calendar dateTime = event_info.getEventDateTime();
            long eventMillis = dateTime.getTimeInMillis();
            long currentMillis = System.currentTimeMillis();
            long difference = eventMillis - currentMillis;

            if (difference > 0 && difference > 1000 * 60 * 60 || // If the event is within 24 hours but not within an hour
                    difference < 1000 * 60 * 60 * 24) {
                long temp = (difference) / (1000 * 60 * 60);
                int rounded = Math.round(temp);
                if (temp > rounded) {
                    int temp1 = rounded + 1;
                    holder.number.setText(Long.toString(temp1));
                } else {
                    holder.number.setText(Integer.toString(rounded));
                }
                if (holder.number.getText().toString().equals("1")) {
                    holder.dayOrHour.setText("hour");
                } else {
                    holder.dayOrHour.setText("hours");
                }
            }

            if (difference < 0) { // If the event has started
                holder.number.setText("NOW");
                holder.dayOrHour.setVisibility(View.GONE);
            }

            if (difference > 1000 * 60 * 60 * 24) { // The event is days from now
                long temp = difference / (1000 * 60 * 60 * 24);
                int temp1 = 0;
                int rounded = Math.round(temp);
                if (temp > rounded) {
                    temp1 = rounded + 1;
                    holder.number.setText(Long.toString(temp1));
                } else {
                    holder.number.setText(Integer.toString(rounded));
                }

                if (temp1 == 1 || rounded == 1) {
                    holder.dayOrHour.setText("day");
                } else {
                    holder.dayOrHour.setText("days");
                }
            }

            if (difference > 0 && difference < 1000 * 60 * 60) { // The event is within 60 minutes
                long temp = difference / (1000 * 60);
                int rounded = Math.round(temp);
                holder.number.setText(Integer.toString(rounded));
                holder.dayOrHour.setText("min");
            }

            // Set event name
            holder.event_name.setText(event_info.getEventName().toUpperCase());

            convertView.setTag(holder);


        }
        return convertView;
    }


    public View CreateFriendsBubble(final int i, Context context) {
        LayoutInflater m = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View ind_layout = m.inflate(R.layout.event_displayed_friend_bubble, null);

        ImageView invitees_bubble = (ImageView) ind_layout.findViewById(R.id.individual_bubble2);
        int image2 = R.drawable.wilson;
        Bitmap bm = BitmapFactory.decodeResource(context.getResources(), image2);
        RoundImage displayImage = new RoundImage(bm);


        invitees_bubble.setImageDrawable(displayImage);

        return ind_layout;

    }

}

