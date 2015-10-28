package com.example.projecttesting;

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
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.facebook.login.widget.ProfilePictureView;

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
		face = Typeface.createFromAsset(getActivity().getAssets(), "ubuntu_bold.ttf");

		TextView fb_name = (TextView) view.findViewById(R.id.facebook_name);
		ProfilePictureView display_view ;
		display_view = (ProfilePictureView) view.findViewById(R.id.facebook_dp);
		display_view.setProfileId(fbid);

		fb_name.setText(name);
	}

}

