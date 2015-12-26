package com.example.projecttesting;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.login.widget.ProfilePictureView;
import com.pkmmte.view.CircularImageView;

public class SettingsFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.settings_main, container, false);
		
				
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Bundle bundle = getArguments();
		User user = bundle.getParcelable("user");
		String name = user.getUsername();
		String fbid = user.getFBId();

		Typeface face;
		face = FontCache.getFont(getContext(), "sf_reg.ttf");

		TextView logged=  (TextView) view.findViewById(R.id.logged_text);
		TextView fb_name = (TextView) view.findViewById(R.id.facebook_name);
		CircularImageView logged_image = (CircularImageView) view.findViewById(R.id.logged_image);
		logged_image.setImageBitmap(BitmapFactory.decodeFile(Utility.getImage(fbid, getContext()).getPath()));
		TextView set_all_star = (TextView) view.findViewById(R.id.set_all_star);
		TextView set_all_unstar = (TextView) view.findViewById(R.id.set_all_unstar);
		TextView notify_event = (TextView) view.findViewById(R.id.notify_event_soon);
		TextView notify_friend = (TextView) view.findViewById(R.id.friend_notify_nearby);
		TextView auto_cal_ETA = (TextView) view.findViewById(R.id.set_auto_load_location);

		// Settings buttons
		Switch friends_nearby = (Switch) view.findViewById(R.id.friends_nearby_switch);
		Switch event_soon = (Switch) view.findViewById(R.id.notify_event_switch);

		logged.setTypeface(face);
		fb_name.setTypeface(face);
		set_all_star.setTypeface(face);
		set_all_unstar.setTypeface(face);
		notify_event.setTypeface(face);
		notify_friend.setTypeface(face);
		auto_cal_ETA.setTypeface(face);

		fb_name.setText(name);

		friends_nearby.setOnCheckedChangeListener(new mOnCheckedChangedListener(1));
		event_soon.setOnCheckedChangeListener(new mOnCheckedChangedListener(2));
	}

	public class mOnCheckedChangedListener implements CompoundButton.OnCheckedChangeListener {

		int select;

		public mOnCheckedChangedListener(int i){
			select = i;
		}

		@Override
		public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
			switch (select) {
				case 1:
					Toast.makeText(getContext(),"FRIENDS TOGGLE TEST",Toast.LENGTH_SHORT).show();
					break;

				case 2:
					Toast.makeText(getContext(),"EVENT TOGGLE TEST",Toast.LENGTH_SHORT).show();
					break;
			}

		}
	}

}

