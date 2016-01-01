package com.example.projecttesting;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

public class MasterListFriendsFragment extends Fragment implements UpdateResult {

    // friendsStarredStatus, 1 = starred, 0 is normal
    SwipeMenuListView listView;
    ArrayList<FriendsRowItem> rowItems;
    Location mLastLocation;
    User user;
    FriendsListAdapter adapter;
    ArrayList<String> list1;
    ArrayList<OtherUser> list;
    FragmentManager fm;

    public MasterListFriendsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.master_friends, container,
                false);

        final Typeface face;
        face = FontCache.getFont(getContext(), "sf_reg.ttf");

        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");
        mLastLocation = bundle.getParcelable("location");
        list = new ArrayList<>();
        list1 = new ArrayList<>();

        for (int i = 0; i < user.getMasterList().size(); i++) {
            list1.add("N");
        }

        final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.friends_overall);

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

        fm = getActivity().getSupportFragmentManager();

        listView = (SwipeMenuListView) rootView.findViewById(R.id.friendslist);

        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {

                switch (menu.getViewType()) {
                    case 0:
                        // Create menu type for normal friends
                        SwipeMenuItem unstarItem = new SwipeMenuItem(getContext());
                        // set item background
                        ColorDrawable color = new ColorDrawable(Color.parseColor("#f9f9f9"));
                        color.setAlpha(40);
                        unstarItem.setBackground(color);
                        // set item width
                        unstarItem.setWidth(160);
                        // add to menu
                        unstarItem.setIcon(R.drawable.fav_yellow);
                        menu.addMenuItem(unstarItem);
                        break;

                    case 1:
                        //Create menu type for favorite friends
                        //SwipeMenuItem unstarItem = new SwipeMenuItem(getContext());
                        //set item background
                        //ColorDrawable color2 = new ColorDrawable(Color.parseColor("#f9f9f9"));
                        //color2.setAlpha(40);
                        //unstarItem.setBackground(color2);
                        // set item width
                        //unstarItem.setWidth(160);
                        // add to menu
                        //unstarItem.setIcon(R.drawable.unfav_red);
                        //menu.addMenuItem(unstarItem);
                        break;
                }


            }
        };

        // set creator
        listView.setMenuCreator(creator);

        listView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // Right
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        //List<OtherUser> tempList = new ArrayList<OtherUser>();
                        list1.set(position, "Y");
                        int temp = 0;
                        for (int i = 0; i < list1.size(); i++) {
                            if (list1.get(i).equals("Y")) {
                                temp++;
                            }
                        }
                        String snackBarDisplay = "TEST";
                        if (temp == 1) {
                            snackBarDisplay = temp + " friend is added to favorite";
                        } else if (temp > 1) {
                            snackBarDisplay = temp + " friends are added to favorite";
                        }
                        Snackbar snackbar = Snackbar.make(linearLayout, snackBarDisplay, Snackbar.LENGTH_INDEFINITE)
                                .setAction("CONFIRM", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        list.clear();
                                        for (int i = 0; i < list1.size(); i++) {
                                            if (list1.get(i).equals("Y")) {
                                                list.add(user.getMasterList().get(i));
                                            }
                                        }
                                        user.addFrdsToStar(list, MasterListFriendsFragment.this);
                                        list1.clear();
                                    }
                                });
                        View sbView = snackbar.getView();
                        TextView text = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        text.setTypeface(face);

                        snackbar.show();
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

        // Load friends list with Asynctask
        LoadingFriendsList load = new LoadingFriendsList(rowItems);
        load.execute();

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

            // Establish data here
            rowItems = new ArrayList<>();
            for (int i = 0; i < master_list.size(); i++) {
                String distance = new String();
                long lastUpdate;

                // Get friends last location's distance from you
                OtherUser otheruser = master_list.get(i);
                if (otheruser.hasLoc) {
                    Location location = new Location("TEST");
                    location.setLongitude(otheruser.longitude);
                    location.setLatitude(otheruser.lat);
                    distance = Integer.toString(Math.round(mLastLocation.distanceTo(location)));

                    // Time
                    lastUpdate = otheruser.timestamp;

                } else {
                    distance = "NULL";
                    lastUpdate = 0;
                }
                FriendsRowItem item = new FriendsRowItem(master_list.get(i).username, 0, master_list.get(i).fbid,
                        distance, lastUpdate);
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

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    Boolean hasLoc = user.getMasterList().get(i).hasLoc;

                    if (hasLoc) {
                        Bundle bundle = new Bundle();
                        double lat = user.getMasterList().get(i).lat;
                        double lng = user.getMasterList().get(i).longitude;
                        Location location = new Location("TEST");
                        location.setLatitude(lat);
                        location.setLongitude(lng);

                        bundle.putParcelable("location", location);
                        bundle.putParcelable("mLocation", mLastLocation);
                        Log.i("WT",Long.toString(user.getMasterList().get(i).timestamp));
                        bundle.putLong("updatetime",user.getMasterList().get(i).timestamp);
                        bundle.putParcelable("user", user);
                        bundle.putString("name", user.getMasterList().get(i).username);
                        FriendsDisplayDialog dialog = new FriendsDisplayDialog();
                        dialog.setArguments(bundle);
                        dialog.show(fm, "");
                    }
                }
            });


        }
    }

    @Override
    public void handleUpdateResults(int result) {
        if (result == 0) {
            Toast.makeText(getContext(), "Friends are added to your favorite list successfully.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Server error. Failed to add friends to favorite list.", Toast.LENGTH_LONG).show();
        }
    }

}


