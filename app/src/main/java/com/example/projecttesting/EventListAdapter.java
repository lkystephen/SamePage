package com.example.projecttesting;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projecttesting.AllocateEventPhoto;

import org.joda.time.DateTime;
import org.joda.time.JodaTimePermission;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventListAdapter extends ArrayAdapter<EventTypes> {

    Context context;
    List<EventTypes> items;
    String[] name;

    public EventListAdapter(Context context, int resourceId, List<EventTypes> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder school*/
    private class ViewHolder {
        TextView event_name;
        //TextView event_invitees;
        TextView event_location;
        TextView event_date, event_time, whoInvited;
        //LinearLayout invitees_display;
        ImageView event_image, rsvp_status;
        LinearLayout bubbles;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        EventTypes rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Typeface face_r, face_b;
        face_r = FontCache.getFont(getContext(),"sf_reg.ttf");
        face_b = FontCache.getFont(getContext(),"sf_bold.ttf");

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.event_list_display, null);

            holder = new ViewHolder();
            holder.event_name = (TextView) convertView.findViewById(R.id.event_name_list_item);
            holder.event_time = (TextView) convertView.findViewById(R.id.event_time);
            holder.event_location = (TextView) convertView.findViewById(R.id.event_location_list_item);
            holder.rsvp_status = (ImageView) convertView.findViewById(R.id.rsvp_status);
            holder.whoInvited = (TextView) convertView.findViewById(R.id.who_is_going_text);
            holder.event_image = (ImageView) convertView.findViewById(R.id.event_type);
            holder.event_date = (TextView) convertView.findViewById(R.id.event_date_list_item);
            holder.bubbles = (LinearLayout) convertView.findViewById(R.id.circles_display);
            //holder.invitees_display = (LinearLayout) convertView.findViewById(R.id.invitees_dis);

            // Base on the RSVP status, for now we set all to going
            holder.rsvp_status.setImageResource(R.drawable.accept_nobackground);

            convertView.setTag(holder);

            holder.event_name.setTypeface(face_b);
            holder.event_time.setTypeface(face_r);
            holder.event_location.setTypeface(face_r);
            holder.event_date.setTypeface(face_r);
            holder.whoInvited.setTypeface(face_r);

            holder.event_name.setText(rowItem.getEventName().toUpperCase());
            holder.event_location.setText(rowItem.getEventVenue());
            //holder.event_invitees.setText(rowItem.getFriendsInvited().toString());
            long event_time = rowItem.getEventDateTime().getTimeInMillis();
            java.util.Date juDate = new Date(event_time);
            DateTime dt = new DateTime(juDate);

            int month = dt.getMonthOfYear();
            int day = dt.getDayOfMonth();
            int hour = dt.getHourOfDay();
            int minute = dt.getMinuteOfHour();
            TimeConvertToText convert = new TimeConvertToText();
            String minute2 = convert.ConvertMinuteToText(minute);
            String month2 = convert.ConvertMonthToText(month);
            String amOrPm = new String();
            String hour3 = new String();
            if (hour > 12){
                int hour2 = hour - 12;
                amOrPm = "pm";
                hour3 = Integer.toString(hour2);
            }
            if (hour < 12){
                amOrPm = "am";
                hour3 = Integer.toString(hour);
                if (hour == 0){
                    hour3 = "12";
                }
            }
            if (hour == 12){
                amOrPm = "nn";
                hour3 = Integer.toString(hour);
            }

            holder.event_date.setText(new StringBuilder().append(month2).append(" ").append(day).toString());
            holder.event_time.setText(new StringBuilder().append(hour3).append(":").append(minute2).append(" ").append(amOrPm).toString());
            //holder.event_organiser.setText(rowItem.getOrganiser());
            AllocateEventPhoto allocate = new AllocateEventPhoto();

            int allocated = allocate.EventTypeDetermine(rowItem.getEventName().toLowerCase());

            //Bitmap bitmap = BitmapFactory.decodeFile(Utility.getImage(rowItem.getOrganiser()).getPath());
            //RoundImage roundImage = new RoundImage(bitmap);
            holder.event_image.setImageResource(allocated);

            int organise_number = rowItem.getEventInvitees().size();
            for (int i = 0; i < organise_number; i++) {
                if (i <= 4 && organise_number < 6) {
                    CreateFriendsBubble createFriendsBubble = new CreateFriendsBubble();
                    View v = createFriendsBubble.create(context,18,rowItem.getEventInvitees().get(i));

                    holder.bubbles.addView(v);
                }
            }

        }
        return convertView;
    }
}

