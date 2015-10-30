package com.example.projecttesting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//fb imports
import com.astuetz.PagerSlidingTabStrip;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.gcm.GoogleCloudMessaging;

@SuppressWarnings("unused")
public class MainActivity extends ActionBarActivity implements MainAct {

    // Backpress setting
    private long backPressedTime = 0;

    // Facebook login variables
    CallbackManager callbackManager;
    private String fbid;
    private ProfilePictureView proPic;
    private String username;

    //for GCM
    GoogleCloudMessaging gcm;
    String regid;
    String PROJECT_NUMBER = "603098203110";

    //user info
    HashMap events;

    // user
    public User user;

    public static String PACKAGE_NAME;

    String selection_locationID;
    int temp;
    //public static ArrayList<String> placeID = new ArrayList<String>();

    public static final String API_KEY = "AIzaSyCEBmXKQ5k42UsKvCmZBPnmv3BDTqds52k";

    public Fragment fragment;
    public ViewPager mViewPager;
    public PagerSlidingTabStrip tabs;
    //Bundle bundle;

    public void handleLoginResults(boolean isNewUser, Users users) {

        RetrieveFBPhotos retrieve = new RetrieveFBPhotos();
        retrieve.execute(null,null,null);
    }

    @Override
    public void handleGetFrdsLocResults(final HashMap<String, OtherUser> masterListwLoc) {

        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        mViewPager = (ViewPager) findViewById(R.id.main_viewPager);

        Bundle abc = new Bundle();
        abc.putParcelable("user", user);
        mViewPager.setAdapter(new MainViewAdapter(getSupportFragmentManager(), abc));
        mViewPager.setOffscreenPageLimit(4);

        Typeface face = Typeface.createFromAsset(getAssets(), "sf_bold.ttf");

        // Assigning the Sliding Tab Layout View
        tabs = (PagerSlidingTabStrip) findViewById(R.id.main_tabs);
        tabs.setTypeface(face, 0);
        tabs.setTextColor(Color.parseColor("#000000"));


        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(mViewPager);

        LayoutInflater inflator = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.title_actionbar, null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);
        actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#f9f9f9")));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        //actionBar.setStackedBackgroundDrawable(new ColorDrawable(Color.parseColor("#f9f9f9")));

        TextView title = (TextView) v.findViewById(R.id.app_title);
        title.setTypeface(face);
        actionBar.setCustomView(v);

        // update user location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String bestProvider = locationManager.NETWORK_PROVIDER;
        final Location location = locationManager.getLastKnownLocation(bestProvider);
        user.updateLocation(location);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        //get RegId
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                Log.i("GCM", "starting");

                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                    // msg = "Device registered, registration ID=" + regid;
                    Log.i("GCM", regid);

                } catch (IOException ex) {
                    Log.i("GCM", "Error :" + ex.getMessage());
                }
                return regid;
            }

            @Override
            protected void onPostExecute(String regId_input) {
                // get with db
                fbid = Profile.getCurrentProfile().getId();
                Log.i("name", Profile.getCurrentProfile().getName());
                username = Profile.getCurrentProfile().getName();
                fbid = Profile.getCurrentProfile().getId();

                user = new User(fbid, username, regId_input, MainActivity.this);
                user.execute();
            }
        }.execute(null, null, null);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item= menu.findItem(R.id.action_settings);
        item.setVisible(false);
        super.onPrepareOptionsMenu(menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        long t = System.currentTimeMillis();
        if (t - backPressedTime > 2000) {
            backPressedTime = t;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        } else {
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
        }
    }

    public class MainViewAdapter extends FragmentPagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        Bundle bundle;
        //private String[] mtitle = new String[]{"Main", "Event", "Friends"};

        private int[] imageResID = {
                R.drawable.button_home, R.drawable.button_event, R.drawable.button_friends, R.drawable.button_settings
        };

        public MainViewAdapter(FragmentManager fm, Bundle bundle) {
            super(fm);
            this.bundle = bundle;
        }

        @Override
        public int getCount() {
            // Return sections
            return 4;
        }

        @Override
        public Fragment getItem(int index) {
            final MainFragment a = new MainFragment();
            final EventFragment b = new EventFragment();
            final FriendsFragment c = new FriendsFragment();
            final SettingsFragment d = new SettingsFragment();

            switch (index) {
                case 0:
                    a.setArguments(bundle);
                    return a;
                case 1:
                    b.setArguments(bundle);
                    return b;
                case 2:
                    c.setArguments(bundle);
                    return c;
                case 3:
                    d.setArguments(bundle);
                    return d;
                default:
                    return new Fragment();
            }
        }

        @Override
        public int getPageIconResId(int position) {
            return imageResID[position];
        }
    }

    public class RetrieveFBPhotos extends AsyncTask<Void, Void, Integer>{
        protected Integer doInBackground(Void... params){

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String downloaded = preferences.getString("download", "no");
            Log.i("Download indicator",downloaded);
            String old_list = preferences.getString("friends_id","no");
            Log.i("Download frds indicator",old_list);
            List<OtherUser> temp = user.getMasterList();
            String temp2 = user.getUserId();
            for (int i = 0; i < temp.size(); i++){
                String temp3 = new StringBuilder().append(temp2).append("|").append(temp.get(i).fbid).toString();
                temp2 = temp3;
            }
            Log.i("Generated dl indicator",temp2);
            //int last_number = preferences.getInt("friends",0);
            //int this_number = user.getMasterList().size();
            if (downloaded.equals("yes") || temp2.equals(old_list)){
                // photos have been downloaded before and friends unchanged
                Log.i("FB display","Have been downloaded before");
                // Time to move on
                // user.getMasterList --> all friends
            } else {
                Log.i("FB display","Will download photos now");
                Utility utility = new Utility();

                // Download your own image first
                Bitmap photo = utility.downloadImage(user.getFBId());
                utility.storeImage(photo, user.getFBId());

                for (int i=0; i< temp.size(); i++) {
                    Bitmap frd_photo = utility.downloadImage(temp.get(i).fbid);
                    utility.storeImage(frd_photo, temp.get(i).fbid);
                }
                    // Update value in sharedpref
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("download","yes");
                    editor.putString("friends_id", temp2);
                    editor.apply();

            }
            return 1;
        }

        protected void onPostExecute(Integer result){
            Log.i("come on","dude");
            user.getMasterListwLoc(MainActivity.this);
        }
    }

}

