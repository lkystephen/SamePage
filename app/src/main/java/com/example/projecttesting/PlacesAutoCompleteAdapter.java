package com.example.projecttesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PlacesAutoCompleteAdapter extends SimpleAdapter implements
		Filterable {

	static ArrayList<HashMap<String, Object>> mArrayList = new ArrayList<HashMap<String, Object>>();
	static ArrayList<String> editTextDisplay = new ArrayList<String>();


	public PlacesAutoCompleteAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, mArrayList, resource, from, to);
	}

	@Override
	public int getCount() {
		return editTextDisplay.size();
	}

	@Override
	public Object getItem(int index) {
		return editTextDisplay.get(index);
	}

	@Override
	public SimpleAdapter.ViewBinder getViewBinder() {
		SimpleAdapter.ViewBinder viewBinder = new SimpleAdapter.ViewBinder() {
			public boolean setViewValue(View view, Object data, String arg2) {
				int viewId = view.getId();
				if (viewId == R.id.resulttext) {
					((TextView) view).setText((String) data);
				} else if (viewId == R.id.placesecondname) {
					((TextView) view).setText((String) data);
				}
				return true;
			}
		};

		return viewBinder;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@Override
			public FilterResults performFiltering(CharSequence constraint) {
				FilterResults filterResults = new FilterResults();

				if (constraint != null) {
					// Retrieve the AutoComplete results.
					ArrayList<String> resultList = PlacesAutocomplete
							.autocomplete(constraint.toString());

					// initialize the array every time a new search starts
					mArrayList.clear();
					editTextDisplay.clear();

					// separate id and description for displaying results
					for (int i = 0; i < resultList.size(); i++) {
						String description = resultList.get(i);
						HashMap<String, Object> hashmap = new HashMap<String, Object>();
						hashmap.put(
								"placeId",
								description.substring(10,
										description.indexOf("description") - 2));

						// Extract the main name and 2nd name of the String, the
						// if function is to validate how many "," there are in
						// the string
						String mainName;
						String secondName;

						if (description.indexOf(",") == description
								.lastIndexOf(",")) {
							mainName = description.substring(
									description.indexOf("description") + 12,
									description.length() - 1);
							secondName = mainName;

						} else {
							mainName = description
									.substring(
											description.indexOf("description") + 12,
											description.indexOf(
													",",
													description
															.indexOf("description") + 13));
							secondName = description.substring(
									description.indexOf(mainName)
											+ mainName.length() + 2,
									description.length() - 1);
						}

						// Log.i("LOG", secondName);

						hashmap.put("placeDesc", mainName);
						hashmap.put("placeSecondName", secondName);
						// Log.i("LOG", description);
						// Create String ArrayList for displaying the result in
						// the EditText
						editTextDisplay.add(mainName);
						// To create HashMap for storing all useful information
						// of the address (but not for display)
						mArrayList.add(hashmap);
					}

					// Assign the data to the FilterResults
					filterResults.values = editTextDisplay;
					filterResults.count = editTextDisplay.size();
				}
				return filterResults;
			}

			@Override
			public void publishResults(CharSequence constraint,
					FilterResults results) {
				if (results != null && results.count > 0) {
					notifyDataSetChanged();
				} else {
					notifyDataSetInvalidated();
				}
			}
		};

		return filter;

	}




} // end of AutoCompleteAdapter