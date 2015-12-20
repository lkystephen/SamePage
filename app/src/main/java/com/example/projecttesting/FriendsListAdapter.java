package com.example.projecttesting;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;

import org.w3c.dom.Text;

public class FriendsListAdapter extends ArrayAdapter<FriendsRowItem> implements Filterable {

    Context context;
    private ArrayList<FriendsRowItem> originalData = null;
    private ArrayList<FriendsRowItem> filteredData = null;
    private CustomFilter filter;

    public FriendsListAdapter(Context context, int resourceId,
                              ArrayList<FriendsRowItem> items) {
        super(context, resourceId, items);
        this.context = context;

        this.originalData = new ArrayList<>();
        this.filteredData = new ArrayList<>();
        this.originalData.addAll(items);
        this.filteredData.addAll(items);
        this.filter = new CustomFilter();
    }

    /*private view holder school*/
    private class ViewHolder {
        TextView name, last_update, distance;
        ImageView image;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Typeface face_r, face_b;
        face_r = FontCache.getFont(getContext(),"sf_reg.ttf");
        face_b = FontCache.getFont(getContext(),"sf_bold.ttf");

        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.friendslistdisplay, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.friends_display_list);
            holder.last_update = (TextView) convertView.findViewById(R.id.friends_display_last_update);
            holder.distance = (TextView) convertView.findViewById(R.id.friends_display_loc);
            holder.image = (ImageView) convertView.findViewById(R.id.display_image);

            holder.name.setTypeface(face_b);
            holder.distance.setTypeface(face_r);
            holder.last_update.setTypeface(face_r);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        FriendsRowItem rowItem = filteredData.get(position);

        holder.name.setText(rowItem.getName());

        if (!rowItem.getTimeDifference().equals("NULL")){
            String time = rowItem.getTimeDifference();
            int temp = Integer.parseInt(time);
            // time is already in minutes
            if (temp < 60){
                holder.last_update.setText(Integer.toString(temp) + " mins ago");
            } else {
                if (temp > 60 && temp < 1440){
                    int temp3 = Math.round(temp / 60);
                    holder.last_update.setText(Integer.toString(temp3) + " hours ago");
                } else if (temp > 1440 && temp < 525600){
                    int temp4 = Math.round(temp / 1440);
                    holder.last_update.setText(Integer.toString(temp4) + " days ago");
                } else if (temp > 525600){
                    int temp5 = Math.round(temp / 525600);
                    holder.last_update.setText(Integer.toString(temp5) + " years ago");
                }
            }
        } else
        holder.last_update.setText("Unknown");

        // Set distance
        if (!rowItem.getDistance().equals("NULL")){
            int temp = Integer.parseInt(rowItem.getDistance());
            if (temp < 1000){
                holder.distance.setText(rowItem.getDistance() + " m away");
            } else{
                int temp2 = Math.round(temp / 1000);
                holder.distance.setText(Integer.toString(temp2) + " km away");
            }
        } else
        holder.distance.setText("Unknown");

        // Set image
        String fbid = rowItem.getFbId();

        if (fbid != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(Utility.getImage(fbid, context).getPath());
            //RoundImage roundImage = new RoundImage(bitmap);

            holder.image.setImageBitmap(bitmap);
        } else {
            holder.image.setImageResource(R.drawable.fb_display_male);
        }


        return convertView;
    }


    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            String input = constraint.toString().toLowerCase();

            final ArrayList<FriendsRowItem> list = originalData;

            int count = list.size();


            if (input != null && input.length() > 0) {
                ArrayList<FriendsRowItem> filtered = new ArrayList<FriendsRowItem>();

                for (int i = 0; i < count; i++) {
                    FriendsRowItem row = list.get(i);
                    if (row.getName().toLowerCase().contains(constraint)) {
                        filtered.add(row);
                    }
                }
                results.count = filtered.size();
                results.values = filtered;

            } else {
                synchronized (this) {
                    results.values = originalData;
                    results.count = originalData.size();
                }
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<FriendsRowItem>) results.values;
            Log.i("fin", Integer.toString(filteredData.size()));
            notifyDataSetChanged();
            //clear();
            /*int count = filtered.size();

            for (int i = 0; i < count; i++) {
                add(filtered.get(i));
                notifyDataSetInvalidated();
            }*/

        }
    }


    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new CustomFilter();
        }

        return filter;
    }

    @Override
    public int getCount() {
        return filteredData.size();
    }

    @Override
    public FriendsRowItem getItem(int position) {
        return filteredData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}