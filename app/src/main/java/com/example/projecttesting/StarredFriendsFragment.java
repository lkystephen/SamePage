package com.example.projecttesting;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StarredFriendsFragment extends Fragment {

    //public int[] friendsLastOnlineTime = new int[]{1, 10};
    //public int[] friends_image = {R.drawable.nigel, R.drawable.avery};

    // friendsStarredStatus, 1 = starred, 0 is normal
    //public int[] friendsStarredStatus = new int[]{1, 1};

    ListView listView;
    ArrayList<FriendsRowItem> rowItems;
    User user;
    FriendsListAdapter adapter;
    TextView no_star_text;

    public StarredFriendsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.starred_friends, container,
                false);

        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");

        // Get names of friends
        listView = (ListView) rootView.findViewById(R.id.starredfriendslist);
        no_star_text = (TextView) rootView.findViewById(R.id.nostarfriendstext);

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
                    FriendsRowItem item = new FriendsRowItem(
                            user.getStarList().get(i).username, 0, user.getStarList().get(i).fbid);
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
