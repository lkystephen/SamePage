package com.example.projecttesting;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainFragment extends Fragment implements LocationListener, TextWatcher {

	/*public int[] friendsLastOnlineTime = new int[] { 1, 10, 5};
    public int[] friends_image = {R.drawable.edmund, R.drawable.lkk};
	// friendsStarredStatus, 1 = starred, 0 is normal
	public int[] friendsStarredStatus = new int[] { 1, 1};*/

    //public static GoogleMap googleMap;
    public static Marker selectedMarker;
    public LatLng myPosition;
    public int travel_time;
    public AutoCompleteTextView autoCompView;
    //ImageButton delButton;
    LatLng currentLatLng;
    TextView closest_location, event_details1;
    public static SupportMapFragment mMapFragment;
    //ArrayList<FriendsRowItem> rowItems;
    //ImageView test_button, test_button2;


    // Define testing location data
    final LatLng test_QC_location = new LatLng(22.2814, 114.1916);

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        User user = bundle.getParcelable("user");
        LocationListener mLocationListener = this;

        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.mainmap, container, false);

        // Typeface
        Typeface typeface_reg = FontCache.getFont(getContext(),"sf_bold.ttf");

        closest_location = (TextView) rootView.findViewById(R.id.closest_location);
        event_details1 = (TextView) rootView.findViewById(R.id.event_details1);

        closest_location.setTypeface(typeface_reg);
        event_details1.setTypeface(typeface_reg);

        // Get facebook photo and turn to bitmap

        //ImageView image = (ImageView) rootView.findViewById(R.id.welcome_image);

        //Bitmap bitmap = BitmapFactory.decodeFile(Utility.getImage(user.getFBId()).getPath());
        //if(bitmap == null) {
        //	Log.i("Image returned","null");
        //}
        //image.setImageBitmap(bitmap);

        Context context;
        context = this.getActivity().getApplicationContext();

		/*autoCompView = (AutoCompleteTextView) rootView.findViewById(R.id.autocompletetext);
        Typeface face;
		face = Typeface.createFromAsset(getActivity().getAssets(), "sf_reg.ttf");
		autoCompView.setTypeface(face);
*/

        // Get names of friends
        final List<OtherUser> master_list = user.getMasterList();
/*
		autoCompView.setSelectAllOnFocus(true);
		String[] columnForMatch = new String[]{"placeDesc", "placeSecondName"};
		int[] columnToMatch = new int[]{R.id.resulttext, R.id.placesecondname};
		autoCompView.setAdapter(new PlacesAutoCompleteAdapter(getActivity()
				.getApplicationContext(), null, R.layout.list_item,
				columnForMatch, columnToMatch));
*/
		/*PlacesAdapterListitemsOnClick adapter = new PlacesAdapterListitemsOnClick(
				this.getActivity(), "MainPageInput", context);
		autoCompView.setOnItemClickListener(adapter);
*/
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        String bestProvider = locationManager.NETWORK_PROVIDER;
        final Location location = locationManager.getLastKnownLocation(bestProvider);
        currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        //final FrameLayout mapLayout = (FrameLayout) getActivity().findViewById(R.id.map);

		/*mMapFragment = new SupportMapFragment();
		android.support.v4.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
		fragmentTransaction.add(R.id.map, mMapFragment).commit();*/

        if (location != null) {
            onLocationChanged(location);
            locationManager.removeUpdates(mLocationListener);
        }

        locationManager.requestLocationUpdates(bestProvider, 50000, 50, mLocationListener);
/*
		// EditText delete button
		delButton = (ImageButton) rootView.findViewById(R.id.deletetextbutton);
		delButton.setVisibility(View.INVISIBLE);
		delButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				autoCompView.setText("");
			}
		});

		autoCompView.addTextChangedListener(this);
		*/
        // Calculating location now
        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(myPosition, test_QC_location);
        DownloadTask downloadTask = new DownloadTask();
        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        // Set drag and drop listener for the main button
        //test_button.setOnTouchListener(new MyTouchListener());
        //drop_zone_test.setOnDragListener(new MyDragListener());

        return rootView;
    }

    class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // Do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    // Do nothing
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    // Do nothing
                    break;
                case DragEvent.ACTION_DROP:
                    //   View view = (View) event.getLocalState();
                    //  ViewGroup owner = (ViewGroup) view.getParent();
                    //owner.removeView(view);
                    //test_button.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Oh yeah", Toast.LENGTH_LONG).show();
                    int[] coordinates = {0,0};
                    //test_button2.getLocationOnScreen(coordinates);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    // Do nothing
                default:
                    break;
            }
            return true;
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }


    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        myPosition = new LatLng(latitude, longitude);
        // destroy the old marker to prevent duplicated markers when new
        // location is received
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Default the mode of transportation to public transit
        String mode_transit = "mode=transit";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode_transit + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        Log.i("Download URL", url);
        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception dl-ing url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);

            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Set calculating display
                    closest_location.setText("Calculating...");
                }
            });


            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                // Get overall points information
                JSONArray routes_test = jObject.getJSONArray("routes");
                JSONObject route_1 = routes_test.getJSONObject(0);
                JSONArray leg = route_1.getJSONArray("legs");
                JSONObject leg_1 = leg.getJSONObject(0);
                JSONObject duration = leg_1.getJSONObject("duration");

                travel_time = duration.getInt("value");
                Log.i("travel time", Integer.toString(travel_time));

                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing routing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Set the distance measured in minutes
            closest_location.setText("is " + travel_time / 60 + "mins away");



            /*
            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
                */
        }

        // Drawing polyline in the Google Map for the i-th route
        //mMapFragment.getMap().addPolyline(lineOptions);
        //}
    }

    @Override
    public void afterTextChanged(Editable arg0) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String input = autoCompView.getText().toString();

        if (input.equals("")) {
            //delButton.setVisibility(View.INVISIBLE);
        } else {
            //delButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int before, int count) {

    }


}
/*
	@Override
	public void onMapReady(GoogleMap googleMap) {
		MapObjectControl control = new MapObjectControl();
		if (currentLatLng == null){
			currentLatLng = new LatLng(0,0);
		}
		//Log.i("latlng",currentLatLng.toString());

		googleMap.setMyLocationEnabled(true);

		control.MovedToCurrentLoc(currentLatLng, googleMap, 15);
		googleMap.addMarker(new MarkerOptions().position(test_QC_location));

		// Set marker click listener to display route
		googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
			@Override
			public boolean onMarkerClick(Marker marker) {

				// Getting URL to the Google Directions API
				String url = getDirectionsUrl(myPosition, test_QC_location);

				DownloadTask downloadTask = new DownloadTask();

				// Start downloading json data from Google Directions API
				downloadTask.execute(url);

				return false;
			}
		});
	}
}
*/