package com.example.projecttesting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventReturnLocation extends Fragment {
    public double lat;
    public double lng;
    LatLng selectedLocation;
    EditText locationDisplay;

    public EventReturnLocation() {
        selectedLocation = new LatLng(lat, lng);
    }

    ;

    private String downloadUrl(URL url) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            // Creating an HTTP connection to communicate with URL
            Log.i("URL", url.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connect to URL
            urlConnection.connect();
            // Reading data from URL
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception dl url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
        // data is returned in the form of string

    }

    public class EventAsyncTaskRunner extends AsyncTask<String, Void, LatLng> {

        @Override
        protected LatLng doInBackground(String... params) {

            // Do operations here and return the result
            URL urls = null;
            try {
                urls = new URL(
                        "https://maps.googleapis.com/maps/api/place/details/json?placeid="
                                + params[0] + "&key=" + MainActivity.API_KEY);
                //Log.i("url",urls.toString());
            } catch (MalformedURLException e1) {
            }

            // data is for storing info from url
            String data = new String();
            try {
                data = downloadUrl(urls);
                // Log.i("test",data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            //return data;

            JSONObject jObj = null;
            LatLng selectedLocation = null;

            try {
                jObj = new JSONObject(data);
                //Log.i("diu jobj",jObj.toString());
                // Fetch Lat and Lng from the JSON object
                double lat = jObj.getJSONObject("result")
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");

                Log.i("diu diu lat", Double.toString(lat));

                double lng = jObj.getJSONObject("result")
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                //double lat_double = Double.valueOf(lat);
                //double lng_double = Double.valueOf(lng);
                selectedLocation = new LatLng(lat, lng);

                // Return selectedLocation to outside
                // listener.processFinish(selectedLocation);
                // delegate.processFinish(selectedLocation);

            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }
            return selectedLocation;

        }

        @Override
        public void onPostExecute(LatLng latLng) {
            super.onPostExecute(latLng);
            /*JSONObject jObj = null;
			LatLng selectedLocation = null;

			try {
				jObj = new JSONObject(data);
				//Log.i("diu jobj",jObj.toString());
				// Fetch Lat and Lng from the JSON object
				double lat = jObj.getJSONObject("result")
						.getJSONObject("geometry").getJSONObject("location")
						.getDouble("lat");

				Log.i("diu diu lat",Double.toString(lat));

				double lng = jObj.getJSONObject("result")
						.getJSONObject("geometry").getJSONObject("location")
						.getDouble("lng");

				//double lat_double = Double.valueOf(lat);
				//double lng_double = Double.valueOf(lng);
				selectedLocation = new LatLng(lat, lng);

				// Return selectedLocation to outside
				// listener.processFinish(selectedLocation);
				// delegate.processFinish(selectedLocation);


			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
		return selectedLocation;
		*/
        }

    }
}
