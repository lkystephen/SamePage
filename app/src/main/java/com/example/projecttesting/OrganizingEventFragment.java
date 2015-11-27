package com.example.projecttesting;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.joda.time.DateTime;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OrganizingEventFragment extends Fragment implements UpdateableFragment {

    ListView listview;
    List<EventTypes> data;
    User user;
    //ArrayList<EventEntryItem> bigdata;
    String fbid;
    EventListAdapter adapter;
    Context mContext;

    public OrganizingEventFragment() {
    }

    public void update(List<EventTypes> data) {
        // do whatever you want to update your UI
        this.data = data;

        for (int i=0; i< this.data.size(); i++){
            Log.i("event name",this.data.get(i).getEventName());
        }

        //this.mContext = context;
        Log.i("Organising Fragment", "Number of events retrieved is " + Integer.toString(data.size()));
        //adapter = new EventListAdapter(context, R.layout.event_list_display, data);
        adapter.clear();
        adapter.addAll(this.data);
        adapter.notifyDataSetChanged();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.organize_event, container,
                false);

        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");

        fbid = user.getFBId();
        // Get data
        //EventDetailsFetch fetch = new EventDetailsFetch();
        List<EventTypes> list = user.getEventsOrganised();
        //int i = list.get(0).getEventInvitees().size();
        //Log.i("size",Integer.toString(i));
        //bigdata = fetch.FetchDetails(list);


        // Set up list view
        listview = (ListView) rootView.findViewById(R.id.event_main_list);

        // Set up drag zone buttons
        LinearLayout rsvp_attending = (LinearLayout) rootView.findViewById(R.id.rsvp_attending);
        LinearLayout rsvp_rejecting = (LinearLayout) rootView.findViewById(R.id.rsvp_rejecting);
        rsvp_attending.setVisibility(View.GONE);
        rsvp_rejecting.setVisibility(View.GONE);

        LoadingAdapter loading = new LoadingAdapter(user.getEventsOrganised());
        loading.execute();


        return rootView;
    }

    private class LoadingAdapter extends AsyncTask<Void, String, List<EventTypes>> {

        final FragmentManager fm = getActivity().getSupportFragmentManager();

        List<EventTypes> mItem;
        public LoadingAdapter(List<EventTypes> a){
            mItem = a;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
       // @Override
        //protected void onProgressUpdate(String... values) {

        //}

        @Override
        protected List<EventTypes> doInBackground(Void ... params){

            return mItem;
        }

        @Override
        protected void onPostExecute(List<EventTypes> result){
            super.onPostExecute(result);

            data = result;
            adapter = new EventListAdapter(getActivity()
                    .getApplicationContext(), R.layout.event_list_display,
                    data);


            listview.setAdapter(adapter);

            if (result == null) {
                TextView no_event_msg = (TextView) getActivity().findViewById(R.id.no_event_text);
                no_event_msg.setVisibility(View.VISIBLE);

            } else {

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        EventTypes et = user.getEventsOrganised().get(i);
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
                        Log.i("PUT LAT", Double.toString(et.getVenueLat()));
                        bundle.putDouble("event_lng",et.getVenueLong());

                        // Set event invitees
                        ArrayList<String> invitees = (ArrayList<String>) et.getEventInvitees();
                        Log.i("number",Integer.toString(et.getEventInvitees().size()));
                        bundle.putStringArrayList("event_invitees",invitees);

                        // Determine if the event has started or not
                        long notification_time = et.getEventDateTime().getTimeInMillis() - 1000 * 60 *45;
                        Log.i("Event time in millis", Long.toString(notification_time));
                        long current_time = new DateTime().getMillis();
                        Log.i("Current time in millis", Long.toString(current_time));
                        if (current_time > notification_time){
                            EventStartDialog event_dialog = new EventStartDialog();
                            Log.i("EventStart","EventStart");
                            event_dialog.setArguments(bundle);
                            event_dialog.show(fm, "");
                        } else {
                            EventOrganisingDialog event_dialog = new EventOrganisingDialog();
                            event_dialog.setArguments(bundle);
                            event_dialog.show(fm, "");
                        }
                    }
                });
            }

        }
    }

}
