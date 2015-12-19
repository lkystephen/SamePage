package com.example.projecttesting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.graphics.Typeface;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.facebook.Profile;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MasterListFriendsFragment extends Fragment {

    // friendsStarredStatus, 1 = starred, 0 is normal
    ListView listView;
    ArrayList<FriendsRowItem> rowItems;
    Location mLastLocation;
    User user;
    FriendsListAdapter adapter;

    public MasterListFriendsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.master_friends, container,
                false);

        Typeface face;
        face = FontCache.getFont(getContext(), "sf_reg.ttf");

        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");
        mLastLocation = bundle.getParcelable("location");

        Log.i("Number of friends", Integer.toString(user.getMasterList().size()));

        final EditText search = (EditText) rootView.findViewById(R.id.master_search);
        search.setTypeface(face);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence word, int i, int i1, int i2) {
                adapter.getFilter().filter(word.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //final FragmentManager fm = getActivity().getSupportFragmentManager();

        listView = (ListView) rootView.findViewById(R.id.starredfriendslist);

        // Load friends list with Asynctask
        LoadingFriendsList load = new LoadingFriendsList(rowItems);
        load.execute();

/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Bundle bundle = new Bundle();
				bundle.putString("username", master_list.get(i).username);
				bundle.putInt("image",friends_image[i]);
				bundle.putInt("online", friendsLastOnlineTime[i]);

				FriendsDisplayDialog dialog = new FriendsDisplayDialog();
				dialog.setArguments(bundle);
				dialog.show(fm, "");
			}
		});
*/
        return rootView;
    }

    public class LoadingFriendsList extends AsyncTask<Void, String, ArrayList<FriendsRowItem>> {

        ArrayList<FriendsRowItem> mItem;

        public LoadingFriendsList(ArrayList<FriendsRowItem> a) {
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
        protected ArrayList<FriendsRowItem> doInBackground(Void... params) {

            List<OtherUser> master_list = user.getMasterList();

            // Getting the location information of friends in another async task
            //ParseLocation parserTask = new ParseLocation(user);


            // Establish data here
            rowItems = new ArrayList<>();
            for (int i = 0; i < master_list.size(); i++) {
                String distance = new String();
                String diff_time = new String();


                // Get friends last location's distance from you
                OtherUser otheruser = master_list.get(i);
                if (otheruser.hasLoc) {
                    Location location = new Location("TEST");
                    location.setLongitude(otheruser.longitude);
                    location.setLatitude(otheruser.lat);
                    distance = Integer.toString(Math.round(mLastLocation.distanceTo(location)));

                    // Time
                    long lastUpdate = otheruser.timestamp;
                    long time = System.currentTimeMillis();
                    long difference = time - lastUpdate;
                    int diff_min = Math.round(difference / 60000);
                    diff_time = Integer.toString(diff_min);

                } else {
                    distance = "NULL";
                    diff_time = "NULL";
                }
                FriendsRowItem item = new FriendsRowItem(master_list.get(i).username, 0, master_list.get(i).fbid, distance, diff_time);
                rowItems.add(item);
            }

            return rowItems;
        }

        @Override
        protected void onPostExecute(ArrayList<FriendsRowItem> result) {
            super.onPostExecute(result);

            adapter = new FriendsListAdapter(getActivity()
                    .getApplicationContext(), R.layout.friendslistdisplay,
                    rowItems);
            listView.setAdapter(adapter);

        }
    }


}
