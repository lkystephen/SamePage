package com.example.projecttesting;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ParseLocation extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    String distance, destination;
    User user;
    public LocationAsyncResponse delegate = null;

    // Testing data
    String id = "106808403007880";

    public ParseLocation(User user){
        this.user = user;
    }

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

            distance = duration.getString("text");
            Log.i("travel time", distance);

            String tempDes = leg_1.getString("end_address");//.getString("end_address");
            String[] parts = tempDes.split(", ");
            destination = parts[0] + ", " + parts[1];
            Log.i("destination", destination);

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
        ArrayList<String> output = new ArrayList<>();
        //ArrayList<LatLng> points = null;
        //PolylineOptions lineOptions = null;
        //MarkerOptions markerOptions = new MarkerOptions();

        // Get the list of OtherUser
        List<OtherUser> otherUsers = user.getMasterList();

        // Get the name of the closest friend
        int sizeOfMasterFriends = user.getMasterList().size();
        String name = new String();

        for (int i = 0; i < sizeOfMasterFriends; i++) {
            String id2 = otherUsers.get(i).fbid;
            if (id2.equals(id)) {
                name = otherUsers.get(i).username;
                break;
            }
        }

        output.add(name);
        output.add(distance);
        output.add(destination);

        delegate.processFinish(output);

        // Set the distance measured in minutes
        //closest_location.setText(name + " is closest to you");
        //closest_location_details.setText("@ "+destination);
        //closest_location_details2.setText(distance + " away");

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


}