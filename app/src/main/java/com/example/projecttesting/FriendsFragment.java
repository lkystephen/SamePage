package com.example.projecttesting;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;

import java.util.HashMap;

public class FriendsFragment extends Fragment {

	/*Toolbar toolbar; (it is not used for now; activate toolbar.xml when needed:
    <include
    android:id="@+id/tool_bar"
    layout="@layout/toolbar"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
    */
    User user;
    PagerSlidingTabStrip tabs;
    HashMap<String, OtherUser> loc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.friends_main, container, false);


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();

        //loc =  (HashMap<String,OtherUser>) bundle.getSerializable("location");
        //OtherUser tmp111 = loc.get("10153147789760449");
        //Log.i("serial loc",Double.toString(tmp111.lat));
        Typeface face;
        face = Typeface.createFromAsset(getActivity().getAssets(), "sf_reg.ttf");

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mViewPager
                .setAdapter(new FriendsViewAdapter(getChildFragmentManager(), bundle));

        // Assigning the Sliding Tab Layout View
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.tabs);
        tabs.setTypeface(face, 0);
        tabs.setTextColor(Color.parseColor("#ffffff"));

        // To make the Tabs Fixed set this true,
        // This makes the tabs Space Evenly in
        // Available width

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(mViewPager);

    }

    public static class FriendsViewAdapter extends FragmentPagerAdapter {

        private String[] mtitle = new String[]{"Favorites", "Friends"};
        private User user2;
        private Bundle bundle;

        public FriendsViewAdapter(FragmentManager fm, Bundle bundle2) {
            super(fm);
            bundle = bundle2;

        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int index) {
            final MasterListFriendsFragment m = new MasterListFriendsFragment();
            final StarredFriendsFragment s = new StarredFriendsFragment();

            if (index == 0) {
                s.setArguments(bundle);
                return s;
            }
            if (index == 1) {

                m.setArguments(bundle);
                return m;
            } else {

                return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return mtitle[position];
        }

    }
}
