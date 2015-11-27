package com.example.projecttesting;

import android.content.ClipData;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class InvitedEventFragment extends Fragment {

    ListView listview;
    User user;
    //ArrayList<EventEntryItem> bigdata;
    String fbid;
    LinearLayout rsvp_attending, rsvp_rejecting, rsvp;
    Animation vibrate;
    EventTypes eventTypes;

    public InvitedEventFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.organize_event, container,
                false);

        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");
        fbid = user.getFBId();

        // Load animation
        vibrate = AnimationUtils.loadAnimation(getContext(), R.anim.vibrate);

        // Get data
        List<EventTypes> list = user.getEventsInvited();

        // Set up list view
        listview = (ListView) rootView.findViewById(R.id.event_main_list);

        // Set up drag zone buttons
        rsvp_attending = (LinearLayout) rootView.findViewById(R.id.rsvp_attending);
        rsvp_rejecting = (LinearLayout) rootView.findViewById(R.id.rsvp_rejecting);
        rsvp = (LinearLayout) rootView.findViewById(R.id.rsvp);

        LoadingAdapter loading = new LoadingAdapter(list);
        loading.execute();


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

                rsvp_attending.setOnDragListener(new MyDragListener(1, user.getUserId()));
                rsvp_rejecting.setOnDragListener(new MyDragListener(2, user.getUserId()));

                // Set onTouchListener for each item
                listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                        rsvp_rejecting.setVisibility(View.VISIBLE);
                        rsvp_attending.setVisibility(View.VISIBLE);

                        ClipData data = ClipData.newPlainText("", "");
                        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                        view.startDrag(data, shadowBuilder, view, 0);
                        rsvp.setVisibility(View.VISIBLE);
                        // Set the current event selected
                        eventTypes = user.getEventsInvited().get(i);
                        return true;
                    }
                });

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
                        bundle.putDouble("event_lat",et.getVenueLat());
                        bundle.putDouble("event_lng",et.getVenueLong());

                        // Set event invitees
                        ArrayList<String> invitees = (ArrayList<String>) et.getEventInvitees();
                        bundle.putStringArrayList("event_invitees",invitees);

                        // Determine if the event has started or not
                        long notification_time = et.getEventDateTime().getTimeInMillis() + 1000 * 60 *45;
                        long current_time = new DateTime().getMillis();
                        Log.i("Current time in millis", Long.toString(current_time));
                        if (current_time > notification_time){
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

    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(R.drawable.circle_dropzone, null);
        Drawable normalShape = getResources().getDrawable(R.drawable.circle_dropzone, null);
        int response;
        String userid;

        public MyDragListener(int response, String userid) {
            this.response = response;
            this.userid = userid;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {

            int action = event.getAction();
            View view = (View) event.getLocalState();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // both accept and reject will vibrate
                    v.startAnimation(vibrate);
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:

                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    //rsvp_attending.getAnimation().cancel();
                    //rsvp_rejecting.getAnimation().cancel();

                    //rsvp_attending.setVisibility(View.GONE);
                    //rsvp_rejecting.setVisibility(View.GONE);
                    break;
                case DragEvent.ACTION_DROP:

                    view.setVisibility(View.VISIBLE);
                    if (response == 1) {
                        Toast.makeText(getContext(), "You are going", Toast.LENGTH_LONG).show();
                        eventTypes.rsvp(userid, 1);
                    }
                    if (response == 2) {
                        eventTypes.rsvp(userid, 2);
                        Toast.makeText(getContext(), "You are rejecting", Toast.LENGTH_LONG).show();
                    }

                    break;
                case DragEvent.ACTION_DRAG_ENDED:

                    rsvp.setVisibility(View.GONE);
                    break;
                //v.setBackground(normalShape);
                default:
                    break;
            }
            return true;
        }

    }
}
