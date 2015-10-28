package com.example.projecttesting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class EventPeopleInput extends ActionBarActivity {

    ListView list;
    public int[] friends_image = {R.drawable.edmund, R.drawable.wilson};

    Activity mActivity;
    TextView submitButton;
    AutoCompleteTextView peopleAutocomplete;
    ImageView imageStar;
    ArrayList<EventPeopleItem> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_people_input);

        ArrayList<EventPeopleItem> selected_data = new ArrayList<>();

        Typeface face;
        face = Typeface.createFromAsset(getAssets(), "ubuntu_regular.ttf");

        mActivity = this;
        final LayoutInflater mInflater = (LayoutInflater) mActivity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        Bundle bundle = getIntent().getExtras();
        User user = bundle.getParcelable("user");
        ArrayList<String> selectedStatus = bundle.getStringArrayList("selectedId");

        // Set up data
        List<OtherUser> master_friends = user.getMasterList();

        for (int i = 0; i < master_friends.size(); i++) {

            String name = master_friends.get(i).username;
            String id = master_friends.get(i).fbid;
            int image = R.drawable.edmund;
            String select = "N";

            if (selectedStatus != null) {

                for (int j = 0; j < selectedStatus.size(); j++) {

                    if (selectedStatus.get(j).equals(id)) {
                        select = "Y";
                    }
                }

            }
            EventPeopleItem temp = new EventPeopleItem(name, select, image, id, i);
            data.add(temp);
        }

        ArrayList<EventPeopleItem> listForAdapter = new ArrayList<>(data.size());
        for (int i = 0; i < data.size(); i++){
            EventPeopleItem a = data.get(i);
            listForAdapter.add(a);
        }

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getSelection().equals("Y")) {
                selected_data.add(data.get(i));
            }
        }

        Log.i("instantiate data", Integer.toString(data.size()));

        list = (ListView) findViewById(R.id.friends_selected_list);

        if (selectedStatus != null) {
            list.setVisibility(View.VISIBLE);

            SelectedFriendAdapter adapter_select = new SelectedFriendAdapter(this, R.layout.event_list_display, selected_data
            );
            list.setAdapter(adapter_select);

        } else {
            list.setVisibility(View.GONE);
        }

        list.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View view, int i, long l) {
                EventPeopleItem b = (EventPeopleItem) a.getAdapter().getItem(i);
                Log.i("position", Integer.toString(b.getPosition()));
                data.get(b.getPosition()).setSelection("N");

                ArrayList<EventPeopleItem> c = new ArrayList<>();
                for (int k = 0; k < data.size(); k++) {
                    if (data.get(k).getSelection().equals("Y")) {
                        c.add(data.get(k));
                    }
                }

                SelectedFriendAdapter lala = new SelectedFriendAdapter(EventPeopleInput.this,
                        R.layout.event_list_display, c);

                list.setAdapter(lala);
                if (c != null) {
                    list.setVisibility(View.VISIBLE);
                    lala.notifyDataSetChanged();
                } else {
                    list.setVisibility(View.GONE);
                    lala.notifyDataSetChanged();
                }
            }
        });

        // Setting up friends input Autocomplete
        peopleAutocomplete = (AutoCompleteTextView) findViewById(R.id.eventPeopleAutocomplete);
        peopleAutocomplete.setTypeface(face);

        // Remove the background shadow of autocomplete
        peopleAutocomplete.setDropDownBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.eventautocomplete_dropdownstyle));

        SelectingFriendsAdapter adapter = new SelectingFriendsAdapter(this, R.layout.event_people_list_item, listForAdapter);

        peopleAutocomplete.setAdapter(adapter);
        peopleAutocomplete.setOnItemClickListener(
                new OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                        ArrayList<EventPeopleItem> selection = new ArrayList<>();
                        EventPeopleItem temp = (EventPeopleItem) parent.getAdapter().getItem(i);

                        int real = temp.getPosition();
                        if (data.get(real).getSelection().equals("N")) {
                            data.get(real).setSelection("Y");
                        } else {
                            data.get(real).setSelection("N");
                        }


                        for (int j = 0; j < data.size(); j++) {
                            if (data.get(j).getSelection().equals("Y")) {

                                selection.add(data.get(j));
                            }
                        }
                        view.clearFocus();

                        SelectedFriendAdapter refresh = new SelectedFriendAdapter(EventPeopleInput.this,
                                R.layout.event_list_display, selection);

                        peopleAutocomplete.setText("");

                        list.setAdapter(refresh);
                        list.setVisibility(View.VISIBLE);
                        refresh.notifyDataSetChanged();

                        // Hide Keyboard after making selection
                        InputMethodManager in = (InputMethodManager) mActivity
                                .getApplicationContext().getSystemService(
                                        Context.INPUT_METHOD_SERVICE);
                        in.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
                    }
                }
        );

        submitButton = (TextView) findViewById(R.id.tempButton);

        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Extract the names selected for display after finishing this activity
                        TextView assd = (TextView) view.findViewById(R.id.peopleName);

                        ArrayList<String> selectedId = new ArrayList<String>();
                        ArrayList<String> selectedName = new ArrayList<>();

                        for (int i = 0; i < data.size(); i++) {
                            if (data.get(i).getSelection().equals("Y")) {
                                selectedId.add(data.get(i).getId());
                                selectedName.add(data.get(i).getName());

                            }
                        }

                        Intent i = new Intent(view.getContext(), EventCreation.class);
                        i.putExtra("selectedPeopleId", selectedId);
                        i.putExtra("selectedPeopleName", selectedName);
                        i.putExtra("requestCode", 2);
                        ((Activity) view.getContext()).setResult(RESULT_OK, i);

                        ((Activity) view.getContext()).finish();
                    }
                }

        );

    }


    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent intent = new Intent(EventPeopleInput.this, EventCreation.class);
        intent.putExtra("requestCode", 2);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public class SelectedFriendAdapter extends ArrayAdapter<EventPeopleItem> implements Filterable {

        Context context;
        ArrayList<EventPeopleItem> items;
        String[] name;

        public SelectedFriendAdapter(Context context, int resourceId,
                                     ArrayList<EventPeopleItem> items) {
            super(context, resourceId, items);
            this.context = context;
            this.items = items;
        }

        /*private view holder school*/
        private class ViewHolder {
            TextView name;
            ImageView image;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            EventPeopleItem item = getItem(position);

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            if (convertView == null)
                convertView = mInflater.inflate(R.layout.event_selected_friend_view, null);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.selected_name);
//                holder.name.setTypeface(face);
                holder.image = (ImageView) convertView.findViewById(R.id.display_image2);

                convertView.setTag(holder);

                holder.name.setText(item.getName());
                holder.image.setImageResource(item.getImageId());


            return convertView;
        }
    }

    public class SelectingFriendsAdapter extends ArrayAdapter<EventPeopleItem> implements Filterable {

        Context context;
        ArrayList<EventPeopleItem> big_items;

        public SelectingFriendsAdapter(Context context, int resource, ArrayList<EventPeopleItem> items) {
            super(context, resource, items);
            this.context = context;

            big_items = new ArrayList<>(items.size());
            for (int i = 0; i <items.size(); i++){
                EventPeopleItem a = items.get(i);
                big_items.add(a);
            }

            Log.i("big_size", Integer.toString(big_items.size()));
            for (int i = 0 ; i < items.size() ; i++){
                Log.i("big_name",big_items.get(i).getName());
                Log.i("big_pos",Integer.toString(big_items.get(i).getPosition()));
            }
        }

        /*private view holder school*/
        private class ViewHolder {
            TextView name;
            ImageView image, selected;
            TextView select;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;


            EventPeopleItem item = getItem(position);
            //Log.i("position", item.getName());

            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            Typeface face;
            //face = Typeface.createFromAsset(getContext().getAssets(), "ubuntu_regular.ttf");

            if (convertView == null)
                convertView = mInflater.inflate(R.layout.event_people_list_item, parent, false);

            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.peopleName);
//            holder.name.setTypeface(face);
            holder.image = (ImageView) convertView.findViewById(R.id.friends_image);
            holder.select = (TextView) convertView.findViewById(R.id.peopleSelectionStatus);
            holder.selected = (ImageView) convertView.findViewById(R.id.friend_selected);
  //          holder.select.setTypeface(face);

            holder.name.setText(item.getName());
            holder.image.setImageResource(item.getImageId());
            holder.select.setText(item.getSelection());
            if (item.getSelection().equals("Y")){
                holder.selected.setVisibility(View.VISIBLE);
            } else
            holder.selected.setVisibility(View.INVISIBLE);
            //convertView.setTag(holder);
            //Log.w("view created", item.getName());

            return convertView;
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }

        private Filter mFilter = new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((EventPeopleItem) resultValue).getName();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (constraint != null) {
                    //Log.i("goodest con2", Integer.toString(big_items.size()));
                    ArrayList<EventPeopleItem> suggestions = new ArrayList<>();
                    for (int i = 0; i < big_items.size(); i++) {
                        if (big_items.get(i).getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            //Log.i("goodest con", constraint.toString());
                            //Log.i("goodest name", big_items.get(i).getName());
                            suggestions.add(big_items.get(i));
                        }
                    }

                    results.values = suggestions;
                    results.count = suggestions.size();
                    //Log.i("goodest", Integer.toString(results.count));
                }
                //Log.i("wait2",Integer.toString(data.size()));

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                clear();
                if (results != null && results.count > 0) {

                    ArrayList<EventPeopleItem> temp = (ArrayList<EventPeopleItem>) results.values;
                    //Log.i("final goode", Integer.toString(temp.size()));
                   // for (int u = 0; u < temp.size(); u++) {
                 //       Log.i("goodest goodest", temp.get(u).getName());
                    //}
                    addAll((ArrayList<EventPeopleItem>) results.values);
                }
                //Log.i("wait3",Integer.toString(data.size()));
                notifyDataSetChanged();
            }
        };

    }

}
