package com.example.projecttesting;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class PlacesAutocomplete {

	private static final String LOG_TAG = "locationtrial";
	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";

	
public static ArrayList<String> autocomplete(String input) {
    ArrayList<String> resultList = null;

    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();
    try {
        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
        sb.append("?key=" + MainActivity.API_KEY);
        sb.append("&components;=country:uk");
        sb.append("&input=" + URLEncoder.encode(input, "utf8"));

        URL url = new URL(sb.toString());
        //Log.i("PLACES",sb.toString());
        conn = (HttpURLConnection) url.openConnection();
        InputStreamReader in = new InputStreamReader(conn.getInputStream());

        
        // Load the results into a StringBuilder
        int read;
        char[] buff = new char[1024];
        while ((read = in.read(buff)) != -1) {
            jsonResults.append(buff, 0, read);
            Log.e("JASON",jsonResults.toString());
        }
    } catch (MalformedURLException e) {
        Log.e(LOG_TAG, "Error processing Places API URL", e);
        return resultList;
    } catch (IOException e) {
        Log.e(LOG_TAG, "Error connecting to Places API", e);
        return resultList;
    } finally {
        if (conn != null) {
            conn.disconnect();
        }
    }

    try {
        // Create a JSON object hierarchy from the results
        JSONObject jsonObj = new JSONObject(jsonResults.toString());
        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

        // Extract the Place descriptions and place id from the results
        resultList = new ArrayList<String>(predsJsonArray.length());
        for (int i = 0; i < predsJsonArray.length(); i++) {
        	HashMap<String, String> place = new HashMap<String, String>();
        	String description = predsJsonArray.getJSONObject(i).getString("description");
            String placeId = predsJsonArray.getJSONObject(i).getString("place_id");
            place.put("description", description);
            place.put("place_id", placeId);
            
            resultList.add(place.toString());
          
        }

    } catch (JSONException e) {
        Log.e(LOG_TAG, "Cannot process JSON results", e);
    }
    
    return resultList;
}

}
//end of AutoComplete

