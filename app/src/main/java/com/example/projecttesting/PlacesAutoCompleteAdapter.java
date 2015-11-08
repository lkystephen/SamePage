package com.example.projecttesting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<HashMap<String, Object>> implements
        Filterable {

    ArrayList<HashMap<String, Object>> mArrayList;
    static ArrayList<String> editTextDisplay = new ArrayList<String>();
    Context context;

    public PlacesAutoCompleteAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public HashMap<String, Object> getItem(int index) {
        return mArrayList.get(index);
    }

    /*private view holder school*/
    private class ViewHolder {
        TextView main_location;
        TextView second_location;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Typeface face_r, face_b;
        face_r = FontCache.getFont(getContext(), "sf_reg.ttf");
        face_b = FontCache.getFont(getContext(), "sf_bold.ttf");

        if (convertView != null) {
            convertView = mInflater.inflate(R.layout.event_location_list_item, null);
            holder = new ViewHolder();
            holder.main_location = (TextView) convertView.findViewById(R.id.resulttext);
            holder.second_location = (TextView) convertView.findViewById(R.id.placesecondname);

            holder.main_location.setTypeface(face_r);
            holder.second_location.setTypeface(face_r);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, Object> map = getItem(position);
        holder.main_location.setText(map.get("placeDesc").toString());
        holder.second_location.setText(map.get("placeSecondName").toString());

        return convertView;
    }

	/*@Override
    public SimpleAdapter.ViewBinder getViewBinder() {
		SimpleAdapter.ViewBinder viewBinder = new SimpleAdapter.ViewBinder() {
			public boolean setViewValue(View view, Object data, String arg2) {
				int viewId = view.getId();

				((TextView) view).setTypeface(typeface);

				if (viewId == R.id.resulttext) {
					((TextView) view).setText((String) data);
				} else if (viewId == R.id.placesecondname) {
					((TextView) view).setText((String) data);
				}
				return true;
			}
		};

		return viewBinder;
	}*/

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            public FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (constraint != null) {

                    Log.i("Constraint", constraint.toString());
                    // Retrieve the AutoComplete results.
                    ArrayList<String> resultList = PlacesAutocomplete
                            .autocomplete(constraint.toString());

                    Log.i("Size of result list",Integer.toString(resultList.size()));

                    // initialize the array every time a new search starts
                    mArrayList = new ArrayList<>();
                    editTextDisplay = new ArrayList<>();

                    // separate id and description for displaying results
                    for (int i = 0; i < resultList.size(); i++) {
                        String description = resultList.get(i);
                        HashMap<String, Object> hashMap = new HashMap<String, Object>();
                        hashMap.put("placeId", description.substring(10,
                                description.indexOf("description") - 2));

                        // Extract the main name and 2nd name of the String, the
                        // if function is to validate how many "," there are in
                        // the string
                        String mainName;
                        String secondName;

                        if (description.indexOf(",") == description.lastIndexOf(",")) {
                            mainName = description.substring(
                                    description.indexOf("description") + 12,
                                    description.length() - 1);
                            secondName = mainName;

                        } else {
                            mainName = description.substring(description.indexOf("description") + 12,
                                    description.indexOf(",",
                                            description.indexOf("description") + 13));
                            secondName = description.substring(description.indexOf(mainName)
                                            + mainName.length() + 2, description.length() - 1);
                        }

                        hashMap.put("placeDesc", mainName);
                        hashMap.put("placeSecondName", secondName);
                        // Log.i("LOG", description);
                        // Create String ArrayList for displaying the result in
                        // the EditText
                        editTextDisplay.add(mainName);
                        // To create HashMap for storing all useful information
                        // of the address (but not for display)
                        mArrayList.add(hashMap);
                    }

                    // Assign the data to the FilterResults
                    filterResults.values = editTextDisplay;
                    filterResults.count = editTextDisplay.size();
                    Log.i("Filtered Result size", Integer.toString(filterResults.count));
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