package com.example.projecttesting;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Typeface;
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
        List<OtherUser> star_list = user.getStarList();
        listView = (ListView) rootView.findViewById(R.id.starredfriendslist);

        // adapterStarredorNot is to tell the adapter whether the adapter would
        // work for starred / master list
        int adapterStarredorNot = 1;

        TextView no_star_text = (TextView) rootView.findViewById(R.id.nostarfriendstext);

        // Establish testing data here
        rowItems = new ArrayList<FriendsRowItem>();

        if (star_list == null) {
            no_star_text.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        } else {

            no_star_text.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            for (int i = 0; i < star_list.size(); i++) {

                FriendsRowItem item = new FriendsRowItem(
                        user.getStarList().get(i).username, 0, user.getStarList().get(i).fbid);
                rowItems.add(item);

            }
        }


        FriendsListAdapter adapter = new FriendsListAdapter(getActivity()
                .getApplicationContext(), R.layout.friendslistdisplay, rowItems);
        listView.setAdapter(adapter);

        return rootView;
    }

}
