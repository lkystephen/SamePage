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

import java.util.ArrayList;
import java.util.List;

public class InvitedEventFragment extends Fragment {

    ListView listview;
    List<FriendsRowItem> rowItems;
    User user;
    ArrayList<EventEntryItem> bigdata;
    String fbid;
    LinearLayout rsvp_attending, rsvp_rejecting, rsvp;
    Animation vibrate;

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
        vibrate = AnimationUtils.loadAnimation(getContext(),R.anim.vibrate);

        final FragmentManager fm = getActivity().getSupportFragmentManager();

        // Get data
        EventDetailsFetch fetch = new EventDetailsFetch();
        List<EventTypes> list = user.getEventsInvited();
        bigdata = fetch.FetchDetails(list);

        // Set up list view
        listview = (ListView) rootView.findViewById(R.id.event_main_list);

        // Set up drag zone buttons
        rsvp_attending = (LinearLayout) rootView.findViewById(R.id.rsvp_attending);
        rsvp_rejecting = (LinearLayout) rootView.findViewById(R.id.rsvp_rejecting);
        rsvp = (LinearLayout) rootView.findViewById(R.id.rsvp);

        LoadingAdapter loading = new LoadingAdapter(bigdata);
        loading.execute();


        return rootView;
    }

    private class LoadingAdapter extends AsyncTask<Void, String, ArrayList<EventEntryItem>> {

        ArrayList<EventEntryItem> mItem;

        public LoadingAdapter(ArrayList<EventEntryItem> a) {
            mItem = a;
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
        protected ArrayList<EventEntryItem> doInBackground(Void... params) {


            return bigdata;
        }

        @Override
        protected void onPostExecute(ArrayList<EventEntryItem> result) {
            super.onPostExecute(result);

            EventListAdapter adapter = new EventListAdapter(getActivity()
                    .getApplicationContext(), R.layout.event_list_display,
                    bigdata);

            listview.setAdapter(adapter);

            if (bigdata == null) {
                TextView no_event_msg = (TextView) getActivity().findViewById(R.id.no_event_text);
                no_event_msg.setVisibility(View.VISIBLE);

            } else {
                listview.setAdapter(adapter);

                rsvp_attending.setOnDragListener(new MyDragListener(1));
                rsvp_rejecting.setOnDragListener(new MyDragListener(2));

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
                        return true;
                    }
                });

                // Set pop up dialog
                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("data", bigdata.get(i));
                        bundle.putString("my_id", fbid);
                        String organiser_name = "myself";
                        String organiser_fbid = bigdata.get(i).getOrganiser();
                        for (int j = 0; j < user.getMasterList().size(); j++) {
                            if (user.getMasterList().get(j).fbid.equals(organiser_fbid)) {
                                organiser_name = user.getMasterList().get(j).username;
                            }
                        }
                        bundle.putString("organiser_fbid", organiser_fbid);
                        bundle.putString("organiser_name", organiser_name);
                        //bundle.putInt("position", i);
                        EventDisplayDialog event_dialog = new EventDisplayDialog();
                        event_dialog.setArguments(bundle);
                        event_dialog.show(fm, "");
                    }
                });
            }

        }
    }

    class MyDragListener implements View.OnDragListener {
        Drawable enterShape = getResources().getDrawable(R.drawable.circle_dropzone, null);
        Drawable normalShape = getResources().getDrawable(R.drawable.circle_dropzone, null);
        int response;

        public MyDragListener (int response){
            this.response = response;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {

            int action = event.getAction();
            View view = (View) event.getLocalState();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //v.setVisibility(View.VISIBLE);

                    // both accept and reject will vibrate
                    v.startAnimation(vibrate);
                    //rsvp_rejecting.startAnimation(vibrate);

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
                    // Dropped, reassign View to ViewGroup
                    //rsvp_rejecting.getAnimation().cancel();
                    //rsvp_rejecting.setVisibility(View.GONE);

                    view.setVisibility(View.VISIBLE);
                    if (response == 1)
                    Toast.makeText(getContext(), "You are going", Toast.LENGTH_LONG).show();
                    if (response == 2)
                        Toast.makeText(getContext(), "You are rejecting", Toast.LENGTH_LONG).show();

                    //view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:


                    /*v.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("vi", "sihg");
                            rsvp_attending.setVisibility(View.GONE);
                            rsvp_rejecting.setVisibility(View.GONE);
                        }
                    });*/
                    //rsvp_attending.setVisibility(View.GONE);
                    //rsvp_rejecting.setVisibility(View.GONE);
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
