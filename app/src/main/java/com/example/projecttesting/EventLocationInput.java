package com.example.projecttesting;

import com.example.projecttesting.PlacesReturnLocation.AsyncTaskRunner;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;

import com.google.android.gms.common.api.ResultCallback;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("unused")
public class EventLocationInput extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    AutoCompleteTextView placeAutocomplete;
    PlacesAutoCompleteAdapter adapter;
    String selected_location_text = null;

//    private TextView mPlaceDetailsText;
    //   private TextView mPlaceDetailsAttribution;

    Typeface typeface;
    Context mContext;
    Activity mActivity;
    TextView mTextView;
    LatLng latLng;
    CharSequence primaryText;
    GoogleApiClient mGoogleApiClient;
    //public static final String API_KEY = "AIzaSyCEBmXKQ5k42UsKvCmZBPnmv3BDTqds52k";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_location_input);
        mActivity = this;
        mContext = EventLocationInput.this;

        // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
        // functionality, which automatically sets up the API client to handle Activity lifecycle
        // events. If your activity does not extend FragmentActivity, make sure to call connect()
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addApi(Places.GEO_DATA_API)
                        //.addApi(Places.PLACE_DETECTION_API)
                        //.addConnectionCallbacks(this)
                        //.addOnConnectionFailedListener(this)
                .build();


        typeface = FontCache.getFont(this, "sf_reg.ttf");

        // Setting up location input Autocomplete
        placeAutocomplete = (AutoCompleteTextView) findViewById(R.id.eventPlaceAutocomplete);

        placeAutocomplete.setTypeface(typeface);

        placeAutocomplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 2) {
                    adapter.getFilter().filter(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // Set the location input Autocomplete
        adapter = new PlacesAutoCompleteAdapter(this, mGoogleApiClient, null, null);

        placeAutocomplete.setAdapter(adapter);

        // Remove the background shadow of autocomplete
        placeAutocomplete.setDropDownBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.eventautocomplete_dropdownstyle, null));

        placeAutocomplete.setOnItemClickListener(mAutocompleteClickListener);
    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();

        Intent intent = new Intent(EventLocationInput.this, EventCreation.class);
        intent.putExtra("requestCode", 1);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data API
     * to retrieve more details about the place.
     *
     * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
     * String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = adapter.getItem(position);
            final String placeId = item.getPlaceId();
            primaryText = item.getPrimaryText(null);


            Log.i("TAG", "Autocomplete item selected: " + primaryText);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
             details about the place.
              */
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);


            //Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
            //        Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e("TAG", "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);

            latLng = place.getLatLng();

            double lat = latLng.latitude;
            double lng = latLng.longitude;

            // Hide Keyboard after making selection
            InputMethodManager in = (InputMethodManager) getApplicationContext().getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), 0);

            //Log.i("TAG", "Called getPlaceById to get Place details for " + placeId);

            Intent i = new Intent(EventLocationInput.this, EventCreation.class);
            i.putExtra("selectedLocation", primaryText.toString());
            i.putExtra("LATITUDE", lat);
            i.putExtra("LONGITUDE", lng);
            i.putExtra("requestCode", 1);
            EventLocationInput.this.setResult(Activity.RESULT_OK, i);
            EventLocationInput.this.finish();

            Log.i("TAG", "Place details received: " + place.getName());

            places.release();
        }
    };

    /*private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        Log.e("TAG", res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }*/

    /**
     * Called when the Activity could not connect to Google Play services and the auto manager
     * could resolve the error automatically.
     * In this case the API is not available and notify the user.
     *
     * @param connectionResult can be inspected to determine the cause of the failure
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        Log.e("onConnectionFailed", "onConnectionFailed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());

        // TODO(Developer): Check error code and notify the user of error state and resolution.
        Toast.makeText(this,
                "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                Toast.LENGTH_SHORT).show();
    }

}
