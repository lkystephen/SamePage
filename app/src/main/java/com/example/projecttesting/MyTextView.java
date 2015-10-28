package com.example.projecttesting;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class MyTextView extends TextView {

	public MyTextView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		init(attr);
	}
	
	public MyTextView(Context context, AttributeSet attr) {
		super(context, attr);
		init(attr);
		
	}
	
	public MyTextView(Context context) {
		super(context);
		init(null);
	}
	
	private void init(AttributeSet attr) {
		if (attr!=null) {
			 TypedArray a = getContext().obtainStyledAttributes(attr, R.styleable.MyTextView);
			 String fontName = a.getString(R.styleable.MyTextView_fontName);
			 if (fontName!=null) {
				 Typeface myTypeface = Typeface.createFromAsset(getContext().getAssets(), fontName);
				 setTypeface(myTypeface);
			 }
			 a.recycle();
		}
	}

}