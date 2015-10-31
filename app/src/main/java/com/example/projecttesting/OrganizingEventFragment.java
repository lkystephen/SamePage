package com.example.projecttesting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OrganizingEventFragment extends Fragment {

    ListView listview;
    List<FriendsRowItem> rowItems;
    User user;
    ArrayList<EventEntryItem> bigdata;
    String fbid;

    public OrganizingEventFragment() {
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
        EventDetailsFetch fetch = new EventDetailsFetch();
        List<EventTypes> list = user.getEventsOrganised();
        bigdata = fetch.FetchDetails(list);


        // Set up list view
        listview = (ListView) rootView.findViewById(R.id.event_main_list);


        LoadingAdapter loading = new LoadingAdapter(bigdata);
        loading.execute();


        return rootView;
    }

    private class LoadingAdapter extends AsyncTask<Void, String, ArrayList<EventEntryItem>> {

        final FragmentManager fm = getActivity().getSupportFragmentManager();

        ArrayList<EventEntryItem> mItem;
        public LoadingAdapter(ArrayList<EventEntryItem> a){
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
        protected ArrayList<EventEntryItem> doInBackground(Void ... params){



            return bigdata;
        }

        @Override
        protected void onPostExecute(ArrayList<EventEntryItem> result){
            super.onPostExecute(result);

            EventListAdapter adapter = new EventListAdapter(getActivity()
                    .getApplicationContext(), R.layout.event_list_display,
                    bigdata);


            listview.setAdapter(adapter);

            if (bigdata == null) {
                TextView no_event_msg = (TextView) getActivity().findViewById(R.id.no_event_text);
                no_event_msg.setVisibility(View.VISIBLE);

            } else {
                //    listview.setAdapter(adapter);
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
                        EventOrganisingDialog event_dialog = new EventOrganisingDialog();
                        event_dialog.setArguments(bundle);
                        event_dialog.show(fm, "");
                    }
                });
            }

        }
    }

}
