package com.example.projecttesting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.example.projecttesting.EventReturnLocation.EventAsyncTaskRunner;
import com.example.projecttesting.PlacesReturnLocation.AsyncTaskRunner;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.ExecutionException;

public class PlacesAdapterListitemsOnClick extends Activity implements
		OnItemClickListener {
	Activity mainActivity;
	Context contextToBeKilled;
	String mInputPage, selectedLocation;
	LatLng coordinates;

	// ReturnOthersLocation returnOthers = new ReturnOthersLocation();
	// AsyncTaskRunner asyncTask = returnOthers.new AsyncTaskRunner();

	public PlacesAdapterListitemsOnClick(Activity activity, String inputPage,
			Context mContext) {
		mainActivity = activity;
		mInputPage = inputPage;
		contextToBeKilled = mContext;
		Log.i("context", contextToBeKilled.toString());
	}

	// add onClickListener to detect when user make a selection to detect the
	// selection
	public void onItemClick(AdapterView<?> autoCompView, View view,
			int position, long id) {

		String selection_locationID = PlacesAutoCompleteAdapter.mArrayList
				.get(position).get("placeId").toString();
		Log.i("locationid", selection_locationID);

		autoCompView.clearFocus();
		// Start the activity to download JSON and get location
		EventReturnLocation othersLocation = new EventReturnLocation();
		EventAsyncTaskRunner locationRunner = othersLocation.new EventAsyncTaskRunner();
		try {
			coordinates = locationRunner.execute(selection_locationID).get();
		} catch (InterruptedException e){
			e.printStackTrace();
		} catch (ExecutionException e){
			e.printStackTrace();
		}

		// asyncTask.listener = this;

		// Hide Keyboard after making selection
		InputMethodManager in = (InputMethodManager) mainActivity
				.getApplicationContext().getSystemService(
						Context.INPUT_METHOD_SERVICE);
		in.hideSoftInputFromWindow(mainActivity.getCurrentFocus()
				.getWindowToken(), 0);

		// Animate camera
		MapObjectControl mapControl = new MapObjectControl();
		mapControl.AddSearchedMarker(coordinates,MainFragment.mMapFragment.getMap(), 15);

	}

}

// End of onClickListener