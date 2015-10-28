package com.example.projecttesting;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

public class MasterListFriendsFragment extends Fragment {

	public int[] friendsLastOnlineTime = new int[] { 1, 10};
	public int[] friends_image = {R.drawable.edmund, R.drawable.lkk};
	// friendsStarredStatus, 1 = starred, 0 is normal
	public int[] friendsStarredStatus = new int[] { 1, 1};
	ListView listView;
	ArrayList<FriendsRowItem> rowItems;
	User user;
	FriendsListAdapter adapter;

	public MasterListFriendsFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.master_friends, container,
				false);

		Typeface face;
		face = Typeface.createFromAsset(getActivity().getAssets(), "sf_reg.ttf");

		Bundle bundle = getArguments();
		user = bundle.getParcelable("user");

		final EditText search = (EditText) rootView.findViewById(R.id.master_search);
		search.setTypeface(face);

		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence word, int i, int i1, int i2) {
				adapter.getFilter().filter(word.toString());
			}

			@Override
			public void afterTextChanged(Editable editable) {
			}
		});

		final FragmentManager fm = getActivity().getSupportFragmentManager();

		// Get names of friends
		final List<OtherUser> master_list = user.getMasterList();

		// Establish data here
		rowItems = new ArrayList<>();
		//Log.i("master",Integer.toString(master_list.size()));
		for (int i = 0; i < master_list.size(); i++) {
			FriendsRowItem item = new FriendsRowItem(master_list.get(i).username,
					friendsLastOnlineTime[i], friendsStarredStatus[i],friends_image[i]);
			rowItems.add(item);
		}

		listView = (ListView) rootView.findViewById(R.id.starredfriendslist);

		adapter = new FriendsListAdapter(getActivity()
				.getApplicationContext(), R.layout.friendslistdisplay,
				rowItems);
		listView.setAdapter(adapter);
/*
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				Bundle bundle = new Bundle();
				bundle.putString("username", master_list.get(i).username);
				bundle.putInt("image",friends_image[i]);
				bundle.putInt("online", friendsLastOnlineTime[i]);

				FriendsDisplayDialog dialog = new FriendsDisplayDialog();
				dialog.setArguments(bundle);
				dialog.show(fm, "");
			}
		});
*/
		return rootView;
	}
}
