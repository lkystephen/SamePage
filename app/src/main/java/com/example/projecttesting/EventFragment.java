package com.example.projecttesting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EventFragment extends Fragment implements MainAct {

    //for GCM
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "603098203110";

    EventViewAdapter mPagerAdapter;
    PagerSlidingTabStrip tabs;

    private String fbid;
    private String username;
    MaterialRefreshLayout refreshLayout;
    User user;
    List<EventTypes> data;
    OrganizingEventFragment m;
    ProgressDialog dialog;
    Context mContext;
    NonSwipeableViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.event_main, container, false);
    }

    public void handleLoginResults(boolean isNewUser, Users users) {

        Log.e("revisit", Integer.toString(user.getEventsOrganised().size()));

        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);

        // Assigning the Sliding Tab Layout View
        Typeface face_b;
        //face = Typeface.createFromAsset(getActivity().getAssets(), "sf_bold.ttf");
        face_b = FontCache.getFont(getContext(), "sf_bold.ttf");
        mPagerAdapter.update(user.getEventsOrganised());

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentManager fm = getActivity().getSupportFragmentManager();

        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");
        //Log.i("Parcelable username", user.getUsername());

        refreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.big_layout);
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                Intent intent = new Intent(getActivity(), EventCreation.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);
                intent.putExtras(bundle);
                startActivityForResult(intent, 10);

                refreshLayout.finishRefresh();
            }
        });

        mContext = getContext();
        // Set up pager view
        Typeface face;
        face = FontCache.getFont(getContext(), "sf_reg.ttf");

        TextView createText = (TextView) view.findViewById(R.id.update_status2);
        TextView rsvpText = (TextView) view.findViewById(R.id.update_status3);
        createText.setTypeface(face);
        rsvpText.setTypeface(face);

        mViewPager = (NonSwipeableViewPager) view.findViewById(R.id.event_viewPager);

        mPagerAdapter = new EventViewAdapter(getChildFragmentManager(), bundle);

        mViewPager.setAdapter(mPagerAdapter);

        // Get background
        ImageView friends_background = (ImageView) view.findViewById(R.id.event_display_bg);
        friends_background.setImageResource(R.drawable.event_main);

        // Assigning the Sliding Tab Layout View
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.event_tabs);
        tabs.setTypeface(face, 0);
        tabs.setTextColor(Color.parseColor("#ffffff"));

        // To make the Tabs Fixed set this true,
        // This makes the tabs Space Evenly in
        // Available width

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(mViewPager);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {

                //get RegId
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        Log.i("GCM", "starting");

                        try {
                            if (gcm == null) {
                                gcm = GoogleCloudMessaging.getInstance(getActivity().getApplicationContext());
                            }
                            regid = gcm.register(PROJECT_NUMBER);
                            // msg = "Device registered, registration ID=" + regid;
                            Log.i("GCM", regid);

                        } catch (IOException ex) {
                            Log.i("GCM", "Error :" + ex.getMessage());
                        }
                        return regid;
                    }

                    // Publish progress
                    protected void onPreExecute() {

                    }


                    @Override
                    protected void onPostExecute(String regId_input) {
                        // get with db
                        fbid = Profile.getCurrentProfile().getId();

                        username = Profile.getCurrentProfile().getName();
                        fbid = Profile.getCurrentProfile().getId();

                        user = null;
                        user = new User(fbid, username, regId_input, EventFragment.this);
                        user.execute();
                    }
                }.execute(null, null, null);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public class EventViewAdapter extends FragmentPagerAdapter {

        List<EventTypes> data;
        FragmentManager fm;

        public void update(List<EventTypes> data) {
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            if (object instanceof UpdateableFragment) {
                ((UpdateableFragment) object).update(user.getEventsOrganised());
            }
            //don't return POSITION_NONE, avoid fragment recreation.
            return super.getItemPosition(object);
        }


        private String[] mtitle = new String[]{"PENDING", "Organizing"};
        //private User user2;
        private Bundle bundle;

        public EventViewAdapter(FragmentManager fm, Bundle bundle2) {
            super(fm);
            this.fm = fm;
            bundle = bundle2;
            //bundle.putParcelable("user", user2);
        }

        @Override
        public int getCount() {
            // Return three sections for friends lists
            return 2;
        }

        @Override
        public Fragment getItem(int index) {
            m = new OrganizingEventFragment();
            final InvitedEventFragment s = new InvitedEventFragment();

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
