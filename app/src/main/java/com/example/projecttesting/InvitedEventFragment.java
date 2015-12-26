package com.example.projecttesting;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

public class InvitedEventFragment extends Fragment {

    SwipeMenuListView listview;
    User user;
    //ArrayList<EventEntryItem> bigdata;
    String fbid;
    LinearLayout rsvp_attending, rsvp_rejecting, rsvp;
    TextView no_pending;
    Animation vibrate;
    EventTypes eventTypes;

    public InvitedEventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.event_main_list, container,
                false);

        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");
        fbid = user.getFBId();

        Typeface typeface_reg = FontCache.getFont(getContext(), "sf_reg.ttf");
        Typeface typeface_bold = FontCache.getFont(getContext(), "sf_bold.ttf");

        // Get data
        List<EventTypes> list = user.getEventsInvited();

        // Set up list view
        listview = (SwipeMenuListView) rootView.findViewById(R.id.event_main_list);
        if (user.getEventsInvited().size() == 0) {
            listview.setVisibility(View.GONE);
        }

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                // Create open menu
                SwipeMenuItem starItem = new SwipeMenuItem(getContext());
                // set item background
                ColorDrawable color = new ColorDrawable(Color.parseColor("#f9f9f9"));
                color.setAlpha(40);
                starItem.setBackground(color);
                // set item width
                starItem.setWidth(160);
                // add to menu
                starItem.setIcon(R.drawable.accept_green);
                menu.addMenuItem(starItem);

            }
        };

        // set creator
        listview.setMenuCreator(creator);

        // Right
        listview.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        listview.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        break;
                    //case 1:
                    //  break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });


        // Set up textview if no pending events
        no_pending = (TextView) rootView.findViewById(R.id.no_pending);
        no_pending.setTypeface(typeface_bold);
        if (user.getEventsInvited().size() == 0) {
            Log.i("No of events pending", Integer.toString(user.getEventsInvited().size()));
            no_pending.setVisibility(View.VISIBLE);
        }


        if (user.getEventsInvited().size() != 0) {
            LoadingAdapter loading = new LoadingAdapter(list);
            loading.execute();
        }

        return rootView;
    }

    private class LoadingAdapter extends AsyncTask<Void, String, List<EventTypes>> {

        List<EventTypes> mItem;
        //ArrayList<EventEntryItem> mItem;

        public LoadingAdapter(List<EventTypes> mItem) {
            this.mItem = mItem;
        }

        final FragmentManager fm = getActivity().getSupportFragmentManager();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        // @Override
        //protected void onProgressUpdate(String... values) {

        //}

        @Override
        protected List<EventTypes> doInBackground(Void... params) {
            return mItem;
        }

        @Override
        protected void onPostExecute(List<EventTypes> result) {
            super.onPostExecute(result);

            EventListAdapter adapter = new EventListAdapter(getActivity(), R.layout.event_list_display,
                    result);

            listview.setAdapter(adapter);

            if (result == null) {
                TextView no_event_msg = (TextView) getActivity().findViewById(R.id.no_event_text);
                no_event_msg.setVisibility(View.VISIBLE);

            } else {
                listview.setAdapter(adapter);

                // Set pop up dialog
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        EventTypes et = user.getEventsInvited().get(i);
                        Bundle bundle = new Bundle();
                        // Set Facebook id of user
                        bundle.putString("my_id", fbid);
                        String organiser_name = "myself";
                        for (int j = 0; j < user.getMasterList().size(); j++) {
                            if (user.getMasterList().get(j).fbid.equals(et.getOrganiser())) {
                                organiser_name = user.getMasterList().get(j).username;
                            }
                        }
                        bundle.putString("organiser_fbid", et.getOrganiser());
                        bundle.putString("organiser_name", organiser_name);

                        // Set event name
                        bundle.putString("event_name", et.getEventName());

                        // Set event start time
                        bundle.putLong("event_start", et.getEventDateTime().getTimeInMillis());

                        // Set event location
                        bundle.putString("event_location", et.getEventVenue());

                        // Set event latlng
                        bundle.putDouble("event_lat", et.getVenueLat());
                        bundle.putDouble("event_lng", et.getVenueLong());

                        // Set event invitees
                        ArrayList<String> invitees = (ArrayList<String>) et.getEventInvitees();
                        bundle.putStringArrayList("event_invitees", invitees);

                        // Determine if the event has started or not
                        long notification_time = et.getEventDateTime().getTimeInMillis() + 1000 * 60 * 45;
                        long current_time = new DateTime().getMillis();
                        Log.i("Current time in millis", Long.toString(current_time));
                        if (current_time > notification_time) {
                            EventStartDialog event_dialog = new EventStartDialog();
                            event_dialog.setArguments(bundle);
                            event_dialog.show(fm, "");
                        } else {
                            EventDisplayDialog event_dialog = new EventDisplayDialog();
                            event_dialog.setArguments(bundle);
                            event_dialog.show(fm, "");
                        }

                    }
                });
            }

        }
    }

}
