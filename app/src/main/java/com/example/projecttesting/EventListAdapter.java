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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.projecttesting.AllocateEventPhoto;
import com.example.projecttesting.EventEntryItem;
import com.example.projecttesting.R;

import org.joda.time.DateTime;
import org.joda.time.JodaTimePermission;

import java.util.Date;
import java.util.List;

public class EventListAdapter extends ArrayAdapter<EventEntryItem> {

    Context context;
    List<EventEntryItem> items;
    String[] name;

    public EventListAdapter(Context context, int resourceId,
                            List<EventEntryItem> items) {
        super(context, resourceId, items);
        this.context = context;
    }

    /*private view holder school*/
    private class ViewHolder {
        TextView event_name;
        //TextView event_invitees;
        TextView event_location;
        TextView event_date, event_time;
        //LinearLayout invitees_display;
        ImageView event_image;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        EventEntryItem rowItem = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Typeface face;
        face = Typeface.createFromAsset(getContext().getAssets(), "ubuntu_regular.ttf");

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.event_list_display, null);
            LinearLayout abc = (LinearLayout) convertView.findViewById(R.id.event_display_bg);
            if (position % 2 == 1){ //odd number item
                abc.setBackgroundColor(Color.parseColor("#f0f0f0"));
            }

            holder = new ViewHolder();
            holder.event_name = (TextView) convertView.findViewById(R.id.event_name_list_item);
            holder.event_time = (TextView) convertView.findViewById(R.id.event_time);
            holder.event_location = (TextView) convertView.findViewById(R.id.event_location_list_item);

            //holder.event_invitees = (TextView) convertView.findViewById(R.id.friends_invited_list_item);

            //holder.event_organiser = (TextView) convertView.findViewById(R.id.organiser);

            holder.event_image = (ImageView) convertView.findViewById(R.id.event_type);
            holder.event_date = (TextView) convertView.findViewById(R.id.event_date_list_item);
            //holder.invitees_display = (LinearLayout) convertView.findViewById(R.id.invitees_dis);

            convertView.setTag(holder);

            holder.event_name.setText(rowItem.getTitle().toUpperCase());
            holder.event_location.setText(rowItem.getEventLocation());
            //holder.event_invitees.setText(rowItem.getFriendsInvited().toString());
            long event_time = rowItem.getStartTime();
            java.util.Date juDate = new Date();
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

            int allocated = allocate.EventTypeDetermine(rowItem.getTitle().toLowerCase());

            //Bitmap bitmap = BitmapFactory.decodeFile(Utility.getImage(rowItem.getOrganiser()).getPath());
            //RoundImage roundImage = new RoundImage(bitmap);
            holder.event_image.setImageResource(allocated);


            /*for (int i = 0; i < rowItem.getFriendsInvited().size(); i++) {
                if (i <= 4 || rowItem.getFriendsInvited().size() < 6) {
                    View v = CreateFriendsBubble(i,context);

                    holder.invitees_display.addView(v);
                }
            }*/

        }
        return convertView;
    }



    public View CreateFriendsBubble (final int i, Context context){
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
