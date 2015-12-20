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
import android.widget.TextView;

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

		logged.setTypeface(face);
		fb_name.setTypeface(face);

		fb_name.setText(name);
	}

}

