package com.example.projecttesting;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlacesReturnLocation {
	double lat;
	double lng;
	LatLng selectedLocation;

	public PlacesReturnLocation() {
		selectedLocation = new LatLng(lat, lng);
	};

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
			Log.d("Exception dl URL", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
		// data is returned in the form of string

	}

	public class AsyncTaskRunner extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {

			// Do operations here and return the result
			URL urls = null;
			try {
				urls = new URL(
						"https://maps.googleapis.com/maps/api/place/details/json?placeid="
								+ params[0] + "&key=" + MainActivity.API_KEY);
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
			return data;
		}

		@Override
		public void onPostExecute(String data) {
			super.onPostExecute(data);
			JSONObject jObj = null;
			LatLng selectedLocation = null;

			try {
				jObj = new JSONObject(data);
				// Fetch Lat and Lng from the JSON object
				double lat = jObj.getJSONObject("result")
						.getJSONObject("geometry").getJSONObject("location")
						.getDouble("lat");

				double lng = jObj.getJSONObject("result")
						.getJSONObject("geometry").getJSONObject("location")
						.getDouble("lng");

				selectedLocation = new LatLng(lat, lng);

				// Make Marker on map
				if (MainFragment.selectedMarker != null) {
					MainFragment.selectedMarker.remove();
				}
				MainFragment.selectedMarker = MainFragment.mMapFragment.getMap()
						.addMarker(new MarkerOptions().position(
								selectedLocation).title("Selected place"));
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
						selectedLocation, 15);
				MainFragment.mMapFragment.getMap().animateCamera(cameraUpdate);

			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}

		}
	}

}
