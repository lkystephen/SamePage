package com.example.projecttesting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.prefs.PreferenceChangeEvent;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.widget.FrameLayout.LayoutParams;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

//fb imports
import com.astuetz.PagerSlidingTabStrip;
import com.astuetz.PagerSlidingTabStrip.IconTabProvider;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.location.LocationListener;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


@SuppressWarnings("unused")
public class MainActivity extends AppCompatActivity implements MainAct, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

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

    // Google Play service location
    private LocationRequest mLocationRequest;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    // Toggle periodic location updates
    private boolean mRequestingLocationUpdates = false; //boolean flag to toggle periodic location updates
    private static int LONG_INTERVAL = 30000; // 30 sec
    private static int SHORT_INTERVAL = 20000; // 20 sec

    // user
    public User user;

    public static String PACKAGE_NAME;

    String selection_locationID;
    int temp;
    //public static ArrayList<String> placeID = new ArrayList<String>();

    public static final String API_KEY = "AIzaSyCDY8ulp1VGKwGdaRU19G4sfuXsymZGgoY";

    public Fragment fragment;

    private static final String TAG = MainActivity.class.getSimpleName();

    // Circular FAB
    public FloatingActionMenu actionMenu;

    public void handleLoginResults(boolean isNewUser, Users users) {
        getLocation();

        // Retrieve display photos
        RetrieveFBPhotos retrieve = new RetrieveFBPhotos();
        retrieve.execute(null, null, null);
    }

    @Override
    public void handleGetFrdsLocResults(final HashMap<String, OtherUser> masterListwLoc) {
    }

    public class mOnClickListener implements View.OnClickListener {
        int type;
        Context context;
        FragmentManager fm;

        public mOnClickListener(int type, Context context) {
            this.type = type;
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            fm = getSupportFragmentManager();
            // Remove previous fragment first

            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            actionMenu.close(true);

            switch (type) {
                case 1:
                    MainFragment fragment = new MainFragment();
                    fragment.setArguments(bundle);
                    fragmentTransaction.replace(R.id.mFragment, fragment);
                    fragmentTransaction.commit();
                    break;
                case 2:
                    EventFragment fragment2 = new EventFragment();
                    fragment2.setArguments(bundle);
                    fragmentTransaction.replace(R.id.mFragment, fragment2);
                    fragmentTransaction.commit();
                    break;
                case 3:
                    FriendsFragment fragment3 = new FriendsFragment();
                    fragment3.setArguments(bundle);
                    fragmentTransaction.replace(R.id.mFragment, fragment3);
                    fragmentTransaction.commit();
                    break;
                case 4:
                    SettingsFragment fragment4 = new SettingsFragment();
                    fragment4.setArguments(bundle);
                    fragmentTransaction.replace(R.id.mFragment, fragment4);
                    fragmentTransaction.commit();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        super.onCreate(savedInstanceState);

        // Set loading screen
        setContentView(R.layout.loading);

        // Check availability of play services
        if (checkPlayServices()) {

            // Building the GoogleApi client
            buildGoogleApiClient();

            // Create periodic request
            createLocationRequest();
        }

        // Check permission for dangerous permissions
        /*if (ContextCompat.checkSelfPermission(this,
                Manifest.permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {
                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }*/

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
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.action_settings);
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

    public class RetrieveFBPhotos extends AsyncTask<Void, Void, Integer> {
        protected Integer doInBackground(Void... params) {

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            String downloaded = preferences.getString("download", "no");
            Log.i("Download indicator", downloaded);
            String old_list = preferences.getString("friends_id", "no");
            Log.i("Download frds indicator", old_list);
            List<OtherUser> temp = user.getMasterList();
            String temp2 = user.getUserId();
            for (int i = 0; i < temp.size(); i++) {
                String temp3 = new StringBuilder().append(temp2).append("|").append(temp.get(i).fbid).toString();
                temp2 = temp3;
            }
            Log.i("Generated dl indicator", temp2);
            //int last_number = preferences.getInt("friends",0);
            //int this_number = user.getMasterList().size();
            if (downloaded.equals("yes") && temp2.equals(old_list)) {
                // photos have been downloaded before and friends unchanged
                Log.i("FB display", "Have been downloaded before");
                // Time to move on
                // user.getMasterList --> all friends
            } else {
                Log.i("FB display", "Will download photos now");
                Utility utility = new Utility();

                // Download your own image first
                Bitmap photo = utility.downloadImage(user.getFBId());
                utility.storeImage(photo, user.getFBId());

                for (int i = 0; i < temp.size(); i++) {
                    Bitmap frd_photo = utility.downloadImage(temp.get(i).fbid);
                    utility.storeImage(frd_photo, temp.get(i).fbid);
                }
                // Update value in sharedpref
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("download", "yes");
                editor.putString("friends_id", temp2);
                editor.apply();

            }
            return 1;
        }

        protected void onPostExecute(Integer result) {
            //Log.i("come on", "dude");
            setContentView(R.layout.activity_main);

            PACKAGE_NAME = getApplicationContext().getPackageName();

            Bundle abc = new Bundle();
            abc.putParcelable("user", user);

            // Create main floating icon
            ImageView add_icon = new ImageView(MainActivity.this);
            add_icon.setImageResource(R.drawable.more_w);

            int[][] states = {{android.R.attr.state_enabled}, {
                    android.R.attr.state_pressed}};
            int[] colors = {Color.parseColor("#ff4f6069"), Color.parseColor("#ffffff")};
            ColorStateList colorStateList = new ColorStateList(states, colors);

            FloatingActionButton actionButton = new FloatingActionButton.Builder(MainActivity.this).setContentView(add_icon).build();
            actionButton.setBackgroundTintList(colorStateList);

            // Create sub menu items
            SubActionButton.Builder itemBuilder = new SubActionButton.Builder(MainActivity.this);
            ImageView homeIcon = new ImageView(MainActivity.this);
            homeIcon.setImageResource(R.drawable.button_home_w);
            ImageView eventIcon = new ImageView(MainActivity.this);
            eventIcon.setImageResource(R.drawable.button_event_w);
            ImageView friendsIcon = new ImageView(MainActivity.this);
            friendsIcon.setImageResource(R.drawable.button_friends_w);
            ImageView settingIcon = new ImageView(MainActivity.this);
            settingIcon.setImageResource(R.drawable.button_settings_w);

            SubActionButton button1 = itemBuilder.setContentView(homeIcon).build();
            SubActionButton button2 = itemBuilder.setContentView(eventIcon).build();
            SubActionButton button3 = itemBuilder.setContentView(friendsIcon).build();
            SubActionButton button4 = itemBuilder.setContentView(settingIcon).build();


            int size = getResources().getDimensionPixelSize(com.oguzdev.circularfloatingactionmenu.library.R.dimen.sub_action_button_size);
            Log.i("size", Integer.toString(size));
            int size2 = (int) Math.round(size * 1.3);
            LayoutParams params = new LayoutParams(size2, size2, 51);

            button1.setBackgroundTintList(colorStateList);
            button2.setBackgroundTintList(colorStateList);
            button3.setBackgroundTintList(colorStateList);
            button4.setBackgroundTintList(colorStateList);

            // Enlarge the sub action buttons
            button1.setLayoutParams(params);
            button2.setLayoutParams(params);
            button3.setLayoutParams(params);
            button4.setLayoutParams(params);

            actionMenu = new FloatingActionMenu.Builder(MainActivity.this).addSubActionView(button1)
                    .addSubActionView(button2).addSubActionView(button3).addSubActionView(button4).attachTo(actionButton).build();

            //Instantiate the Main Fragment
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            MainFragment mainFragment = new MainFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            Log.i("Number of friends", Integer.toString(user.getMasterList().size()));
            mainFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.mFragment, mainFragment);
            fragmentTransaction.commit();

            //LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // update user location
            //LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //String bestProvider = locationManager.NETWORK_PROVIDER;
            //final Location location = locationManager.getLastKnownLocation(bestProvider);

            button1.setOnClickListener(new mOnClickListener(1, MainActivity.this));
            button2.setOnClickListener(new mOnClickListener(2, MainActivity.this));
            button3.setOnClickListener(new mOnClickListener(3, MainActivity.this));
            button4.setOnClickListener(new mOnClickListener(4, MainActivity.this));

            // After everything is said and done, time to toggle periodic update for location
            togglePeriodicLocationUpdates();  // this is periodic update when app is active

            // This is periodic update when app is inactive
            Intent intent = new Intent(MainActivity.this, MyLocationHandler.class);
            Bundle b = new Bundle();
            bundle.putParcelable("user", user);
            intent.putExtra("bundle",b);
            intent.setAction("MyLocationHandler");

            PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this ,0, intent ,PendingIntent.FLAG_UPDATE_CURRENT);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, pendingIntent);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {
        // Once connected with google api, get the location
        if (user != null)
            getLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }

    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    /**
     * Method to display the location on UI
     */
    private void getLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            user.updateLocation(mLastLocation);

            Log.e("Location", Double.toString(latitude) + ", " + Double.toString(longitude));

        } else {
            Toast.makeText(MainActivity.this, "Cannot get location", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Method to verify google play services on the device
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        //Toast.makeText(getApplicationContext(), "Location changed!",
        //        Toast.LENGTH_SHORT).show();

        // Update the latest location to server
        getLocation();
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Creating location request object
     */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(LONG_INTERVAL);
        mLocationRequest.setFastestInterval(SHORT_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient!= null){
            stopLocationUpdates();
        }
    }

    /**
     * Method to toggle periodic location updates
     */
    private void togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {

            mRequestingLocationUpdates = true;
            // Starting the location updates
            startLocationUpdates();

            Log.d(TAG, "Periodic location updates started!");

        } else {
            mRequestingLocationUpdates = false;
            // Stopping the location updates
            stopLocationUpdates();
            Log.d(TAG, "Periodic location updates stopped!");
        }
    }

    // Building pendingIntent
    //private PendingIntent buildRequestPendingIntent(Intent theIntent) {
        //Log.w(TAG, "building pending intent");
      //  return PendingIntent.getBroadcast(MainActivity.this, 0, theIntent, 0);
    //}

}