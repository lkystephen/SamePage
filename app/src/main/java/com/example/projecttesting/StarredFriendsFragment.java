package com.example.projecttesting;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.w3c.dom.Text;

public class StarredFriendsFragment extends Fragment {

    //public int[] friendsLastOnlineTime = new int[]{1, 10};
    //public int[] friends_image = {R.drawable.nigel, R.drawable.avery};

    // friendsStarredStatus, 1 = starred, 0 is normal
    //public int[] friendsStarredStatus = new int[]{1, 1};

    SwipeMenuListView listView;
    Location mLastLocation;
    ArrayList<FriendsRowItem> rowItems;
    User user;
    FriendsListAdapter adapter;
    TextView no_star_text;
    ArrayList<String> list1;
    ArrayList<OtherUser> list;


    public StarredFriendsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.starred_friends, container,
                false);

        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");
        mLastLocation = bundle.getParcelable("location");
        list = new ArrayList<>();
        list1 = new ArrayList<>();

        final Typeface face;
        face = FontCache.getFont(getContext(), "sf_reg.ttf");

        for (int i = 0; i < user.getStarList().size(); i++) {
            list1.add("N");
        }
        // Get names of friends
        listView = (SwipeMenuListView) rootView.findViewById(R.id.starredfriendslist);
        no_star_text = (TextView) rootView.findViewById(R.id.nostarfriendstext);

        final LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.linearLayout);

        Typeface face_r, face_b;
        face_b = FontCache.getFont(getContext(), "sf_bold.ttf");
        face_r = FontCache.getFont(getContext(), "sf_reg.ttf");
        no_star_text.setTypeface(face_b);


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
                starItem.setIcon(R.drawable.unfav_red);
                menu.addMenuItem(starItem);

            }
        };

        // set creator
        listView.setMenuCreator(creator);


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
                            snackBarDisplay = temp + " friend is set to be removed from favorite";
                        } else if (temp > 1) {
                            snackBarDisplay = temp + " friends are set to be removed from favorite";
                        }
                        Snackbar snackbar = Snackbar.make(linearLayout, snackBarDisplay, Snackbar.LENGTH_INDEFINITE)
                                .setAction("CONFIRM", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        list.clear();
                                        for (int i = 0; i < list1.size(); i++) {
                                            if (list1.get(i).equals("Y")) {
                                                list.add(user.getStarList().get(i));
                                            }
                                        }
                                        //user.addFrdsToStar(list, StarredFriendsFragment.this);
                                        list1.clear();
                                    }
                                });
                        View sbView = snackbar.getView();
                        TextView text = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                        text.setTypeface(face);

                        snackbar.show();
                        break;
                    //case 1:
                    // delete
                    //  break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });

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

            List<OtherUser> star_list = user.getStarList();
            // Establish testing data here
            rowItems = new ArrayList<FriendsRowItem>();

            if (star_list == null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        no_star_text.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.GONE);
                    }
                });
            } else {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        no_star_text.setVisibility(View.GONE);
                        listView.setVisibility(View.VISIBLE);
                    }
                });
                for (int i = 0; i < star_list.size(); i++) {
                    String distance = new String();
                String diff_time = new String();


                // Get friends last location's distance from you
                OtherUser otheruser = star_list.get(i);
                if (otheruser.hasLoc){
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

                } else{
                    distance = "NULL";
                    diff_time = "NULL";
                }

                    FriendsRowItem item = new FriendsRowItem(
                            user.getStarList().get(i).username, 0, user.getStarList().get(i).fbid, distance, diff_time);
                    rowItems.add(item);
                }
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
