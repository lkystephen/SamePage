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
        TextView event_invitees;
        TextView event_location;
        TextView event_date;
        LinearLayout invitees_display;
        ImageView organiser_image;
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

            holder.event_location = (TextView) convertView.findViewById(R.id.event_location_list_item);

            //holder.event_invitees = (TextView) convertView.findViewById(R.id.friends_invited_list_item);

            //holder.event_organiser = (TextView) convertView.findViewById(R.id.organiser);

            holder.organiser_image = (ImageView) convertView.findViewById(R.id.organiser_image);
            holder.event_date = (TextView) convertView.findViewById(R.id.event_date_list_item);
            holder.invitees_display = (LinearLayout) convertView.findViewById(R.id.invitees_dis);

            convertView.setTag(holder);

            holder.event_name.setText(rowItem.getTitle().toUpperCase());
            holder.event_location.setText(rowItem.getEventLocation());
            //holder.event_invitees.setText(rowItem.getFriendsInvited().toString());
            long event_time = rowItem.getStartTime();
            EventDateConvert converter = new EventDateConvert();
            String temp = converter.MillisToStringForServer(event_time);
            String temp2 = temp.substring(0,temp.length()-3);
            holder.event_date.setText(temp2);
            //holder.event_organiser.setText(rowItem.getOrganiser());
            //AllocateEventPhoto allocate = new AllocateEventPhoto();

            Bitmap bitmap = BitmapFactory.decodeFile(Utility.getImage(rowItem.getOrganiser()).getPath());

            RoundImage roundImage = new RoundImage(bitmap);

            holder.organiser_image.setImageDrawable(roundImage);


            for (int i = 0; i < rowItem.getFriendsInvited().size(); i++) {
                if (i <= 4 || rowItem.getFriendsInvited().size() < 6) {
                    View v = CreateFriendsBubble(i,context);

                    holder.invitees_display.addView(v);
                }
            }

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

