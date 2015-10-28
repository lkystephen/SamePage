package com.example.projecttesting;

import com.example.projecttesting.PlacesReturnLocation.AsyncTaskRunner;
import com.google.android.gms.maps.model.LatLng;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;

import java.util.concurrent.ExecutionException;

@SuppressWarnings("unused")
public class EventLocationInput extends ActionBarActivity {

	AutoCompleteTextView placeAutocomplete;
	String selected_location_text = null;
	Context mContext;
	Activity mActivity;
	TextView mTextView;
	LatLng location_coordinates;
	public static final String API_KEY = "AIzaSyCEBmXKQ5k42UsKvCmZBPnmv3BDTqds52k";
	

	/*@Override
	protected void onDestroy(){
		super.onDestroy();
		Log.w("onDestroy", "onDestroy");		
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		Log.w("onPause", "onPause");		
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		Log.w("onResume", "onResume");		
	}*/

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event_location_input);
		mActivity = this;
		mContext = EventLocationInput.this;

		// Setting up location input Autocomplete
		placeAutocomplete = (AutoCompleteTextView) findViewById(R.id.eventPlaceAutocomplete);

		// Set the location input Autocomplete
		String[] columnForMatch = new String[] { "placeDesc", "placeSecondName" };
		int[] columnToMatch = new int[] { R.id.resulttext, R.id.placesecondname };
		placeAutocomplete.setAdapter(new PlacesAutoCompleteAdapter(this, null,
				R.layout.event_location_list_item, columnForMatch,
				columnToMatch));

		// Remove the background shadow of autocomplete
		placeAutocomplete.setDropDownBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.eventautocomplete_dropdownstyle));

		placeAutocomplete.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				String selection_locationID = PlacesAutoCompleteAdapter.mArrayList
						.get(position).get("placeId").toString();
				Log.i("locationid", selection_locationID);

				// Start the activity to download JSON and get location

				EventReturnLocation othersLocation = new EventReturnLocation();
				EventReturnLocation.EventAsyncTaskRunner locationRunner = othersLocation.new EventAsyncTaskRunner();
				try {
					location_coordinates = locationRunner.execute(selection_locationID).get();
				} catch (InterruptedException e){
					e.printStackTrace();
				} catch (ExecutionException e){
					e.printStackTrace();
				}

				//LatLng location_coordinates = othersLocation.selectedLocation;

				double lat = location_coordinates.latitude;
				double lng = location_coordinates.longitude;
				//Log.i("diu, lat",Double.toString(lat));

				// Hide Keyboard after making selection
				InputMethodManager in = (InputMethodManager) mActivity
						.getApplicationContext().getSystemService(
								Context.INPUT_METHOD_SERVICE);
				in.hideSoftInputFromWindow(mActivity.getCurrentFocus()
						.getWindowToken(), 0);

				
				TextView assd = (TextView) view.findViewById(R.id.resulttext);
				selected_location_text = assd.getText().toString();

				Intent i = new Intent(view.getContext(), EventCreation.class);
				i.putExtra("selectedLocation", selected_location_text);
				i.putExtra("LATITUDE", lat);
				i.putExtra("LONGITUDE",lng);
				i.putExtra("requestCode", 1);
				// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
				// Intent.FLAG_ACTIVITY_CLEAR_TASK);
				//((Activity) view.getContext()).setResult(RESULT_OK, i);
				mActivity.setResult(RESULT_OK, i);
				
				//((Activity) view.getContext()).finish();
				mActivity.finish();
			}

		}

		);

	}
	
	@Override
	public void onBackPressed() {
		// super.onBackPressed();

		Intent intent = new Intent(EventLocationInput.this, EventCreation.class);
		intent.putExtra("requestCode", 1);
		setResult(RESULT_CANCELED, intent);
		finish();
	}
}
