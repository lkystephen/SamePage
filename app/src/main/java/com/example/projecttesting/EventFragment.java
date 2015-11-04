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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class EventFragment extends Fragment implements MainAct {

    //for GCM
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "603098203110";


    PagerSlidingTabStrip tabs;

    private String fbid;
    private String username;
    ArrayList<EventEntryItem> bigdata;
    ListView listview;
    User user;
    EventListAdapter adapter;
    ProgressDialog dialog;
    Context mContext;
    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //mContext = this.getActivity();

       // facebold = Typeface.createFromAsset(getActivity().getAssets(), "ubuntu_bold.ttf");
        //face_light = Typeface.createFromAsset(getActivity().getAssets(), "ubuntu_light.ttf");

        return inflater.inflate(R.layout.event_main, container, false);

    }

    public void handleLoginResults(boolean isNewUser, Users users) {

        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);


        //ViewPager mViewPager = (ViewPager) EventFragment.findViewById(R.id.event_viewPager);

        EventViewAdapter v = new EventViewAdapter(getChildFragmentManager(), bundle);

        mViewPager.setAdapter(v);

        v.update(bundle);

        // Assigning the Sliding Tab Layout View
        Typeface face_b;
        //face = Typeface.createFromAsset(getActivity().getAssets(), "sf_bold.ttf");
        face_b = FontCache.getFont(getContext(),"sf_bold.ttf");

        tabs.setTypeface(face_b, 0);
        tabs.setTextColor(Color.parseColor("#000000"));

        // To make the Tabs Fixed set this true,
        // This makes the tabs Space Evenly in
        // Available width

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(mViewPager);

        // Update the event count
        int total_event_number = user.getEventsInvited().size() + user.getEventsOrganised().size() + user.getEventsAttending().size();

        Toast.makeText(getActivity(), "Event added", Toast.LENGTH_LONG).show();
    }

    @Override
    public void handleGetFrdsLocResults(HashMap<String, OtherUser> masterListwLoc) {

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final FragmentManager fm = getActivity().getSupportFragmentManager();

        Bundle bundle = getArguments();
        user = bundle.getParcelable("user");
        Log.i("Parcelable username", user.getUsername());

        bigdata = new ArrayList<EventEntryItem>();
        //final List<OtherUser> otherUsers = user.getMasterList();

        FloatingActionButton add_event = (FloatingActionButton)view.findViewById(R.id.create_button);

        add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EventCreation.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("user", user);
                intent.putExtras(bundle);
                startActivityForResult(intent, 10);

            }
        });

        // Set up pager view
        Typeface face;
        face = FontCache.getFont(getContext(),"sf_reg.ttf");

        mViewPager = (ViewPager) view.findViewById(R.id.event_viewPager);

        mViewPager
                .setAdapter(new EventViewAdapter(getChildFragmentManager(), bundle));


        // Get background
       ImageView friends_background = (ImageView) view.findViewById(R.id.event_display_bg);
        //Bitmap processed = BitmapFactory.decodeResource(getResources(), R.drawable.event_main);
        //Bitmap blurred_bg = BlurBuilder.blur(getActivity(), processed);
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

                       /* dialog = new ProgressDialog(mContext);
                        dialog.setMessage("Adding event");
                        Log.i("dialog msg", "YES");
                        dialog.show();
*/
                    }


                    @Override
                    protected void onPostExecute(String regId_input) {
                        // get with db
                        fbid = Profile.getCurrentProfile().getId();

                        Log.i("name", Profile.getCurrentProfile().getName());
                        username = Profile.getCurrentProfile().getName();
                        fbid = Profile.getCurrentProfile().getId();

                        user = null;

                        user = new User(fbid, username, regId_input, EventFragment.this);
                        user.execute();
                    }
                }.execute(null, null, null);

               /* if (dialog.isShowing()) {
                    dialog.dismiss();
                }*/
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
      //  facebold = Typeface.createFromAsset(getActivity().getAssets(), "ubuntu_bold.ttf");
//        face_light = Typeface.createFromAsset(getActivity().getAssets(), "ubuntu_light.ttf");


    }

    public static class EventViewAdapter extends FragmentPagerAdapter {

        private String[] mtitle = new String[]{"Invited", "Organizing"};
        //private User user2;
        private Bundle bundle;

        public EventViewAdapter(FragmentManager fm, Bundle bundle2) {
            super(fm);
            bundle = bundle2;
            //bundle.putParcelable("user", user2);

        }

        public void update(Bundle bundle){
            this.bundle = bundle;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            // Return three sections for friends lists
            return 2;
        }

        @Override
        public Fragment getItem(int index) {
            final OrganizingEventFragment m = new OrganizingEventFragment();
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
